/*******************************************************************************
 *  @file      SessionModule_Impl.h 2014\7\27 10:10:59 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief   
 ******************************************************************************/

#ifndef SESSIONMODULE_IMPL_414A6CFB_C817_43C4_9C73_7E965E7317C7_H__
#define SESSIONMODULE_IMPL_414A6CFB_C817_43C4_9C73_7E965E7317C7_H__

#include "Modules/ISessionModule.h"
/******************************************************************************/
class SyncTimeTimer;

/**
 * The class <code>SessionModule_Impl</code> 
 *
 */
class MessageEntity;
class SessionModule_Impl final : public module::ISessionModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	SessionModule_Impl();
    /**
     * Destructor
     */
    virtual ~SessionModule_Impl();
    //@}
	virtual void release();
	virtual void onPacket(std::auto_ptr<CImPdu> pdu);

public:
	virtual DuiLib::CControlUI* createMainDialogControl(
		LPCTSTR pstrClass, DuiLib::CPaintManagerUI& paintManager);
	virtual void OnGroupUnreadMsgRespone(IN CImPdu* pdu);

	/**@name ͬ��������ʱ��*/
	//@{
	virtual UInt32 getTime()const;
	virtual void setTime(UInt32 time);
	virtual void startSyncTimeTimer();
	//@}

private:
	/**@name �������˲��*/
	//@{
	void _sessionMsgResponse(CImPdu* pdu);
	void _sessionMsgACK(CImPdu* pdu);
	void _sessionMsgTimeResponse(CImPdu* pdu);
	void _sessionMsgUnreadCntResponse(CImPdu* pdu);
	void _sessionMsgUnreadMsgResponse(CImPdu* pdu);
	//@}
	BOOL _checkMsgFromStranger(IN MessageEntity msg);//��Ϣ��Դ��ID�Ǵ��ڵ�ǰ�ỰID�б��У������ڣ���Ҫȥ��ȡ
	BOOL _banGroupMSG(IN MessageEntity msg);//Ⱥ��Ϣ����

private:
	SyncTimeTimer*              m_pSyncTimer;
};
/******************************************************************************/
#endif// SESSIONMODULE_IMPL_414A6CFB_C817_43C4_9C73_7E965E7317C7_H__
