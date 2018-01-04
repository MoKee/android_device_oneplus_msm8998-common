/*
Copyright (c) 2013, The Linux Foundation. All rights reserved.
Copyright (c) 2018, The MoKee Open Source Project

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of The Linux Foundation nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#include <cstdlib>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>

#include <android-base/logging.h>

#include "vendor_init.h"
#include "property_service.h"

#define FP_DETECT "/sys/devices/soc/soc:fingerprint_detect/sensor_version"

#define SENSOR_FPC_1  0x01
#define SENSOR_FPC_2  0x02
#define SENSOR_GOODIX 0x03

using android::init::property_set;

static int get_sensor_version()
{
    int fd, ret;
    char buf[80];

    fd = open(FP_DETECT, O_RDONLY);
    if (fd < 0) {
        strerror_r(errno, buf, sizeof(buf));
        PLOG(ERROR) << "Failed to open fp_detect: " << buf;
        ret = -errno;
        goto end;
    }

    if (read(fd, buf, 80) < 0) {
        strerror_r(errno, buf, sizeof(buf));
        PLOG(ERROR) << "Failed to read fp_detect: " << buf;
        ret = -errno;
        goto close;
    }

    if (sscanf(buf, "%d", &ret) != 1) {
        PLOG(ERROR) << "Failed to parse fp_detect: " << buf;
        ret = -EINVAL;
        goto close;
    }

close:
    close(fd);
end:
    return ret;
}

void vendor_load_properties()
{
    int sensor_version = get_sensor_version();
    if (sensor_version < 0) {
        LOG(ERROR) << "Failed to detect sensor version";
        return;
    }

    LOG(INFO) << "Loading HAL for sensor version " << sensor_version;
    switch (sensor_version) {
        case SENSOR_FPC_1:
        case SENSOR_FPC_2:
            property_set("ro.hardware.fingerprint", "fpc");
            break;
        case SENSOR_GOODIX:
            property_set("ro.hardware.fingerprint", "goodix");
            break;
        default:
            LOG(ERROR) << "Unsupported sensor: " << sensor_version;
            break;
    }
}
