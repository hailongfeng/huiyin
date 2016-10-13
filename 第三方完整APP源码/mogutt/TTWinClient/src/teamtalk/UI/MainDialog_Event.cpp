/******************************************************************************* 
 *  @file      MainDialog_Event.cpp 2014\8\20 15:43:48 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     
 ******************************************************************************/

#include "stdafx.h"
#include "MainDialog.h"
#include "UI/UIMenu.h"
#include "TTLogic/ITcpClientModule.h"
#include "Modules/ILoginModule.h"
#include "Modules/IMiscModule.h"
#include "Modules/IUserListModule.h"
#include "Modules/ISessionModule.h"
#include "Modules/ISysConfigModule.h"
#include "utility/Multilingual.h"
#include "src/base/ImPduClient.h"
#include "Modules/IFileTransferModule.h"
/******************************************************************************/
void MainDialog::OnClick(TNotifyUI& msg)
{
	PTR_VOID(msg.pSender);
	if (msg.pSender == m_pbtnSysConfig)
	{
		//ϵͳ����
		module::getSysConfigModule()->showSysConfigDialog(m_hWnd);
	}
	else if (msg.pSender == m_pbtnOnlineStatus)
	{
		CMenuWnd* pMenu = new CMenuWnd(m_hWnd);
		DuiLib::CPoint point = msg.ptMouse;
		ClientToScreen(m_hWnd, &point);
		STRINGorID xml(_T("menu\\lineStatus.xml"));
		pMenu->Init(NULL, xml, _T("xml"), point);
	}
	else if (msg.pSender == m_pbtnMyFace)
	{
		//show the detail of myself.
		logic::GetLogic()->asynNotifyObserver(module::KEY_SYSCONFIG_SHOW_USERDETAILDIALOG,module::getSysConfigModule()->userID());	//֪ͨ�Ķ�
	}
	else if (msg.pSender == m_pbtnClose
		|| msg.pSender == m_pbtnMinMize)
	{
		ShowWindow(false);
		return;
	}
	__super::OnClick(msg);
}

void MainDialog::OnTcpClientModuleEvent(UInt16 moduleId, UInt32 keyId, MKO_TUPLE_PARAM mkoParam)
{
	if (logic::KEY_TCPCLIENT_STATE == keyId)
	{
		//TCP�����Ͽ�����
		if (logic::TCPCLIENT_STATE_DISCONNECT == logic::getTcpClientModule()->getTcpClientNetState())
		{
			APP_LOG(LOG_ERROR, _T("OnTcpClientModuleEvent TCP Client link broken!!!"));

			//������ͼ��Ҷȵ�
			CString csTip = util::getMultilingual()->getStringViaID(_T("STRID_MAINDIALOG_TIP_DISCONNECT"));
			SetTrayTooltipText(csTip);
			UpdateLineStatus(USER_STATUS_OFFLINE);

			//��������ҵ��
			logic::getTcpClientModule()->closeSocket();
			logic::getTcpClientModule()->shutdown();
			if (!module::getLoginModule()->isOfflineByMyself())
				module::getLoginModule()->relogin(FALSE);
		}
	}
}
void MainDialog::OnCopyData(IN COPYDATASTRUCT* pCopyData)
{
	if (nullptr == pCopyData)
	{
		return;
	}
	logic::GetLogic()->asynNotifyObserver(module::TAG_SESSION_TRAY_COPYDATA, pCopyData->dwData);
}
void MainDialog::OnLoginModuleEvent(UInt16 moduleId, UInt32 keyId, MKO_TUPLE_PARAM mkoParam)
{
	if (module::KEY_LOGIN_RELOGINOK == keyId)
	{
		CString sText = util::getMultilingual()->getStringViaID(_T("STRID_GLOBAL_CAPTION_NAME"));
#ifdef _DEBUG
		sText += _T("(Debug)");
#endif
		SetTrayTooltipText(sText);
		UpdateLineStatus(USER_STATUS_ONLINE);
	}
	else if (module::KEY_LOGIN_KICKOUT == keyId)//�˻����߳��ˣ������Լ�����
	{
		APP_LOG(LOG_INFO, _T("Offline by kickout"));
		logic::getTcpClientModule()->shutdown();
		module::getLoginModule()->setOfflineByMyself(TRUE);
		CString strText = util::getMultilingual()->getStringViaID(_T("STRID_LOGINDIALOG_TIP_KICKOUT"));
		CString strCaption = util::getMultilingual()->getStringViaID(_T("STRID_LOGINDIALOG_TIP_CAPTION"));
		::MessageBoxEx(m_hWnd, strText, strCaption, MB_OK, LANG_CHINESE);
	}
}

void MainDialog::OnMenuModuleEvent(UInt16 moduleId, UInt32 keyId, MKO_TUPLE_PARAM mkoParam)
{
	std::string& itemName = std::get<MKO_STRING>(mkoParam);
	if ("exitMenuItem" == itemName)
	{
		module::getMiscModule()->quitTheApplication();
	}
	else if ("OnlineMenuItem" == itemName)
	{
		module::getLoginModule()->setOfflineByMyself(FALSE);
		module::getLoginModule()->relogin(TRUE);
	}
	else if ("OfflineMenuItem" == itemName)
	{
		APP_LOG(LOG_INFO,_T("Offline by myself"));
		logic::getTcpClientModule()->shutdown();
		module::getLoginModule()->setOfflineByMyself(TRUE);
	}
	else if ("SysSettingMenuItem" == itemName)
	{
		//ϵͳ����
		module::getSysConfigModule()->showSysConfigDialog(m_hWnd);
	}
	else if ("showFileTransferDialog" == itemName)
	{
		//���ļ��������
		module::getFileTransferModule()->showFileTransferDialog();
	}
	else if ("serverAddressSetting" == itemName)
	{
		module::getSysConfigModule()->showServerConfigDialog(m_hWnd);
	}
}

void MainDialog::OnUserListModuleEvent(UInt16 moduleId, UInt32 keyId, MKO_TUPLE_PARAM mkoParam)
{
	if (module::KEY_USERLIST_DOWNAVATAR_SUCC == keyId)
	{
		//ˢ��ͷ��
		module::UserInfoEntity myInfo;
		module::getUserListModule()->getMyInfo(myInfo);
		m_pbtnMyFace->SetBkImage(util::stringToCString(myInfo.getAvatarPath()));
	}
}
void MainDialog::OnSessionModuleEvent(UInt16 moduleId, UInt32 keyId, MKO_TUPLE_PARAM mkoParam)
{
	if (module::TAG_SESSION_TRAY_STARTEMOT == keyId)
	{
		StartNewMsgTrayEmot();
	}
	else if (module::TAG_SESSION_TRAY_STOPEMOT == keyId)
	{
		StopNewMsgTrayEmot();
	}
}

void MainDialog::StartNewMsgTrayEmot()
{
	SetTimer(m_hWnd, TIMER_TRAYEMOT, 500, NULL);
}

void MainDialog::StopNewMsgTrayEmot()
{
	KillTimer(m_hWnd, TIMER_TRAYEMOT);
	UpdateLineStatus(module::getUserListModule()->getMyLineStatus());
}
/******************************************************************************/