/******************************************************************************* 
 *  @file      Multilingual.cpp 2014\7\16 14:13:45 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief   
 ******************************************************************************/

#include "stdafx.h"
#include "utility/Multilingual.h"

/******************************************************************************/
NAMESPACE_BEGIN(util)

// -----------------------------------------------------------------------------
//  Multilingual: Public, Constructor

Multilingual::Multilingual()
{

}

// -----------------------------------------------------------------------------
//  Multilingual: Public, Destructor

Multilingual::~Multilingual()
{
	
}

CString Multilingual::getStringViaID(LPCTSTR strID)
{
	CString	strText;

	if (strID != NULL)
		m_mapKey2Value.Lookup(strID, strText);

	return strText;
}

BOOL Multilingual::loadStringTable(LPCTSTR strFilePath)
{
	BOOL ret = FALSE;
	FILE* fp = NULL;
	CString	strVal;
	CStringList	strStringList;
	TCHAR buffer[4096];

	if (NULL == (fp = _tfopen(strFilePath, _T("rt,ccs=UTF-8"))))
	{
		goto leave;
	}

	while (_fgetts(buffer, sizeof(buffer) / sizeof(buffer[0]), fp))
	{
		strVal = buffer;
		strVal.Trim();
		if (!strVal.IsEmpty())
			strStringList.AddTail(strVal);
	}
	fclose(fp);

	_analyzeStringTable(strStringList);

	ret = TRUE;

leave:
	return ret;
}

BOOL Multilingual::_analyzeStringTable(CStringList& list)
{
	int nStartPos = 0;
	int nComment;
	POSITION pos;

	for (pos = list.GetHeadPosition(); pos;)
	{
		CString& csLine = list.GetNext(pos);

		csLine.Trim();
		nComment = csLine.Find(_T(';'));
		if (nComment != -1)
			csLine = csLine.Left(nComment);

		nStartPos = 0;

		/* ȡKey */
		CString& csKey = csLine.Tokenize(_T("="), nStartPos);
		csKey.Trim();
		if (!csKey.IsEmpty())
		{
			/* ȡValue */
			CString& csValue = csLine.Tokenize(_T("="), nStartPos);
			csValue.Trim();
			if (!csValue.IsEmpty())
			{
				LPTSTR pch1, pch2, pch3;
				pch1 = (LPTSTR)(LPCTSTR)csValue;
				pch3 = pch1 + csValue.GetLength();

				/* ���Ը��Ӷ�ɨ�裬ת�廻�С��س� */
				for (pch2 = pch1; pch1 < pch3; pch1++)
				{
					if (*pch1 == _T('\\'))
					{
						if (*(pch1 + 1) == _T('r'))
						{
							pch1++;
							*(pch2++) = _T('\r');
						}
						else if (*(pch1 + 1) == _T('n'))
						{
							pch1++;
							*(pch2++) = _T('\n');
						}
						else if (*(pch1 + 1) == _T('t'))
						{
							pch1++;
							*(pch2++) = _T('\t');
						}
						else
						{
							*(pch2++) = _T('\\');
							*(pch2++) = *(++pch1);
						}
					}
					else
						*(pch2++) = *pch1;
				}

				*pch2 = 0;

				/* ����ǼǸö�Ԫ�� */
				m_mapKey2Value.SetAt(csKey, csValue);
			}
		}
	}
	return TRUE;
}

Multilingual* getMultilingual()
{
	static Multilingual multi;
	return &multi;
}

NAMESPACE_END(util)
/******************************************************************************/