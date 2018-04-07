#!/bin/bash
#
# Copyright (C) 2017-2018 The MoKee Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -e

# Load extract_utils and do some sanity checks
MY_DIR="${BASH_SOURCE%/*}"
if [[ ! -d "$MY_DIR" ]]; then MY_DIR="$PWD"; fi

MK_ROOT="$MY_DIR"/../../..

HELPER="$MK_ROOT"/vendor/mk/build/tools/extract_utils.sh
if [ ! -f "$HELPER" ]; then
    echo "Unable to find helper script at $HELPER"
    exit 1
fi
. "$HELPER"

# Default to sanitizing the vendor folder before extraction
CLEAN_VENDOR=true

while [ "$1" != "" ]; do
    case $1 in
        -n | --no-cleanup )     CLEAN_VENDOR=false
                                ;;
        -s | --section )        shift
                                SECTION=$1
                                CLEAN_VENDOR=false
                                ;;
        * )                     SRC=$1
                                ;;
    esac
    shift
done

if [ -z "$SRC" ]; then
    SRC=adb
fi

# Initialize the helper
setup_vendor "$DEVICE_COMMON" "$VENDOR" "$MK_ROOT" true "$CLEAN_VENDOR"

extract "$MY_DIR"/proprietary-files-qc.txt "$SRC" "$SECTION"
extract "$MY_DIR"/proprietary-files-qc-perf.txt "$SRC" "$SECTION"
extract "$MY_DIR"/proprietary-files.txt "$SRC" "$SECTION"

if [ -s "$MY_DIR"/../$DEVICE/proprietary-files.txt ]; then
    # Reinitialize the helper for device
    setup_vendor "$DEVICE" "$VENDOR" "$MK_ROOT" false "$CLEAN_VENDOR"

    extract "$MY_DIR"/../$DEVICE/proprietary-files.txt "$SRC" "$SECTION"
fi

function fix_vendor () {
    sed -i \
        "s/\/system\/$1\//\/vendor\/$1\//g" \
        "$MK_ROOT"/vendor/"$VENDOR"/"$DEVICE_COMMON"/proprietary/vendor/"$2"
}

# Camera
fix_vendor etc lib/libmmcamera_imglib.so
fix_vendor etc lib/libmmcamera_interface.so
fix_vendor etc lib/libopcamera_native_modules.so

# CNE
fix_vendor etc lib/libwqe.so
fix_vendor etc lib64/libwqe.so
fix_vendor framework etc/permissions/cneapiclient.xml
fix_vendor framework etc/permissions/com.quicinc.cne.xml

# DPM
fix_vendor bin etc/init/dpmd.rc
fix_vendor etc lib/libdpmframework.so
fix_vendor etc lib64/libdpmframework.so
fix_vendor framework etc/permissions/com.qti.dpmframework.xml
fix_vendor framework etc/permissions/dpmapi.xml

# Fingerprint sensor
fix_vendor framework etc/permissions/com.fingerprints.extension.xml

# GPS
fix_vendor framework etc/permissions/com.qti.location.sdk.xml
fix_vendor framework etc/permissions/izat.xt.srv.xml

# Postprocessing
fix_vendor framework etc/permissions/com.qti.snapdragon.sdk.display.xml

# Radio
fix_vendor framework etc/permissions/embms.xml
fix_vendor framework etc/permissions/qcnvitems.xml
fix_vendor framework etc/permissions/qcrilhook.xml
fix_vendor framework etc/permissions/telephonyservice.xml

# OpenMobile
fix_vendor framework etc/permissions/org.simalliance.openmobileapi.xml

# Wi-Fi Display
fix_vendor etc lib/libwfdrtsp.so
fix_vendor etc lib/libwfdservice.so
fix_vendor etc lib/libwfdsm.so
fix_vendor etc lib/libwfduibcsinkinterface.so
fix_vendor etc lib/libwfduibcsrcinterface.so
fix_vendor etc lib64/libwfdrtsp.so
fix_vendor etc lib64/libwfdservice.so
fix_vendor etc lib64/libwfdsm.so
fix_vendor etc lib64/libwfduibcsinkinterface.so
fix_vendor etc lib64/libwfduibcsrcinterface.so
fix_vendor framework etc/init/wfdservice.rc

"$MY_DIR"/setup-makefiles.sh
