/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Project\\mycode\\oma\\oma\\SmartCardService\\openmobileapi\\src\\org\\simalliance\\openmobileapi\\service\\ISmartcardServiceChannel.aidl
 */
package org.simalliance.openmobileapi.service;
public interface ISmartcardServiceChannel extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.simalliance.openmobileapi.service.ISmartcardServiceChannel
{
private static final java.lang.String DESCRIPTOR = "org.simalliance.openmobileapi.service.ISmartcardServiceChannel";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.simalliance.openmobileapi.service.ISmartcardServiceChannel interface,
 * generating a proxy if needed.
 */
public static org.simalliance.openmobileapi.service.ISmartcardServiceChannel asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.simalliance.openmobileapi.service.ISmartcardServiceChannel))) {
return ((org.simalliance.openmobileapi.service.ISmartcardServiceChannel)iin);
}
return new org.simalliance.openmobileapi.service.ISmartcardServiceChannel.Stub.Proxy(obj);
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
case TRANSACTION_isClosed:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isClosed();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isBasicChannel:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isBasicChannel();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getSelectResponse:
{
data.enforceInterface(DESCRIPTOR);
byte[] _result = this.getSelectResponse();
reply.writeNoException();
reply.writeByteArray(_result);
return true;
}
case TRANSACTION_getSession:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.ISmartcardServiceSession _result = this.getSession();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_transmit:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
org.simalliance.openmobileapi.service.SmartcardError _arg1;
_arg1 = new org.simalliance.openmobileapi.service.SmartcardError();
byte[] _result = this.transmit(_arg0, _arg1);
reply.writeNoException();
reply.writeByteArray(_result);
if ((_arg1!=null)) {
reply.writeInt(1);
_arg1.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_selectNext:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.SmartcardError _arg0;
_arg0 = new org.simalliance.openmobileapi.service.SmartcardError();
boolean _result = this.selectNext(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.simalliance.openmobileapi.service.ISmartcardServiceChannel
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
     * Closes the specified connection and frees internal resources.
     * A logical channel will be closed.
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
     * Tells if this channel is closed.
     * 
     * @return <code>true</code> if the channel is closed, <code>false</code> otherwise.
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
     * Returns a boolean telling if this channel is the basic channel.
     * 
     * @return <code>true</code> if this channel is a basic channel. <code>false</code> if
     *         this channel is a logical channel.
     */
@Override public boolean isBasicChannel() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isBasicChannel, _data, _reply, 0);
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
     * Returns the data as received from the application select command inclusively the status word.
     * The returned byte array contains the data bytes in the following order:
     * [<first data byte>, ..., <last data byte>, <sw1>, <sw2>]
     */
@Override public byte[] getSelectResponse() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
byte[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSelectResponse, _data, _reply, 0);
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
     * Get the session that has opened this channel.
     * 
     * @return the session object this channel is bound to.
     */
@Override public org.simalliance.openmobileapi.service.ISmartcardServiceSession getSession() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.simalliance.openmobileapi.service.ISmartcardServiceSession _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSession, _data, _reply, 0);
_reply.readException();
_result = org.simalliance.openmobileapi.service.ISmartcardServiceSession.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Transmits the specified command APDU and returns the response APDU.
     * MANAGE channel commands are not supported.
     * Selection of applets is not supported in logical channels.
     */
@Override public byte[] transmit(byte[] command, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
byte[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(command);
mRemote.transact(Stub.TRANSACTION_transmit, _data, _reply, 0);
_reply.readException();
_result = _reply.createByteArray();
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
     * Performs a selection of the next Applet on this channel that matches to the partial AID specified   
     * in the openBasicChannel(byte[] aid) or openLogicalChannel(byte[] aid) method.  
     * This mechanism can be used by a device application to iterate through all Applets 
     * matching to the same partial AID. 
     * If selectNext() returns true a new Applet was successfully selected on this channel. 
     * If no further Applet exists with matches to the partial AID this method returns false
     * and the already selected Applet stays selected.
     *
     * @return <code>true</code> if new Applet was successfully selected.
               <code>false</code> if no further Applet exists which matches the partial AID. 
     */
@Override public boolean selectNext(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_selectNext, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
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
static final int TRANSACTION_close = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_isClosed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isBasicChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getSelectResponse = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_transmit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_selectNext = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
/**
     * Closes the specified connection and frees internal resources.
     * A logical channel will be closed.
     */
public void close(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Tells if this channel is closed.
     * 
     * @return <code>true</code> if the channel is closed, <code>false</code> otherwise.
     */
public boolean isClosed() throws android.os.RemoteException;
/**
     * Returns a boolean telling if this channel is the basic channel.
     * 
     * @return <code>true</code> if this channel is a basic channel. <code>false</code> if
     *         this channel is a logical channel.
     */
public boolean isBasicChannel() throws android.os.RemoteException;
/**
     * Returns the data as received from the application select command inclusively the status word.
     * The returned byte array contains the data bytes in the following order:
     * [<first data byte>, ..., <last data byte>, <sw1>, <sw2>]
     */
public byte[] getSelectResponse() throws android.os.RemoteException;
/**
     * Get the session that has opened this channel.
     * 
     * @return the session object this channel is bound to.
     */
public org.simalliance.openmobileapi.service.ISmartcardServiceSession getSession() throws android.os.RemoteException;
/**
     * Transmits the specified command APDU and returns the response APDU.
     * MANAGE channel commands are not supported.
     * Selection of applets is not supported in logical channels.
     */
public byte[] transmit(byte[] command, org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Performs a selection of the next Applet on this channel that matches to the partial AID specified   
     * in the openBasicChannel(byte[] aid) or openLogicalChannel(byte[] aid) method.  
     * This mechanism can be used by a device application to iterate through all Applets 
     * matching to the same partial AID. 
     * If selectNext() returns true a new Applet was successfully selected on this channel. 
     * If no further Applet exists with matches to the partial AID this method returns false
     * and the already selected Applet stays selected.
     *
     * @return <code>true</code> if new Applet was successfully selected.
               <code>false</code> if no further Applet exists which matches the partial AID. 
     */
public boolean selectNext(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
}
