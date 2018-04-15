/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Project\\mycode\\oma\\oma\\SmartCardService\\openmobileapi\\src\\org\\simalliance\\openmobileapi\\service\\ISmartcardService.aidl
 */
package org.simalliance.openmobileapi.service;
/**
 * Smartcard service interface.
 */
public interface ISmartcardService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.simalliance.openmobileapi.service.ISmartcardService
{
private static final java.lang.String DESCRIPTOR = "org.simalliance.openmobileapi.service.ISmartcardService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.simalliance.openmobileapi.service.ISmartcardService interface,
 * generating a proxy if needed.
 */
public static org.simalliance.openmobileapi.service.ISmartcardService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.simalliance.openmobileapi.service.ISmartcardService))) {
return ((org.simalliance.openmobileapi.service.ISmartcardService)iin);
}
return new org.simalliance.openmobileapi.service.ISmartcardService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getReaders:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.SmartcardError _arg0;
_arg0 = new org.simalliance.openmobileapi.service.SmartcardError();
java.lang.String[] _result = this.getReaders(_arg0);
reply.writeNoException();
reply.writeStringArray(_result);
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getReader:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
org.simalliance.openmobileapi.service.SmartcardError _arg1;
_arg1 = new org.simalliance.openmobileapi.service.SmartcardError();
org.simalliance.openmobileapi.service.ISmartcardServiceReader _result = this.getReader(_arg0, _arg1);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
if ((_arg1!=null)) {
reply.writeInt(1);
_arg1.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_isNFCEventAllowed:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
byte[] _arg1;
_arg1 = data.createByteArray();
java.lang.String[] _arg2;
_arg2 = data.createStringArray();
org.simalliance.openmobileapi.service.ISmartcardServiceCallback _arg3;
_arg3 = org.simalliance.openmobileapi.service.ISmartcardServiceCallback.Stub.asInterface(data.readStrongBinder());
org.simalliance.openmobileapi.service.SmartcardError _arg4;
_arg4 = new org.simalliance.openmobileapi.service.SmartcardError();
boolean[] _result = this.isNFCEventAllowed(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
reply.writeBooleanArray(_result);
if ((_arg4!=null)) {
reply.writeInt(1);
_arg4.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.simalliance.openmobileapi.service.ISmartcardService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
     * Returns the friendly names of available smart card readers.
     */
@Override public java.lang.String[] getReaders(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getReaders, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Returns Smartcard Service reader object to the given name.
     */
@Override public org.simalliance.openmobileapi.service.ISmartcardServiceReader getReader(java.lang.String reader, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.simalliance.openmobileapi.service.ISmartcardServiceReader _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(reader);
mRemote.transact(Stub.TRANSACTION_getReader, _data, _reply, 0);
_reply.readException();
_result = org.simalliance.openmobileapi.service.ISmartcardServiceReader.Stub.asInterface(_reply.readStrongBinder());
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Checks if the application defined by the package name is allowed to receive 
     * NFC transaction events for the defined AID. 
     */
@Override public boolean[] isNFCEventAllowed(java.lang.String reader, byte[] aid, java.lang.String[] packageNames, org.simalliance.openmobileapi.service.ISmartcardServiceCallback callback, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(reader);
_data.writeByteArray(aid);
_data.writeStringArray(packageNames);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_isNFCEventAllowed, _data, _reply, 0);
_reply.readException();
_result = _reply.createBooleanArray();
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getReaders = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getReader = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isNFCEventAllowed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
/**
     * Returns the friendly names of available smart card readers.
     */
public java.lang.String[] getReaders(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Returns Smartcard Service reader object to the given name.
     */
public org.simalliance.openmobileapi.service.ISmartcardServiceReader getReader(java.lang.String reader, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Checks if the application defined by the package name is allowed to receive 
     * NFC transaction events for the defined AID. 
     */
public boolean[] isNFCEventAllowed(java.lang.String reader, byte[] aid, java.lang.String[] packageNames, org.simalliance.openmobileapi.service.ISmartcardServiceCallback callback, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
}
