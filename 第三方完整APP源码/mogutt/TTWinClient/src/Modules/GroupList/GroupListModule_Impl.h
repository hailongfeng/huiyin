/*******************************************************************************
 *  @file      GroupListModule_Impl.h 2014\8\6 15:30:37 $
 *  @author    ���<dafo@mogujie.com>
 *  @brief     
 ******************************************************************************/

#ifndef GROUPLISTMODULE_IMPL_A4D4E682_0D42_4CF1_BF4B_B05E2F62C248_H__
#define GROUPLISTMODULE_IMPL_A4D4E682_0D42_4CF1_BF4B_B05E2F62C248_H__

#include "Modules/IGroupListModule.h"
#include "utility/TTAutoLock.h"
/******************************************************************************/

/**
 * The class <code>GroupListModule_Impl</code> 
 *
 */
class CImPdu;
class GroupListModule_Impl final : public module::IGroupListModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
    GroupListModule_Impl();
    /**
     * Destructor
     */
    virtual ~GroupListModule_Impl();
    //@}
	virtual void release();
	virtual void onPacket(std::auto_ptr<CImPdu> pdu);

public:
	virtual void getAllGroupListInfo(OUT module::GroupInfoMap& Groups);

	virtual BOOL getGroupInfoBySId(IN const std::string& sID, OUT module::GroupInfoEntity& groupInfo);

	virtual BOOL getGroupUserVecBySId(IN const std::string& sID, OUT module::UserInfoEntityVec& groupUserVec) ;

	virtual void getGroupListVec(OUT module::GroupVec& groups);   

	virtual void releaseGroupListInfoVec();

	virtual BOOL popAddedMemberVecBySId(IN const std::string& sID, OUT module::UserInfoEntityVec& AddedMemberVec);

	virtual BOOL popRemovedMemberVecBySId(IN const std::string& sID, OUT module::UserInfoEntityVec& RemovedMemberVec);

	virtual BOOL tcpGetGroupOfflineMsg();

	virtual CString getDefaultAvatartPath();

	virtual BOOL DeleteGroupIDFromVecOfflineMsgGroup(const std::string& sid);	

	virtual void tcpGetGroupInfo(IN const std::string& groupId);

	virtual void GetSearchGroupNameListByShortName(IN const CString& sShortName, OUT	module::GroupVec & gidList);
	virtual void onCreateDiscussionGrpDialog(const std::string& currentSessionId);

private:
	/**@name �������˲��*/
	//@{
	void _grouplistResponse(CImPdu* pdu);
	void _groupuserlistResponse(CImPdu* pdu);
	void _groupUnreadCntResponse(CImPdu* pdu);
	void _groupJionGroupResponse(CImPdu* pdu);
	void _groupQuitGroupResponse(CImPdu* pdu);	//�����뿪��Ⱥ
	void _groupDiscussListResponse(CImPdu* pdu);
	void _groupCreatTempGroupRespone(CImPdu* pdu);
	void _groupChangedGroupMembersResponse(CImPdu* pdu);
	//@}

	std::string _MakeGroupSID(const std::string& sid);
	std::string _GetOriginalSID(const std::string& sid);
	void _downloadAllGroupAvatarImg();
	void onCallbackOperation(std::shared_ptr<void> param);

	BOOL _updateRecentGroupList(void);
private:
	util::TTFastLock					m_lock;

	module::GroupInfoMap                m_mapGroups;       //ÿ��Ⱥ����ϸ��Ϣ
	module::GroupVec                    m_vecGroup;       //���浱ǰ��ȡ����ȺID�б�
	module::GroupVec                    m_vecOfflineMsgGroup;//��������Ϣ��ȺID�б�(����ȺIDû�м�ǰ׺)

	std::map<std::string, module::UserInfoEntityVec> m_mapAddedMember;
	std::map<std::string, module::UserInfoEntityVec> m_mapRemovedMember;

	UInt8								 m_GroupInfoGetTime;
};
/******************************************************************************/
#endif// GROUPLISTMODULE_IMPL_a4d4e682-0d42-4cf1-bf4b-b05e2f62c248_H__
