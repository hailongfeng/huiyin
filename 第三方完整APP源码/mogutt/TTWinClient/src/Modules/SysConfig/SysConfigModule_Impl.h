/*******************************************************************************
 *  @file      SysConfigModule_Impl.h 2014\8\4 10:56:38 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     ϵͳ������Ϣ������ϵͳ���ú�ȫ��������Ϣ
 ******************************************************************************/

#ifndef SYSCONFIGMODULE_IMPL_9E63D68E_676C_49DB_A936_7F97A626D551_H__
#define SYSCONFIGMODULE_IMPL_9E63D68E_676C_49DB_A936_7F97A626D551_H__

#include "Modules/ISysConfigModule.h"
/******************************************************************************/


/**
 * The class <code>ϵͳ������Ϣ������ϵͳ���ú�ȫ��������Ϣ</code> 
 *
 */
class SysConfigModule_Impl final : public module::ISysConfigModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
    SysConfigModule_Impl();
    /**
     * Destructor
     */
    virtual ~SysConfigModule_Impl();
    //@}
	virtual void release();
	virtual logic::LogicErrorCode onLoadModule();
	virtual logic::LogicErrorCode onUnLoadModule();

public:
	virtual module::TTConfig* getSystemConfig();
	virtual BOOL saveData();
	virtual std::string userID()const;
	virtual CString UserID()const;
	virtual void showSysConfigDialog(HWND hParentWnd);
	virtual BOOL showServerConfigDialog(HWND hParentWnd);
	virtual void SetSysConfigDialogFlag(BOOL bIsExist);
private:
	/**
	 * �������л�������
	 *
	 * @return  BOOL
	 * @exception there is no any exception to throw.
	 */	
	BOOL _loadData();
	/**
	* �������л�������
	*
	* @return  BOOL
	* @exception there is no any exception to throw.
	*/
	BOOL _saveData();
	/**
	 * �����л�
	 *
	 * @param   CArchive & ar
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	void _unMarshal(CArchive& ar);
	/**
	* ���л�
	*
	* @param   CArchive & ar
	* @return  void
	* @exception there is no any exception to throw.
	*/
	void _marshal(CArchive& ar);

private:
	module::TTConfig			m_pConfig;
	BOOL						m_bSysConfigDialogFlag;//ȷ����������ʵ��
};
/******************************************************************************/
#endif// SYSCONFIGMODULE_IMPL_9E63D68E_676C_49DB_A936_7F97A626D551_H__
