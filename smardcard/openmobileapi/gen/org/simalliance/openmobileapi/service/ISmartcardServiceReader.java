/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Project\\mycode\\oma\\oma\\SmartCardService\\openmobileapi\\src\\org\\simalliance\\openmobileapi\\service\\ISmartcardServiceReader.aidl
 */
package org.simalliance.openmobileapi.service;
public interface ISmartcardServiceReader extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.simalliance.openmobileapi.service.ISmartcardServiceReader
{
private static final java.lang.String DESCRIPTOR = "org.simalliance.openmobileapi.service.ISmartcardServiceReader";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.simalliance.openmobileapi.service.ISmartcardServiceReader interface,
 * generating a proxy if needed.
 */
public static org.simalliance.openmobileapi.service.ISmartcardServiceReader asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.simalliance.openmobileapi.service.ISmartcardServiceReader))) {
return ((org.simalliance.openmobileapi.service.ISmartcardServiceReader)iin);
}
return new org.simalliance.openmobileapi.service.ISmartcardServiceReader.Stub.Proxy(obj);
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
case TRANSACTION_getName:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.SmartcardError _arg0;
_arg0 = new org.simalliance.openmobileapi.service.SmartcardError();
java.lang.String _result = this.getName(_arg0);
reply.writeNoException();
reply.writeString(_result);
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_isSecureElementPresent:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.SmartcardError _arg0;
_arg0 = new org.simalliance.openmobileapi.service.SmartcardError();
boolean _result = this.isSecureElementPresent(_arg0);
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
case TRANSACTION_openSession:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.SmartcardError _arg0;
_arg0 = new org.simalliance.openmobileapi.service.SmartcardError();
org.simalliance.openmobileapi.service.ISmartcardServiceSession _result = this.openSession(_arg0);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_closeSessions:
{
data.enforceInterface(DESCRIPTOR);
org.simalliance.openmobileapi.service.SmartcardError _arg0;
_arg0 = new org.simalliance.openmobileapi.service.SmartcardError();
this.closeSessions(_arg0);
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
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.simalliance.openmobileapi.service.ISmartcardServiceReader
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
     * Return the user-friendly name of this reader.
     * <ul>
	 * <li>If this reader is a SIM reader, then its name must start with the "SIM" prefix.</li>
	 * <li>If the reader is a SD or micro SD reader, then its name must start with the "SD" prefix</li>
	 * <li>If the reader is a embedded SE reader, then its name must start with the "eSE" prefix</li>
	 * <ul>
     * 
     * @return name of this Reader
     */
@Override public java.lang.String getName(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
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
     * Returns true if a card is present in the specified reader.
     * Returns false if a card is not present in the specified reader.
     */
@Override public boolean isSecureElementPresent(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isSecureElementPresent, _data, _reply, 0);
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
/**
     * Connects to a secure element in this reader. <br>
     * This method prepares (initialises) the Secure Element for communication
     * before the Session object is returned (e.g. powers the Secure Element by
     * ICC ON if its not already on). There might be multiple sessions opened at
     * the same time on the same reader. The system ensures the interleaving of
     * APDUs between the respective sessions.
     * 
     * @return a Session object to be used to create Channels.
     */
@Override public org.simalliance.openmobileapi.service.ISmartcardServiceSession openSession(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.simalliance.openmobileapi.service.ISmartcardServiceSession _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_openSession, _data, _reply, 0);
_reply.readException();
_result = org.simalliance.openmobileapi.service.ISmartcardServiceSession.Stub.asInterface(_reply.readStrongBinder());
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
     * Close all the sessions opened on this reader. All the channels opened by
     * all these sessions will be closed.
     */
@Override public void closeSessions(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_closeSessions, _data, _reply, 0);
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
}
static final int TRANSACTION_getName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_isSecureElementPresent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_openSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_closeSessions = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
/**
     * Return the user-friendly name of this reader.
     * <ul>
	 * <li>If this reader is a SIM reader, then its name must start with the "SIM" prefix.</li>
	 * <li>If the reader is a SD or micro SD reader, then its name must start with the "SD" prefix</li>
	 * <li>If the reader is a embedded SE reader, then its name must start with the "eSE" prefix</li>
	 * <ul>
     * 
     * @return name of this Reader
     */
public java.lang.String getName(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Returns true if a card is present in the specified reader.
     * Returns false if a card is not present in the specified reader.
     */
public boolean isSecureElementPresent(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Connects to a secure element in this reader. <br>
     * This method prepares (initialises) the Secure Element for communication
     * before the Session object is returned (e.g. powers the Secure Element by
     * ICC ON if its not already on). There might be multiple sessions opened at
     * the same time on the same reader. The system ensures the interleaving of
     * APDUs between the respective sessions.
     * 
     * @return a Session object to be used to create Channels.
     */
public org.simalliance.openmobileapi.service.ISmartcardServiceSession openSession(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
/**
     * Close all the sessions opened on this reader. All the channels opened by
     * all these sessions will be closed.
     */
public void closeSessions(org.simalliance.openmobileapi.service.SmartcardError error) throws android.os.RemoteException;
}
