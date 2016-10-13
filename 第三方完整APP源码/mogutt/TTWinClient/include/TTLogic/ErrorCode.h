/*******************************************************************************
 *  @file      ErrorCode.h 2012\8\16 22:21:34 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief   Logic���ش�����Ķ���,ͨ��Э��Ĵ����붨����ProtErrorCode.h
 ******************************************************************************/

#ifndef ERRORCODE_016A9735_BF5C_425C_822A_C4E4680D3449_H__
#define ERRORCODE_016A9735_BF5C_425C_822A_C4E4680D3449_H__

#include "GlobalDefine.h"
/******************************************************************************/
NAMESPACE_BEGIN(logic)

typedef UInt32  LogicErrorCode;
//�������룬error code flag
const LogicErrorCode LOGIC_FLAG                 = 0x000000U; // ������������
const LogicErrorCode LOGIC_MODULE_FLAG         = 0x010000U; // �������module�����Ĵ���
const LogicErrorCode LOGIC_WORK_FLAG            = 0x020000U; // ��������̨����opertaion,event�����Ĵ���
const LogicErrorCode LOGIC_DOCUMENT_FLAH        = 0x040000U; // ��������ĵ����Ʋ����Ĵ���

//�������
const LogicErrorCode LOGIC_OK                               = LOGIC_FLAG | 0x00U;   //һ��OK
const LogicErrorCode LOGIC_ALLOC_ERROR                      = LOGIC_FLAG | 0x01U;   //�ڴ�������
const LogicErrorCode LOGIC_INVALID_HWND_ERROR               = LOGIC_FLAG | 0x02U;   //��Ч�Ĵ��ھ��
const LogicErrorCode LOGIC_ARGUMENT_ERROR                   = LOGIC_FLAG | 0x03U;   //�߼���������
const LogicErrorCode LOGIC_FILE_OPEN_ERROR                  = LOGIC_FLAG | 0x04U;   //�ļ���ʧ��
const LogicErrorCode LOGIC_FILE_READ_ERROR                  = LOGIC_FLAG | 0x05U;   //�ļ���ȡʧ��
const LogicErrorCode LOGIC_FILE_WRITE_ERROR                 = LOGIC_FLAG | 0x06U;   //�ļ���ȡʧ��
const LogicErrorCode LOGIC_FILE_SYSTEM_ERROR                = LOGIC_FLAG | 0x07U;   //�ļ�δ֪�쳣

//module ����
const LogicErrorCode LOGIC_MODULE_LOAD_ERROR               = LOGIC_MODULE_FLAG | 0x01; //����ģ��ʧ��
const LogicErrorCode LOGIC_MODULE_HASONE_ERROR             = LOGIC_MODULE_FLAG | 0x02; //ģ���Ѿ�����
const LogicErrorCode LOGIC_MODULE_INEXISTENCE_ERROR        = LOGIC_MODULE_FLAG | 0x03; //ģ�鲻����

//opertaion event����
const LogicErrorCode LOGIC_WORK_INTERNEL_ERROR              = LOGIC_WORK_FLAG | 0x01;   //worker�ڲ�����
const LogicErrorCode LOGIC_WORK_POSTMESSAGE_ERROR           = LOGIC_WORK_FLAG | 0x02;   //event post��Ϣʧ��
const LogicErrorCode LOGIC_WORK_PUSHOPERTION_ERROR          = LOGIC_WORK_FLAG | 0x03;   //opertaion pushʧ��
const LogicErrorCode LOGIC_WORK_TIMER_INEXISTENCE_ERROR     = LOGIC_WORK_FLAG | 0x04;   //Timer������

//document����


NAMESPACE_END(logic)
/******************************************************************************/
#endif// ERRORCODE_016A9735_BF5C_425C_822A_C4E4680D3449_H__