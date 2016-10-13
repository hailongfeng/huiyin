#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"

class MainDialog;

class CteamtalkApp : public CWinApp
{
public:
	CteamtalkApp();

public:
	virtual BOOL InitInstance();
	virtual BOOL ExitInstance();
	DECLARE_MESSAGE_MAP()

private:
	/**
	 *  �����û�Ŀ¼
	 *
	 * @return  BOOL
	 * @exception there is no any exception to throw.
	 */	
	BOOL _CreateUsersFolder();
	/**
	 * ����������
	 *
	 * @return  BOOL
	 * @exception there is no any exception to throw.
	 */	
	BOOL _CreateMainDialog();
	/**
	* ����������
	*
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	BOOL _DestroyMainDialog();
	/**
	* �ж��Ƿ��ǵ�ʵ��
	*
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	BOOL IsHaveInstance();
private:
	MainDialog*						m_pMainDialog;
};

extern CteamtalkApp theApp;