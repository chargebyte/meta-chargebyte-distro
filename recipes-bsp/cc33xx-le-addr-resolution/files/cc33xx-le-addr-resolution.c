// SPDX-License-Identifier: MIT
//
// TI CC33xx controllers ignore connection requests from peers using resolvable
// private addresses unless LE address resolution is enabled. HCI_Reset clears
// the setting, so it is applied again every time the controller comes up.

#include <errno.h>
#include <poll.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <time.h>
#include <unistd.h>

#ifndef AF_BLUETOOTH
#define AF_BLUETOOTH 31
#endif
#define BTPROTO_HCI 1
#define SOL_HCI 0
#define HCI_FILTER 2
#define HCI_DEV_NONE 0xffff
#define HCI_CHANNEL_RAW 0

#define HCI_COMMAND_PKT 0x01
#define HCI_EVENT_PKT 0x04
#define EVT_CMD_COMPLETE 0x0e
#define EVT_CMD_STATUS 0x0f
#define EVT_STACK_INTERNAL 0xfd
#define EVT_SI_DEVICE 0x0001
#define HCI_DEV_UP 3
#define HCI_DEV_DOWN 4

#define OP_LE_SET_ADDR_RESOLUTION_ENABLE 0x202d
#define COMMAND_TIMEOUT_MS 2000

#define LOG_INFO(fmt, ...) fprintf(stderr, "<6>" fmt "\n", __VA_ARGS__)
#define LOG_ERR(fmt, ...) fprintf(stderr, "<3>" fmt "\n", __VA_ARGS__)

struct sockaddr_hci {
    sa_family_t hci_family;
    unsigned short hci_dev;
    unsigned short hci_channel;
};

struct hci_filter {
    uint32_t type_mask;
    uint32_t event_mask[2];
    uint16_t opcode;
};

static uint16_t get_le16(const uint8_t *data)
{
    return (uint16_t)(data[0] | data[1] << 8);
}

static void filter_set_event(struct hci_filter *filter, uint8_t event)
{
    const unsigned int bit = event & 63;
    filter->event_mask[bit >> 5] |= 1u << (bit & 31);
}

static int64_t now_ms(void)
{
    struct timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return (int64_t)ts.tv_sec * 1000 + ts.tv_nsec / 1000000;
}

static int open_hci(unsigned short dev, const struct hci_filter *filter)
{
    const int fd = socket(AF_BLUETOOTH, SOCK_RAW | SOCK_CLOEXEC, BTPROTO_HCI);
    if (fd < 0)
        return -errno;

    const struct sockaddr_hci addr = { AF_BLUETOOTH, dev, HCI_CHANNEL_RAW };
    if (bind(fd, (const struct sockaddr *)&addr, sizeof(addr)) < 0
        || setsockopt(fd, SOL_HCI, HCI_FILTER, filter, sizeof(*filter)) < 0) {
        const int result = -errno;
        close(fd);
        return result;
    }
    return fd;
}

