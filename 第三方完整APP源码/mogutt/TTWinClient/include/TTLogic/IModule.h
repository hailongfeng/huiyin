/*******************************************************************************
 *  @file      IModule.h 2014\7\16 17:52:47 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief   ҵ��ģ��ӿ�
 ******************************************************************************/

#ifndef IMODULE_3A2EC0A6_395A_4300_8D4D_905BEA8513DC_H__
#define IMODULE_3A2EC0A6_395A_4300_8D4D_905BEA8513DC_H__

#include "GlobalDefine.h"
#include "TTLogic/ErrorCode.h"
#include "TTLogic/ModuleID.h"
#include "TTLogic/ILogic.h"
#include "TTLogic/TTLogicDll.h"
#include "src/base/ImPduBase.h"
/******************************************************************************/
NAMESPACE_BEGIN(logic)

struct  ILogic;

/**
 * The class <code>IModule</code> 
 *
 */
class TTLOGIC_API IModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	IModule()
		:m_moduleId(MODULE_ID_NONE)
	{

	}
    /**
     * Destructor
     */
	virtual ~IModule()
	{
	}
    //@}

public:
	/**
	* ��ȡģ������
	*
	* @return  Int16   �����ģ���ID
	* @exception there is no any exception to throw.
	*/
	Int16 getModuleId()const
	{
		return m_moduleId;
	}

	/**
	* ���ظ�ģ��󱻵���
	*
	* @return  LogicErrorCode
	* @exception there is no any exception to throw.
	*/
	virtual LogicErrorCode onLoadModule() { return LOGIC_OK; }

	/**
	* ж�ظ�ģ��ǰ������
	*
	* @return  LogicErrorCode
	* @exception there is no any exception to throw.
	*/
	virtual LogicErrorCode onUnLoadModule() { return LOGIC_OK; }
	/**
	* �Ƿ����ӳټ���ģ��
	*
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	virtual BOOL isLazyLoadModule()const { return FALSE; }
	/**
	* �ͷ��Լ�
	*
	* @return  void
	* @exception there is no any exception to throw.
	*/
	virtual void release() = 0;

protected:
	UInt16          m_moduleId;
};

/**
* The class <code>IPduAsyncSocketModule</code>
*
*/
class IPduAsyncSocketModule : public IModule
{
public:
	/**
	 * �յ�TcpClient�������Ĵ�����
	 *
	 * @param   std::auto_ptr<CImPdu> pdu
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	virtual void onPacket(std::auto_ptr<CImPdu> pdu) = 0;
};

NAMESPACE_END(logic)
/******************************************************************************/
#endif// IMODULE_3A2EC0A6_395A_4300_8D4D_905BEA8513DC_H__
