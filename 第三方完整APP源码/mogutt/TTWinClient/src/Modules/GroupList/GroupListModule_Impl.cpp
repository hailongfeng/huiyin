/******************************************************************************* 
 *  @file      GroupListModule_Impl.cpp 2014\8\6 15:30:42 $
 *  @author    ���<dafo@mogujie.com>
 *  @brief     
 ******************************************************************************/

#include "stdafx.h"
#include "GroupListModule_Impl.h"
#include "Modules/IMiscModule.h"
#include "Modules/ISessionModule.h"
#include "utility/utilStrCodeAPI.h"
#include "utility/Multilingual.h"
#include "TTLogic/ITcpClientModule.h"
#include "src/base/ImPduGroup.h"
#include "UI/CreateDiscussionGrpDialog.h"
#include "../Session/Operation/DownloadImgHttpOperation.h"

/******************************************************************************/
namespace module
{
	module::IGroupListModule* getGroupListModule()
	{
		return (module::IGroupListModule*)logic::GetLogic()->getModule(MODULE_ID_GROUPLIST);
	}
}

// -----------------------------------------------------------------------------
//  GroupListModule_Impl: Public, Constructor

GroupListModule_Impl::GroupListModule_Impl()
:m_GroupInfoGetTime(0)
{

}

// -----------------------------------------------------------------------------
//  GroupListModule_Impl: Public, Destructor

GroupListModule_Impl::~GroupListModule_Impl()
{

}

void GroupListModule_Impl::release()
{
	delete this;
}

void GroupListModule_Impl::onPacket(std::auto_ptr<CImPdu> pdu)
{
	CImPdu* pPdu = pdu.get();
	PTR_VOID(pPdu);
	switch (pdu->GetCommandId())
	{
	case CID_GROUP_LIST_RESPONSE://�̶�Ⱥ�б�
		_grouplistResponse(pPdu);
		break;
	case CID_GROUP_DIALOG_LIST_RESPONSE://������
		_groupDiscussListResponse(pPdu);
		break;
	case CID_GROUP_USER_LIST_RESPONSE://İ��Ⱥ/������ ��ϸ��Ϣ�����յ�İ��Ⱥ��Ϣ��ʱ�������Ⱥ��Ϣ�����أ�
		_groupuserlistResponse(pPdu);
		break;
	case CID_GROUP_UNREAD_CNT_RESPONSE:
		_groupUnreadCntResponse(pPdu);	//������Ϣ����
		break;
	case CID_GROUP_UNREAD_MSG_RESPONSE://�����Ⱥ��Ϣ����
		module::getSessionModule()->OnGroupUnreadMsgRespone(pPdu);
		break;
	case CID_GROUP_CREATE_TMP_GROUP_RESPONSE:
		APP_LOG(LOG_DEBUG, _T("������ʱȺ����"));
		_groupCreatTempGroupRespone(pPdu);
		break;
	case CID_GROUP_CHANGE_MEMBER_RESPONSE:	//Ⱥ��Ա�����䶯
		_groupChangedGroupMembersResponse(pPdu);
		break;
	default:
		return;
	}
}

void GroupListModule_Impl::getAllGroupListInfo(OUT module::GroupInfoMap& Groups)
{
	Groups = m_mapGroups;
}

BOOL GroupListModule_Impl::getGroupInfoBySId(IN const std::string& sID, OUT module::GroupInfoEntity& groupInfo)
{
	util::TTAutoLock lock(&m_lock);
	module::GroupInfoMap::iterator it = m_mapGroups.find(sID);
	if (it != m_mapGroups.end())
	{
		groupInfo = it->second;
		return TRUE;
	}
	return FALSE;
}

BOOL GroupListModule_Impl::getGroupUserVecBySId(IN const std::string& sID, OUT module::UserInfoEntityVec& groupUserVec)
{
	util::TTAutoLock lock(&m_lock);
	module::GroupInfoMap::iterator it = m_mapGroups.find(sID);
	if (it != m_mapGroups.end())
	{
		groupUserVec.insert(groupUserVec.begin(),
			(it->second).groupMemeberList.begin(),
			(it->second).groupMemeberList.end());
		return TRUE;
	}
	return FALSE;
}

