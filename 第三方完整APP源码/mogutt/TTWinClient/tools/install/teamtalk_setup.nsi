;���ߣ��쵶
; NSIS �������汾�� 2.44

!define SOURCE_DIR "bin"

SetCompressor /SOLID  /FINAL lzma
SetCompressorDictSize 32

!include "Library.nsh"
!include "MUI.nsh"

/******************************
 *   ��װ�����ʼ���峣��  *
 ******************************/
;������
!define INSTALL_EXE_NAME					"teamtalk-1.0.exe"
!define MAIN_EXE_NAME      					"teamtalk.exe"
!define UPDATE_EXE_NAME						"teamtalkdownload.exe"
!define SPEEXDEC_EXE_NAME                   "speexdec.exe"
!define IOS 								"ioSpecial.ini"
!define INSTALL_BIN							"bin"

;��Ʒ��Ϣ
!define PRODUCT_NAME 						"TeamTalk"
!define INSTALL_DIR							"TeamTalk"
!define PRODUCT_DETAIL 						"TeamTalk��װ����"
!define PRODUCT_VERSION 					"1.0"
!define PRODUCT_VERSION_DETAIL				"1.0.0.1"
!define PRODUCT_PUBLISHER 					"Ģ����"
!define PRODUCT_WEB_SITE 					"http://tt.mogu.io/"

;��
!define PRODUCT_UNINST_KEY 					"Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_DIR_REGKEY 					"Software\Microsoft\Windows\CurrentVersion\App Paths\${MAIN_EXE_NAME}"

;ȫ�ֱ�������								
Var 	g_IsInstalled					;�Ƿ�װ�����ж�
Var		g_InstallPath					;��װ·��

; ------ MUI �ִ����涨�� (1.67 �汾���ϼ���) ------

; MUI Ԥ���峣��
!define MUI_ABORTWARNING
!define MUI_WELCOMEFINISHPAGE_BITMAP "res\WelcomeImage.bmp"
!define MUI_ICON "res\installer.ico"
!define MUI_UNICON "res\uninstaller.ico"

; ��ӭҳ��
!insertmacro MUI_PAGE_WELCOME
; ��װĿ¼ѡ��ҳ��
!insertmacro MUI_PAGE_DIRECTORY
; ��װ����ҳ��
!insertmacro MUI_PAGE_INSTFILES
!define MUI_FINISHPAGE_SHOWREADME "$INSTDIR\${PRODUCT_NAME}������.txt"
!define MUI_FINISHPAGE_SHOWREADME_TEXT "��ʾ������"
;!define MUI_FINISHPAGE_SHOWREADME_NOTCHECKED
!define MUI_PAGE_CUSTOMFUNCTION_PRE  FinishPagePre
!define MUI_PAGE_CUSTOMFUNCTION_SHOW  FinishPageShow
!define MUI_PAGE_CUSTOMFUNCTION_LEAVE  FinishPageLeave
!insertmacro MUI_PAGE_FINISH

; ��װж�ع���ҳ��
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

; ��װ�����������������
!insertmacro MUI_LANGUAGE "SimpChinese"
; When using waskin.dll, XPStyle must be off.
; And we specify it after '!insertmacro MUI_LANGUAGE "English"'
; so we can override Modern UI default of "XPStyle on".
XPStyle on

; ��װԤ�ͷ��ļ�
!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS
; ------ MUI �ִ����涨����� ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile ${INSTALL_EXE_NAME}
InstallDir "$PROGRAMFILES\${INSTALL_DIR}"
ShowInstDetails show
ShowUnInstDetails show
BrandingText ${PRODUCT_NAME}
LicenseText "" "��ͬ��(I)"
UninstallButtonText "�����װ(U)"

VIProductVersion ${PRODUCT_VERSION_DETAIL}
VIAddVersionKey  "ProductName" "${PRODUCT_NAME}"
VIAddVersionKey "CompanyName" ${PRODUCT_PUBLISHER}
VIAddVersionKey "FileDescription" "${PRODUCT_NAME}"
VIAddVersionKey "ProductVersion" ${PRODUCT_VERSION_DETAIL}
VIAddVersionKey "FileVersion" ${PRODUCT_VERSION_DETAIL}
VIAddVersionKey "Comments" "����ϸ�Ķ����Э��"
VIAddVersionKey "LegalCopyright" ${PRODUCT_PUBLISHER}

