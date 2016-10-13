package cn.xing.remoteservice;
import cn.xing.remoteservice.Person;
interface IRemoteService{
	long getServiceRunTime();
	String getCurrentTime();
	Person changePersion(in Person p);
} 