void GroupListModule_Impl::getGroupListVec(OUT module::GroupVec& groups)
{
	groups = m_vecGroup;
}

void GroupListModule_Impl::releaseGroupListInfoVec()
{
	m_vecGroup.clear();
}

BOOL GroupListModule_Impl::popAddedMemberVecBySId(IN const std::string& sID, OUT module::UserInfoEntityVec& AddedMemberVec)
{
	std::map<std::string, module::UserInfoEntityVec>::iterator it = m_mapAddedMember.find(sID);
	if (it != m_mapAddedMember.end())
	{
		AddedMemberVec = it->second;
		{
			util::TTAutoLock lock(&m_lock);
			m_mapAddedMember.erase(it);
		}
		return TRUE;
	}
	return FALSE;
}

BOOL GroupListModule_Impl::popRemovedMemberVecBySId(IN const std::string& sID, OUT module::UserInfoEntityVec& RemovedMemberVec)
{
	std::map<std::string, module::UserInfoEntityVec>::iterator it = m_mapRemovedMember.find(sID);
	if (it != m_mapRemovedMember.end())
	{
		RemovedMemberVec = it->second;
		{
			util::TTAutoLock lock(&m_lock);
			m_mapRemovedMember.erase(it);
		}
		return TRUE;
	}
	return FALSE;
}

BOOL GroupListModule_Impl::tcpGetGroupOfflineMsg()
{
	util::TTAutoLock lock(&m_lock);
	for (std::string groupId:m_vecOfflineMsgGroup)
	{
		logic::GetLogic()->pushBackOperationWithLambda(
			[=]()
		{
			CImPduClientGroupUnreadMsgRequest pduMsgData(groupId.c_str());
			logic::getTcpClientModule()->sendPacket(&pduMsgData);
		}
		);

	}
	return TRUE;
}

CString GroupListModule_Impl::getDefaultAvatartPath()
{
	return module::getMiscModule()->getDataDir() + _T("default.jpg");
}

BOOL GroupListModule_Impl::DeleteGroupIDFromVecOfflineMsgGroup(const std::string& sid)
{
	module::GroupVec::iterator it = m_vecOfflineMsgGroup.begin();
	for (; it != m_vecOfflineMsgGroup.end(); ++it)
	{
		if (sid == *it)
		{
			util::TTAutoLock lock(&m_lock);
			m_vecOfflineMsgGroup.erase(it);
			return TRUE;
		}
	}
	return FALSE;
}

void GroupListModule_Impl::tcpGetGroupInfo(IN const std::string& groupId)
{
	logic::GetLogic()->pushBackOperationWithLambda(
		[=]()
	{
		CImPduClientGroupUnreadMsgRequest pduMsgData(groupId.c_str());
		logic::getTcpClientModule()->sendPacket(&pduMsgData);
	}
	);
}

