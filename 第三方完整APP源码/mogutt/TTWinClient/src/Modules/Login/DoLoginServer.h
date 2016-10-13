/*******************************************************************************
 *  @file      DoLoginServer.h 2013\8\28 14:55:12 $
 *  @author    kuaidao<kuaidao@mogujie.com>
 *  @brief   ��½�����������ӣ�Ŀ���ǻ�ȡ��Ϣ��������IP��ַ
 ******************************************************************************/

#ifndef LOGINSERVERLINK_9C3305C5_9230_4174_8B69_3CC9B0DEE848_H__
#define LOGINSERVERLINK_9C3305C5_9230_4174_8B69_3CC9B0DEE848_H__

#include "TTLogic/ITcpClientModule.h"

/******************************************************************************/
namespace logic
{
	class TcpClientSocket;
}
class CImPdu;

/**
 * The class <code>DoLoginServer</code> 
 *
 */
class DoLoginServer final : public logic::ITcpClientSocketCallback
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
    DoLoginServer();
    /**
     * Destructor
     */
    virtual ~DoLoginServer(); 
    //@}
    static DoLoginServer* getInstance();
    static void releaseInstance();

public:
    BOOL createLink();
    void shutdown();
    CImPdu* doLogin();

public:
	virtual void onClose(int error);
	virtual void onReceiveData(const char* data, UInt32 size);
	virtual void onParseError();
	virtual void onConnectDone();

private:
    CImPdu* _sendPacketAndWaitResponse(CImPdu* pdu,UInt32 timeout=10);
    BOOL _waitConnectedNotify();

private:
	logic::TcpClientSocket*         m_pLinkSocket;				// ����

    HANDLE							m_eventReceived;			//���յ����¼�
    HANDLE							m_eventConnected;			//���ӳɹ��¼�
    CImPdu*							m_pImPdu;
    ByteBuffer						m_serailBuffer;
};
/******************************************************************************/
#endif// LOGINSERVERLINK_9C3305C5_9230_4174_8B69_3CC9B0DEE848_H__
