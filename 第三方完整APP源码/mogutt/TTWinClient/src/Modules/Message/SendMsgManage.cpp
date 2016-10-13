/******************************************************************************* 
 *  @file      SendMsgManage.cpp 2014\8\7 13:52:48 $
 *  @author    ���<dafo@mogujie.com>
 *  @brief     
 ******************************************************************************/

#include "stdafx.h"
#include "SendMsgManage.h"
#include "TTLogic/ILogic.h"
#include "TTLogic/ITcpClientModule.h"
#include "src/base/ImPduClient.h"
#include "Modules/IMessageModule.h"
#include "Modules/ISysConfigModule.h"
#include "Modules/ISessionModule.h"
namespace
{
	const UInt8 TIMEOUT_SENDINGMSG = 5;	//���ڷ��͵���Ϣ����ʱΪ5S
	const UInt8 SENDMSG_RETRY_CNT = 1;	//
	const UInt32 SENDMSG_INTERVAL = 100;//ʱ����Ϊ100����
}

/******************************************************************************/

// -----------------------------------------------------------------------------
//  SendMsgManage: Public, Constructor

SendMsgManage::SendMsgManage()
{
	CheckSendMsgTimer* pCheckSendMsgTimer = new CheckSendMsgTimer(this);
	logic::GetLogic()->scheduleTimer(pCheckSendMsgTimer, 1, TRUE);
	srand(static_cast<UInt32>(time(0)));
}

// -----------------------------------------------------------------------------
//  SendMsgManage: Public, Destructor

SendMsgManage::~SendMsgManage()
{

}

void SendMsgManage::pushSendingMsg(IN MessageEntity& msg)
{
	SendingMsg sendingMsg;
	util::TTAutoLock autoLock(&m_lock);
	if (m_ListSendingMsg.empty())
	{
		sendingMsg.sendtime = static_cast<long>(clock());
		sendingMsg.msg = msg;
		sendingMsg.seqNo = _getSeqNo();
		sendingMsg.status = MSGSTATUS_SENDING;
		m_ListSendingMsg.push_back(sendingMsg);
		sendMessage(sendingMsg);
	}
	else
	{
		sendingMsg.sendtime = static_cast<long>(clock());
		sendingMsg.msg = msg;
		sendingMsg.seqNo = _getSeqNo();
		SendingMsg& BackMsg = m_ListSendingMsg.back();
		if (sendingMsg.sendtime - BackMsg.sendtime > SENDMSG_INTERVAL)
		{
			m_ListSendingMsg.push_back(sendingMsg);
			SendingMsgList::iterator itBegin = m_ListSendingMsg.begin();

			if (itBegin->status == MSGSTATUS_TOSEND)
			{
				itBegin->status = MSGSTATUS_SENDING;
				sendMessage(*itBegin);
			}
		}
		else
		{//����Ƶ��̫��
			logic::GetLogic()->asynNotifyObserver(module::KEY_SESSION_SENDMSG_TOOFAST, sendingMsg.msg.sessionId);//������Ϣ̫��
			return;
		}

	}

}
void SendMsgManage::getSendFailedMsgs(OUT SendingMsgList& FailedMsgList)
{
	util::TTAutoLock autoLock(&m_lock);
	if (m_ListSendingMsg.empty())
	{
		return;
	}
	SendingMsg& itFront = m_ListSendingMsg.front();
	if (itFront.waitSeconds > TIMEOUT_SENDINGMSG
		&&itFront.retrySendCnt <= SENDMSG_RETRY_CNT)
	{
		std::copy(m_ListSendingMsg.begin(), m_ListSendingMsg.end(), std::back_inserter(FailedMsgList));
	}
	m_ListSendingMsg.clear();
}

SendMsgManage* SendMsgManage::getInstance()
{
	static SendMsgManage manager;
	return &manager;
}

UInt32 SendMsgManage::_getSeqNo(void)
{
	static UInt32 seqNo = static_cast<UInt32>(rand() % 10000);
	return ++seqNo;
}

