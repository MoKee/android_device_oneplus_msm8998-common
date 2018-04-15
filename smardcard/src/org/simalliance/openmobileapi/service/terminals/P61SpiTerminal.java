/*
 * Copyright (C) 2011, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Contributed by: Giesecke & Devrient GmbH.
 */
/******************************************************************************
 *
 *  The original Work has been changed by NXP Semiconductors.
 *
 *  Copyright (C) 2015 NXP Semiconductors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/
package org.simalliance.openmobileapi.service.terminals;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.nxp.eseclient.EseClientManager;
import com.nxp.eseclient.EseClientServicesAdapterBuilder;
import com.nxp.eseclient.EseClientServicesAdapter;
import com.nxp.intf.INxpExtrasService;

import java.util.MissingResourceException;
import java.util.NoSuchElementException;

import org.simalliance.openmobileapi.service.Terminal;
import org.simalliance.openmobileapi.service.CardException;
import org.simalliance.openmobileapi.service.SmartcardService;

public class P61SpiTerminal extends Terminal {
	/* + snb log */
	public static final String P61SpiTerminal_TAG = "P61SpiTerminal";
	/* - snb log */

	/* + snb NXP for Android M */
	private static EseClientManager mEseManager;
	private static EseClientServicesAdapter mEseClientServicesAdapter;
	private static EseClientServicesAdapterBuilder mEseClientServicesAdapterBuilder;
	private static INxpExtrasService mINxpExtrasService;
	public static Integer type = EseClientManager.SPI;
	/* - snb NXP for Android M */
	private Binder binder = new Binder();

	public P61SpiTerminal(Context context) {
		super(SmartcardService._P61Spi_TERMINAL, context);
		/* + snb NXP for Android M */
		try {
			mEseManager = EseClientManager.getInstance();
			mEseManager.initialize();
			INxpExtrasService NxpExtrasServiceIntf = null;
			mEseClientServicesAdapterBuilder = new EseClientServicesAdapterBuilder();
			mEseClientServicesAdapter = mEseClientServicesAdapterBuilder.getEseClientServicesAdapterInstance(type);
			NxpExtrasServiceIntf = mEseClientServicesAdapter.getNxpExtrasService();
			mINxpExtrasService = NxpExtrasServiceIntf;
		} catch (Exception e) {
			Log.d(SmartcardService.LOG_LEVEL_BOTTOM, e.getMessage());
		}
		/* - snb NXP for Android M */
	}
	/* + snb log */
	public static String toHexString(byte[] data) {
		if (data == null) {
			return null;
		} else {
			int len = data.length;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len; i++) {
				if ((data[i] & 0xFF) < 16)
					sb.append("0" + Integer.toHexString(data[i] & 0xFF));
				else
					sb.append(Integer.toHexString(data[i] & 0xFF));
			}
			return sb.toString().toUpperCase();
		}
	}
	/* - snb log */
	
	public boolean isCardPresent() throws CardException {
		/* + snb NXP for Android M */
		SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "isCardPresent start");
