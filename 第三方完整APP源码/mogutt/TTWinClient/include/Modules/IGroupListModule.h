/*******************************************************************************
 *  @file      IGroupListModule.h 2014\8\6 15:29:01 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     Ⱥ�����������ģ��
 ******************************************************************************/

#ifndef IGROUPLISTMODULE_3AD36DFC_4041_486A_A437_948E152517E8_H__
#define IGROUPLISTMODULE_3AD36DFC_4041_486A_A437_948E152517E8_H__

#include "GlobalDefine.h"
#include "TTLogic/IModule.h"
#include "Modules/ModuleDll.h"
#include "Modules/IUserListModule.h"
#include <list>
#include <string>
/******************************************************************************/
NAMESPACE_BEGIN(module)

enum
{
	KEY_GROUPLIST_UPDATE_GROUPLIST			= MODULE_ID_GROUPLIST << 16 | 1,	//�ɹ���ȡ���̶�Ⱥ��Ϣ
	KEY_GROUPLIST_UPDATE_GROUPDISCUSSLIST	= MODULE_ID_GROUPLIST << 16 | 2,	//�ɹ���ȡ����������Ϣ
	KEY_GROUPLIST_UPDATE_RECENTGROUPLIST	= MODULE_ID_GROUPLIST << 16 | 3,	//�ɹ���ȡ�����е�Ⱥ�������飬���������ϵ��Ⱥ�б�
	KEY_GROUPLIST_UPDATE_NEWGROUPADDED		= MODULE_ID_GROUPLIST << 16 | 4,	//�յ�һ��İ��Ⱥ��Ϣ����
	KEY_GROUPLIST_UPDATE_CREATNEWGROUP		= MODULE_ID_GROUPLIST << 16 | 5,	//����һ�������鷵��

};


class GroupInfoEntity
{
public:
	string				gId;					//ȺID
	std::string			avatarUrl;
	std::string			avatarLocalPath;             //ͷ�����سɹ���Ĵ洢·��
	CString				desc;					//Ⱥ����
	CString				csName;
	UInt32				type = 0;				//Ⱥ���ͣ�1���̶�Ⱥ 2,������
	UInt32				groupUpdated = 0;		//���һ�θ�����Ϣʱ��
	std::list<string>	groupMemeberList;

public:
	std::string getAvatarPath()
	{
		std::string path = avatarLocalPath;
		if (path.empty())
		{
			std::string sDataPath = util::cStringToString(module::getMiscModule()->getDefaultAvatar());
			if (1 == type)
			{
				path = sDataPath + "Groups.png";
			}
			else
			{
				path = sDataPath + "DiscussionGroups.png";
			}
		}
		else
		{
			std::string sDownPath = util::cStringToString(module::getMiscModule()->getDownloadDir());
			path = sDownPath + avatarLocalPath;
		}

		return path;
	}
};

typedef std::map<std::string, GroupInfoEntity>       GroupInfoMap;//Ⱥ�б�
typedef std::vector<std::string>     GroupVec;//Ⱥ�б�ID,�����ϵȺ
/**
 * The class <code>Ⱥ�����������ģ��</code> 
 *
 */
class MODULE_API IGroupListModule : public logic::IPduAsyncSocketModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	IGroupListModule()
	{
		m_moduleId = MODULE_ID_GROUPLIST;
	}
    //@}

public:
	/**
	* ��ȡ����Ⱥ����Ϣ�����ڲ���Ⱥ�б�
	*
	* @param   OUT module::GroupMap & Groups
	* @return  void
	* @exception there is no any exception to throw.
	*/
	virtual void getAllGroupListInfo(OUT module::GroupInfoMap& Groups) = 0;
	/**
	* ��ѯȺ��Ϣ
	*
	* @param   IN const std::string & sID
	* @param   OUT module::GroupInfoEntity & groupInfo
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	virtual BOOL getGroupInfoBySId(IN const std::string& sID, OUT module::GroupInfoEntity& groupInfo) = 0;
	/**
	* ��ȡĳ��Ⱥ�����г�Ա
	*
	* @param   IN const std::string & sID
	* @param   OUT module::VecUserInfoEntity & groupUserVec
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	virtual BOOL getGroupUserVecBySId(IN const std::string& sID, OUT module::UserInfoEntityVec& groupUserVec) = 0;

	/**
	* ��ȡȺ�б�ID �û����������ϵ���е�Ⱥ��
	*
	* @param   OUT module::GroupVec & groups
	* @return  void
	* @exception there is no any exception to throw.
	*/
	virtual void getGroupListVec(module::GroupVec& groups) = 0;
	/**
	* ���֮ǰ��ȡ�������ϵȺ�б�
	*
	* @return  void
	* @exception there is no any exception to throw.
	*/
	virtual void releaseGroupListInfoVec() = 0;
	/**
	* ��ȡȺ������Ա
	*
	* @param   IN const std::string & sID
	* @param   OUT module::VecUserInfoEntity & AddedMemberVec
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	virtual BOOL popAddedMemberVecBySId(IN const std::string& sID, OUT module::UserInfoEntityVec& AddedMemberVec) = 0;
	/**
	* ��ȡȺ���ߵ�����Ա
	*
	* @param   IN const std::string & sID
	* @param   OUT module::VecUserInfoEntity & RemovedMemberVec
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	virtual BOOL popRemovedMemberVecBySId(IN const std::string& sID, OUT module::UserInfoEntityVec& RemovedMemberVec) = 0;

	/**
	* ��ȡȺ������Ϣ
	*
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	virtual BOOL tcpGetGroupOfflineMsg() = 0;

	/**
	* ��ȡĬ�ϵ�Ⱥͷ����Ϣ
	*
	* @return  CString
	* @exception there is no any exception to throw.
	*/
	virtual CString getDefaultAvatartPath() = 0;

	/**
	* ɾ������Ⱥ��Ϣ��Ⱥ
	*
	* @param   const std::string & sid
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	virtual BOOL DeleteGroupIDFromVecOfflineMsgGroup(const std::string& sid) = 0;

	/**
	* ��ȡ�µ�Ⱥ��Ա
	*
	* @param   IN const std::string & groupId
	* @return  void
	* @exception there is no any exception to throw.
	*/
	virtual void tcpGetGroupInfo(IN const std::string& groupId) = 0;//��ȡ��Ⱥ����Ϣ

	virtual void onCreateDiscussionGrpDialog(const std::string& currentSessionId) = 0;

	virtual void GetSearchGroupNameListByShortName(IN const CString& sShortName, OUT module::GroupVec & gidList) = 0;
};

MODULE_API IGroupListModule* getGroupListModule();

NAMESPACE_END(module)
/******************************************************************************/
#endif// IGROUPLISTMODULE_3AD36DFC_4041_486A_A437_948E152517E8_H__
