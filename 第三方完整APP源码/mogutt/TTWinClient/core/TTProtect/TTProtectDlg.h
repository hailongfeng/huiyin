#pragma once

#include "Parameters.h"
#include "CrashManager.h"
#include "afxwin.h"
#include "iniConfig.h"
#include "sendRpt.h"

#define		WM_START_CRASHDLG		WM_USER + 1000

class CTTProtectDlg : public CDialogEx
{
// ����
public:
	CTTProtectDlg(CWnd* pParent = NULL);	// ��׼���캯��

// �Ի�������
	enum { IDD = IDD_TTPROTECT_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV ֧��

// ʵ��
protected:
	HICON m_hIcon;
	CIniConfig m_iniConfig;
	CCrashManager m_crashMgr;

	// ���ɵ���Ϣӳ�亯��
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg HRESULT OnSendRptFinish(WPARAM wparam, LPARAM lparam);
	afx_msg void OnClose();
	afx_msg LRESULT OnStartCrash(WPARAM wparm, LPARAM lparam);
	afx_msg void OnBnClickedOk();
	afx_msg HBRUSH OnCtlColor(CDC* pDC, CWnd* pWnd, UINT nCtlColor);
	DECLARE_MESSAGE_MAP()

private:
	void Destory();
	void InitUI();
	BOOL HttpQueryCanUpload();

private:
	CButton			m_ctrRestartAppCheck;
	CButton			m_ctrReportCheck;

	CSendRpt		*m_pSendRpt;
	BOOL			m_bSendFinish;
	BOOL			m_bClickOK;
};
