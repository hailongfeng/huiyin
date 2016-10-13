/*******************************************************************************
 *  @file      IHitoryMsgModule.h 2014\8\3 11:10:16 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     ��ʷ��ϢDB��ؽӿ�
 ******************************************************************************/

#ifndef IHITORYMSGMODULE_D042F2F2_05B0_45E6_9746_344A76279AE8_H__
#define IHITORYMSGMODULE_D042F2F2_05B0_45E6_9746_344A76279AE8_H__

#include "GlobalDefine.h"
#include "TTLogic/IModule.h"
#include "Modules/ModuleDll.h"
#include "MessageEntity.h"
/******************************************************************************/
class MessageEntity;
class TransferFileEntity;
NAMESPACE_BEGIN(module)

/**
 * The class <code>��ʷ��ϢDB��ؽӿ�</code> 
 *
 */
class MODULE_API IMessageModule : public logic::IModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	IMessageModule()
	{
		m_moduleId = MODULE_ID_MESSAGE;
	}
    //@}

public:
	/**@name ��ʷ��Ϣ���*/
	//@{
	/**
	 * ����Ϣ���ݿ�
	 *
	 * @return  BOOL
	 * @exception there is no any exception to throw.
	 */
	virtual BOOL openDB() = 0;
	/**
	 * ���Ự�ۼ���ʷ��Ϣ������
	 *
	 * @param   const std::string & sId
	 * @param   UInt8 v
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	virtual void countMsgOffset(const std::string& sId, Int32 v) = 0;
	/**
	 * ����Ự��ʷ��Ϣ������
	 *
	 * @param   const std::string & sId
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	virtual void clearMsgOffset(const std::string& sId) = 0;
	virtual BOOL sqlInsertHistoryMsg(IN MessageEntity& msg) = 0;
	virtual BOOL sqlGetHistoryMsg(IN std::string sId, IN UInt32 nMsgCount, OUT std::vector<MessageEntity>& msgList) = 0;
	virtual BOOL sqlBatchInsertHistoryMsg(IN std::list<MessageEntity>& msgList) = 0;
	//@}

	/**@name �ļ��������*/
	//@{
	virtual BOOL sqlInsertFileTransferHistory(IN TransferFileEntity& fileInfo) = 0;
	virtual BOOL sqlGetFileTransferHistory(OUT std::vector<TransferFileEntity>& fileList) = 0;
	//@}

	/**@name ����ʱ��Ϣ���*/
	//@{
	/**
	 * ���������������Ҫ������Ϣ 
	 *
	 * @param   const std::string & sId
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	virtual void removeMessageBySId(const std::string& sId) = 0;
	virtual BOOL pushMessageBySId(const std::string& sId, MessageEntity& msg) = 0;
	//@}
};

MODULE_API IMessageModule* getMessageModule();

NAMESPACE_END(module)
/******************************************************************************/
#endif// IHITORYMSGMODULE_D042F2F2_05B0_45E6_9746_344A76279AE8_H__