BOOL SendMsgManage::popUpSendingMsgByAck(UInt32 seqNo)
{
	util::TTAutoLock autoLock(&m_lock);
	//�ҵ���Ӧ��seqNo�Ļ�����erase��
	SendingMsgList::iterator iter = m_ListSendingMsg.begin();
	for (; iter != m_ListSendingMsg.end(); ++iter)
	{
		if (iter->seqNo == seqNo)
		{
			//����Ϣ���浽DB
			if (module::getMessageModule()->sqlInsertHistoryMsg(iter->msg))
			{
				//�����յ�����Ϣ����offset����
				module::getMessageModule()->countMsgOffset(iter->msg.getOriginSessionId(), 1);
			}
			
			SendingMsgList::iterator iterNext = m_ListSendingMsg.erase(iter);
			if (iterNext != m_ListSendingMsg.end() && iterNext->status == MSGSTATUS_TOSEND)//����һ��
			{
				iterNext->status = MSGSTATUS_SENDING;
				sendMessage(*iterNext);
			}

			return TRUE;
		}
	}
	return FALSE;
}

void SendMsgManage::clearSendMessageList()
{
	if (!m_ListSendingMsg.empty())
	{
		m_ListSendingMsg.clear();
	}
}

void SendMsgManage::sendMessage(IN SendingMsg& sendingMsg)
{
	logic::GetLogic()->pushBackOperationWithLambda(
		[=]()
	{
		SendingMsg sendingmsgTemp = sendingMsg;
		CImPduClientMsgData pduMsgData(sendingmsgTemp.seqNo
			, module::getSysConfigModule()->userID().c_str()
			, sendingmsgTemp.msg.getOriginSessionId().c_str()//Ҫ��ԭʼ��ʼID
			, sendingmsgTemp.msg.msgTime                              //����ʱ�� Ŀǰ�ɷ������ṩ
			, sendingmsgTemp.msg.msgType           //��Ϣ��Դ����
			, sendingmsgTemp.msg.content.length(), (uchar_t*)sendingmsgTemp.msg.content.c_str()
			, sendingmsgTemp.msg.imageId.length(), (char*)sendingmsgTemp.msg.imageId.c_str());
		logic::getTcpClientModule()->sendPacket(&pduMsgData);
	}
	);

}



//////////////////////////////////////////////////////////////////////////
CheckSendMsgTimer::CheckSendMsgTimer(SendMsgManage* pMsgCheck)
:m_pMsgCheck(pMsgCheck)
{

}

CheckSendMsgTimer::~CheckSendMsgTimer()
{

}

void CheckSendMsgTimer::process()
{
	if (m_pMsgCheck == nullptr)
	{
		return;
	}

	util::TTAutoLock autoLock(&m_pMsgCheck->m_lock);
	if (m_pMsgCheck->m_ListSendingMsg.empty())
	{
		return;
	}
	SendingMsg& itFront = m_pMsgCheck->m_ListSendingMsg.front();
	if (itFront.status == MSGSTATUS_SENDING)//����Ϊ���ڷ��͵���Ϣ
	{
		++itFront.waitSeconds;
		if (itFront.waitSeconds > TIMEOUT_SENDINGMSG)//��ʱ����
		{
			if (itFront.retrySendCnt < SENDMSG_RETRY_CNT)
			{
				++itFront.retrySendCnt;
				itFront.waitSeconds = 0;
				//��Ϣ����ʧ��һ�κ�����һ��
				m_pMsgCheck->sendMessage(itFront);
			}
			else//���������׸���Ϣ����ʧ�ܣ�����Ϊ������Ϣ���ж���ʱ
			{
				logic::GetLogic()->asynNotifyObserver(module::KEY_SESSION_SENDMSG_FAILED, itFront.msg.sessionId);//������Ϣʧ��
			}
		}
	}
	else if (MSGSTATUS_TOSEND == itFront.status)//����Ϊ�ȴ�����״̬����Ϣ
	{
		itFront.status = MSGSTATUS_SENDING;
		m_pMsgCheck->sendMessage(itFront);
	}
}

void CheckSendMsgTimer::release()
{
	delete this;
}

/******************************************************************************/