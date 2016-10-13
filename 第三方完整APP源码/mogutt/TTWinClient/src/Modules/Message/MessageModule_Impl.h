/*******************************************************************************
 *  @file      HistoryMsgModule_Impl.h 2014\8\3 11:12:29 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     
 ******************************************************************************/

#ifndef HISTORYMSGMODULE_IMPL_C28C3F8C_9A47_48DF_840B_BE394EAFFFEA_H__
#define HISTORYMSGMODULE_IMPL_C28C3F8C_9A47_48DF_840B_BE394EAFFFEA_H__

#include "Modules/IMessageModule.h"
#include "../FileTransfer/TransferManager.h"
#include <map>
/******************************************************************************/
class CppSQLite3DB;

/**
 * The class <code>HistoryMsgModule_Impl</code> 
 *
 */
class MessageModule_Impl final : public module::IMessageModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
    MessageModule_Impl();
    /**
     * Destructor
     */
    virtual ~MessageModule_Impl();
    //@}
	virtual logic::LogicErrorCode onUnLoadModule();
	virtual void release();

public:
	/**@name ��ʷ��Ϣ���*/
	//@{
	virtual BOOL openDB();
	virtual void countMsgOffset(const std::string& sId, Int32 v);
	virtual void clearMsgOffset(const std::string& sId);
	virtual BOOL sqlInsertHistoryMsg(IN MessageEntity& msg);
	virtual BOOL sqlGetHistoryMsg(IN std::string sId, IN UInt32 nMsgCount, OUT std::vector<MessageEntity>& msgList);
	virtual BOOL sqlBatchInsertHistoryMsg(IN std::list<MessageEntity>& msgList);
	//@}

	/**@name �ļ��������*/
	//@{
	BOOL execFileTransferHistoryDML();
	virtual BOOL sqlInsertFileTransferHistory(IN TransferFileEntity& fileInfo);
	virtual BOOL sqlGetFileTransferHistory(OUT std::vector<TransferFileEntity>& fileList);
	//@}

	/**@name ����ʱ��Ϣ���*/
	//@{
	virtual void removeMessageBySId(const std::string& sId);//���������������Ҫ������Ϣ 
	virtual BOOL pushMessageBySId(const std::string& sId, MessageEntity& msg);
	//@}

private:
	void _closeDB();
	void _msgToTrace(const MessageEntity& msg);

private:
	CppSQLite3DB*							m_pHistoryMSGDB;
	std::map<std::string, UInt32>           m_mapMsgOffset;	//��ʷ��Ϣƫ��
};
/******************************************************************************/
#endif// HISTORYMSGMODULE_IMPL_C28C3F8C_9A47_48DF_840B_BE394EAFFFEA_H__
