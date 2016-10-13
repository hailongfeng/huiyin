/*******************************************************************************
 *  @file      SessionManager.h 2014\8\11 16:58:48 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     �Ự��Ϣ����
 ******************************************************************************/

#ifndef SESSIONMANAGER_863FBDB8_F00A_4A46_8F57_1AECBC9D277E_H__
#define SESSIONMANAGER_863FBDB8_F00A_4A46_8F57_1AECBC9D277E_H__

#include "GlobalDefine.h"
#include "utility/TTAutoLock.h"
#include <string>
#include <map>
#include <list>
/******************************************************************************/
class SessionDialog;
enum
{
	SESSION_ERRORTYPE = 0,
	SESSION_USERTYPE,		//���˻Ự
	SESSION_GROUPTYPE,		//Ⱥ�Ự
};
class SessionEntity
{
public:
	SessionEntity();
	std::string getAvatarPath();
	UInt8 getOnlineState();
	CString getName();
	void SetUpdatedTimeByServerTime();

public:
	BOOL                                m_bBanGroupMsg;		//����Ⱥ��Ϣ
	UInt8								m_sessionType;		//SESSION_USERTYPE / SESSION_GROUPTYPE
	time_t                              m_updatedTime;      //��Ϣ������ʱ��
	UInt32                              m_unReadMsgCount;   //��Ϣ��
	CString								m_csDesc;			//
	std::string                         m_sId;				//�ỰID
};

/**
 * The class <code>�Ự��Ϣ����</code> 
 *
 */
class SessionEntityManager
{
public:
	~SessionEntityManager();
	static SessionEntityManager* getInstance();

private:
	SessionEntityManager();

public:
	SessionEntity* createSessionEntity(const std::string& sId);
	SessionEntity* getSessionEntityBySId(IN const std::string& sId);
	BOOL removeSessionEntity(const std::string& sId);
	BOOL saveBanGroupMSGSetting(IN const std::string& sId, IN BOOL bBanMsg);//����Ⱥ��Ϣ����
private:
	UInt8 _getSessionType(IN const std::string& sID);
	BOOL _getBanGroupMSGSetting(IN const std::string& sId);
	void _removeAllSessionEntity();

private:
	util::TTFastLock							m_lock;
	std::map<std::string,SessionEntity*>		m_mapSessionEntity;
};

//�Ѿ��򿪵ĻỰ
class SessionDialogManager
{
public:
	SessionDialog* openSessionDialog(const std::string& sId);
	SessionDialog* findSessionDialogBySId(const std::string& sId);
	void closeSessionDialog(const std::string& sId);
	static SessionDialogManager* getInstance();

private:
	SessionDialogManager();

private:
	std::list<SessionDialog*>					m_lstSessionDialog;
};

/******************************************************************************/
#endif// SESSIONMANAGER_863FBDB8_F00A_4A46_8F57_1AECBC9D277E_H__
