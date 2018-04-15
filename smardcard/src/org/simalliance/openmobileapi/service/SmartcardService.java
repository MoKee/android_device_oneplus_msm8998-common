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

package org.simalliance.openmobileapi.service;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
/* + snb signature */
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.os.Environment;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
/* - snb signature */
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException; 

import org.simalliance.openmobileapi.service.Channel;
import org.simalliance.openmobileapi.service.Channel.SmartcardServiceChannel;
import org.simalliance.openmobileapi.service.ISmartcardService;
import org.simalliance.openmobileapi.service.ISmartcardServiceCallback;
import org.simalliance.openmobileapi.service.Terminal.SmartcardServiceReader;

import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;









import org.simalliance.openmobileapi.service.security.AccessControlEnforcer;
import org.simalliance.openmobileapi.service.security.ChannelAccess;


/**
 * The smartcard service is setup with privileges to access smart card hardware.
 * The service enforces the permission
 * 'org.simalliance.openmobileapi.service.permission.BIND'.
 */
public final class SmartcardService extends Service {

    public static final String _TAG = "SmartcardService";
    public static final String _UICC_TERMINAL = "SIM";
    public static final String _eSE_TERMINAL = "eSE";
    public static final String _SD_TERMINAL = "SD";
    /* + snb log */
    public static final String _P61Spi_TERMINAL = "P61Spi";
	/* - snb log */
    /* + snb signature */
    static boolean DBG = false;
    public static final String NFCEE_ACCESS_PATH = "/etc/sbtoma_access.xml";
    public static final String ASSET_ACCESS_PATH = "sbtoma_access.xml";
    public static final String LOG_LEVEL_TOP = "TOP";
    public static final String LOG_LEVEL_MIDDLE = "MIDDLE";
    public static final String LOG_LEVEL_BOTTOM = "BOTTOM";
    /**
     * Map of signatures to valid packages names, as read from nfcscc_access.xml.
     * An empty list of package names indicates that any package
     * with this signature is allowed.
     */
    final HashMap<Signature, String[]> mNfceeAccess;  // contents final after onCreate()
    
    /** Regular NFC permission */
    private static final String NFC_PERM = android.Manifest.permission.NFC;
    private static final String NFC_PERM_ERROR = "NFC permission required";
    private Context mContext;
    
    static String level = null;
	/* - snb signature */
    
    static void clearError(SmartcardError error) {
        if (error != null) {
            error.clear();
        }
    }

    @SuppressWarnings({ "rawtypes" })
    static void setError(SmartcardError error, Class clazz, String message) {
        if (error != null) {
            error.setError(clazz, message);
        }
    }

    static void setError(SmartcardError error, Exception e) {
        if (error != null) {
            error.setError(e.getClass(), e.getMessage());
        }
    }

    /**
     * For now this list is setup in onCreate(), not changed later and therefore
     * not synchronized.
     */
    private Map<String, ITerminal> mTerminals = new TreeMap<String, ITerminal>();

    /**
     * For now this list is setup in onCreate(), not changed later and therefore
     * not synchronized.
     */
    private Map<String, ITerminal> mAddOnTerminals = new TreeMap<String, ITerminal>();

    /* Broadcast receivers */
    private BroadcastReceiver mSimReceiver;
    private BroadcastReceiver mNfcReceiver;
    private BroadcastReceiver mMediaReceiver;
    
    /* Async task */
    InitialiseTask mInitialiseTask;

    /**
     * ServiceHandler use to load rules from the terminal
     */
    private ServiceHandler mServiceHandler;
    
    
    public SmartcardService() {
        super();
		/* + snb signature */
        mNfceeAccess = new HashMap<Signature, String[]>();
		/* - snb signature */
    }
    
    @Override
    public IBinder onBind(Intent intent) {
		/* + snb log */
        log(_TAG + LOG_LEVEL_BOTTOM, Thread.currentThread().getName()
                        + " smartcard service onBind");
		/* - snb log */
        if (ISmartcardService.class.getName().equals(intent.getAction())) {
            return mSmartcardBinder;
        }
        return null;
    }

    @Override
    public void onCreate() {
		/* + snb log */
        log(_TAG + LOG_LEVEL_BOTTOM, Thread.currentThread().getName()
                + " smartcard service onCreate start");
		/* - snb log */
        /* + snb signature */
        mContext = this;
        DBG = parseNfceeAccess();
		/* - snb log */
		
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("SmartCardServiceHandler");
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceHandler = new ServiceHandler(thread.getLooper());
        
        createTerminals();
        mInitialiseTask = new InitialiseTask();
        mInitialiseTask.execute();
		/* + snb log */
        log(_TAG + LOG_LEVEL_BOTTOM, Thread.currentThread().getName()
                + " smartcard service onCreate end");
		/* - snb log */
    } 
    
