package cn.xing.remoteservice;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

	private String name;
	private int sex;

	// �����ṩһ����ΪCREATOR��static final���� ��������Ҫʵ��android.os.Parcelable.Creator<T>�ӿ�
	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {

		@Override
		public Person createFromParcel(Parcel source) {
			return new Person(source);
		}

		@Override
		public Person[] newArray(int size) {
			return new Person[size];
		}
	};

	public Person() {

	}

	private Person(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// ע��д������Ͷ�ȡ������˳��Ӧ��һ�� ��Ȼ�ò�����ȷ�Ľ��
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(sex);
	}

	// ע���ȡ������д�������˳��Ӧ��һ�� ��Ȼ�ò�����ȷ�Ľ��
	public void readFromParcel(Parcel source) {
		name = source.readString();
		sex = source.readInt();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

}
