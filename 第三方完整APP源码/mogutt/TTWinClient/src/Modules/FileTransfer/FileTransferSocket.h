/*******************************************************************************
 *  @file      FileTransferSocket.h 2014\8\30 13:31:33 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     �ļ�����socket
 ******************************************************************************/

#ifndef FILETRANSFERSOCKET_612C0628_0BB6_4FF0_BE0F_F5DE2C35836D_H__
#define FILETRANSFERSOCKET_612C0628_0BB6_4FF0_BE0F_F5DE2C35836D_H__

#include "GlobalDefine.h"
#include "TTLogic/IOperation.h"
#include "TTLogic/IEvent.h"
#include "TTLogic/ITcpClientModule.h"

/******************************************************************************/
class CImPdu;
class FileTransferSocket;
class FileTransTaskBase;

namespace logic
{
	class TcpClientSocket;
	struct IOperation;
	struct ITimerEvent;
}

class PingFileSevTimer : public logic::ITimerEvent
{
public:
	PingFileSevTimer(FileTransferSocket* pTransSocket);
	virtual void process();
	virtual void release();

private:
	FileTransferSocket* m_pFileTransSocket;
};

/**
* The class <code>�ļ�����socket</code>
*
*/
class FileTransferSocket :public logic::ITcpClientSocketCallback
{
public:
	FileTransferSocket(std::string& taskId);
	~FileTransferSocket(void);

public:
	BOOL startFileTransLink();
	void stopfileTransLink();
	void sendPacket(CImPdu* pdu);

private:
	//��������
	virtual BOOL create();
	virtual BOOL connect(const CString &linkaddr, UInt16 port);
	virtual BOOL shutdown();

	//������
	virtual void startHeartbeat();
	virtual void stopHeartbeat();

	//�ص��ӿ�
	virtual void registerCB(logic::ITcpClientSocketCallback *cb);
	virtual void unRegisterCB();
	virtual logic::ITcpClientSocketCallback* getCallback();

	virtual void onClose(int error);
	virtual void onReceiveData(const char* data, UInt32 size);
	virtual void onParseError();
	virtual void onConnectDone();

private:
	/**@name �������˲��*/
	//@{
	void _fileLoginResponse(CImPdu* pdu);
	void _filePullDataReqResponse(CImPdu* pdu);
	void _filePullDataRspResponse(CImPdu* pdu);
	void _fileState(CImPdu* pdu);
	//@}

public:
	std::string							m_sTaskId;

private:
	logic::TcpClientSocket*             m_pLinkSocket;
	PingFileSevTimer*                   m_pPingTimer;
	ITcpClientSocketCallback*           m_Icallback;
	UInt32                              m_progressRefreshMark;
};
/******************************************************************************/
#endif// #define FILETRANSFERSOCKET_612C0628_0BB6_4FF0_BE0F_F5DE2C35836D_H__