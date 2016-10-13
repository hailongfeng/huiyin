/*******************************************************************************
 *  @file      IHttpPoolModule.h 2014\7\25 11:18:16 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief   һ���򵥵�HTTP���س�
 ******************************************************************************/

#ifndef IHTTPPOOLMODULE_3ABB1077_8BD7_44DC_90B6_FEB35B03F6CF_H__
#define IHTTPPOOLMODULE_3ABB1077_8BD7_44DC_90B6_FEB35B03F6CF_H__

#include "GlobalDefine.h"
#include "TTLogic/IModule.h"
#include "TTLogic/IOperation.h"
#include "Modules/ModuleDll.h"
/******************************************************************************/
NAMESPACE_BEGIN(module)

struct MODULE_API IHttpOperation : public logic::ICallbackOpertaion
{
public:
	IHttpOperation(logic::ICallbackHandler& callback)
		:logic::ICallbackOpertaion(callback)
	{

	}
	virtual void release() = 0;
};

//KEYID
enum
{
	KEY_HTTPPOOL_CALLBACK					= MODULE_ID_HTTPPOOL << 16 | 1,
};

/**
 * The class <code>IHttpPoolModule</code> 
 *
 */
class MODULE_API IHttpPoolModule : public logic::IModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	IHttpPoolModule()
	{			
		m_moduleId = MODULE_ID_HTTPPOOL;
	}

public:
	/**
	* ���http�������̳߳�
	*
	* @param   IHttpOperation	 * pOperaion    ����Ԫ
	* @param   BOOL bHighPriority       ������һ���򵥵����ȼ�����ʱ����������
	* @return
	* @exception there is no any exception to throw.
	*/
	virtual void    pushHttpOperation(IHttpOperation * pOperaion, BOOL bHighPriority = FALSE) = 0;
	/**
	* �ر��̹߳�����
	*
	* @param
	* @return
	* @exception there is no any exception to throw.
	*/
	virtual void    shutdown() = 0;
};

MODULE_API IHttpPoolModule* getHttpPoolModule();

NAMESPACE_END(module)
/******************************************************************************/
#endif// IHTTPPOOLMODULE_3abb1077-8bd7-44dc-90b6-feb35b03f6cf_H__
