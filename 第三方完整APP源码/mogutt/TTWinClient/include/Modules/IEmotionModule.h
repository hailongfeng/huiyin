/*******************************************************************************
 *  @file      IEmotionModule.h 2014\8\6 20:03:22 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     �������ģ��
 ******************************************************************************/

#ifndef IEMOTIONMODULE_8E431B43_8F05_4934_8B5B_BB839730F3A6_H__
#define IEMOTIONMODULE_8E431B43_8F05_4934_8B5B_BB839730F3A6_H__

#include "GlobalDefine.h"
#include "TTLogic/IModule.h"
#include "Modules/ModuleDll.h"
/******************************************************************************/
NAMESPACE_BEGIN(module)

enum
{
	KEY_EMOTION_SELECTED = MODULE_ID_EMOTION << 16 | 1,      //ѡ����ĳ������
};

/**
 * The class <code>�������ģ��</code> 
 *
 */

class EmotionParam
{
public:
	std::string			 sid;
	CString				 strPath;
};

class MODULE_API IEmotionModule : public logic::IModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	IEmotionModule()
	{
		m_moduleId = MODULE_ID_EMOTION;
	}
    //@}

public:
	/**
	 * ��ʾ���鴰��
	 *
	 * @param   POINT pt ��ʾ��λ��
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	virtual void showEmotionDialog(IN std::string sid, IN POINT pt) = 0;
	virtual BOOL getEmotionNameByID(IN CString ID, OUT CString& csPath) = 0;
	virtual BOOL getEmotionIDByName(IN CString csPath, OUT CString& ID) = 0;
	/**
	* ��ȡ��ǰ��ʾ���ڵĻỰID
	*
	* @param   POINT pt ��ʾ��λ��
	* @return  void
	* @exception there is no any exception to throw.
	*/
	virtual std::string  getCurEmotionWndSessionId(void) = 0;
};

MODULE_API IEmotionModule* getEmotionModule();

NAMESPACE_END(module)
/******************************************************************************/
#endif// IEMOTIONMODULE_8E431B43_8F05_4934_8B5B_BB839730F3A6_H__
