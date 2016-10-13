/*******************************************************************************
 *  @file      TcpClientSocket.h 2014\7\29 13:36:34 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     �Կ�Դ��CAsyncSocketEx����һ��Facadeģʽ,��ʱ��֧�ִ���ģʽ����֧��SSL
 ******************************************************************************/

#ifndef TCPCLIENTSOCKET_A769B67E_BBDB_43C0_AC10_13491537A775_H__
#define TCPCLIENTSOCKET_A769B67E_BBDB_43C0_AC10_13491537A775_H__

#include "GlobalDefine.h"
#include "nio/AsyncSocketEx.h"
#include "utility/TTAutoLock.h"
#include "TTLogicDll.h"
/******************************************************************************/
NAMESPACE_BEGIN(logic)
struct ITcpClientSocketCallback;

/**
 * The class <code>TcpClientSocket</code> 
 *
 */
class TTLOGIC_CLASS TcpClientSocket : public CAsyncSocketEx
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
    TcpClientSocket();
    /**
     * Destructor
     */
    virtual ~TcpClientSocket();
    //@}

public:
	BOOL create();
	BOOL connect(LPCTSTR lpszHostAddress, UInt32 nHostPort);
	void send(const char* lpBuf, int nBufLen);
	void closeSocket();
	BOOL shutdown();

	/**@name TcpClientSocket�ص��ӿ�ע�����*/
	//@{
	inline void unRegisterCB() { m_pTcpClientSocketCB = 0; }
	inline void registerCB(ITcpClientSocketCallback *cb) { m_pTcpClientSocketCB = cb; }
	inline ITcpClientSocketCallback* getCallback()const { return m_pTcpClientSocketCB; }
	//@}

private:
	BOOL _wouldBlock();

private:
	/**@name CAsyncSocketEx�¼��ص��ӿ����*/
	//@{
	virtual void onAccept(int nErrorCode);
	virtual void onClose(int nErrorCode);
	virtual void onConnect(int nErrorCode);
	virtual void onReceive(int nErrorCode);
	virtual void onSend(int nErrorCode);
	//@}

private:
	ByteBuffer                      m_OutBuffer;
	ByteBuffer                      m_InBuffer;
	util::TTFastLock				m_OutBufferLock;
	ITcpClientSocketCallback*       m_pTcpClientSocketCB;
};

NAMESPACE_END(logic)
/******************************************************************************/
#endif// TCPCLIENTSOCKET_a769b67e-bbdb-43c0-ac10-13491537a775_H__
