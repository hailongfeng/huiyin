/*******************************************************************************
 *  @file      FileTransfer_Impl.h 2014\8\26 11:53:06 $
 *  @author    快刀<kuaidao@mogujie.com>
 *  @brief     文件传输业务module
 ******************************************************************************/

#ifndef FILETRANSFER_IMPL_AB8D6DD0_25EF_4809_A857_9450EEA7CBFE_H__
#define FILETRANSFER_IMPL_AB8D6DD0_25EF_4809_A857_9450EEA7CBFE_H__

#include "TransferManager.h"
#include "Modules/IFileTransferModule.h"
/******************************************************************************/
class FileTransferDialog;                                                                                                                                             

/**
 * The class <code>FileTransfer_Impl</code> 
 *
 */
class FileTransferModule_Impl : public module::IFileTransferModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
    FileTransferModule_Impl();
    /**
     * Destructor
     */
    virtual ~FileTransferModule_Impl();
    //@}
	virtual void onPacket(std::auto_ptr<CImPdu> pdu);
	virtual void release();
	virtual logic::LogicErrorCode onLoadModule();
	virtual logic::LogicErrorCode onUnLoadModule();

public:
	//发送文件
	virtual BOOL sendFile(IN const CString& sFilePath, IN const std::string& sSendToSID,IN BOOL bOnlineMode);
	virtual BOOL acceptFileTransfer(IN const std::string& sTaskId, IN const BOOL bAccept = TRUE);
	virtual BOOL doCancel(IN const std::string& sFileID);

	virtual void showFileTransferDialog();

	void OnFileTransferModuleEvent(UInt16 moduleId, UInt32 keyId, MKO_TUPLE_PARAM mkoParam);

private:
	/**@name 服务器端拆包*/
	//@{
	void _sendfileResponse(CImPdu* pdu);
	void _hasOfflineRes(CImPdu* pdu);
	void _fileNotify(CImPdu* pdu);
	//@}
	BOOL _checkIfIsSending(IN  CString sFilePath);//正在传输的文件不能再次传输
private:
	FileTransferDialog*							m_pFileTransferDialog;
};
/******************************************************************************/
#endif// FILETRANSFER_IMPL_AB8D6DD0_25EF_4809_A857_9450EEA7CBFE_H__
