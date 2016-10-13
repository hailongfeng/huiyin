/*******************************************************************************
 *  @file      ILoginModule.h 2014\7\17 19:38:12 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief   
 ******************************************************************************/

#ifndef ILOGINMODULE_2BB11F1D_3E16_44D3_92FB_6CAE04D48B6B_H__
#define ILOGINMODULE_2BB11F1D_3E16_44D3_92FB_6CAE04D48B6B_H__

#include "GlobalDefine.h"
#include "TTLogic/IModule.h"
#include "Modules/ModuleDll.h"
/******************************************************************************/
NAMESPACE_BEGIN(module)
//KEYID
enum
{
	KEY_LOGIN_USERID	= MODULE_ID_LOGIN << 16 | 1,
	KEY_LOGIN_KICKOUT	= MODULE_ID_LOGIN << 16 | 2,		//�߳�����
	KEY_LOGIN_RELOGINOK = MODULE_ID_LOGIN << 16 | 3,		//relogin�ɹ�֪ͨ
};

/**
 * The class <code>ILoginModule</code> 
 *
 */
class MODULE_API ILoginModule : public logic::IPduAsyncSocketModule
{
public:
	ILoginModule()
	{
		m_moduleId = MODULE_ID_LOGIN;
	}

public:
	virtual BOOL showLoginDialog() = 0;
	/**
	 * ��½�ɹ�����ʼ��ȡ��֯�ܹ���Ⱥ��Ϣ
	 *
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	virtual void notifyLoginDone() = 0;
	/**
	 * ���������ӿ�
	 *
	 * @param   BOOL bForce �Ƿ�ǿ������
	 * @return  void
	 * @exception there is no any exception to throw.
	 */	
	virtual void relogin(BOOL bForce) = 0;
	virtual BOOL isOfflineByMyself()const = 0;
	virtual void setOfflineByMyself(BOOL b) = 0;
};

MODULE_API ILoginModule* getLoginModule();

NAMESPACE_END(module)
/******************************************************************************/
#endif// ILOGINMODULE_2BB11F1D_3E16_44D3_92FB_6CAE04D48B6B_H__
