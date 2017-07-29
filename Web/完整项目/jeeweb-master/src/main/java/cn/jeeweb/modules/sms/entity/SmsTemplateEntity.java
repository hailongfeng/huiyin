package cn.jeeweb.modules.sms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import cn.jeeweb.core.common.entity.AbstractEntity;
import cn.jeeweb.modules.sys.entity.User;
import java.util.Date;

/**   
 * @Title: 短信模版
 * @Description: 短信模版
 * @author jeeweb
 * @date 2017-06-08 10:50:52
 * @version V1.0   
 *
 */
@Entity
@Table(name = "sms_template")
@DynamicUpdate(false)
@DynamicInsert(false)
@SuppressWarnings("serial")
public class SmsTemplateEntity extends AbstractEntity<String> {

    /**字段主键*/
	private String id;
    /**模版名称*/
	private String name;
    /**模版编码*/
	private String code;
    /**业务类型*/
	private String businessType;
    /**模版ID*/
	private String templateId;
    /**模版内容*/
	private String templateContent;
    /**创建者*/
	private User createBy;
    /**创建时间*/
	private Date createDate;
    /**更新者*/
	private User updateBy;
    /**更新时间*/
	private Date updateDate;
    /**删除标记（0：正常；1：删除）*/
	private String delFlag;
    /**备注信息*/
	private String remarks;
	/**
	 * 获取  id
	 *@return: String  字段主键
	 */
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="id",nullable=false,length=32,scale=0)
	public String getId(){
		return this.id;
	}

	/**
	 * 设置  id
	 *@param: id  字段主键
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 * 获取  name
	 *@return: String  模版名称
	 */
	@Column(name ="name",nullable=false,length=255)
	public String getName(){
		return this.name;
	}

	/**
	 * 设置  name
	 *@param: name  模版名称
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 * 获取  code
	 *@return: String  模版编码
	 */
	@Column(name ="code",nullable=false,length=255)
	public String getCode(){
		return this.code;
	}

	/**
	 * 设置  code
	 *@param: code  模版编码
	 */
	public void setCode(String code){
		this.code = code;
	}
	/**
	 * 获取  businessType
	 *@return: String  业务类型
	 */
	@Column(name ="business_type",nullable=false,length=4)
	public String getBusinessType(){
		return this.businessType;
	}

	/**
	 * 设置  businessType
	 *@param: businessType  业务类型
	 */
	public void setBusinessType(String businessType){
		this.businessType = businessType;
	}
	/**
	 * 获取  templateId
	 *@return: String  模版ID
	 */
	@Column(name ="template_id",nullable=false,length=4)
	public String getTemplateId(){
		return this.templateId;
	}

	/**
	 * 设置  templateId
	 *@param: templateId  模版ID
	 */
	public void setTemplateId(String templateId){
		this.templateId = templateId;
	}
	/**
	 * 获取  templateContent
	 *@return: String  模版内容
	 */
	@Column(name ="template_content",nullable=false,length=255)
	public String getTemplateContent(){
		return this.templateContent;
	}

	/**
	 * 设置  templateContent
	 *@param: templateContent  模版内容
	 */
	public void setTemplateContent(String templateContent){
		this.templateContent = templateContent;
	}
	/**
	 * 获取  createBy
	 *@return: User  创建者
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "create_by")
	public User getCreateBy(){
		return this.createBy;
	}

	/**
	 * 设置  createBy
	 *@param: createBy  创建者
	 */
	public void setCreateBy(User createBy){
		this.createBy = createBy;
	}
	/**
	 * 获取  createDate
	 *@return: Date  创建时间
	 */
	@Column(name ="create_date",nullable=true,length=19,scale=0)
	public Date getCreateDate(){
		return this.createDate;
	}

	/**
	 * 设置  createDate
	 *@param: createDate  创建时间
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 * 获取  updateBy
	 *@return: User  更新者
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "update_by")
	public User getUpdateBy(){
		return this.updateBy;
	}

	/**
	 * 设置  updateBy
	 *@param: updateBy  更新者
	 */
	public void setUpdateBy(User updateBy){
		this.updateBy = updateBy;
	}
	/**
	 * 获取  updateDate
	 *@return: Date  更新时间
	 */
	@Column(name ="update_date",nullable=true,length=19,scale=0)
	public Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 * 设置  updateDate
	 *@param: updateDate  更新时间
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 * 获取  delFlag
	 *@return: String  删除标记（0：正常；1：删除）
	 */
	@Column(name ="del_flag",nullable=false,length=1,scale=0)
	public String getDelFlag(){
		return this.delFlag;
	}

	/**
	 * 设置  delFlag
	 *@param: delFlag  删除标记（0：正常；1：删除）
	 */
	public void setDelFlag(String delFlag){
		this.delFlag = delFlag;
	}
	/**
	 * 获取  remarks
	 *@return: String  备注信息
	 */
	@Column(name ="remarks",nullable=true,length=255,scale=0)
	public String getRemarks(){
		return this.remarks;
	}

	/**
	 * 设置  remarks
	 *@param: remarks  备注信息
	 */
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}
	
}