	/* + snb signature */
    @SuppressWarnings("unused")
	private boolean parseNfceeAccess() {
        File file = new File(Environment.getRootDirectory(), NFCEE_ACCESS_PATH);
        FileReader reader = null;
        InputStream is = null;
        boolean debug = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            if (file.exists()) {
            	 reader = new FileReader(file);
                 parser.setInput(reader);
			}else {
				Context test_Context = this.createPackageContext(this.getPackageName(), Context.CONTEXT_IGNORE_SECURITY);
				AssetManager am = test_Context.getAssets();
				is = am.open(ASSET_ACCESS_PATH);
				parser.setInput(is, "utf-8");
			}
           

            int event;
            ArrayList<String> packages = new ArrayList<String>();
            Signature signature = null;
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            while (true) {
                event = parser.next();
                String tag = parser.getName();
                if (event == XmlPullParser.START_TAG && "signer".equals(tag)) {
                    signature = null;
                    packages.clear();
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if ("android:signature".equals(parser.getAttributeName(i))) {
                            signature = new Signature(parser.getAttributeValue(i));
                            break;
                        }
                    }
                    if (signature == null) {
                        log(_TAG + LOG_LEVEL_MIDDLE, "signer tag is missing android:signature attribute, igorning");
                        continue;
                    }
                    if (mNfceeAccess.containsKey(signature)) {
                        log(_TAG + LOG_LEVEL_MIDDLE, "duplicate signature, ignoring");
                        signature = null;
                        continue;
                    }
                } else if (event == XmlPullParser.END_TAG && "signer".equals(tag)) {
                    if (signature == null) {
                        log(_TAG + LOG_LEVEL_MIDDLE, "mis-matched signer tag");
                        continue;
                    }
                    mNfceeAccess.put(signature, packages.toArray(new String[0]));
                    packages.clear();
                } else if (event == XmlPullParser.START_TAG && "package".equals(tag)) {
                    if (signature == null) {
                        log(_TAG + LOG_LEVEL_MIDDLE, "ignoring unnested packge tag");
                        continue;
                    }
                    String name = null;
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if ("android:name".equals(parser.getAttributeName(i))) {
                            name = parser.getAttributeValue(i);
                            break;
                        }
                    }
                    if (name == null) {
                        log(_TAG + LOG_LEVEL_MIDDLE, "package missing android:name, ignoring signer group");
                        signature = null;  // invalidate signer
                        continue;
                    }
                    // check for duplicate package names
                    if (packages.contains(name)) {
                        log(_TAG + LOG_LEVEL_MIDDLE, "duplicate package name in signer group, ignoring");
                        continue;
                    }
                    packages.add(name);
                } else if (event == XmlPullParser.START_TAG && "debug".equals(tag)) {
                    debug = true;
                } else if(event == XmlPullParser.START_TAG && "log".equals(tag)){
                	if ("level".equals(parser.getAttributeName(0))){
						level = parser.getAttributeValue(0);
					}
                }else if (event == XmlPullParser.END_DOCUMENT) {
                    break;
                } 
            }
        } catch (XmlPullParserException e) {
            log(_TAG + LOG_LEVEL_MIDDLE, "failed to load NFCEE access list", e);
            mNfceeAccess.clear();  // invalidate entire access list
        } catch (FileNotFoundException e) {
            log(_TAG + LOG_LEVEL_MIDDLE, "could not find " + NFCEE_ACCESS_PATH + ", no NFCEE access allowed");
        } catch (IOException e) {
            log(_TAG + LOG_LEVEL_MIDDLE, "Failed to load NFCEE access list", e);
            mNfceeAccess.clear();  // invalidate entire access list
        } catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2)  { }
            }
            if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
        log(_TAG + LOG_LEVEL_MIDDLE, "read " + mNfceeAccess.size() + " signature(s) for sbtoma access");
        return debug;
    }
	/* - snb log */
	
    @Override
    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.println("SMARTCARD SERVICE (dumpsys activity service org.simalliance.openmobileapi)");
        writer.println();

        String prefix = "  ";

        if(!Build.IS_DEBUGGABLE) {
            writer.println(prefix + "Your build is not debuggable!");
            writer.println(prefix + "Smartcard service dump is only available for userdebug and eng build");
        } else {
            writer.println(prefix + "List of terminals:");
            for (ITerminal terminal : mTerminals.values()) {
               writer.println(prefix + "  " + terminal.getName());
            }
            writer.println();

            writer.println(prefix + "List of add-on terminals:");
            for (ITerminal terminal : mAddOnTerminals.values()) {
               writer.println(prefix + "  " + terminal.getName());
            }
            writer.println();

            for (ITerminal terminal : mTerminals.values()) {
               terminal.dump(writer, prefix);
            }
            for (ITerminal terminal : mAddOnTerminals.values()) {
               terminal.dump(writer, prefix);
            }
        }
    }
        
    
    private class InitialiseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
            	initializeAccessControl(null, null);
            } catch( Exception e ){
            	// do nothing since this is called were nobody can react.
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
			/* + snb log */
            log(_TAG + LOG_LEVEL_BOTTOM, "OnPostExecute()");
			/* - snb log */
            registerSimStateChangedEvent(getApplicationContext()) ;
            registerAdapterStateChangedEvent(getApplicationContext());
            registerMediaMountedEvent(getApplicationContext());
            mInitialiseTask = null;
        }
    }    
    
    private void registerSimStateChangedEvent(Context context) {
		/* + snb log */
        log(_TAG + LOG_LEVEL_BOTTOM, "register SIM_STATE_CHANGED event");
		/* - snb log */

        IntentFilter intentFilter = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
        mSimReceiver = new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			if("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                    final Bundle  extras    = intent.getExtras();
                    final boolean simReady  = (extras != null) && "READY".equals(extras.getString("ss"));
                    final boolean simLoaded = (extras != null) && "LOADED".equals(extras.getString("ss"));
                    if( simReady ){
						/* + snb log */
                        log(_TAG + LOG_LEVEL_BOTTOM, "SIM is ready. Checking access rules for updates.");
						/* - snb log */
                        mServiceHandler.sendMessage(MSG_LOAD_UICC_RULES, 5);
                    }
                    else if( simLoaded){
						/* + snb log */
                        log(_TAG + LOG_LEVEL_BOTTOM, "SIM is loaded. Checking access rules for updates.");
						/* - snb log */
                        mServiceHandler.sendMessage(MSG_LOAD_UICC_RULES, 5);
                    }
    			}
			}
        };
        
        context.registerReceiver(mSimReceiver, intentFilter);
    }    
    private void unregisterSimStateChangedEvent(Context context) {
    	if(mSimReceiver!= null) {
			/* + snb log */
            log(_TAG + LOG_LEVEL_BOTTOM, "unregister SIM_STATE_CHANGED event");
			/* - snb log */
            context.unregisterReceiver(mSimReceiver);
            mSimReceiver = null;
        }
    }
    

    private void registerAdapterStateChangedEvent(Context context) {
		/* + snb log */
        log(_TAG + LOG_LEVEL_BOTTOM, "register ADAPTER_STATE_CHANGED event");
		/* - snb log */
        IntentFilter intentFilter = new IntentFilter("android.nfc.action.ADAPTER_STATE_CHANGED");
        mNfcReceiver = new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    		    final boolean nfcAdapterAction = intent.getAction().equals("android.nfc.action.ADAPTER_STATE_CHANGED");
    		    final boolean nfcAdapterOn = nfcAdapterAction && intent.getIntExtra("android.nfc.extra.ADAPTER_STATE", 1) == 3; // is NFC Adapter turned on ?		
    		    if( nfcAdapterOn){
					/* + snb log */
    		    	log(_TAG + LOG_LEVEL_BOTTOM, "NFC Adapter is ON. Checking access rules for updates.");
					/* - snb log */
    		    	mServiceHandler.sendMessage(MSG_LOAD_ESE_RULES, 5);
		    	}
    		}
        };
        context.registerReceiver(mNfcReceiver, intentFilter);
    }    
    
    private void unregisterAdapterStateChangedEvent(Context context) {
        if(mNfcReceiver!= null) {
			/* + snb log */
            log(_TAG + LOG_LEVEL_BOTTOM, "unregister ADAPTER_STATE_CHANGED event");
			/* - snb log */
            context.unregisterReceiver(mNfcReceiver);
            mNfcReceiver = null;
        }
     }    
    
    private void registerMediaMountedEvent(Context context) {
		/* + snb log */
        log(_TAG + LOG_LEVEL_BOTTOM, "register MEDIA_MOUNTED event");
		/* - snb log */
        IntentFilter intentFilter = new IntentFilter("android.intent.action.MEDIA_MOUNTED");
        mMediaReceiver = new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    		    final boolean mediaMounted = intent.getAction().equals("android.intent.action.MEDIA_MOUNTED");
    		    if( mediaMounted){
					/* + snb log */
    		    	log(_TAG + LOG_LEVEL_BOTTOM, "New Media is mounted. Checking access rules for updates.");
					/* - snb log */
    		    	 mServiceHandler.sendMessage(MSG_LOAD_SD_RULES, 5);
		    	 }
    		}
        };
        context.registerReceiver(mMediaReceiver, intentFilter);
    }    
    
    private void unregisterMediaMountedEvent(Context context) {
        if(mMediaReceiver != null) {
			/* + snb log */
            log(_TAG + LOG_LEVEL_BOTTOM, "unregister MEDIA_MOUNTED event");
			/* - snb log */
            context.unregisterReceiver(mMediaReceiver);
            mMediaReceiver = null;
        }
     }
    
    /**
     * Initalizes Access Control.
     * At least the refresh tag is read and if it differs to the previous one (e.g. is null) the all
     * access rules are read.
     * 
     * @param se
     */
    public boolean initializeAccessControl(String se, ISmartcardServiceCallback callback ) {
    	return initializeAccessControl(false, se, callback);
	}
	
	public synchronized boolean initializeAccessControl(boolean reset, String se, ISmartcardServiceCallback callback ) {
		boolean result = true;
		/* + snb log */
    	log(_TAG + LOG_LEVEL_MIDDLE, "Initializing Access Control start");
    	/* - snb log */
    	if( callback == null ) {
    		callback = new ISmartcardServiceCallback.Stub(){};
    	}

        Collection<ITerminal>col = mTerminals.values();
        Iterator<ITerminal> iter = col.iterator();
        while(iter.hasNext()){
        	ITerminal terminal = iter.next();
        	if( terminal == null ){

        		continue;
        	}

        		if( se == null || terminal.getName().startsWith(se)) {
        			boolean isCardPresent = false;
					//add by sbt start
                	try {
						/* + snb log */
				    	log(_TAG + LOG_LEVEL_MIDDLE, "isCardPresent start" + terminal.getName());
						/* - snb log */
                		isCardPresent = terminal.isCardPresent();
						/* + snb log */
				    	log(_TAG + LOG_LEVEL_MIDDLE, "isCardPresent end" + terminal.getName());
						/* - snb log */
        			} catch (CardException e) {
        				isCardPresent = false;
						/* + snb */
        				e.printStackTrace();
						/* - snb */
					}
					//add by sbt end

                	if(isCardPresent) {
						/* + snb log */
				    	log(_TAG + LOG_LEVEL_MIDDLE, "Initializing Access Control for " + terminal.getName());
						/* - snb log */
				    	if(reset) terminal.resetAccessControl();
				    	result &= terminal.initializeAccessControl(true, callback);
        			} else {
						/* + snb log */
    			    	log(_TAG + LOG_LEVEL_MIDDLE, "NOT initializing Access Control for " + terminal.getName() + " SE not present.");
						/* - snb log */
        			}
        		}
        }
        col = this.mAddOnTerminals.values();
        iter = col.iterator();
        while(iter.hasNext()){
        	ITerminal terminal = iter.next();
        	if( terminal == null ){

        		continue;
        	}

    		if( se == null || terminal.getName().startsWith(se)) {
    			boolean isCardPresent = false;
            	try {
            		isCardPresent = terminal.isCardPresent();
    			} catch (CardException e) {
    				isCardPresent = false;
					/* + snb */
    				e.printStackTrace();
					/* + snb */
				}

            	if(isCardPresent) {
					/* + snb log */
			    	log(_TAG + LOG_LEVEL_MIDDLE, "Initializing Access Control for " + terminal.getName());
					/* - snb log */
			    	if(reset) terminal.resetAccessControl();
			    	result &= terminal.initializeAccessControl(true, callback);
    			} else {
					/* + snb log */
			    	log(_TAG + LOG_LEVEL_MIDDLE, "NOT initializing Access Control for " + terminal.getName() + " SE not present.");
					/* - snb log */
    			}
    		}
        }
		/* + snb log */
    	log(_TAG + LOG_LEVEL_MIDDLE, "Initializing Access Control end");
		/* - snb log */
        return result;
	}

	public void onDestroy() {
		/* + snb log */
        log(_TAG + LOG_LEVEL_BOTTOM, " smartcard service onDestroy ...");
		/* - snb log */
        for (ITerminal terminal : mTerminals.values())
            terminal.closeChannels();
        for (ITerminal terminal : mAddOnTerminals.values())
            terminal.closeChannels();

        // Cancel the inialization background task if still running
        if(mInitialiseTask != null) mInitialiseTask.cancel(true);
        mInitialiseTask = null;

        // Unregister all the broadcast receivers
        unregisterSimStateChangedEvent(getApplicationContext()) ;
        unregisterAdapterStateChangedEvent(getApplicationContext());
        unregisterMediaMountedEvent(getApplicationContext());
        
        mServiceHandler = null;        
        /* + snb log */
        log(_TAG + LOG_LEVEL_BOTTOM, Thread.currentThread().getName()
                + " ... smartcard service onDestroy");
		/* - snb log */
		/* + snb restart service */
        Intent serviceIntent = new Intent(mContext, org.simalliance.openmobileapi.service.SmartcardService.class );
        mContext.startService(serviceIntent);
		/* - snb restart service */
	}

	private ITerminal getTerminal(String reader, SmartcardError error) {
        if (reader == null) {
            setError(error, NullPointerException.class, "reader must not be null");
            return null;
        }
        ITerminal terminal = mTerminals.get(reader);
        if (terminal == null) {
            terminal = mAddOnTerminals.get(reader);
            if (terminal == null) {
                setError(error, IllegalArgumentException.class, "unknown reader");
            }
        }
        return terminal;
    }

    private String[] createTerminals() {
        createBuildinTerminals();

        Set<String> names = mTerminals.keySet();
        ArrayList<String> list = new ArrayList<String>(names);
        Collections.sort(list);
        
        // set UICC on the top
        if(list.remove(_UICC_TERMINAL + " - UICC")) 
        	list.add(0, _UICC_TERMINAL + " - UICC");

        createAddonTerminals();
        names = mAddOnTerminals.keySet();
        for (String name : names) {
            if (!list.contains(name)) {
                list.add(name);
            }
        }
        
        return list.toArray(new String[list.size()]);
    }

    private String[] updateTerminals() {
        Set<String> names = mTerminals.keySet();
        ArrayList<String> list = new ArrayList<String>(names);
        Collections.sort(list);
        
        // set UICC on the top
        if(list.remove(_UICC_TERMINAL + " - UICC")) 
        	list.add(0, _UICC_TERMINAL + " - UICC");
        
        updateAddonTerminals();
        names = mAddOnTerminals.keySet();
        for (String name : names) {
            if (!list.contains(name)) {
                list.add(name);
            }
        }
        
        return list.toArray(new String[list.size()]);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void createBuildinTerminals() {
        Class[] types = new Class[] {
            Context.class
        };
        Object[] args = new Object[] {
            this
        };
        Object[] classes = getBuildinTerminalClasses();
        for (Object clazzO : classes) {
            try {
                Class clazz = (Class) clazzO;
                Constructor constr = clazz.getDeclaredConstructor(types);
                
                ITerminal terminal = (ITerminal) constr.newInstance(args);
                mTerminals.put(terminal.getName(), terminal);
				/* + snb log */
                log(_TAG + LOG_LEVEL_MIDDLE, Thread.currentThread().getName() + " adding "
                        + terminal.getName());
				/* - snb log */
            } catch (Throwable t) {
				/* + snb log */
                log(_TAG + LOG_LEVEL_MIDDLE, Thread.currentThread().getName()
                        + " CreateReaders Error: "
                        + ((t.getMessage() != null) ? t.getMessage() : "unknown"));
				/* - snb log */
            }
        }
    }

    private void createAddonTerminals() {
        String[] packageNames = AddonTerminal.getPackageNames(this);
        for (String packageName : packageNames) {
			/* + snb dex close */
        	DexFile dexFile = null;
			/* - snb dex close */
            try {
                String apkName = getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
                dexFile = new DexFile(apkName);
                Enumeration<String> classFileNames = dexFile.entries();
                while (classFileNames.hasMoreElements()) {
                    String className = classFileNames.nextElement();
                    if (className.endsWith("Terminal")) {
                        ITerminal terminal = new AddonTerminal(this, packageName, className);
                        mAddOnTerminals.put(terminal.getName(), terminal);
						/* + snb log */
                        log(_TAG + LOG_LEVEL_MIDDLE, Thread.currentThread().getName() + " adding "
                                + terminal.getName());
						/* - snb log */
                    }
                }
            } catch (Throwable t) {
				/* + snb log */
                log(_TAG + LOG_LEVEL_MIDDLE, Thread.currentThread().getName()
                        + " CreateReaders Error: "
                        + ((t.getMessage() != null) ? t.getMessage() : "unknown"));
				/* - snb log */
			/* + snb dex close */
            }finally{
            	try {
            		if(dexFile != null)
					dexFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			/* - snb dex close */
            }
        }
    }

    private void updateAddonTerminals() {
        Set<String> names = mAddOnTerminals.keySet();
        ArrayList<String> namesToRemove = new ArrayList<String>();
        for (String name : names) {
            ITerminal terminal = mAddOnTerminals.get(name);
            if (!terminal.isConnected()) {
                namesToRemove.add(terminal.getName());
            }
        }
        for (String name : namesToRemove) {
            mAddOnTerminals.remove(name);
        }

        String[] packageNames = AddonTerminal.getPackageNames(this);
        for (String packageName : packageNames) {
			/* + snb dex close */
        	DexFile dexFile = null;
			/* - snb dex close */
            try {
                String apkName = getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
                dexFile = new DexFile(apkName);
                Enumeration<String> classFileNames = dexFile.entries();
                while (classFileNames.hasMoreElements()) {
                    String className = classFileNames.nextElement();
                    if (className.endsWith("Terminal")) {
                        ITerminal terminal = new AddonTerminal(this, packageName, className);
                        if (!mAddOnTerminals.containsKey(terminal.getName())) {
                            mAddOnTerminals.put(terminal.getName(), terminal);
							/* + snb log */
                            log(_TAG + LOG_LEVEL_MIDDLE, Thread.currentThread().getName()
                                    + " adding " + terminal.getName());
							/* - snb log */
                        }
                    }
                }

            } catch (Throwable t) {
				/* + snb log */
                log(_TAG + LOG_LEVEL_MIDDLE, Thread.currentThread().getName()
                        + " CreateReaders Error: "
                        + ((t.getMessage() != null) ? t.getMessage() : "unknown"));
				/* - snb log */
			/* + snb dex close */
            }finally{
            	try {
            		if(dexFile != null)
					dexFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			/* - snb dex close */
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Object[] getBuildinTerminalClasses() {
        ArrayList classes = new ArrayList();
		/* + snb dex close */
        DexFile dexFile = null;
		/* - snb dex close */
        try {
            String packageName = "org.simalliance.openmobileapi.service";
            String apkName = getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
            DexClassLoader dexClassLoader = new DexClassLoader(apkName, getApplicationContext().getFilesDir().getAbsolutePath(), null, getClass()
                    .getClassLoader());

            Class terminalClass = Class.forName("org.simalliance.openmobileapi.service.Terminal", true, dexClassLoader);
            if (terminalClass == null) {
                return classes.toArray();
            }

            dexFile = new DexFile(apkName);
            Enumeration<String> classFileNames = dexFile.entries();
            while (classFileNames.hasMoreElements()) {
                String className = classFileNames.nextElement();
                /* + snb move try catch inside while block */
				/* + snb log */
                log(_TAG + LOG_LEVEL_MIDDLE, Thread.currentThread().getName() + " getBuildinTerminalClasses adding "
                        + className);
				/* - snb log */
                try{
                /* - snb move try catch inside while block */
                Class clazz = Class.forName(className);
                Class superClass = clazz.getSuperclass();
                if (superClass != null && superClass.equals(terminalClass)
                        && !className.equals("org.simalliance.openmobileapi.service.AddonTerminal")) {
                    classes.add(clazz);
                }
                /* + snb move try catch inside while block */
                }catch(Throwable exp){
                	exp.printStackTrace();
                }
                /* - snb move try catch inside while block */
            }
        } catch (Throwable exp) {
			/* + snb */
        	exp.printStackTrace();
			/* + snb */
            // nothing to to
		/* + snb dex close */
        }finally{
        	try {
        		if(dexFile != null)
				dexFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		/* - snb dex close */
        }
        return classes.toArray();
    }

    /**
     * Get package name from the user id.
     *
     * This shall fix the problem the issue that process name != package name
     * due to anndroid:process attribute in manifest file.
     *
     * But this call is not really secure either since a uid can be shared between one
     * and more apks
     *
     * @param uid
     * @return The first package name associated with this uid.
     */
    public String getPackageNameFromCallingUid(int uid ){
       PackageManager packageManager = getPackageManager();
       if(packageManager != null){
               String packageName[] = packageManager.getPackagesForUid(uid);
               if( packageName != null && packageName.length > 0 ){
                       return packageName[0];
               }
       }
       throw new AccessControlException("Caller PackageName can not be determined");
    }
    
    
	/* + snb signature */
    /**
     * Check with package manager if the pkg may use NFCEE.
     * Does not use cache.
     */
    private boolean checkPackageAccess(String pkg) {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES);
            if (info.signatures == null) {
                return false;
            }
            log(_TAG + LOG_LEVEL_MIDDLE, "checkPackageAccess start");
            for (Signature s : info.signatures){
                if (s == null) {
                    continue;
                }
                String[] packages = mNfceeAccess.get(s);
                if (packages == null) {
                    continue;
                }
                if (packages.length == 0) {
                    // wildcard access
                	if (DBG) log(_TAG +LOG_LEVEL_MIDDLE, "Granted NFCEE access to " + pkg + " (wildcard)");
                    return true;
                }
                for (String p : packages) {
                    if (pkg.equals(p)) {
                        // explicit package access
                    	if (DBG) log(_TAG + LOG_LEVEL_MIDDLE, "Granted access to " + pkg + " (explicit)");
                        return true;
                    }
                }
            }
        }catch (NameNotFoundException e) {
            // ignore
        }
    	return false;
    }
	/* - snb signature */
    
    /**
     * The smartcard service interface implementation.
     */
    private final ISmartcardService.Stub mSmartcardBinder = new ISmartcardService.Stub() {

		@Override
        public String[] getReaders(SmartcardError error) throws RemoteException {
            clearError(error);
			/* + snb log */
            log(_TAG + LOG_LEVEL_MIDDLE, "getReaders()"); 
			/* + snb log */
            return updateTerminals();
        }
        
		@Override
		public ISmartcardServiceReader getReader(String reader,
				SmartcardError error) throws RemoteException {
        	clearError(error);
			Terminal terminal = (Terminal)getTerminal(reader, error);
			if( terminal != null ){
				return terminal.new SmartcardServiceReader(SmartcardService.this);
			}
            setError(error, IllegalArgumentException.class, "invalid reader name");
			return null;
		}		
        

		@Override
        public synchronized boolean[] isNFCEventAllowed(
        		String reader, 
        		byte[] aid,
                String[] packageNames, 
                ISmartcardServiceCallback callback, 
                SmartcardError error) 
                		throws RemoteException
        {
        	clearError(error);
            try
            {
                if (callback == null) {
                    setError(error, NullPointerException.class, "callback must not be null");
                    return null;
                }
                ITerminal terminal = getTerminal(reader, error);
                if (terminal == null) {
                    return null;
                }
                if (aid == null || aid.length == 0) {
                    aid = new byte[] {
                            0x00, 0x00, 0x00, 0x00, 0x00
                    };
                }
                if (aid.length < 5 || aid.length > 16) {
                     setError(error, IllegalArgumentException.class, "AID out of range");
                     return null;
                }
                if (packageNames == null || packageNames.length == 0) {
                     setError(error, IllegalArgumentException.class, "process names not specified");
                     return null;
                }
                AccessControlEnforcer ac = null;
                if( terminal.getAccessControlEnforcer() == null ) {
                	ac = new AccessControlEnforcer( terminal );
                } else {
                	ac = terminal.getAccessControlEnforcer();
                }
	            ac.setPackageManager(getPackageManager());
	            ac.initialize(true, callback);
	            return ac.isNFCEventAllowed(aid, packageNames, callback );
            } catch (Exception e) {
                setError(error, e);
				/* + snb log */
                log(_TAG + LOG_LEVEL_BOTTOM, "isNFCEventAllowed Exception: " + e.getMessage() );
				/* + snb log */
                return null;
            }
        }
    };

    /**
     * The smartcard service interface implementation.
     */
    final class SmartcardServiceSession extends ISmartcardServiceSession.Stub {

    	private final SmartcardServiceReader mReader;
        /** List of open channels in use of by this client. */
        private final Set<Channel> mChannels = new HashSet<Channel>();
        
        private final Object mLock = new Object();

        private boolean mIsClosed;

        private byte[] mAtr;
    	
    	public SmartcardServiceSession(SmartcardServiceReader reader){
    		mReader = reader;
    		mAtr = mReader.getAtr();
    		mIsClosed = false;
    	}
    	
		@Override
		public ISmartcardServiceReader getReader() throws RemoteException {
			return mReader;
		}

		@Override
		public byte[] getAtr() throws RemoteException {
			return mAtr;
        }

		@Override
		public void close(SmartcardError error) throws RemoteException {
            clearError(error);
            if (mReader == null) {
                return;
            }
            try {
				mReader.closeSession(this);
			} catch (CardException e) {
				setError(error,e);
			}
		}

		@Override
		public void closeChannels(SmartcardError error) throws RemoteException {
	        synchronized (mLock) {
				/* + snb log */
                log(_TAG + LOG_LEVEL_MIDDLE, "closeChannels start");
				/* + snb log */

	        	Iterator<Channel>iter = mChannels.iterator();
	        	try {
		            while(iter.hasNext()) {
		            	Channel channel = iter.next();
		                if (channel != null && !channel.isClosed()) {
		                    try {
		                        channel.close();
		                        // close changes indirectly mChannels, so we need a new iterator.
		                        iter = mChannels.iterator();
		                    } catch (Exception ignore) {
								/* + snb */
		                    	ignore.printStackTrace();
								/* - snb */
								/* + snb log */
		    	    	        log(_TAG + LOG_LEVEL_BOTTOM, "ServiceSession channel - close Exception " + ignore.getMessage());
								/* - snb log */
		                    }
		                    channel.setClosed();
		                }
		            }
		            mChannels.clear();
	        	} catch( Exception e ) {
					/* + snb */
	        		e.printStackTrace();
					/* - snb */
					/* + snb log */
	    	        log(_TAG + LOG_LEVEL_MIDDLE, "ServiceSession closeChannels Exception " + e.getMessage());
					/* - snb log */
	        	}
				/* + snb log */
                log(_TAG + LOG_LEVEL_MIDDLE, "closeChannels end");
				/* - snb log */
	        }
		}

		@Override
		public boolean isClosed() throws RemoteException {

			return mIsClosed;
		}

		@Override
		public ISmartcardServiceChannel openBasicChannel(
				ISmartcardServiceCallback callback, SmartcardError error)
				throws RemoteException {
            return openBasicChannelAid( null, callback, error);
		}

		@Override
		public ISmartcardServiceChannel openBasicChannelAid(byte[] aid,
				ISmartcardServiceCallback callback, SmartcardError error)
				throws RemoteException {
            clearError(error);
            if ( isClosed() ) {
                setError( error, IllegalStateException.class, "session is closed");
                return null;
            }
            if (callback == null) {
                setError(error, NullPointerException.class, "callback must not be null");
                return null;
            }
            if (mReader == null) {
                setError(error, NullPointerException.class, "reader must not be null");
                return null;
            }

            try {
                boolean noAid = false;
                if (aid == null || aid.length == 0) {
                    aid = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00 };
                    noAid = true;
                }

                if (aid.length < 5 || aid.length > 16) {
                    setError(error, IllegalArgumentException.class, "AID out of range");
                    return null;
                }
				
				/* + snb signature */
                boolean bFullAccess = false;
                PackageManager pm = mContext.getPackageManager();
                String[] pkgs = pm.getPackagesForUid(Binder.getCallingUid());
                //Add special Access Check.                
                try {
                	log(_TAG + LOG_LEVEL_BOTTOM, "sbtoma Access Check, pkg is "+pkgs[0]);
                    mContext.enforceCallingOrSelfPermission(NFC_PERM, NFC_PERM_ERROR);
                    bFullAccess = checkPackageAccess(pkgs[0]);
                }catch (Exception e) {
                    log(_TAG + LOG_LEVEL_BOTTOM, "fail to sbtoma Access Check within openBasicChannelAid");
                }
                

                ChannelAccess channelAccess = null;
                
                if(!bFullAccess) {
				/* - snb signature */
                    String packageName = getPackageNameFromCallingUid( Binder.getCallingUid());
					/* + snb log */
                    log(_TAG + LOG_LEVEL_MIDDLE, "Enable access control on basic channel for " + packageName);
					/* - snb log */
                    channelAccess = mReader.getTerminal().setUpChannelAccess(
                    		getPackageManager(), 
                    		aid,
                    		packageName, 
                            callback );
					/* + snb log */
                    log(_TAG + LOG_LEVEL_MIDDLE, "Access control successfully enabled.");
					/* - snb log */
				/* + snb signature */
                    }
                else
                {
                    channelAccess = new ChannelAccess();
                    channelAccess.setSpecialAccess(bFullAccess);
                }
				/* + snb signature */
                
                channelAccess.setCallingPid(Binder.getCallingPid());

                log(_TAG + LOG_LEVEL_MIDDLE, "OpenBasicChannel(AID)");
                Channel channel = null;
                if (noAid) {
                    channel = mReader.getTerminal().openBasicChannel(this, callback);
                } else {
                    channel = mReader.getTerminal().openBasicChannel(this, aid, callback);
                }

                channel.setChannelAccess(channelAccess);

                /* + snb log */
				log(_TAG + LOG_LEVEL_MIDDLE, "Open basic channel success. Channel: " + channel.getChannelNumber() );
				/* - snb log */
                
                SmartcardServiceChannel basicChannel = channel.new SmartcardServiceChannel(this);
                mChannels.add(channel);
                return basicChannel;
                
            } catch (Exception e) {
                setError(error, e);
				/* + snb log */
                log(_TAG + LOG_LEVEL_BOTTOM, "OpenBasicChannel Exception: " + e.getMessage());
				/* - snb log */
                return null;
            }
		}

		@Override
		public ISmartcardServiceChannel openLogicalChannel(byte[] aid,
				ISmartcardServiceCallback callback, SmartcardError error)
				throws RemoteException {
            clearError(error);

            if ( isClosed() ) {
                setError( error, IllegalStateException.class, "session is closed");
                return null;
            }

            if (callback == null) {
                setError(error, NullPointerException.class, "callback must not be null");
                return null;
            }
            if (mReader == null) {
                setError(error, NullPointerException.class, "reader must not be null");
                return null;
            }

            try {
                boolean noAid = false;
                if (aid == null || aid.length == 0) {
                    aid = new byte[] {
                            0x00, 0x00, 0x00, 0x00, 0x00
                    };
                    noAid = true;
                }

                if (aid.length < 5 || aid.length > 16) {
                    setError(error, IllegalArgumentException.class, "AID out of range");
                    return null;
                }
				

				/* + snb signature */
                boolean bFullAccess = false;
                PackageManager pm = mContext.getPackageManager();
                String[] pkgs = pm.getPackagesForUid(Binder.getCallingUid());
                //Add special Access Check.                
                try {
                	log(_TAG + LOG_LEVEL_BOTTOM, "special Access Check, pkg is "+pkgs[0]);
                    mContext.enforceCallingOrSelfPermission(NFC_PERM, NFC_PERM_ERROR);
                    bFullAccess = checkPackageAccess(pkgs[0]);
                }catch (Exception e) {
                    log(_TAG + LOG_LEVEL_BOTTOM, "fail to special Access Check within openBasicChannelAid");
                }
                
                ChannelAccess channelAccess = null;
	            if(!bFullAccess) {
				/* - snb signature */
	                String packageName = getPackageNameFromCallingUid( Binder.getCallingUid());
					/* + snb log */
	                log(_TAG + LOG_LEVEL_MIDDLE, "Enable access control on logical channel for " + packageName);
					/* - snb log */
	                channelAccess = mReader.getTerminal().setUpChannelAccess(
	                		getPackageManager(), 
	                		aid,
	                		packageName, 
	                        callback );
					/* + snb log */
	                log(_TAG + LOG_LEVEL_MIDDLE, "Access control successfully enabled.");
					/* - snb log */
				/* + snb signature */
                }
                else
                {
                	channelAccess = new ChannelAccess();
                	channelAccess.setSpecialAccess(bFullAccess);
                }
				/* + snb signature */
                
               channelAccess.setCallingPid(Binder.getCallingPid());

				/* + snb log */
                log(_TAG + LOG_LEVEL_MIDDLE, "OpenLogicalChannel");
				/* - snb log */
                Channel channel = null;
                if (noAid) {
                    channel = mReader.getTerminal().openLogicalChannel(this, callback);
                } else {
                    channel = mReader.getTerminal().openLogicalChannel(this, aid, callback);
                }

                channel.setChannelAccess(channelAccess);

                /* + snb log */
				log(_TAG + LOG_LEVEL_MIDDLE, "Open logical channel successfull. Channel: " + channel.getChannelNumber());
				/* - snb log */
                SmartcardServiceChannel logicalChannel = channel.new SmartcardServiceChannel(this);
                mChannels.add(channel);
                return logicalChannel;
            } catch (Exception e) {
                setError(error, e);
				/* + snb log */
                log(_TAG + LOG_LEVEL_BOTTOM, "OpenLogicalChannel Exception: " + e.getMessage());
				/* - snb log */
                return null;
            }
		}
		
		void setClosed(){
			mIsClosed = true;

		}
    	
	    /**
	     * Closes the specified channel. <br>
	     * After calling this method the session can not be used for the
	     * communication with the secure element any more.
	     * 
	     * @param hChannel the channel handle obtained by an open channel command.
	     */
	    void removeChannel(Channel channel) {
	        if (channel == null) {
	        	return;
	        }
            mChannels.remove(channel);
	    }
    }
    
    /*
     * Handler Thread used to load and initiate ChannelAccess condition
     */
    public final static int MSG_LOAD_UICC_RULES  = 1;
    public final static int MSG_LOAD_ESE_RULES   = 2;
    public final static int MSG_LOAD_SD_RULES    = 3;

    public final static int NUMBER_OF_TRIALS      = 3;
    public final static long WAIT_TIME            = 1000;

    private final class ServiceHandler extends Handler {

        @SuppressLint("HandlerLeak")
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        public void sendMessage(int what, int nbTries) {
           mServiceHandler.removeMessages(what);
           Message newMsg = mServiceHandler.obtainMessage(what, nbTries, 0);
           mServiceHandler.sendMessage(newMsg);
        }

        @Override
        public void handleMessage(Message msg) {
           boolean result = true;

           /* + snb log */
		   log(_TAG + LOG_LEVEL_MIDDLE, "Handle msg: what=" + msg.what + " nbTries=" + msg.arg1);
		   /* - snb log */

           switch(msg.what) {
           case MSG_LOAD_UICC_RULES:
               try {
                   result = initializeAccessControl(true, _UICC_TERMINAL, null );
               } catch (Exception e) {
                   /* + snb log */
				   log(_TAG + LOG_LEVEL_MIDDLE, "Got exception:" + e);
				   /* - snb log */
               }
               break;

           case MSG_LOAD_ESE_RULES:
               try {
                   result = initializeAccessControl(true, _eSE_TERMINAL, null );
               } catch (Exception e) {
                   /* + snb log */
				   log(_TAG + LOG_LEVEL_MIDDLE, "Got exception:" + e);
				   /* - snb log */
               }
               break;

           case MSG_LOAD_SD_RULES:
               try {
                   result = initializeAccessControl(true, _SD_TERMINAL, null );
               } catch (Exception e) {
                   /* + snb log */
				   log(_TAG + LOG_LEVEL_MIDDLE, "Got exception:" + e);
				   /* - snb log */
               }
               break;
           }

           if(!result && msg.arg1 > 0) {
               // Try to re-post the message
			   /* + snb log */
               log(_TAG + LOG_LEVEL_MIDDLE, "Fail to load rules: Let's try another time (" + msg.arg1 + " remaining attempt");
			   /* - snb log */
               Message newMsg = mServiceHandler.obtainMessage(msg.what, msg.arg1 - 1, 0);
               mServiceHandler.sendMessageDelayed(newMsg, WAIT_TIME);
           }
        }
    }
    
    /* + snb log */
    public static void log(String tag,String message){
    	if (tag.endsWith(LOG_LEVEL_BOTTOM)) {
			Log.i(_TAG, message);
		}
    	if (level != null) {
    		if (tag.endsWith(LOG_LEVEL_MIDDLE)) {
    			if (level.equals("1") || level.equals("2")) {
    				Log.i(_TAG, message);
    			}
    		}
    		if (tag.endsWith(LOG_LEVEL_TOP)) {
    			if (level.equals("2")) {
    				Log.i(_TAG, message);
    			}
    		}
		}
    }
    
    public static void log(String tag,String message,Throwable e){
    	if (tag.endsWith(LOG_LEVEL_BOTTOM)) {
			Log.i(_TAG, message,e);
		}
    	if (level != null) {
    		if (tag.endsWith(LOG_LEVEL_MIDDLE)) {
    			if (level == "1" || level == "2") {
    				Log.i(_TAG, message,e);
    			}
    		}
    		if (tag.endsWith(LOG_LEVEL_TOP)) {
    			if (level == "2") {
    				Log.i(_TAG, message,e);
    			}
    		}
		}
    }
    /* - snb log */
}
