/*******************************************************************************
 *  @file      DumpReporter.h 2014\9\1 14:17:33 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     dump�ռ�
 ******************************************************************************/

#ifndef DUMPREPORTER_498E76ED_F037_4BFD_ADD4_6DEC1A318AE0_H__
#define DUMPREPORTER_498E76ED_F037_4BFD_ADD4_6DEC1A318AE0_H__

/******************************************************************************/

typedef void (CALLBACK *PRE_EXCEPTION_PROC)(void);

class CDumpReporter
{
public:
	class CReportDetail
	{
	private:
		DWORD		m_dwPid;				// ����ID
		DWORD		m_dwTid;				// �߳�ID
		DWORD		m_dwExceptionAddr;		// �쳣�ṹ��ַ
		CString		m_sParameter;			// �Զ������
		CString		m_sAppPath;				// ������·��������dumper.exe����֮��
	public:
		void SetProcessId(DWORD dwPid);
		void SetThreadId(DWORD dwTid);
		void SetExceptionAddr(PEXCEPTION_POINTERS pException);
		void SetParameter(CString sParam);
		void SetAppPath(CString sPath);
		CString ToCommandLine();
	};

	class CInstaller
	{
	public:
		void SetParameter(CString sParam);
		CInstaller(PRE_EXCEPTION_PROC preExceptionProc = NULL, LPCTSTR ProgramPath = NULL);
		~CInstaller();
	};

	static BOOL Initialize(PRE_EXCEPTION_PROC preExceptionProc, LPCTSTR ProgramPath = NULL);
	static DWORD Report(CReportDetail& Details, DWORD Timeout);
	static void InstallExceptionFilter();
	static void UninstallExceptionFilter();
	static LONG WINAPI TopLevelExceptionFilter(EXCEPTION_POINTERS* pExceptionInfo);
	CDumpReporter(void);
	~CDumpReporter(void);

	static CReportDetail m_ExceptionReportDetails;

private:
	enum { REPORT_RESULT_NOTFOUND = 1023, REPORT_RESULT_DEBUG = 1024 };
	static TCHAR m_ProgramPath[MAX_PATH];
	static BOOL m_bExceptionFilterInstalled;
	static LPTOP_LEVEL_EXCEPTION_FILTER m_OldFilter;
	static PRE_EXCEPTION_PROC m_preExceptionProc;
};

/******************************************************************************/
#endif// DUMPREPORTER_498E76ED_F037_4BFD_ADD4_6DEC1A318AE0_H__