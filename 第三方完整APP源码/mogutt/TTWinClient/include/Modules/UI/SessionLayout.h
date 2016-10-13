 /*******************************************************************************
 *  @file      SessionLayout.h 2014\8\5 20:25:03 $
 *  @author    ���<dafo@mogujie.com>
 *  @brief     
 ******************************************************************************/

#ifndef SESSIONLAYOUT_6BC9730E_47F6_4BCB_936D_AC034AA10DFF_H__
#define SESSIONLAYOUT_6BC9730E_47F6_4BCB_936D_AC034AA10DFF_H__

#include "DuiLib/UIlib.h"
#include "GlobalDefine.h"
#include "UIIMEdit.h"
#include "TTLogic/MKObserver.h"
#include <memory>

/******************************************************************************/
using namespace DuiLib;

/**
 * The class <code>SessionLayout</code> 
 *
 */

class MessageEntity;
class EmotionDialog;
class SessionLayout :public CHorizontalLayoutUI, public INotifyUI, public CWebBrowserEventHandler
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	SessionLayout(const std::string& sId, CPaintManagerUI& paint_manager);
    /**
     * Destructor
     */
    virtual ~SessionLayout();
    //@}

public:
	virtual void DoInit();
	virtual void DoEvent(TEventUI& event);
	virtual void Notify(TNotifyUI& msg);

	void OnEmotionModuleEvent(UInt16 moduleId, UInt32 keyId, MKO_TUPLE_PARAM mkoParam);

	void OnWindowInitialized(TNotifyUI& msg);

	void DocmentComplete(IDispatch *pDisp, VARIANT *&url);//�򿪻Ự�Ѿ������������ʼ����ɣ�����δ����Ϣ
	virtual HRESULT STDMETHODCALLTYPE TranslateUrl(
		/* [in] */ DWORD dwTranslate,
		/* [in] */ OLECHAR __RPC_FAR *pchURLIn,
		/* [out] */ OLECHAR __RPC_FAR *__RPC_FAR *ppchURLOut);
	virtual void NewWindow2(VARIANT_BOOL *&Cancel, BSTR bstrUrl);

	//�������Ŷ�������
	BOOL StopPlayingAnimate(std::string& sAudioPlayingID);
	BOOL StartPlayingAnimate(std::string sAudioPlayingID);

public:
	void SendMsg();
	void UpdateRunTimeMsg();
	void UpdateSendMsgKey();
	void UpdateBottomLayout();
	void FreshGroupMemberAvatar(IN const std::string& sID);		//ˢ��Ⱥ��Ա������״̬
	void FreshAllGroupMemberAvatar();
	void OnCallbackOperation(std::shared_ptr<void> param);//����ͼƬ����

private:
	/**
	 * չʾȺ��Ա
	 *
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	void _UpdateGroupMembersList();
	void _AddGroupMemberToList(IN const std::string& sID);
	void _UpdateSearchRsultList(IN const std::vector<std::string>& nameList);
	BOOL _DisplayUnreadMsg();
	void _DisplayHistoryMsgToIE(UInt32 nMsgCount, BOOL scrollBottom);
	BOOL _DisplayMsgToIE(IN MessageEntity msg, IN CString jsInterface);
	void _DisplaySysMsg(IN CString strID);
	void _SendSessionMsg(IN MixedMsg mixMsg);
	void _SendImage(CString& csFilePath);

	void _GetGroupNameListByShortName(IN const CString& sShortName, OUT std::vector<string>& nameList);

	void _OnBanGroupMsg(const bool bBan);
	/**
	 * �����Ѷ�ȷ��
	 *
	 * @param   IN MessageEntity msg
	 * @return  void
	 * @exception there is no any exception to throw.
	 */
	void _AsynSendReadAck(IN MessageEntity msg);	

public:
	CPaintManagerUI&	m_paint_manager;

	CWebBrowserUI*		m_pWebBrowser;//������ʾ��
	UIIMEdit*			m_pInputRichEdit;

	CContainerUI*		m_pRightContainer;
	CListUI*			m_pGroupMemberList;
	CEditUI*			m_pSearchEdit;
	CListUI*			m_pSearchResultList;

	CTextUI*			m_pSendDescription;	// ctrl+enter /enter

	CButtonUI*			m_pBtnSendMsg;
	CButtonUI*			m_pBtnClose;

	CButtonUI*			m_pBtnEmotion;
	CButtonUI*			m_pBtnSendImage;
	CButtonUI*			m_pBtnshock;
	CButtonUI*			m_pBtnsendfile;
	CButtonUI*			m_pBtnbanGroupMsg;
	CButtonUI*			m_pBtndisplayGroupMsg;
	CButtonUI*			m_pBtnadduser;//����������Ա

	CHorizontalLayoutUI*	m_bottomLayout;

	std::string				m_sId;								//�ỰId
	std::vector<MixedMsg>	m_SendingMixedMSGList;				//���ڷ��͵�ͼ�Ļ�����Ϣ
	BOOL					m_bGroupSession;					//��Ϊ����״̬�ж��ã�Ⱥ�ǲ���Ҫ��״̬��
	time_t					m_tShakeWindow;
	CString					m_csTobeTranslateUrl;				//IE�ؼ�ת��URL
};
/******************************************************************************/
#endif// SESSIONLAYOUT_6BC9730E_47F6_4BCB_936D_AC034AA10DFF_H__
