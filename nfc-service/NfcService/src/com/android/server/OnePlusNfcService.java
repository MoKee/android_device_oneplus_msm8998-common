package com.android.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Slog;

import com.oem.os.IOnePlusNfcService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OnePlusNfcService extends IOnePlusNfcService.Stub {

    private static final String CARD_CONFIG_PROPERTY = "persist.oem.nfc.rf.card";

    private static final String TAG = "OnePlusNfcService";

    private final BroadcastReceiver adapterStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (NfcAdapter.ACTION_ADAPTER_STATE_CHANGED.equals(intent.getAction())) {
                int state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, -1);
                Slog.d(TAG, "[NfcBroadcast] state change state " + state);
                if (state == NfcAdapter.STATE_OFF) {
                    NfcAdapter.getDefaultAdapter(context).enable();
                    context.unregisterReceiver(this);
                }
            }
        }
    };

    private final Context context;

    private final Map<String, String> cardConfigs = new HashMap<>();

    public OnePlusNfcService(Context context) {
        this.context = context;
    }

    private String getCurrentConfig() {
        return SystemProperties.get(CARD_CONFIG_PROPERTY, "0");
    }

    private void resetNfcService() {
        final NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        if (!adapter.isEnabled()) {
            Slog.w(TAG, "[resetNfcService] nfc is disable,no need to reset");
            return;
        }

        final IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
        context.registerReceiverAsUser(adapterStateReceiver, UserHandle.ALL, filter, null, null);

        adapter.disable();
    }

    @Override
    public boolean applyConfig() throws RemoteException {
        Slog.d(TAG, "[applyConfig]");
        return false;
    }

    @Override
    public String getDieId() throws RemoteException {
        Slog.d(TAG, "[getDieid]");
        return NfcAdapter.getDefaultAdapter(context).getDieId();
    }

    @Override
    public List<String> getSupportCardTypes() throws RemoteException {
        Slog.d(TAG, "[getSupportCardTypes]");
        return new ArrayList<>();
    }

    @Override
    public void setSupportCardTypes(List<String> cardTypes) throws RemoteException {
        Slog.d(TAG, "[setSupportCardTypes] cardTypes" + cardTypes);
    }

    @Override
    public List<String> getSupportNfcConfigs() throws RemoteException {
        Slog.d(TAG, "[getSupportNfcConfigs]");
        return null;
    }

    @Override
    public void setCardType(String type) throws RemoteException {
        Slog.d(TAG, "[setCardType] type " + type);
        setNfcConfig(this.cardConfigs.get(type));
    }

    @Override
    public void setNfcConfig(String config) throws RemoteException {
        if (config == null) {
            return;
        }

        Slog.d(TAG, "[setNfcConfig] config " + config);

        resetNfcService();
        SystemProperties.set(CARD_CONFIG_PROPERTY, config);
    }

}
