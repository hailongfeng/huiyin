/*******************************************************************************
 *  @file      ILogic.h 2014\7\16 18:29:18 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief   
 ******************************************************************************/

#ifndef ILOGIC_D21393E4_63FB_419A_A4DF_758A7067FE3D_H__
#define ILOGIC_D21393E4_63FB_419A_A4DF_758A7067FE3D_H__

#include "GlobalDefine.h"
#include "TTLogic/ErrorCode.h"
#include "TTLogic/TTLogicDll.h"
#include "TTLogic/Observer.h"
#include "TTLogic/ModuleID.h"
#include <functional>
/******************************************************************************/
NAMESPACE_BEGIN(logic)

class  IModule;
struct IEvent;
struct ITimerEvent;
struct IOperation;

namespace
{
	struct ILogicMKO
	{
		virtual void addObserver(void* pObserObject,UInt16 moduleId, IObserverHandler& handle) = 0;
		virtual void removeObserver(void* pObserObject) = 0;
		virtual void asynNotifyObserver(UInt32 keyId) = 0;
		virtual void asynNotifyObserver(UInt32 keyId, std::string& mkoString) = 0;
		virtual void asynNotifyObserver(UInt32 keyId, Int32 mkoInt) = 0;
		virtual void asynNotifyObserver(UInt32 keyId, void* pmkoVoid) = 0;
		virtual void asynNotifyObserver(UInt32 keyId, std::shared_ptr<void> pmkoShardVoid) = 0;
	};
	/**
	* The class <code>Module�ӿڶ���</code>
	*
	*/
	struct ILogicModule
	{
		/**
		* ע��ģ��(ע���ȥ��Module����ʵ������Ҫ�Լ��ͷţ�
		*         �������е�����unRegisterModule)
		*
		* @param   IModule * pModule     ģ�����ӿ�ָ��
		* @return  LogicErrorCode
		* @exception there is no any exception to throw.
		*/
		virtual LogicErrorCode registerModule(IModule* pModule)throw() = 0;

		/**
		* ��ע��ģ�飨���øýӿ�֮�������е���pModule->release()����������ڴ�й¶��
		*
		* @param   IModule * pModule     ģ�����ӿ�ָ��
		* @return  LogicErrorCode
		* @exception there is no any exception to throw.
		*/
		virtual LogicErrorCode unRegisterModule(IModule* pModule)throw() = 0;

		/**
		* ���ӳټ���ģ�飬�ֶ����м���
		*
		* @param   IModule * pModule     ģ�����ӿ�ָ��
		* @return  LogicErrorCode
		* @exception there is no any exception to throw.
		*/
		virtual LogicErrorCode	loadModule(IModule* pModule) = 0;

		/**
		* ͨ��Module ID��ȡModule�ӿ�ָ��
		*
		* @param   Int16 ModuleId     Module ID(ID�Ŷ������Moduleid.h)
		* @return  IModule*           Module�ӿ�ָ��
		* @exception there is no any exception to throw.
		*/
		virtual IModule* getModule(Int16 ModuleId) = 0;
	};
}

/**
* The class <code>��̨������ؽӿڶ���</code>
*
*/
struct ILogicWorker
{
	/**
	* ����һ��Operation���������β��(����������Operation����ʵ������Ҫ�Լ��ͷ�)
	*
	* @param   IOperation * pOperation
	* @param	int delay = 0
	* @return  void
	*/
	virtual LogicErrorCode pushBackOperation(IN IOperation* pOpertaion,Int32 delay = 0) = 0;

	/**
	* ��Operation ��lambda���ʽ��ʽ���뵽���������
	*
	* @param   std::function<void()> operationRun
	* @return  void
	*/
	virtual LogicErrorCode pushBackOperationWithLambda(std::function<void()> operationRun,Int32 delay = 0) = 0;

	/**
	* �첽����һ��UI�¼������߳�(event����ʵ������Ҫ�Լ��ͷ�)
	*
	* @param   IN const IEvent * const pEvent
	* @return  void
	*/
	virtual LogicErrorCode asynFireUIEvent(IN const IEvent* pEvent) = 0;
	
	/**
	* UIEvent��lambda��ʽ����
	*
	* @param   std::function<void()> eventRun
	* @return  void
	*/
	virtual LogicErrorCode asynFireUIEventWithLambda(std::function<void()> eventRun) = 0;
	/**
	* lambda��ʽ����TT��ʱ��
	*
	* @param   std::function<void()> timerRun timer��ʱ��ִ��lambda���ʽ
	* @param   UInt32 delay				��ʱ������Ϊ��λ��
	* @param   BOOL bRepeat				�Ƿ��ظ�
	* @param   ITimerEvent** ppTimer	lambad����ִ�е�TimerEvent����ָ��
	* @return  logic::LogicErrorCode
	* @exception there is no any exception to throw.
	*/
	virtual LogicErrorCode scheduleTimerWithLambda(IN UInt32 delay, IN BOOL bRepeat
												 , IN std::function<void()> timerRun
												 , OUT ITimerEvent** ppTimer) = 0;
	/**
	* ����TT��ʱ��(TimerEvent����Ҫ�Լ��ͷ�)
	*
	* @param   IN ITimerEvent * pEvent timer��ʱ��ִ�нӿ�
	* @param   UInt32 delay            ��ʱ������Ϊ��λ��
	* @param   BOOL bRepeat            �Ƿ��ظ�
	* @return  logic::LogicErrorCode
	* @exception there is no any exception to throw.
	*/
	virtual LogicErrorCode scheduleTimer(IN ITimerEvent* pTimerEvent, IN UInt32 delay, IN BOOL bRepeat) = 0;

	/**
	* ɾ����ʱ��
	*
	* @param   IN ITimerEvent * pEvent
	* @return  logic::LogicErrorCode
	* @exception there is no any exception to throw.
	*/
	virtual LogicErrorCode killTimer(IN ITimerEvent* pTimerEvent) = 0;
};

/**
 * The class <code>ILogic</code> 
 *
 */
struct TTLOGIC_API ILogic : public ILogicModule
						  , public ILogicWorker
						  , public ILogicMKO
{
	//�߼�����������ر�
	/**
	* �����߼�����
	*
	* @return  LogicErrorCode
	* @exception there is no any exception to throw.
	*/
	virtual LogicErrorCode startup() = 0;
	/**
	* �ر��߼�����,��Ϊ�׶�1��2
	*
	* @return  void
	* @exception there is no any exception to throw.
	*/
	virtual void shutdownPhase1() = 0;
	virtual void shutdownPhase2() = 0;

	/**
	* �ͷ��Լ�
	*
	* @return  void
	* @exception there is no any exception to throw.
	*/
	virtual void release() = 0;
};
TTLOGIC_API LogicErrorCode CreateSingletonILogic();
TTLOGIC_API void DestroySingletonILogic();
TTLOGIC_API ILogic* GetLogic();

NAMESPACE_END(logic)
/******************************************************************************/
#endif// ILOGIC_D21393E4_63FB_419A_A4DF_758A7067FE3D_H__
