package com.oem.os;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;

import java.util.List;

public class OnePlusNfcManager {

    public static final String ONEPLUS_NFC_SERVICE = "oneplus_nfc_service";

    public static final String CARD_TYPE_BJBUS = "BeiJingBaShi";
    public static final String CARD_TYPE_LNT = "LinNanTong";
    public static final String CARD_TYPE_PRODUCT = "ProductLine";
    public static final String CARD_TYPE_SZT = "ShenZhenTong";
    public static final String CARD_TYPE_YCT = "YangChengTong";

    private static final String TAG = "OnePlusNfcManager";

    private static IOnePlusNfcService service;

    public OnePlusNfcManager(Context context) {
    }

    private static IOnePlusNfcService getService() {
        if (service != null) {
            return service;
        }

        service = IOnePlusNfcService.Stub.asInterface(
                ServiceManager.getService("OnePlusNfcService"));

        return service;
    }

    public boolean applyConfig() {
        Slog.d(TAG, "[applyConfig]");
        try {
            return getService().applyConfig();
        } catch (RemoteException e) {
            Slog.e(TAG, "OnePlus Nfc service is unavailable");
            return false;
        }
    }

    public String getDieId() {
        Slog.d(TAG, "[getDieId]");
        try {
            return getService().getDieId();
        } catch (RemoteException e) {
            Slog.e(TAG, "OnePlus Nfc service is unavailable");
            return null;
        }
    }

    public List<String> getSupportCardTypes() {
        Slog.d(TAG, "[getSupportCardTypes]");
        try {
            return getService().getSupportCardTypes();
        } catch (RemoteException e) {
            Slog.e(TAG, "OnePlus Nfc service is unavailable");
            return null;
        }
    }

    public void setSupportCardTypes(List<String> cardTypes) {
        Slog.d(TAG, "[getSupportCardTypes] cardTypes" + cardTypes);
        try {
            getService().setSupportCardTypes(cardTypes);
        } catch (RemoteException e) {
            Slog.e(TAG, "OnePlus Nfc service is unavailable");
        }
    }

    public List<String> getSupportNfcConfigs() {
        Slog.d(TAG, "[getSupportNfcConfigs]");
        try {
            return getService().getSupportNfcConfigs();
        } catch (RemoteException e) {
            Slog.e(TAG, "OnePlus Nfc service is unavailable");
            return null;
        }
    }

    public void setCardType(String type) {
        Slog.d(TAG, "[setCardType] type " + type);
        try {
            getService().setCardType(type);
        } catch (RemoteException e) {
            Slog.e(TAG, "OnePlus Nfc service is unavailable");
        }
    }

    public void setNfcConfig(String config) {
        Slog.d(TAG, "[setNfcConfig] config " + config);
        try {
            getService().setNfcConfig(config);
        } catch (RemoteException e) {
            Slog.e(TAG, "OnePlus Nfc service is unavailable");
        }
    }

}
