/*******************************************************************************
 *  @file      DownloadImgHttpOperation.h 2014\8\14 10:18:26 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     ����ͼƬ����
 ******************************************************************************/

#ifndef DOWNLOADIMGHTTPOPERATION_4BB88F5E_5D0E_4FBA_9530_72972EB7647C_H__
#define DOWNLOADIMGHTTPOPERATION_4BB88F5E_5D0E_4FBA_9530_72972EB7647C_H__

#include "Modules/IHttpPoolModule.h"
#include "Modules/IDatabaseModule.h"
/******************************************************************************/
class DownloadImgParam
{
public:
	enum
	{
		DOWNLOADIMG_OK = 0,             //���سɹ�
		DOWNLOADIMG_ERROR,				//�������
	};

public:
	UInt8						m_result;
	std::string					m_sId;
	module::ImImageEntity		m_imgEntity;
};

/**
 * The class <code>����ͼƬ����</code> 
 *
 */
class DownloadImgHttpOperation : public module::IHttpOperation
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	DownloadImgHttpOperation(std::string sId,std::string& downUrl,BOOL bGrayScale
		,logic::ICallbackHandler& callback);
    /**
     * Destructor
     */
    virtual ~DownloadImgHttpOperation();
    //@}

public:
	virtual void process();
	virtual void release();

private:
	std::string		m_downUrl;
	std::string		m_sId;
	BOOL			m_bGrayScale;
};

/******************************************************************************/
#endif// DOWNLOADIMGHTTPOPERATION_4BB88F5E_5D0E_4FBA_9530_72972EB7647C_H__
