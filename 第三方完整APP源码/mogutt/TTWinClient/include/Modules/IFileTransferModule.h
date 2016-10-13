/*******************************************************************************
 *  @file      IFileTransfer.h 2014\8\26 11:51:55 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     
 ******************************************************************************/

#ifndef IFILETRANSFER_425BDB8D_221E_4952_93C5_4362AF5217CB_H__
#define IFILETRANSFER_425BDB8D_221E_4952_93C5_4362AF5217CB_H__

#include "GlobalDefine.h"
#include "TTLogic/IModule.h"
#include "Modules/ModuleDll.h"
#include "Modules/IMiscModule.h"
#include "utility/utilCommonAPI.h"
#include "utility/utilStrCodeAPI.h"
/******************************************************************************/
NAMESPACE_BEGIN(module)

const std::string FILETRANSFER_IP = "122.225.68.125";
const UInt16 FILETRANSFER_PORT = 29800;

//KEYID
enum
{
	KEY_FILETRANSFER_SENDFILE				= MODULE_ID_FILETRANSFER << 16 | 1,       //�����ļ�
	KEY_FILETRANSFER_REQUEST				= MODULE_ID_FILETRANSFER << 16 | 2,       //�����ļ�����-���շ�
	KEY_FILETRANSFER_RESPONSE				= MODULE_ID_FILETRANSFER << 16 | 3,       //�����ļ����󷵻�-���ͷ�
	KEY_FILESEVER_UPLOAD_OFFLINE_FINISH		= MODULE_ID_FILETRANSFER << 16 | 7,			//�����ļ����䵽�ļ����������
	KEY_FILESEVER_UPDATA_PROGRESSBAR		= MODULE_ID_FILETRANSFER << 16 | 9,			//�����ļ����������
	KEY_FILESEVER_PROGRESSBAR_FINISHED		= MODULE_ID_FILETRANSFER << 16 | 10,		//�������
	KEY_FILESEVER_UPDATA_FAILED				= MODULE_ID_FILETRANSFER << 16 | 11,		//����ʧ��
	KEY_FILESEVER_UPDATA_CANCEL				= MODULE_ID_FILETRANSFER << 16 | 12,		//����ȡ��
	KEY_FILESEVER_UPDATA_REJECT				= MODULE_ID_FILETRANSFER << 16 | 13,		//�ܾ�����
};

/**
 * The class <code>IFileTransfer</code> 
 *
 */
class IFileTransferModule : public logic::IPduAsyncSocketModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	IFileTransferModule()
	{
		m_moduleId = MODULE_ID_FILETRANSFER;
	}
    //@}

public:
	virtual BOOL sendFile(IN const CString& sFilePath, IN const std::string& sSendToSID, IN BOOL bOnlineMode) = 0;
	virtual BOOL acceptFileTransfer(IN const std::string& sTaskId, IN const BOOL bAccept = TRUE) = 0;
	virtual BOOL doCancel(IN const std::string& sFileID) = 0;
	virtual void showFileTransferDialog() = 0;
};

MODULE_API IFileTransferModule* getFileTransferModule();

NAMESPACE_END(module)
/******************************************************************************/
#endif// IFILETRANSFER_425BDB8D_221E_4952_93C5_4362AF5217CB_H__