/******************************
*  ��װ����İ�װsection����  *
******************************/
Section "MainSection" SEC01
	SetOutPath "$INSTDIR"
	SetOverwrite on
  /*�жϲ���ϵͳ�汾�Ƿ���Vista���ǵĻ����䰲װĿ¼����everyoneȨ��*/
	GetDllVersion "$SYSDIR\user32.dll" $R0 $R1
	strcpy $1 $R0
	strcmp $1 "" Skip
	IntOp $R2 $R0 / 0x00010000
	IntOp $R3 $R0 & 0x0000FFFF
	StrCpy $0 "$R2$R3"
	IntCmp $0 60 cacl Skip cacl
cacl:
	ExecShell "open" "cacls" '"$INSTDIR" /e /t /g everyone:F' "SW_HIDE" 
	/*�жϲ���ϵͳ�汾�Ƿ���Vista���ǵĻ����䰲װĿ¼����everyoneȨ��*/
Skip:
	Delete "$INSTDIR\bin\CrashDumper.exe"
	File /r "${SOURCE_DIR}\*.*"
	/*ǿ��ɾ�����ݿ����*/
    ;RMDir /r "$INSTDIR\users\download\"
    /*ǿ��ɾ��config.dat�汾����*/
    ;Delete "$INSTDIR\bin\config.dat"
	Delete "$INSTDIR\data\duoduo_water.wav"
    ;ɾ��JS src�ļ�
    RMDir /r "$INSTDIR\data\module\js\src"
    RMDir /r "$INSTDIR\gui\shading"
    RMDir /r "$INSTDIR\gui\preview"
    Delete "$INSTDIR\gui\skin_base.xml"
    Delete "$INSTDIR\bin\debugConfig.xml"
    
    ExecWait '"regsvr32.exe /s"  "$INSTDIR\${INSTALL_BIN}\GifSmiley.dll"'    
SectionEnd

Section -AdditionalIcons
	;ɾ��ԭ�ȵĿ�ݷ�ʽ
  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$DESKTOP\${PRODUCT_NAME}.lnk"
  Delete "$SENDTO\${PRODUCT_NAME}.lnk"
  Delete "$QUICKLAUNCH\${PRODUCT_NAME}.lnk"	
  RMDir /r "$SMPROGRAMS\${PRODUCT_NAME}"
  SetShellVarContext all
  Delete "$DESKTOP\${PRODUCT_NAME}.lnk"
  Delete "$SENDTO\${PRODUCT_NAME}.lnk"
  Delete "$QUICKLAUNCH\${PRODUCT_NAME}.lnk"	
  RMDir /r "$SMPROGRAMS\${PRODUCT_NAME}"
  ;��ӿ�ݷ�ʽ��all user Ŀ¼��
  SetShellVarContext all
  CreateShortCut "$DESKTOP\${PRODUCT_NAME}.lnk" "$INSTDIR\bin\${MAIN_EXE_NAME}"
  CreateShortCut "$SENDTO\${PRODUCT_NAME}.lnk" "$INSTDIR\bin\${MAIN_EXE_NAME}" "/sendto"
  CreateDirectory "$SMPROGRAMS\${PRODUCT_NAME}"
  CreateShortCut "$SMPROGRAMS\${PRODUCT_NAME}\${PRODUCT_NAME}.lnk" "$INSTDIR\bin\${MAIN_EXE_NAME}"
  WriteIniStr "$INSTDIR\${PRODUCT_NAME}.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
  CreateShortCut "$SMPROGRAMS\${PRODUCT_NAME}\������վ.lnk" "$INSTDIR\${PRODUCT_NAME}.url" "" "$SYSDIR\url.dll"
  CreateShortCut "$SMPROGRAMS\${PRODUCT_NAME}\ж��${PRODUCT_NAME}.lnk" "$INSTDIR\ж��${PRODUCT_NAME}.exe"
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\ж��${PRODUCT_NAME}.exe"

  WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "Path" "$INSTDIR"
  WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "Mark" "Installing"
    
