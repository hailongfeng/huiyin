/*******************************************************************************
 *  @file      ISessionModule.h 2014\7\27 10:06:08 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief   
 ******************************************************************************/

#ifndef ISESSIONMODULE_070C0321_0708_4487_8028_C1D8934B709D_H__
#define ISESSIONMODULE_070C0321_0708_4487_8028_C1D8934B709D_H__

#include "GlobalDefine.h"
#include "TTLogic/IModule.h"
#include "Modules/ModuleDll.h"
/******************************************************************************/
namespace DuiLib
{
	class CControlUI;
	class CPaintManagerUI;
}
NAMESPACE_BEGIN(module)

enum
{
	KEY_SESSION_NEWMESSAGE				= MODULE_ID_SEESION << 16 | 1,      //���յ���Ϣ����������ʱ��Ϣ��������Ϣ
	KEY_SESSION_OPENNEWSESSION			= MODULE_ID_SEESION << 16 | 2,      //֪ͨ��һ���µĻỰ
	KEY_SESSION_SENDMSG_TOOFAST			= MODULE_ID_SEESION << 16 | 3,      //������Ϣ̫��
	KEY_SESSION_SENDMSG_FAILED			= MODULE_ID_SEESION << 16 | 4,      //������Ϣʧ��
	KEY_SESSION_SHAKEWINDOW_MSG			= MODULE_ID_SEESION << 16 | 5,		//����������
	KEY_SESSION_WRITING_MSG				= MODULE_ID_SEESION << 16 | 6,		//��������
	KEY_SESSION_STOPWRITING_MSG			= MODULE_ID_SEESION << 16 | 7,		//ֹͣ����������
	KEY_SESSION_SENDMSGTIP_KEY			= MODULE_ID_SEESION << 16 | 8,		//������Ϣ��ֵ�ı�

	TAG_SESSION_TRAY_STARTEMOT			= MODULE_ID_SEESION << 16 | 9,		//��������ͼ����˸
	TAG_SESSION_TRAY_STOPEMOT			= MODULE_ID_SEESION << 16 | 10,		//�ر�����ͼ����˸
	TAG_SESSION_TRAY_NEWMSGSEND			= MODULE_ID_SEESION << 16 | 11,		//������һ����Ϣ�������ϵ�˸���
	TAG_SESSION_TRAY_COPYDATA			= MODULE_ID_SEESION << 16 | 12,		//��������ģ�鷢��������֪ͨ
};

/**
 * The class <code>ISessionModule</code> 
 *
 */
class MODULE_API ISessionModule : public logic::IPduAsyncSocketModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	ISessionModule()
	{
		m_moduleId = MODULE_ID_SEESION;
	}
	//@}

public:
	virtual DuiLib::CControlUI* createMainDialogControl(
		LPCTSTR pstrClass,DuiLib::CPaintManagerUI& paintManager) = 0;
	virtual void OnGroupUnreadMsgRespone(IN CImPdu* pdu) = 0;	

	/**@name ͬ��������ʱ��*/
	//@{
	virtual UInt32 getTime()const = 0;
	virtual void setTime(UInt32 time) = 0;
	virtual void startSyncTimeTimer() = 0;
	//@}
};

MODULE_API ISessionModule* getSessionModule();

NAMESPACE_END(module)
/******************************************************************************/
#endif// ISESSIONMODULE_070C0321_0708_4487_8028_C1D8934B709D_H__