void GroupListModule_Impl::onCreateDiscussionGrpDialog(const std::string& currentSessionId)
{
	CreateDiscussionGrpDialog* pFrame = new CreateDiscussionGrpDialog(currentSessionId);
	if (pFrame == NULL) return;
	CString strWinName = util::getMultilingual()->getStringViaID(_T("STRID_GROUPLISTMODULE_CREATGROUPWNDNAME"));
	pFrame->Create(NULL, strWinName, UI_WNDSTYLE_FRAME, WS_EX_STATICEDGE | WS_EX_APPWINDOW, 0, 0, 600, 800);
	pFrame->CenterWindow();
	pFrame->ShowWindow(true);
}
std::string GroupListModule_Impl::_MakeGroupSID(const std::string& sid)
{
	if (sid.empty())
	{
		return sid;
	}
	std::string sessionID = ("group_") + sid;
	return sessionID;
}
std::string GroupListModule_Impl::_GetOriginalSID(const std::string& sid)
{
	std::string::size_type len = sid.size();
	if (len <= 6)
	{
		return sid;
	}
	std::string OriginalSID = sid.substr(6, len);
	return sid;
}
void GroupListModule_Impl::_grouplistResponse(CImPdu* pdu)
{
	CImPduClientGroupListResponse* pList = (CImPduClientGroupListResponse*)pdu;
	client_group_info_t* pGroupInfo = pList->GetGroupList();
	for (UInt32 i = 0; i < pList->GetGroupCnt(); ++i)
	{
		module::GroupInfoEntity groupinfo;
		groupinfo.type = pGroupInfo[i].group_type;
		for (std::list<user_id_t>::iterator it = pGroupInfo[i].group_memeber_list.begin();
			it != pGroupInfo[i].group_memeber_list.end(); it++
			)
		{
			if (it->id_len)
			{
				groupinfo.groupMemeberList.push_back(string(it->id_url, it->id_len));
			}
			else
			{
				APP_LOG(LOG_DEBUG, TRUE, _T("group_memeber_list.user_id_t.id_len = 0"));
				continue;
			}
		}
		if (pGroupInfo[i].group_id_len)
		{
			groupinfo.gId.assign(pGroupInfo[i].group_id, pGroupInfo[i].group_id_len);
			groupinfo.gId = _MakeGroupSID(groupinfo.gId);//����sessionͷ
		}
		else
		{
			APP_LOG(LOG_DEBUG, TRUE, _T("groupinfo.group_id_len = 0"));
			continue;
		}
		if (pGroupInfo[i].group_name_len)
		{
			std::string sTemp(pGroupInfo[i].group_name, pGroupInfo[i].group_name_len);
			groupinfo.csName = util::stringToCString(sTemp, CP_UTF8);
		}
		else
		{
			APP_LOG(LOG_DEBUG, TRUE, _T("groupinfo.group_name_len = 0"));
			continue;
		}
		if (pGroupInfo[i].group_avatar_len)
		{
			groupinfo.avatarUrl.assign(pGroupInfo[i].group_avatar, pGroupInfo[i].group_avatar_len);
		}
		else
		{
			//Ⱥͷ�����Ϊ��
			APP_LOG(LOG_DEBUG, TRUE, _T("groupinfo.group_avatar_len = 0"));
			//continue; 
		}
		//if (pGroupInfo[i].group_desc)Ⱥ����
		//{
		//	groupinfo.desc.assign(pGroupInfo[i].group_desc, pGroupInfo[i].group_desc_len);
		//}
		groupinfo.groupUpdated = pGroupInfo[i].group_updated;
		{
			util::TTAutoLock lock(&m_lock);
			m_mapGroups.insert(std::make_pair(groupinfo.gId, groupinfo));
		}
	}
	
	logic::GetLogic()->asynNotifyObserver(module::KEY_GROUPLIST_UPDATE_GROUPLIST);//֪ͨUI���½���

	_updateRecentGroupList();

	//��������Ⱥ��ͷ��
	_downloadAllGroupAvatarImg();
}

