/*******************************************************************************
 *  @file      MakeGroupWnd.h 2014\7\24 17:42:15 $
 *  @author    ���<dafo@mogujie.com>
 *  @brief   
 ******************************************************************************/

#ifndef MAKEGROUPWND_537FB18B_C75C_445C_805F_C2624BCEB298_H__
#define MAKEGROUPWND_537FB18B_C75C_445C_805F_C2624BCEB298_H__

#include "DuiLib/UIlib.h"
/******************************************************************************/
using namespace DuiLib;

/**
 * The class <code>CreateDiscussionGrpDialog</code> 
 *
 */
class UIIMList;

class CreateDiscussionGrpDialog : public WindowImplBase
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	CreateDiscussionGrpDialog(std::string currentSessionId);
    /**
     * Destructor
     */
    virtual ~CreateDiscussionGrpDialog();
    //@}
	DUI_DECLARE_MESSAGE_MAP()
public:
	LPCTSTR GetWindowClassName() const;
	virtual CDuiString GetSkinFile();
	virtual CDuiString GetSkinFolder();
	virtual CControlUI* CreateControl(LPCTSTR pstrClass);
	virtual void OnFinalMessage(HWND hWnd);
	virtual LRESULT HandleMessage(UINT uMsg, WPARAM wParam, LPARAM lParam);
protected:
	void OnPrepare(TNotifyUI& msg);
	void OnItemActive(TNotifyUI& msg);
	void OnItemClick(TNotifyUI& msg);
	void OnClick(TNotifyUI& msg);
	void OnTextChanged(TNotifyUI& msg);
private:
	void _AddToGroupMemberList(std::string sid);
	void _updateSearchResultList(IN const std::vector<std::string>& nameList);
private:
	UIIMList*				m_pListCreatFrom;		//����������Դ�б�	��
	CListUI*				m_pListGroupMembers;	//���������б�	��
	CListUI*				m_pListSearchResult;	//��������б�
	CEditUI*				m_editGroupName;		
	CVerticalLayoutUI*		m_searchePanel;
	CEditUI*				m_editSearch;			//������
	CTextUI*				m_TextaddNums;			//�Ѿ����˶�����

	std::string				m_currentSessionId;
};
/******************************************************************************/
#endif// MAKEGROUPWND_537FB18B_C75C_445C_805F_C2624BCEB298_H__
