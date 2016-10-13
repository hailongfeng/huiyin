/******************************************************************************* 
 *  @file      MainDialog.cpp 2014\7\31 15:24:26 $
 *  @author    ���<dafo@mogujie.com>
 *  @brief     
 ******************************************************************************/

#include "stdafx.h"
#include "Resource.h"
#include "MainDialog.h"
#include "Modules/ISessionModule.h"
#include "Modules/ILoginModule.h"
#include "Modules/ISysConfigModule.h"
#include "Modules/IMiscModule.h"
#include "Modules/IUserListModule.h"
#include "utility/Multilingual.h"
#include "src/base/ImPduClient.h"

DUI_BEGIN_MESSAGE_MAP(MainDialog, WindowImplBase)
	DUI_ON_MSGTYPE(DUI_MSGTYPE_WINDOWINIT, OnWindowInitialized)
	DUI_ON_MSGTYPE(DUI_MSGTYPE_CLICK,OnClick)
	DUI_ON_MSGTYPE(DUI_MSGTYPE_TEXTCHANGED,OnTextChanged)
DUI_END_MESSAGE_MAP()

/******************************************************************************/

// -----------------------------------------------------------------------------
//  MainDialog: Public, Constructor

MainDialog::MainDialog()
:m_pbtnSysConfig(nullptr)
,m_pbtnOnlineStatus(nullptr)
,m_pbtnMyFace(nullptr)
,m_ptxtUname(nullptr)
,m_bInstalled(false)
,m_bHidden(false)
, m_pbtnClose(nullptr)
, m_pbtnMinMize(nullptr)
{

}

// -----------------------------------------------------------------------------
//  MainDialog: Public, Destructor

MainDialog::~MainDialog()
{
	RemoveIcon();
}

LPCTSTR MainDialog::GetWindowClassName() const
{
	return _T("TeamTalkMainDialog");
}

DuiLib::CDuiString MainDialog::GetSkinFile()
{
	return  _T("MainDialog\\MainDialog.xml");
}

DuiLib::CDuiString MainDialog::GetSkinFolder()
{
	return _T("");
}

CControlUI* MainDialog::CreateControl(LPCTSTR pstrClass)
{
	return module::getSessionModule()->createMainDialogControl(pstrClass, m_PaintManager);
}
void MainDialog::OnFinalMessage(HWND hWnd)
{
	WindowImplBase::OnFinalMessage(hWnd);

	//�˳�Ӧ�ó���
	module::getMiscModule()->quitTheApplication();
}


LRESULT MainDialog::HandleMessage(UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	//����˫�����������
	if (WM_NCLBUTTONDBLCLK == uMsg)
	{
		return 0;
	}
	else if (WM_TRAYICON_NOTIFY == uMsg)
	{
		OnTrayIconNotify(wParam, lParam);
		return 0;
	}
	else if (WM_TIMER == uMsg)
	{
		if (wParam == TIMER_TRAYEMOT)
		{
			static BOOL bTrans = FALSE;
			if (bTrans)
			{
				bTrans = FALSE;
				SetTrayIconIndex(ICON_TRAY_LOGO);
			}
			else
			{
				bTrans = TRUE;
				SetTrayIconIndex(-1);
			}
		}
	}
	else if (WM_ENDSESSION == uMsg)
	{
		BOOL bEnding = wParam;
		if (!bEnding)
			return 0;
		module::getMiscModule()->quitTheApplication();
		APP_LOG(LOG_INFO, _T("MainDialog: WM_ENDSESSION System End Session OK"));
	}
	else if (WM_COPYDATA == uMsg)
	{
		COPYDATASTRUCT *pCopyData = (COPYDATASTRUCT*)lParam;
		OnCopyData(pCopyData);
	}

	return WindowImplBase::HandleMessage(uMsg, wParam, lParam);
}

void MainDialog::OnWindowInitialized(TNotifyUI& msg)
{
	m_pbtnSysConfig = (CButtonUI*)m_PaintManager.FindControl(_T("sysconfig"));
	m_pbtnOnlineStatus = (CButtonUI*)m_PaintManager.FindControl(_T("onlineStatus"));
	m_pbtnMyFace = (CButtonUI*)m_PaintManager.FindControl(_T("myfacebtn"));
	m_ptxtUname = (CTextUI*)m_PaintManager.FindControl(_T("unametxt"));
	m_pbtnClose = (CButtonUI*)m_PaintManager.FindControl(_T("closebtn"));
	m_pbtnMinMize = (CButtonUI*)m_PaintManager.FindControl(_T("minbtn"));
	//����ͼ��
	LoadIcons();
	//����ϵͳ����
	CreateTrayIcon();
	//��ʼ��
	Initilize();
}

void MainDialog::OnTextChanged(TNotifyUI& msg)
{
	if (msg.pSender->GetName() == _T("editSearch"))
	{
		CEditUI* pCEditUI = static_cast<CEditUI*>(m_PaintManager.FindControl(_T("editSearch")));
		CControlUI* pMainListLayout = m_PaintManager.FindControl(_T("MainListLayout"));
		if (pMainListLayout && pCEditUI)
		{
			if (!pCEditUI->GetText().IsEmpty())
			{
				pMainListLayout->SetVisible(false);
			}
			else
			{
				pMainListLayout->SetVisible(true);
			}
		}
	}
}

void MainDialog::CreateTrayIcon()
{
	CString sText = util::getMultilingual()->getStringViaID(_T("STRID_GLOBAL_CAPTION_NAME"));
#ifdef _DEBUG
	sText += _T("(Debug)");
#endif

	InstallIcon(sText, m_hIcons[ICON_TRAY_LOGO], IDR_MAINFRAME, FALSE);
}