void GroupListModule_Impl::_groupuserlistResponse(CImPdu* pdu)
{
	CImPduClientGroupUserListResponse* pduData = (CImPduClientGroupUserListResponse*)pdu;
	std::string groupid(pduData->GetGroupId(), pduData->GetGroupIdLen());
	std::string groupSid = _MakeGroupSID(groupid);
	util::TTAutoLock lock(&m_lock);
	module::GroupInfoEntity groupInfo;
	if (getGroupInfoBySId(groupid, groupInfo))
	{
		APP_LOG(LOG_ERROR, _T("��ȡ������֪Ⱥ����Ϣ"));
	}
	else//���������������Ϣ
	{
		groupInfo.gId = groupid;//ID

		user_id_t* pUserInfo = pduData->GetUserList();//Ⱥ��Ա
		if (NULL != pUserInfo)
		{
			for (UInt32 i = 0; i < pduData->GetUserCnt(); ++i)
			{
				std::string Sid;
				Sid.assign(pUserInfo[i].id_url, pUserInfo[i].id_len);
				groupInfo.groupMemeberList.push_back(Sid);
			}
		}
		if (NULL != pduData->GetGroupAvatar())//ͷ��
		{
			groupInfo.avatarLocalPath.assign(pduData->GetGroupAvatar(), pduData->GetGroupAvatarLen());
		}
		if (NULL != pduData->GetGroupName())//����
		{
			std::string sTemp(pduData->GetGroupName(), pduData->GetGroupNameLen());
			groupInfo.csName = util::stringToCString(sTemp, CP_UTF8);
		}
		groupInfo.type = pduData->GetGroupType();
		m_mapGroups.insert(std::make_pair(groupid, groupInfo));
	}

	logic::GetLogic()->asynNotifyObserver(module::KEY_GROUPLIST_UPDATE_NEWGROUPADDED, groupSid);//֪ͨUI��������һ��������
}
void GroupListModule_Impl::_groupUnreadCntResponse(CImPdu* pdu)
{
	CImPduClientGroupUnreadMsgCntResponse* pduOfflineMsgCnt = (CImPduClientGroupUnreadMsgCntResponse*)pdu;
	client_group_unread_cnt_t* pList = pduOfflineMsgCnt->GetGroupUnreadList();
	for (UInt32 i = 0; i < pduOfflineMsgCnt->GetGroupUnreadCnt(); ++i)
	{
		client_group_unread_cnt_t pInfo = pList[i];
		{
			util::TTAutoLock lock(&m_lock);
			std::string sid(pInfo.group_id_url, pInfo.group_id_len);
			m_vecOfflineMsgGroup.push_back(sid);
			logic::GetLogic()->pushBackOperationWithLambda(
				[=]()
			{
				CImPduClientGroupUnreadMsgRequest pduData(sid.c_str());
				logic::getTcpClientModule()->sendPacket(&pduData);
			}
			);
		}
	}
}

void GroupListModule_Impl::_groupJionGroupResponse(CImPdu* pdu)
{
	CImPduClientGroupChangeMemberResponse* pduJoinGroupRes = (CImPduClientGroupChangeMemberResponse*)pdu;
	if (!pduJoinGroupRes || 2 == pduJoinGroupRes->GetResult())
	{
		return;
	}
	std::string gID;
	if (pduJoinGroupRes->GetGroupId() && pduJoinGroupRes->GetGroupIdLen())
	{
		gID.assign(pduJoinGroupRes->GetGroupId(), pduJoinGroupRes->GetGroupIdLen());
		gID = _MakeGroupSID(gID);
	}
}

void GroupListModule_Impl::_groupQuitGroupResponse(CImPdu* pdu)
{
	CImPduClientGroupChangeMemberResponse* pduQuitGroupRes = (CImPduClientGroupChangeMemberResponse*)pdu;
	if (!pduQuitGroupRes || 2 == pduQuitGroupRes->GetResult())
	{
		return;
	}
	std::string gID;
	if (pduQuitGroupRes->GetGroupId() && pduQuitGroupRes->GetGroupIdLen())
	{
		gID.assign(pduQuitGroupRes->GetGroupId(), pduQuitGroupRes->GetGroupIdLen());
		gID = _MakeGroupSID(gID);
	}
}

