/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Project\\mycode\\oma\\oma\\SmartCardService\\openmobileapi\\src\\org\\simalliance\\openmobileapi\\service\\ISmartcardServiceSession.aidl
 */
package org.simalliance.openmobileapi.service;
public interface ISmartcardServiceSession extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.simalliance.openmobileapi.service.ISmartcardServiceSession
{
private static final java.lang.String DESCRIPTOR = "org.simalliance.openmobileapi.service.ISmartcardServiceSession";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.simalliance.openmobileapi.service.ISmartcardServiceSession interface,
 * generating a proxy if needed.
 */
public static org.simalliance.openmobileapi.service.ISmartcardServiceSession asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.simalliance.openmobileapi.service.ISmartcardServiceSession))) {
return ((org.simalliance.openmobileapi.service.ISmartcardServiceSession)iin);
}
return new org.simalliance.openmobileapi.service.ISmartcardServiceSession.Stub.Proxy(obj);
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
case TRANSACTION_getReader:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.ISmartcardServiceReader _result = this.getReader();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_getAtr:
{
data.enforceInterface(DESCRIPTOR);
byte[] _result = this.getAtr();
reply.writeNoException();
reply.writeByteArray(_result);
return true;
}
case TRANSACTION_close:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.SmartcardError _arg0;
_arg0 = new org.simalliance.openmobileapi.service.SmartcardError();
this.close(_arg0);
reply.writeNoException();
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_closeChannels:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.SmartcardError _arg0;
_arg0 = new org.simalliance.openmobileapi.service.SmartcardError();
this.closeChannels(_arg0);
reply.writeNoException();
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_isClosed:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isClosed();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_openBasicChannel:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.ISmartcardServiceCallback _arg0;
_arg0 = org.simalliance.openmobileapi.service.ISmartcardServiceCallback.Stub.asInterface(data.readStrongBinder());
org.simalliance.openmobileapi.service.SmartcardError _arg1;
_arg1 = new org.simalliance.openmobileapi.service.SmartcardError();
org.simalliance.openmobileapi.service.ISmartcardServiceChannel _result = this.openBasicChannel(_arg0, _arg1);
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
case TRANSACTION_openBasicChannelAid:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
org.simalliance.openmobileapi.service.ISmartcardServiceCallback _arg1;
_arg1 = org.simalliance.openmobileapi.service.ISmartcardServiceCallback.Stub.asInterface(data.readStrongBinder());
org.simalliance.openmobileapi.service.SmartcardError _arg2;
_arg2 = new org.simalliance.openmobileapi.service.SmartcardError();
org.simalliance.openmobileapi.service.ISmartcardServiceChannel _result = this.openBasicChannelAid(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_openLogicalChannel:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
org.simalliance.openmobileapi.service.ISmartcardServiceCallback _arg1;
_arg1 = org.simalliance.openmobileapi.service.ISmartcardServiceCallback.Stub.asInterface(data.readStrongBinder());
org.simalliance.openmobileapi.service.SmartcardError _arg2;
_arg2 = new org.simalliance.openmobileapi.service.SmartcardError();
org.simalliance.openmobileapi.service.ISmartcardServiceChannel _result = this.openLogicalChannel(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.simalliance.openmobileapi.service.ISmartcardServiceSession
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
     * Get the reader that provides this session.
     * 
     * @return The Reader object.
     */
@Override public org.simalliance.openmobileapi.service.ISmartcardServiceReader getReader() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.simalliance.openmobileapi.service.ISmartcardServiceReader _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getReader, _data, _reply, 0);
_reply.readException();
_result = org.simalliance.openmobileapi.service.ISmartcardServiceReader.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * Returns the ATR of the connected card or null if the ATR is not available.
	 */
@Override public byte[] getAtr() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
byte[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAtr, _data, _reply, 0);
_reply.readException();
_result = _reply.createByteArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Close the connection with the Secure Element. This will close any
     * channels opened by this application with this Secure Element.
     */
@Override public void close(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_close, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Close any channel opened on this session.
     */
@Override public void closeChannels(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_closeChannels, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Tells if this session is closed.
     * 
     * @return <code>true</code> if the session is closed, false otherwise.
     */
@Override public boolean isClosed() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isClosed, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Opens a connection using the basic channel of the card in the
     * specified reader and returns a channel handle.
     * Logical channels cannot be opened with this connection.
     * Use interface method openLogicalChannel() to open a logical channel.
     */
@Override public org.simalliance.openmobileapi.service.ISmartcardServiceChannel openBasicChannel(org.simalliance.openmobileapi.service.ISmartcardServiceCallback callback, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.simalliance.openmobileapi.service.ISmartcardServiceChannel _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_openBasicChannel, _data, _reply, 0);
_reply.readException();
_result = org.simalliance.openmobileapi.service.ISmartcardServiceChannel.Stub.asInterface(_reply.readStrongBinder());
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
     * Opens a connection using the basic channel of the card in the
     * specified reader and returns a channel handle. Selects the specified applet.
     * Logical channels cannot be opened with this connection.
     * Selection of other applets with this connection is not supported.
     * Use interface method openLogicalChannel() to open a logical channel.
     */
@Override public org.simalliance.openmobileapi.service.ISmartcardServiceChannel openBasicChannelAid(byte[] aid, org.simalliance.openmobileapi.service.ISmartcardServiceCallback callback, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.simalliance.openmobileapi.service.ISmartcardServiceChannel _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(aid);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_openBasicChannelAid, _data, _reply, 0);
_reply.readException();
_result = org.simalliance.openmobileapi.service.ISmartcardServiceChannel.Stub.asInterface(_reply.readStrongBinder());
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
     * Opens a connection using the next free logical channel of the card in the
     * specified reader. Selects the specified applet.
     * Selection of other applets with this connection is not supported.
     */
@Override public org.simalliance.openmobileapi.service.ISmartcardServiceChannel openLogicalChannel(byte[] aid, org.simalliance.openmobileapi.service.ISmartcardServiceCallback callback, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.simalliance.openmobileapi.service.ISmartcardServiceChannel _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(aid);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_openLogicalChannel, _data, _reply, 0);
_reply.readException();
_result = org.simalliance.openmobileapi.service.ISmartcardServiceChannel.Stub.asInterface(_reply.readStrongBinder());
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
static final int TRANSACTION_getReader = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getAtr = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_close = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_closeChannels = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_isClosed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_openBasicChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_openBasicChannelAid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_openLogicalChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
}
/**
     * Get the reader that provides this session.
     * 
     * @return The Reader object.
     */
public org.simalliance.openmobileapi.service.ISmartcardServiceReader getReader() throws android.os.RemoteException;
/**
	 * Returns the ATR of the connected card or null if the ATR is not available.
	 */
public byte[] getAtr() throws android.os.RemoteException;
/**
     * Close the connection with the Secure Element. This will close any
     * channels opened by this application with this Secure Element.
     */
public void close(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Close any channel opened on this session.
     */
public void closeChannels(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Tells if this session is closed.
     * 
     * @return <code>true</code> if the session is closed, false otherwise.
     */
public boolean isClosed() throws android.os.RemoteException;
/**
     * Opens a connection using the basic channel of the card in the
     * specified reader and returns a channel handle.
     * Logical channels cannot be opened with this connection.
     * Use interface method openLogicalChannel() to open a logical channel.
     */
public org.simalliance.openmobileapi.service.ISmartcardServiceChannel openBasicChannel(org.simalliance.openmobileapi.service.ISmartcardServiceCallback callback, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Opens a connection using the basic channel of the card in the
     * specified reader and returns a channel handle. Selects the specified applet.
     * Logical channels cannot be opened with this connection.
     * Selection of other applets with this connection is not supported.
     * Use interface method openLogicalChannel() to open a logical channel.
     */
public org.simalliance.openmobileapi.service.ISmartcardServiceChannel openBasicChannelAid(byte[] aid, org.simalliance.openmobileapi.service.ISmartcardServiceCallback callback, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Opens a connection using the next free logical channel of the card in the
     * specified reader. Selects the specified applet.
     * Selection of other applets with this connection is not supported.
     */
public org.simalliance.openmobileapi.service.ISmartcardServiceChannel openLogicalChannel(byte[] aid, org.simalliance.openmobileapi.service.ISmartcardServiceCallback callback, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
}