void MainDialog::LoadIcons()
{
	CString csPath = module::getMiscModule()->getDataDir() + _T("icons\\newMsg.ico");
	HICON hIcon = 0;
	if (util::isFileExist(csPath))
	{
		hIcon = (HICON)::LoadImage(NULL, csPath, IMAGE_ICON, 0, 0, LR_LOADFROMFILE);
		if (hIcon)
			m_hIcons[ICON_TRAY_NEWMSG] = hIcon;
		else
			APP_LOG(LOG_ERROR, TRUE, _T("LoadIcons failed ,icon %s"), csPath);
	}

	csPath = module::getMiscModule()->getDataDir() + _T("icons\\logo.ico");
	hIcon = 0;
	if (util::isFileExist(csPath))
	{
		hIcon = (HICON)::LoadImage(NULL, csPath, IMAGE_ICON, 0, 0, LR_LOADFROMFILE);
		if (hIcon)
			m_hIcons[ICON_TRAY_LOGO] = hIcon;
		else
			APP_LOG(LOG_ERROR, TRUE, _T("LoadIcons failed ,icon %s"), csPath);
	}

	csPath = module::getMiscModule()->getDataDir() + _T("icons\\leave.ico");
	hIcon = 0;
	if (util::isFileExist(csPath))
	{
		hIcon = (HICON)::LoadImage(NULL, csPath, IMAGE_ICON, 0, 0, LR_LOADFROMFILE);
		if (hIcon)
			m_hIcons[ICON_TRAY_LEAVE] = hIcon;
		else
			APP_LOG(LOG_ERROR, TRUE, _T("LoadIcons failed ,icon %s"), csPath);
	}
	csPath = module::getMiscModule()->getDataDir() + _T("icons\\offline.ico");
	hIcon = 0;
	if (util::isFileExist(csPath))
	{
		hIcon = (HICON)::LoadImage(NULL, csPath, IMAGE_ICON, 0, 0, LR_LOADFROMFILE);
		if (hIcon)
			m_hIcons[ICON_TRAY_OFFLINE] = hIcon;
		else
			APP_LOG(LOG_ERROR, TRUE, _T("LoadIcons failed ,icon %s"), csPath);
	}
}

void MainDialog::UpdateLineStatus(UInt8 status)
{
	module::UserInfoEntity myInfo;
	module::getUserListModule()->getMyInfo(myInfo);
	m_pbtnMyFace->SetBkImage(util::stringToCString(myInfo.getAvatarPath()));

	if (USER_STATUS_ONLINE == status)
	{
		m_pbtnOnlineStatus->SetBkImage(_T("MainDialog/online.png"));
		SetTrayIconIndex(ICON_TRAY_LOGO);
	}
	else
	{
		m_pbtnOnlineStatus->SetBkImage(_T("MainDialog/offline.png"));
		SetTrayIconIndex(ICON_TRAY_OFFLINE);
	}
}

void MainDialog::SetTrayIconIndex(int nIndex)
{
	HICON hIcon = NULL;
	if (nIndex < ICON_TRAY_LOGO || nIndex > ICON_COUNT)
		SetTrayIcon(0);
	else
		SetTrayIcon(m_hIcons[nIndex]);
}

void MainDialog::Initilize()
{
	//MKO
	logic::GetLogic()->addObserver(this, MODULE_ID_LOGIN
		, fastdelegate::MakeDelegate(this, &MainDialog::OnLoginModuleEvent));
	logic::GetLogic()->addObserver(this, MODULE_ID_TCPCLIENT
		, fastdelegate::MakeDelegate(this, &MainDialog::OnTcpClientModuleEvent));
	logic::GetLogic()->addObserver(this, MODULE_ID_MENU
		, fastdelegate::MakeDelegate(this, &MainDialog::OnMenuModuleEvent));
	logic::GetLogic()->addObserver(this, MODULE_ID_USERLIST
		, fastdelegate::MakeDelegate(this, &MainDialog::OnUserListModuleEvent));
	logic::GetLogic()->addObserver(this, MODULE_ID_SEESION
		, fastdelegate::MakeDelegate(this, &MainDialog::OnSessionModuleEvent));


	module::UserInfoEntity myInfo;
	module::getUserListModule()->getMyInfo(myInfo);
	m_pbtnMyFace->SetBkImage(util::stringToCString(myInfo.getAvatarPath()));
	m_ptxtUname->SetText(myInfo.getRealName());

	CString csPath = util::getAppPath() + _T("\\GifSmiley.dll");
	util::registerDll(csPath);
}

void MainDialog::InitWindow()
{
	MONITORINFO oMonitor = {};
	oMonitor.cbSize = sizeof(oMonitor);
	::GetMonitorInfo(::MonitorFromWindow(*this, MONITOR_DEFAULTTOPRIMARY), &oMonitor);
	CDuiRect rcWork = oMonitor.rcWork;

	CDuiRect rcWnd;
	GetWindowRect(m_hWnd, &rcWnd);
	int nWidth = rcWnd.GetWidth();
	int nHeight = rcWnd.GetHeight();
	SetWindowPos(m_hWnd, HWND_TOPMOST, rcWork.GetWidth() - nWidth - 100, 100, 280, rcWork.GetHeight()-200, SWP_SHOWWINDOW);

}

LRESULT MainDialog::HandleCustomMessage(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	if (uMsg == WM_START_MOGUTALKINSTANCE)
	{
		BringToTop();
	}
	return 0;
}

LRESULT MainDialog::ResponseDefaultKeyEvent(WPARAM wParam)
{
	if (wParam == VK_ESCAPE)
	{
		return TRUE;
	}
	return __super::ResponseDefaultKeyEvent(wParam);
}



/******************************************************************************/