void GroupListModule_Impl::_groupDiscussListResponse(CImPdu* pdu)
{
	CImPduClientGroupListResponse* pList = (CImPduClientGroupListResponse*)pdu;
	client_group_info_t* pGroupInfo = pList->GetGroupList();
	for (UInt32 i = 0; i < pList->GetGroupCnt(); ++i)
	{
		module::GroupInfoEntity groupinfo;
		groupinfo.type = pGroupInfo[i].group_type;
		for (std::list<user_id_t>::iterator it = pGroupInfo[i].group_memeber_list.begin();
			it != pGroupInfo[i].group_memeber_list.end(); it++
			)
		{
			if (it->id_len)
			{
				groupinfo.groupMemeberList.push_back(string(it->id_url, it->id_len));
			}
			else
			{
				APP_LOG(LOG_DEBUG, TRUE, _T("group_memeber_list.user_id_t.id_len = 0"));
				continue;
			}
		}
		if (pGroupInfo[i].group_id_len)
		{
			groupinfo.gId.assign(pGroupInfo[i].group_id, pGroupInfo[i].group_id_len);
			groupinfo.gId = _MakeGroupSID(groupinfo.gId);//����sessionͷ
		}
		else
		{
			APP_LOG(LOG_DEBUG, TRUE, _T("groupinfo.group_id_len = 0"));
			continue;
		}
		if (pGroupInfo[i].group_name_len)
		{
			std::string sTemp(pGroupInfo[i].group_name, pGroupInfo[i].group_name_len);
			groupinfo.csName = util::stringToCString(sTemp, CP_UTF8);
		}
		else
		{
			APP_LOG(LOG_DEBUG, TRUE, _T("groupinfo.group_name_len = 0"));
			continue;
		}
		if (pGroupInfo[i].group_avatar_len)
		{
			groupinfo.avatarUrl.assign(pGroupInfo[i].group_avatar, pGroupInfo[i].group_avatar_len);
		}
		else
		{
			//Ⱥͷ�����Ϊ��
			APP_LOG(LOG_DEBUG, TRUE, _T("groupinfo.group_avatar_len = 0"));
			//continue; 
		}
		//if (pGroupInfo[i].group_desc)
		//{
		//	groupinfo.desc.assign(pGroupInfo[i].group_desc, pGroupInfo[i].group_desc_len);
		//}
		groupinfo.groupUpdated = pGroupInfo[i].group_updated;
		{
			util::TTAutoLock lock(&m_lock);
			module::GroupInfoMap::iterator itMap = m_mapGroups.find(groupinfo.gId);
			if (itMap == m_mapGroups.end())
			{
				m_mapGroups.insert(std::make_pair(groupinfo.gId, groupinfo));//��ʱ�������ϵȺ�͹̶�Ⱥ�ϲ���һ��
			}
			else
			{
				itMap->second.groupUpdated = groupinfo.groupUpdated;
			}
			m_vecGroup.push_back(groupinfo.gId);
		}
	}

	logic::GetLogic()->asynNotifyObserver(module::KEY_GROUPLIST_UPDATE_GROUPDISCUSSLIST);//֪ͨUI���½���

	_updateRecentGroupList();

	//��������Ⱥ��ͷ��
	_downloadAllGroupAvatarImg();
}
void GroupListModule_Impl::_groupCreatTempGroupRespone(CImPdu* pdu)
{
	CImPduClientGroupCreateTmpGroupResponse* pduData = (CImPduClientGroupCreateTmpGroupResponse*)pdu;
	module::GroupInfoEntity groupinfo;
	if (pduData->GetGroupName())
	{
		std::string gName(pduData->GetGroupName(), pduData->GetGroupNameLen());
		groupinfo.csName = util::stringToCString(gName);
	}
	if (pduData->GetGroupId())
	{
		std::string gSid(pduData->GetGroupId(),pduData->GetGroupIdLen());
		groupinfo.gId = gSid;
		groupinfo.gId = _MakeGroupSID(groupinfo.gId);//����sessionͷ
	}
	groupinfo.type = 2;//1.�̶�Ⱥ��2.��ʱȺ

	user_id_t* pUserInfo = pduData->GetUserList();
	for (UInt32 i = 0; i < pduData->GetUserCnt(); ++i)
	{
		std::string sUserId(pUserInfo[i].id_url, pUserInfo[i].id_len);
		groupinfo.groupMemeberList.push_back(sUserId);
	}
	groupinfo.groupUpdated = module::getSessionModule()->getTime();
	{
		util::TTAutoLock lock(&m_lock);
		module::GroupInfoMap::iterator itMap = m_mapGroups.find(groupinfo.gId);
		if (itMap == m_mapGroups.end())
		{
			m_mapGroups.insert(std::make_pair(groupinfo.gId, groupinfo));//��ʱ�������ϵȺ�͹̶�Ⱥ�ϲ���һ��
			logic::GetLogic()->asynNotifyObserver(module::KEY_GROUPLIST_UPDATE_CREATNEWGROUP, groupinfo.gId);//֪ͨUI����������
		}
		else
		{
			APP_LOG(LOG_ERROR, _T("_groupCreatTempGroupRespone:�Ѿ��������ID"));
		}
	}
}

