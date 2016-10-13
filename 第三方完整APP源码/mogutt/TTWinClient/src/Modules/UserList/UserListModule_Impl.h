/*******************************************************************************
 *  @file      IUserListModule_Impl.h 2014\8\6 15:27:11 $
 *  @author    ���<dafo@mogujie.com>
 *  @brief     
 ******************************************************************************/

#ifndef IUSERLISTMODULE_IMPL_9768C185_67AE_45BB_B840_F0A66E6A7044_H__
#define IUSERLISTMODULE_IMPL_9768C185_67AE_45BB_B840_F0A66E6A7044_H__

#include "Modules/IUserListModule.h"
/******************************************************************************/

/**
 * The class <code>IUserListModule_Impl</code> 
 *
 */
class UserListModule_Impl final : public module::IUserListModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
    UserListModule_Impl();
    /**
     * Destructor
     */
    virtual ~UserListModule_Impl();
    //@}
	virtual void release();
	virtual void onPacket(std::auto_ptr<CImPdu> pdu);

public:
	virtual const module::DepartmentVec& getAllDepartments();
	virtual void getAllUsersInfo(module::UserInfoEntityMap& MapUsers)const;
	virtual BOOL getDepartmentByDId(const std::string& dId, module::DepartmentEntity& entity);
	virtual BOOL getUserInfoBySId(IN std::string sid, OUT module::UserInfoEntity& userInfo);
	
	virtual void getUserListInfoVec(OUT module::UserInfoEntityVec& VecUsers);				 //�����ϵ���б�
	virtual void releaseUserListInfoVec();

	virtual void removeAllListInfo();
	virtual UInt8 getMyLineStatus();

	virtual void tcpGetUserOnlieStatus(IN const std::string& sId);
	virtual void tcpGetUserOnlieStatus(const module::UserInfoEntityVec& VecId);//Ҫ������ȡ
	virtual void tcpGetAllUserOnlieStatus(void);
	virtual void tcpGetUserInfo(IN const std::string& sId);
	virtual void tcpGetUserInfoList(IN const module::UserInfoEntityVec& VecUnKnowUserInfo);

	virtual CString getDefaultAvatartPath();
	virtual BOOL getMyInfo(OUT module::UserInfoEntity& myInfo);
	virtual BOOL createUserInfo(const std::string& sId, const module::UserInfoEntity& info);

	virtual void getSearchUserNameListByShortName(IN const CString& sShortName, OUT	module::UserInfoEntityVec& nameList);
	void onCallbackOperation(std::shared_ptr<void> param);

private:
	/**@name �������˲��*/
	//@{
	void _departmentResponse(CImPdu* pdu);
	void _allUserlistResponse(CImPdu* pdu);
	void _recentlistResponse(CImPdu* pdu);
	void _newUserListInfoResponse(CImPdu* pdu);
	void _allUserlistLineStatusResponse(CImPdu* pdu);
	void _userLineStatusResponse(CImPdu* pdu);
	//@}

	/**@name ���ߺ���*/
	//@{
	BOOL _pushUserIdToDepartment(const string& sId,const string& dId);
	void _downloadAllUserAvatarImg();
	std::string _makeDepartmentId(IN const string& dId);
	void _tcpGetUserOnlieStatus(IN const module::UserInfoEntityVec& VecId);
	void _tcpGetUserInfoList(IN module::UserInfoEntityVec VecUnKnowUserInfo);
	std::string _getGrayLocalPathFromFilename(std::string& finename);

	BOOL _updateRecentUserList(void);
	//@}	


private:
	util::TTFastLock					m_lock;
	module::DepartmentVec				m_vecDepartment;						//������Ϣ
	module::UserInfoEntityMap           m_mapUsers;								//�����û�����Ϣ
	module::UserInfoEntityVec           m_vecUsers;								//�����ϵ��ID�б�
	UInt32								m_tcpGetUserFriendInfoListTime;			//trick,�����û���Ϣ�Ĵ�����һ�����������������⡣
	UInt32								m_tcpGetUserFriendInfoListBackTime;		//�����û���Ϣ�Ĵ�����һ�����������������⡣���صĴ���

	UInt8								 m_UserListGetTime = 0;		//���ڸ�����Ϣ�������ϵʱ���Ƿֿ�ȡ�ģ�����Ҫ��ȫ����Ϣ��ȡȫ����֪ͨ
};
/******************************************************************************/
#endif// IUSERLISTMODULE_IMPL_9768C185_67AE_45BB_B840_F0A66E6A7044_H__
