ifeq ($(strip $(ONEPLUS_ENABLE_NFC_SIM)),false)
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_AIDL_INCLUDES := packages/apps/SmartCardService/openmobileapi/src/org/simalliance/openmobileapi/service

LOCAL_PACKAGE_NAME := SmartcardService
LOCAL_CERTIFICATE := appstore
LOCAL_PRIVILEGED_MODULE := true

LOCAL_JAVA_LIBRARIES := core-libart framework org.simalliance.openmobileapi

LOCAL_PROGUARD_ENABLED := disabled

LOCAL_ASSET_DIR := $(LOCAL_PATH)/assets

LOCAL_AAPT_FLAGS := --auto-add-overlay

include $(BUILD_PACKAGE)

include $(call all-makefiles-under,$(LOCAL_PATH))
endif