// Returns 0 on success, a positive HCI status code, or a negative errno.
static int enable_address_resolution(unsigned short dev)
{
    struct hci_filter filter = {
        .type_mask = 1u << HCI_EVENT_PKT,
        .opcode = OP_LE_SET_ADDR_RESOLUTION_ENABLE,
    };
    filter_set_event(&filter, EVT_CMD_COMPLETE);
    filter_set_event(&filter, EVT_CMD_STATUS);

    const int fd = open_hci(dev, &filter);
    if (fd < 0)
        return fd;

    const uint8_t command[] = {
        HCI_COMMAND_PKT,
        OP_LE_SET_ADDR_RESOLUTION_ENABLE & 0xff,
        OP_LE_SET_ADDR_RESOLUTION_ENABLE >> 8,
        1,
        0x01,
    };
    const ssize_t written = write(fd, command, sizeof(command));
    if (written != (ssize_t)sizeof(command)) {
        const int result = written < 0 ? -errno : -EIO;
        close(fd);
        return result;
    }

    int result = -ETIMEDOUT;
    const int64_t deadline = now_ms() + COMMAND_TIMEOUT_MS;
    for (int64_t left; (left = deadline - now_ms()) > 0;) {
        struct pollfd pfd = { fd, POLLIN, 0 };
        const int ready = poll(&pfd, 1, (int)left);
        if (ready < 0 && errno == EINTR)
            continue;
        if (ready <= 0) {
            if (ready < 0)
                result = -errno;
            break;
        }

        uint8_t event[260];
        const ssize_t size = read(fd, event, sizeof(event));
        if (size < 0) {
            if (errno == EINTR)
                continue;
            result = -errno;
            break;
        }
        if (size < 7 || event[0] != HCI_EVENT_PKT)
            continue;
        if (event[1] == EVT_CMD_COMPLETE
            && get_le16(&event[4]) == OP_LE_SET_ADDR_RESOLUTION_ENABLE) {
            result = event[6];
            break;
        }
        // A pending Command Status is not completion; only a rejection ends the wait.
        if (event[1] == EVT_CMD_STATUS && event[3] != 0
            && get_le16(&event[5]) == OP_LE_SET_ADDR_RESOLUTION_ENABLE) {
            result = event[3];
            break;
        }
    }
    close(fd);
    return result;
}

static bool configure(unsigned short dev)
{
    const int result = enable_address_resolution(dev);
    if (result == 0) {
        LOG_INFO("hci%u: LE address resolution enabled", dev);
        return true;
    }
    if (result == -ENETDOWN || result == -ENODEV) {
        LOG_INFO("hci%u: controller not up, waiting", dev);
        return true;
    }
    if (result > 0)
        LOG_ERR("hci%u: LE Set Address Resolution Enable failed: HCI status 0x%02x",
                dev, result);
    else
        LOG_ERR("hci%u: LE Set Address Resolution Enable failed: %s",
                dev, strerror(-result));
    return false;
}

static bool parse_adapter(const char *name, unsigned short *dev)
{
    if (strncmp(name, "hci", 3) != 0 || name[3] < '0' || name[3] > '9')
        return false;
    char *end = NULL;
    errno = 0;
    const unsigned long index = strtoul(name + 3, &end, 10);
    if (errno != 0 || *end != '\0' || index >= HCI_DEV_NONE)
        return false;
    *dev = (unsigned short)index;
    return true;
}

int main(int argc, char **argv)
{
    unsigned short dev = 0;
    if (argc > 2 || (argc == 2 && !parse_adapter(argv[1], &dev))) {
        fprintf(stderr, "usage: %s [hciN]\n", argv[0]);
        return EXIT_FAILURE;
    }

    struct hci_filter filter = { .type_mask = 1u << HCI_EVENT_PKT };
    filter_set_event(&filter, EVT_STACK_INTERNAL);
    const int events = open_hci(HCI_DEV_NONE, &filter);
    if (events < 0) {
        LOG_ERR("cannot open HCI event socket: %s", strerror(-events));
        return EXIT_FAILURE;
    }

    // Subscribe before the first attempt so a controller coming up in between is not missed.
    if (!configure(dev))
        return EXIT_FAILURE;

    for (;;) {
        uint8_t event[260];
        const ssize_t size = read(events, event, sizeof(event));
        if (size < 0) {
            if (errno == EINTR)
                continue;
            LOG_ERR("reading HCI events failed: %s", strerror(errno));
            return EXIT_FAILURE;
        }
        if (size < 9 || event[0] != HCI_EVENT_PKT || event[1] != EVT_STACK_INTERNAL
            || get_le16(&event[3]) != EVT_SI_DEVICE || get_le16(&event[7]) != dev)
            continue;

        const uint16_t type = get_le16(&event[5]);
        if (type == HCI_DEV_DOWN)
            LOG_INFO("hci%u: controller down", dev);
        else if (type == HCI_DEV_UP && !configure(dev))
            return EXIT_FAILURE;
    }
}