void GroupListModule_Impl::_groupChangedGroupMembersResponse(CImPdu* pdu)
{
	CImPduClientGroupChangeMemberResponse* pduGroupChangeMemberRes = (CImPduClientGroupChangeMemberResponse*)pdu;
	if (GROUP_MEMBER_ADD_TYPE == pduGroupChangeMemberRes->GetChangeType())
	{
	}
	else if (GROUP_MEMBER_DEL_TYPE == pduGroupChangeMemberRes->GetChangeType())
	{
	}
}
void GroupListModule_Impl::GetSearchGroupNameListByShortName(IN const CString& sShortName, OUT module::GroupVec & gidList)
{
	for (auto& kvp : m_mapGroups)
	{
		module::GroupInfoEntity& groupInfo = kvp.second;
		CString RealName = groupInfo.csName;

		if (util::isIncludeChinese(util::cStringToString(sShortName, CP_ACP)))//��������
		{
			if (RealName.Find(sShortName) != -1)
			{
				gidList.push_back(groupInfo.gId);
			}
		}
		else
		{
			CString firstPY = util::HZ2FirstPY(util::cStringToString(groupInfo.csName, CP_ACP));
			if (firstPY.Find(sShortName) != -1)//�ȼ�����ƴ
			{
				gidList.push_back(groupInfo.gId);
			}
			else
			{
				CString allPY = util::HZ2AllPY(groupInfo.csName);//�ټ���ȫƴ
				if (allPY.Find(sShortName) != -1)
				{
					gidList.push_back(groupInfo.gId);
				}
			}
		}
	}
}

void GroupListModule_Impl::_downloadAllGroupAvatarImg()
{
	for (auto& kv : m_mapGroups)
	{
		module::GroupInfoEntity& group = kv.second;
		if (!group.avatarUrl.empty() && group.avatarUrl.find("avatar_group_default.jpg") == std::string::npos)
		{
			UInt32 hashcode = util::hash_BKDR(group.avatarUrl.c_str());
			module::ImImageEntity imageEntity;
			if (module::getDatabaseModule()->sqlGetImImageEntityByHashcode(hashcode, imageEntity)
				&& util::isFileExist(util::stringToCString(imageEntity.filename)))
			{
				//������ֱ�Ӹ�ֵ
				group.avatarLocalPath = imageEntity.filename;
			}
			else
			{
				//��������ȥ����������
				DownloadImgHttpOperation* pOper = new DownloadImgHttpOperation(group.gId, group.avatarUrl,TRUE
					, fastdelegate::MakeDelegate(this, &GroupListModule_Impl::onCallbackOperation));
				module::getHttpPoolModule()->pushHttpOperation(pOper);
			}
		}
	}
}

void GroupListModule_Impl::onCallbackOperation(std::shared_ptr<void> param)
{
	DownloadImgParam* pDownParam = (DownloadImgParam*)param.get();
	if (DownloadImgParam::DOWNLOADIMG_OK == pDownParam->m_result)
	{
		module::GroupInfoMap::iterator groupIter = m_mapGroups.find(pDownParam->m_sId);
		if (groupIter != m_mapGroups.end())
		{
			groupIter->second.avatarLocalPath = pDownParam->m_imgEntity.filename;
		}
	}
}

BOOL GroupListModule_Impl::_updateRecentGroupList(void)
{
	m_GroupInfoGetTime++;
	if (2 == m_GroupInfoGetTime)
	{
		m_GroupInfoGetTime = 0;
		logic::GetLogic()->asynNotifyObserver(module::KEY_GROUPLIST_UPDATE_RECENTGROUPLIST);//֪ͨUI���������ϵ��Ⱥ����
		return TRUE;
	}
	return FALSE;
}

/******************************************************************************/