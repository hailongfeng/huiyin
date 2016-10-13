/*******************************************************************************
 *  @file      ModuleID.h 2014\7\16 17:56:55 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief   
 ******************************************************************************/

#ifndef MODULEID_D64A8C53_C3BB_4041_A371_CD65CFE7DA37_H__
#define MODULEID_D64A8C53_C3BB_4041_A371_CD65CFE7DA37_H__

#include "GlobalDefine.h"
/******************************************************************************/

const UInt16 MODULE_REMOTE_APPBASE = 0x0000U;    //��Զ�̽�����ģ�����ID
const UInt16 MODULE_LOCAL_BASE = 0x4000U;        //�ͻ��˱���ģ�����ID

enum
{
	MODULE_ID_NONE = 0,

	//��Զ�̽�����ģ�����I	
	MODULE_ID_LOGIN			= MODULE_REMOTE_APPBASE | 0x0001,      //��½ģ��
	MODULE_ID_USERLIST		= MODULE_REMOTE_APPBASE | 0x0002,      //��Ա�б�ģ��
	MODULE_ID_SEESION		= MODULE_REMOTE_APPBASE | 0x0003,      //�Ựģ��
	MODULE_ID_P2PCMD		= MODULE_REMOTE_APPBASE | 0x0004,      //�Զ���P2PЭ����Ϣ
	MODULE_ID_GROUPLIST		= MODULE_REMOTE_APPBASE | 0x0005,      //Ⱥģ��
	MODULE_ID_FILETRANSFER	= MODULE_REMOTE_APPBASE | 0x0006,      //�ļ�����
	MODULE_ID_HTTPPOOL		= MODULE_REMOTE_APPBASE | 0x0007,      //HTTP�̳߳�
	MODULE_ID_TCPCLIENT		= MODULE_REMOTE_APPBASE | 0x0008,      //����ģ��

	//�ͻ��˱���ģ�����ID
	MODULE_ID_DATABASE		= MODULE_LOCAL_BASE | 0x0001,      //�������ݴ洢ģ��
	MODULE_ID_MISC			= MODULE_LOCAL_BASE | 0x0002,      //һЩ�Ƚ��ӵĹ���
	MODULE_ID_CAPTURE		= MODULE_LOCAL_BASE | 0x0003,      //����ģ��
	MODULE_ID_SYSCONFIG		= MODULE_LOCAL_BASE | 0x0004,      //ϵͳ������Ϣ
	MODULE_ID_MESSAGE		= MODULE_LOCAL_BASE | 0x0005,      //��ʷ��Ϣ
	MODULE_ID_EMOTION		= MODULE_LOCAL_BASE | 0x0006,      //����
	MODULE_ID_MENU			= MODULE_LOCAL_BASE | 0x0007,      //�˵�
};

/******************************************************************************/
#endif// MODULEID_D64A8C53_C3BB_4041_A371_CD65CFE7DA37_H__
