// TTLogic.h : TTLogic DLL ����ͷ�ļ�
//

#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"		// ������


// CTTLogicApp
// �йش���ʵ�ֵ���Ϣ������� TTLogic.cpp
//

class CTTLogicApp : public CWinApp
{
public:
	CTTLogicApp();

// ��д
public:
	virtual BOOL InitInstance();

	DECLARE_MESSAGE_MAP()
};
