package cn.jeeweb.modules.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import cn.jeeweb.core.common.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import cn.jeeweb.modules.sys.entity.User;
import java.util.Date;

/**   
 * @Title: 订单主表
 * @Description: 订单主表
 * @author jeeweb
 * @date 2017-06-30 16:31:40
 * @version V1.0   
 *
 */
@Entity
@Table(name = "test_order_main")
@DynamicUpdate(false)
@DynamicInsert(false)
@SuppressWarnings("serial")
public class TestOrderMainEntity extends AbstractEntity<String> {

    /**订单号*/
	private String orderno;
    /**更新者*/
	private User updateBy;
    /**创建者*/
	private User createBy;
    /**订单金额*/
	private String money;
    /**删除标记（0：正常；1：删除）*/
	private String delFlag;
    /**备注信息*/
	private String remarks;
    /**更新时间*/
	private Date updateDate;
    /**创建时间*/
	private Date createDate;
    /**id*/
	private String id;
    /**订单日期*/
	private Date orderdate;
	@JsonIgnore
	private List<TestOrderTicketEntity> testOrderTicketList = new ArrayList<TestOrderTicketEntity>();
	@JsonIgnore
	private List<TestOrderCustomerEntity> testOrderCustomerList = new ArrayList<TestOrderCustomerEntity>();
	
	/**
	 * 获取  orderno
	 *@return: String  订单号
	 */
	@Column(name ="orderno",nullable=false,length=50,scale=0)
	public String getOrderno(){
		return this.orderno;
	}

	/**
	 * 设置  orderno
	 *@param: orderno  订单号
	 */
	public void setOrderno(String orderno){
		this.orderno = orderno;
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
	 * 获取  money
	 *@return: String  订单金额
	 */
	@Column(name ="money",nullable=false,length=22,scale=0)
	public String getMoney(){
		return this.money;
	}

	/**
	 * 设置  money
	 *@param: money  订单金额
	 */
	public void setMoney(String money){
		this.money = money;
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
	 * 获取  id
	 *@return: String  id
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
	 *@param: id  id
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 * 获取  orderdate
	 *@return: Date  订单日期
	 */
	@Column(name ="orderdate",nullable=false,length=19,scale=0)
	public Date getOrderdate(){
		return this.orderdate;
	}

	/**
	 * 设置  orderdate
	 *@param: orderdate  订单日期
	 */
	public void setOrderdate(Date orderdate){
		this.orderdate = orderdate;
	}
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	@Fetch(FetchMode.SELECT)
	public List<TestOrderTicketEntity> getTestOrderTicketList() {
		return testOrderTicketList;
	}

	public void setTestOrderTicketList(List<TestOrderTicketEntity> testOrderTicketList) {
		this.testOrderTicketList = testOrderTicketList;
	}
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	@Fetch(FetchMode.SELECT)
	public List<TestOrderCustomerEntity> getTestOrderCustomerList() {
		return testOrderCustomerList;
	}

	public void setTestOrderCustomerList(List<TestOrderCustomerEntity> testOrderCustomerList) {
		this.testOrderCustomerList = testOrderCustomerList;
	}
}
