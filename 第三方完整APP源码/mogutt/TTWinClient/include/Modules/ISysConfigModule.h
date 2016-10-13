/*******************************************************************************
 *  @file      ISysConfigModule.h 2014\8\4 10:54:39 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     ϵͳ������Ϣ������ϵͳ���ú�ȫ��������Ϣ
 ******************************************************************************/

#ifndef ISYSCONFIGMODULE_03995006_A398_4574_BAE1_549FE4543DD3_H__
#define ISYSCONFIGMODULE_03995006_A398_4574_BAE1_549FE4543DD3_H__

#include "GlobalDefine.h"
#include "TTLogic/IModule.h"
#include "Modules/ModuleDll.h"
/******************************************************************************/
NAMESPACE_BEGIN(module)
const UInt16 SYSCONFIG_VERSIONI = 1;

//BaseFlag
enum
{
	BASE_FLAG_TOPMOST = 1 << 0,						//�Ƿ�������������ǰ��
	BASE_FLAG_NOTIPWHENNEWMSG = 1 << 1,				//��������ϢʱƮ����ʾ
	BASE_FLAG_NOSOUNDWHENMSG = 1 << 2,				//�Ƿ�ر���Ϣ������ʾ
	BASE_FLAG_SENDIMG_BY_CTRLENTER = 1 << 3,        //Ctrl + Enter��������Ϣ����֮Enter
};

//MusicID
enum
{
	MUSIC_SOUND_DINGDONG =  0,		//������
	MUSIC_SOUND_KEYBOARD,      //��������
	MUSIC_SOUND_DINGLIN ,       //������
	MUSIC_SOUND_CALLMSG ,        //��������
};

struct TTConfigNeedCache
{
	UInt16			version = 0;
	BOOL			isRememberPWD = FALSE;
	CString			userName;
	std::string		password;
	std::string		phpServerAddr;
	Int32			sysBaseFlag = 0;			//��������
	Int32			sysSoundTypeBaseFlag = 0;	//��������
};
enum
{
	KEY_SYSCONFIG_UPDATED = MODULE_ID_SYSCONFIG << 16 | 1,      //�������ø���
	KEY_SYSCONFIG_SHOW_USERDETAILDIALOG = MODULE_ID_SYSCONFIG << 16 | 2,      //չʾ�û���ϸ��Ϣ����

};

struct TTConfig : public TTConfigNeedCache
{
	UInt8			myselectStatus;
	UInt32			loginServPort = 0;
	CString			csUserId;
	CString			token;
	CString			fileSysAddr;
	CString			loginServIP;

	std::string		userId;
};

/**
 * The class <code>ϵͳ������Ϣ������ϵͳ���ú�ȫ��������Ϣ</code> 
 *
 */
class MODULE_API ISysConfigModule : public logic::IModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	ISysConfigModule()
	{
		m_moduleId = MODULE_ID_SYSCONFIG;
	}
    //@}

public:
	/**
	 * ��ȡϵͳ�������ݣ��ı���������֮�������Ҫ�������������saveData()��
	 *
	 * @param   SystemConfig & cfg
	 * @return  void
	 * @exception there is no any exception to throw.
	 */	
	virtual TTConfig* getSystemConfig() = 0;
	/**
	* ��ȡ�û�ID
	*
	* @return  std::string
	* @exception there is no any exception to throw.
	*/
	virtual std::string userID()const = 0;
	virtual CString UserID()const = 0;
	/**
	 * ���������ݱ��浽����
	 *
	 * @return  BOOL
	 * @exception there is no any exception to throw.
	 */
	virtual BOOL saveData() = 0;
	virtual BOOL showServerConfigDialog(HWND hParentWnd) = 0;
	/**
	 * ��ʾϵͳ���ô���
	 *
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	virtual void showSysConfigDialog(HWND hParentWnd) = 0;
	/**
	* �ͷŴ��ڵ�ʱ����false��������ʱ����true
	*
	* @return  void
	* @exception there is no any exception to throw.
	*/
	virtual void SetSysConfigDialogFlag(BOOL bIsExist) = 0;
};

MODULE_API ISysConfigModule* getSysConfigModule();

NAMESPACE_END(module)
/******************************************************************************/
#endif// ISYSCONFIGMODULE_03995006_A398_4574_BAE1_549FE4543DD3_H__
