/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\fhl\\Android\\插件\\Android_AIDL进程间通讯实现例子\\invokeRemoteService\\src\\cn\\xing\\remoteservice\\IRemoteService.aidl
 */
package cn.xing.remoteservice;
public interface IRemoteService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements cn.xing.remoteservice.IRemoteService
{
private static final java.lang.String DESCRIPTOR = "cn.xing.remoteservice.IRemoteService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.xing.remoteservice.IRemoteService interface,
 * generating a proxy if needed.
 */
public static cn.xing.remoteservice.IRemoteService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof cn.xing.remoteservice.IRemoteService))) {
return ((cn.xing.remoteservice.IRemoteService)iin);
}
return new cn.xing.remoteservice.IRemoteService.Stub.Proxy(obj);
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
case TRANSACTION_getServiceRunTime:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getServiceRunTime();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_getCurrentTime:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getCurrentTime();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_changePersion:
{
data.enforceInterface(DESCRIPTOR);
cn.xing.remoteservice.Person _arg0;
if ((0!=data.readInt())) {
_arg0 = cn.xing.remoteservice.Person.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
cn.xing.remoteservice.Person _result = this.changePersion(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements cn.xing.remoteservice.IRemoteService
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
@Override public long getServiceRunTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getServiceRunTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getCurrentTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public cn.xing.remoteservice.Person changePersion(cn.xing.remoteservice.Person p) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
cn.xing.remoteservice.Person _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((p!=null)) {
_data.writeInt(1);
p.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_changePersion, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = cn.xing.remoteservice.Person.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getServiceRunTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getCurrentTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_changePersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public long getServiceRunTime() throws android.os.RemoteException;
public java.lang.String getCurrentTime() throws android.os.RemoteException;
public cn.xing.remoteservice.Person changePersion(cn.xing.remoteservice.Person p) throws android.os.RemoteException;
}
