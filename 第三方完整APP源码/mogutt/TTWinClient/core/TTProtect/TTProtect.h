#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"		// ������


// CTTProtectApp: 
// �йش����ʵ�֣������ TTProtect.cpp
//

class CTTProtectApp : public CWinApp
{
public:
	CTTProtectApp();

// ��д
public:
	virtual BOOL InitInstance();
	/**
	 * ��ʵ������
	 *
	 * @return  BOOL
	 * @exception there is no any exception to throw.
	 */	
	BOOL IsHaveInstance();

	DECLARE_MESSAGE_MAP()
};

extern CTTProtectApp theApp;