//		boolean isEnable = false;
//		try {
//			isEnable = mINxpExtrasService.isEnabled();
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "isCardPresent end");
//		return isEnable; 
		return true;// mSpiAdapter.isEnabled(); // Enabled always for testing
		/* - snb NXP for Android M */
	}

	@Override
	protected void internalConnect() throws CardException {
		/* + snb NXP for Android M */
		// spi initialization
		SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "internalConnect start");
		try {
			Bundle b = null;
			if (!mINxpExtrasService.isEnabled()) {
				SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "internalConnect");
				 b = mINxpExtrasService.open("org.simalliance.openmobileapi.service", binder);
			}
            int errorHandle = b.getInt("e");
            String errorStr = b.getString("m");
            if (errorHandle < 0) {
				throw new CardException(errorStr);
			}
		} catch (Exception e) {
			throw new CardException("open SE failed");
		}
		SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "internalConnect end");
		/* - snb NXP for Android M */
		mDefaultApplicationSelectedOnBasicChannel = true;
		mIsConnected = true;
	}

	@Override
	protected void internalDisconnect() throws CardException {
		/* + snb NXP for Android M */
		// spi deinitialization
		SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "internalDisconnect start");
		try {
			Bundle b = null;
			if (mINxpExtrasService.isEnabled()) {
				SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "internalDisconnect");
				b = mINxpExtrasService.close("org.simalliance.openmobileapi.service", binder);
//				 mSpiAdapter.disable();
			}
            int errorHandle = b.getInt("e");
            String errorStr = b.getString("m");
            if (errorHandle < 0) {
				throw new CardException(errorStr);
			}
		} catch (Exception e) {
			throw new CardException("close SE failed");
		}
		SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "internalDisconnect end");
		/* - snb NXP for Android M */
	}

	@Override
	protected byte[] internalTransmit(byte[] command) throws CardException {

			/* + snb log */
			String apducmd = toHexString(command);
			SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "SEND: "+apducmd);
			/* + snb log */ 
			/* + snb NXP for Android M */
		try {
			Bundle b = mINxpExtrasService.transceive("org.simalliance.openmobileapi.service", command);
			if (b == null) {
				throw new CardException("exchange APDU failed");
			}
            int errorHandle = b.getInt("e");
            String errorStr = b.getString("m");
            if (errorHandle < 0) {
				throw new CardException(errorStr);
			}
			/* - snb NXP for Android M */
			/* + snb log */
			byte[] res = b.getByteArray("out");
			SmartcardService.log(P61SpiTerminal_TAG + SmartcardService._TAG + SmartcardService.LOG_LEVEL_TOP, "response data:" + toHexString(res));
			return res;
//			return b.getByteArray("out");
			/* - snb log */
		} catch (Exception e) {
			throw new CardException("exchange APDU failed");
		}
	}

	// SPI won't support getAtr()
	@Override
	public byte[] getAtr() {
		return null;
	}

	@Override
	protected int internalOpenLogicalChannel() throws Exception {

		mSelectResponse = null;
		byte[] manageChannelCommand = new byte[] { 0x00, 0x70, 0x00, 0x00, 0x01 };
		byte[] rsp = transmit(manageChannelCommand, 2, 0x9000, 0, "MANAGE CHANNEL");
		if ((rsp.length == 2) && ((rsp[0] == (byte) 0x68) && (rsp[1] == (byte) 0x81))) {
			throw new NoSuchElementException("logical channels not supported");
		}
		if (rsp.length == 2 && (rsp[0] == (byte) 0x6A && rsp[1] == (byte) 0x81)) {
			throw new MissingResourceException("no free channel available", "", "");
		}
		if (rsp.length != 3) {
			throw new MissingResourceException("unsupported MANAGE CHANNEL response data", "", "");
		}
		int channelNumber = rsp[0] & 0xFF;
		if (channelNumber == 0 || channelNumber > 19) {
			throw new MissingResourceException("invalid logical channel number returned", "", "");
		}

		return channelNumber;
	}

	@Override
	protected int internalOpenLogicalChannel(byte[] aid) throws Exception {

		if (aid == null) {
			throw new NullPointerException("aid must not be null");
		}
		mSelectResponse = null;

		byte[] manageChannelCommand = new byte[] { 0x00, 0x70, 0x00, 0x00, 0x01 };
		byte[] rsp = transmit(manageChannelCommand, 2, 0x9000, 0, "MANAGE CHANNEL");
		if ((rsp.length == 2) && ((rsp[0] == (byte) 0x68) && (rsp[1] == (byte) 0x81))) {
			throw new NoSuchElementException("logical channels not supported");
		}
		if (rsp.length == 2 && (rsp[0] == (byte) 0x6A && rsp[1] == (byte) 0x81)) {
			throw new MissingResourceException("no free channel available", "", "");
		}
		if (rsp.length != 3) {
			throw new MissingResourceException("unsupported MANAGE CHANNEL response data", "", "");
		}
		int channelNumber = rsp[0] & 0xFF;
		if (channelNumber == 0 || channelNumber > 19) {
			throw new MissingResourceException("invalid logical channel number returned", "", "");
		}

		byte[] selectCommand = new byte[aid.length + 6];
		selectCommand[0] = (byte) channelNumber;
		if (channelNumber > 3) {
			selectCommand[0] |= 0x40;
		}
		selectCommand[1] = (byte) 0xA4;
		selectCommand[2] = 0x04;
		selectCommand[4] = (byte) aid.length;
		System.arraycopy(aid, 0, selectCommand, 5, aid.length);
		try {
			mSelectResponse = transmit(selectCommand, 2, 0x9000, 0xFFFF, "SELECT");
		} catch (CardException exp) {
			internalCloseLogicalChannel(channelNumber);
			throw new NoSuchElementException(exp.getMessage());
		}

		return channelNumber;
	}

	@Override
	protected void internalCloseLogicalChannel(int channelNumber) throws CardException {
		if (channelNumber > 0) {
			byte cla = (byte) channelNumber;
			if (channelNumber > 3) {
				cla |= 0x40;
			}
			byte[] manageChannelClose = new byte[] { cla, 0x70, (byte) 0x80, (byte) channelNumber };
			transmit(manageChannelClose, 2, 0x9000, 0xFFFF, "MANAGE CHANNEL");
		}
	}

	/* + snb GetUid */
	@Override
	protected byte[] internalGetUid() throws CardException {
		// TODO Auto-generated method stub
		byte[] uid = null;
		try {
			uid = mINxpExtrasService.getSecureElementUid("org.simalliance.openmobileapi.service");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uid;
	}
	/* - snb GetUid */

}