;ж������
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\ж��${PRODUCT_NAME}.exe"
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\bin\${MAIN_EXE_NAME}"
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"

SectionEnd

/******************************
*  ��װ�����ж��section����  *
******************************/
Section Uninstall
Check:
	KillProcDLL::KillProc "${MAIN_EXE_NAME}"
	KillProcDLL::KillProc "${UPDATE_EXE_NAME}"
    KillProcDLL::KillProc "${SPEEXDEC_EXE_NAME}"
	sleep 200
	FindProcDll::FindProc "${MAIN_EXE_NAME}"
	IntCmp $R0 1 Check 0
	FindProcDll::FindProc "${UPDATE_EXE_NAME}"
	IntCmp $R0 1 Check 0
    FindProcDll::FindProc "${SPEEXDEC_EXE_NAME}"
	IntCmp $R0 1 Check GoOn
GoOn:


;��ݷ�ʽ,��ʼ�˵�
	Delete "$INSTDIR\${PRODUCT_NAME}.url"
	Delete "$DESKTOP\${PRODUCT_NAME}.lnk"
	Delete "$SENDTO\${PRODUCT_NAME}.lnk"  
	Delete "$QUICKLAUNCH\${PRODUCT_NAME}.lnk"	
	RMDir /r "$SMPROGRAMS\${PRODUCT_NAME}"
	SetShellVarContext all
	Delete "$DESKTOP\${PRODUCT_NAME}.lnk"
	Delete "$SENDTO\${PRODUCT_NAME}.lnk"
	Delete "$QUICKLAUNCH\${PRODUCT_NAME}.lnk"	
	RMDir /r "$SMPROGRAMS\${PRODUCT_NAME}"
		
;�ļ��б�
	Delete "$INSTDIR\${PRODUCT_NAME}"
	Delete "$INSTDIR\ж��${PRODUCT_NAME}.exe"
	Delete "$INSTDIR\${PRODUCT_NAME}������.txt"
	Delete "$INSTDIR\stringtable.ini"
	
;�ļ����б�
	RMDir /r "$INSTDIR\gui"
	RMDir /r "$INSTDIR\bin"
	RMDir /r "$INSTDIR\data"
	RMDir /r "$INSTDIR\users"
	RMDir  "$INSTDIR"
	
	DeleteRegKey 	HKLM "${PRODUCT_UNINST_KEY}"
	WriteRegStr 	HKLM "${PRODUCT_DIR_REGKEY}" "Mark" "UnInstalling"
	DeleteRegValue  HKLM "${PRODUCT_DIR_REGKEY}" "SilentMark"
		
	SetAutoClose true
SectionEnd

#-- ���� NSIS �ű��༭�������� Function ���α�������� Section ����֮���д���Ա��ⰲװ�������δ��Ԥ֪�����⡣--#

/******************************
 *  ��װ����İ�װ��������  		*
 ******************************/
 Function .onInit
	InitPluginsDir
		
;�����س����Ƿ�������
	Call CheckRunning
  
	ReadRegStr $0 HKLM "${PRODUCT_DIR_REGKEY}" "Path"  	;ʹ���������Ѱ�Ҿɰ汾��װ·��
	ReadRegStr $1 HKLM "${PRODUCT_DIR_REGKEY}" "Mark"  	;ʹ�����������ʾ�ɰ汾״̬:installing or uninstall
	Strcmp $1 "Installing" installed	end				 ;��ʱ��$1Ӧ����2��״̬ uninstall ������ ���ַ���
installed:
	StrCpy  $g_IsInstalled		"Installed"
	StrCpy  $g_InstallPath 		$0
	StrCpy $INSTDIR $g_InstallPath
	
end: 
FunctionEnd

