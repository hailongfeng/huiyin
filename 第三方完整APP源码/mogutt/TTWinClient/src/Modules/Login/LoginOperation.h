/*******************************************************************************
 *  @file      LoginOperation.h 2014\7\30 15:32:25 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     
 ******************************************************************************/

#ifndef LOGINOPERATION_9610A313_DC31_429E_B9E9_09A34ABA8063_H__
#define LOGINOPERATION_9610A313_DC31_429E_B9E9_09A34ABA8063_H__

#include "TTLogic/IOperation.h"
#include "src/base/ImPduClient.h"

/******************************************************************************/
enum
{
	LOGIN_FAIL = -1,            //��½ʧ��
	LOGIN_OK = 0,               //��½�ɹ�
	LOGIN_LOGINSVR_FAIL,		//��¼��¼������ʧ��
	LOGIN_MSGSVR_FAIL,			//��½��Ϣ������ʧ��
	LOGIN_DB_INVALID,			//DB��֤ʧ��
	LOGIN_VERSION_TOOOLD,       //Э��汾̫���ˣ��Ѿ������ݣ���ʾ�û�ȥ�����������°汾
};

struct LoginParam
{
public:
	Int8            result = LOGIN_FAIL;
	UInt8           mySelectedStatus = USER_STATUS_ONLINE;
	UInt32          serverTime = 0;
	CString			csUserName;
	std::string		password;
};

/**
 * The class <code>LoginOperation</code> 
 *
 */
class LoginOperation : public logic::ICallbackOpertaion
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	LoginOperation(logic::ICallbackHandler& callback, LoginParam& param);
    /**
     * Destructor
     */
    virtual ~LoginOperation();
    //@}

public:
	virtual void process();
	virtual void release();

private:
	LoginParam			m_loginParam;
};
/******************************************************************************/
#endif// LOGINOPERATION_9610A313_DC31_429E_B9E9_09A34ABA8063_H__
