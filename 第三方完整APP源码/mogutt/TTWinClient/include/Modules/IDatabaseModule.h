/*******************************************************************************
 *  @file      IDatabaseModule.h 2014\8\3 10:38:47 $
 *  @author    �쵶<kuaidao@mogujie.com>
 *  @brief     ����sqliteʵ�ֱ������ݴ洢ģ�飬���û���Ϣ��
 ******************************************************************************/

#ifndef IDATABASEMODULE_086C113C_CEE3_423B_81D1_D771B443A991_H__
#define IDATABASEMODULE_086C113C_CEE3_423B_81D1_D771B443A991_H__

#include "GlobalDefine.h"
#include "TTLogic/IModule.h"
#include "Modules/ModuleDll.h"
/******************************************************************************/
NAMESPACE_BEGIN(module)
struct ImImageEntity
{
	UInt32				hashcode;		//����urlPath�����hashֵ
	std::string			filename;		//ͼƬ���ش洢����
	std::string			urlPath;		//ͼƬurl
};

/**
 * The class <code>IDatabaseModule</code> 
 *
 */
class MODULE_API IDatabaseModule : public logic::IModule
{
public:
    /** @name Constructors and Destructor*/

    //@{
    /**
     * Constructor 
     */
	IDatabaseModule()
	{
		m_moduleId = MODULE_ID_DATABASE;
	}
    //@}

public:
	virtual BOOL openDB(const utf8char* pPath) = 0;
	virtual BOOL isDBOpen() = 0;

	/**@name ͼƬ�洢���*/
	//@{
	virtual BOOL sqlInsertImImageEntity(const ImImageEntity& entity) = 0;
	virtual BOOL sqlGetImImageEntityByHashcode(UInt32 hashcode,ImImageEntity& entity) = 0;
	virtual BOOL sqlUpdateImImageEntity(UInt32 hashcode, module::ImImageEntity& entity) = 0;
	//@}
};

MODULE_API IDatabaseModule* getDatabaseModule();

NAMESPACE_END(module)
/******************************************************************************/
#endif// IDATABASEMODULE_086C113C_CEE3_423B_81D1_D771B443A991_H__