Function  CheckRunning 
Check:
	FindProcDll::FindProc "${MAIN_EXE_NAME}"
	IntCmp $R0 1 Running 0
	FindProcDll::FindProc "${UPDATE_EXE_NAME}"
	IntCmp $R0 1 Running 0
	FindProcDll::FindProc "${SPEEXDEC_EXE_NAME}"
	IntCmp $R0 1 Running End    
Running:
	;�����ǿ����������������ʾֱ��ɱ������
	IfSilent kill prompt
prompt:
	MessageBox MB_ICONINFORMATION|MB_YESNO "ϵͳ��⵽��$(^Name)����س����������У����$\r���ǡ���ǿ�ƹرղ�������װ,������� ��ֱ���˳���װ����" IDYES +2
	Quit
kill:
	KillProcDLL::KillProc "${MAIN_EXE_NAME}"
	KillProcDLL::KillProc "${UPDATE_EXE_NAME}"
    KillProcDLL::KillProc "${SPEEXDEC_EXE_NAME}"
	sleep 200
	Goto Check
End:
FunctionEnd

Function  FinishPagePre
	WriteINIStr "$PLUGINSDIR\${IOS}" "Settings" "NumFields" "6" 
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 5" "Type" "CheckBox" 
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 5" "State" "1"
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 5" "Text" "����$(^Name)"
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 5" "Left" "120"
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 5" "Right" "315" 
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 5" "Top" "108"
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 5" "Bottom" "119" 

	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 6" "Type" "CheckBox" 
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 6" "State" "1"
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 6" "Text" "������������"
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 6" "Left" "120"
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 6" "Right" "315" 
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 6" "Top" "129"
	WriteINIStr "$PLUGINSDIR\${IOS}" "Field 6" "Bottom" "140" 
FunctionEnd

;����Ƥ������Ҫ���øÿؼ��ı���ɫ
Function  FinishPageShow 
    ReadINIStr $0 "$PLUGINSDIR\${IOS}" "Field 5" "HWND"
    SetCtlColors $0 0x000000 0xFFFFFF 
    ReadINIStr $0 "$PLUGINSDIR\${IOS}" "Field 6" "HWND"
    SetCtlColors $0 0x000000 0xFFFFFF 
FunctionEnd

Function  FinishPageLeave 
	ReadINIStr $0 "$PLUGINSDIR\${IOS}" "Field 6" "State" 
    StrCmp $0 "1" +1 +2
    CreateShortCut "$QUICKLAUNCH\${PRODUCT_NAME}.lnk" "$INSTDIR\bin\${MAIN_EXE_NAME}"
	
	ReadINIStr $0 "$PLUGINSDIR\${IOS}" "Field 5" "State" 
	StrCmp $0 "1" +1 end
	Exec '"$INSTDIR\bin\${MAIN_EXE_NAME}"'
end:
FunctionEnd

/******************************
*  ��װ�����ж�غ�������  	  *
******************************/
Function un.onInit
	Call un.CheckRunning
FunctionEnd

;��װʱ��;�����س����Ƿ�������
Function  un.CheckRunning 
Check:
	FindProcDll::FindProc "${MAIN_EXE_NAME}"
	IntCmp $R0 1 Running 0
	FindProcDll::FindProc "${UPDATE_EXE_NAME}"
	IntCmp $R0 1 Running 0
	FindProcDll::FindProc "${SPEEXDEC_EXE_NAME}"
	IntCmp $R0 1 Running End        
Running:
	MessageBox MB_ICONINFORMATION|MB_YESNO "ϵͳ��⵽��$(^Name)����س����������У����$\r���ǡ���ǿ�ƹرղ�������װ,������� ��ֱ���˳���װ����" IDYES +2
	Quit
	KillProcDLL::KillProc "${MAIN_EXE_NAME}"
	KillProcDLL::KillProc "${UPDATE_EXE_NAME}"
    KillProcDLL::KillProc "${SPEEXDEC_EXE_NAME}"
	sleep 200
	Goto Check
End:
FunctionEnd

Function un.onUninstSuccess
	MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) �ѳɹ��ش���ļ�����Ƴ���"
FunctionEnd
