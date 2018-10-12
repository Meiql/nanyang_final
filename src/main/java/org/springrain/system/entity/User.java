package org.springrain.system.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springrain.frame.annotation.WhereSQL;
import org.springrain.frame.entity.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * TODO 在此加入类描述
 * @copyright {@link weicms.net}
 * @author springrain<Auto generate>
 * @version  2013-07-06 16:03:00
 * @see org.springrain.system.entity.User
 */
@Table(name="t_user")
public class User  extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	private java.lang.String id;
	/**
	 * 姓名
	 */
	private java.lang.String name;
	/**
	 * 账号
	 */
	private java.lang.String account;
	/**
	 * 密码
	 */
	@JsonIgnore
	private java.lang.String password;
	
	/**
	 * 性别
	 */
	private java.lang.String sex;

	/**
	 * 手机号码
	 */
	@JsonIgnore
	private java.lang.String mobile;
	/**
	 * 邮箱
	 */
	@JsonIgnore
	private java.lang.String email;
	
	/**
	 * 是否有效,是/否/离职
	 */
	private java.lang.Integer active;
	/**
	 * 微信Id
	 */
	private String weixinId;
	
	/**
	 * 用户类型,0:后台管理员,1是用户
	 */
	private Integer userType;
	/**
	 * 年龄
	 */
	private java.lang.Integer age;
	
	/**
	 *身份证号 
	 */
	@JsonIgnore
	private java.lang.String idCard;
	
	/**
	 * 自定义编号
	 */
	private java.lang.String workno;
	
	/**
	 * 站点ID
	 */
	private java.lang.String siteId;
	/**
	 * 离职触发时间
	 */
	private Date  editDate;
	/**
	 * 离职修改人
	 */
	private java.lang.String editUser;
	private String oauserId;
	private java.lang.String bak1;
	private java.lang.String bak2;
	private java.lang.String bak3;
	private java.lang.String bak4;
	private java.lang.String bak5;
	private String state;
	
	//columns END 数据库字段结束
	


	//用户的所有角色
	private List<Role> userRoles;
	
	private List<UserOrg> managerOrgs;
	

	private String endDate;
	private String lizhiremark;
	private BigDecimal payDay;

	//concstructor
	public User(){
	}

	public User(
		java.lang.String id
	){
		this.id = id;
	}

	//get and set
	
		public void setId(java.lang.String value) {
			    if(StringUtils.isNotBlank(value)){
				 value=value.trim();
				}
			this.id = value;
		}
		
		@WhereSQL(sql="workno=:User_workno")
		public java.lang.String getWorkno() {
			return workno;
		}

		public void setWorkno(java.lang.String workno) {
			this.workno = workno;
		}

		@WhereSQL(sql="siteId=:User_siteId")
		public java.lang.String getSiteId() {
			return siteId;
		}

		public void setSiteId(java.lang.String siteId) {
			this.siteId = siteId;
		}

		public Date getEditDate() {
			return editDate;
		}

		public void setEditDate(Date editDate) {
			this.editDate = editDate;
		}

		public java.lang.String getEditUser() {
			return editUser;
		}

		public void setEditUser(java.lang.String editUser) {
			this.editUser = editUser;
		}
		
		@Id
	     @WhereSQL(sql="id=:User_id")
		public java.lang.String getId() {
			return this.id;
		}
		public void setName(java.lang.String value) {
			    if(StringUtils.isNotBlank(value)){
				 value=value.trim();
				}
			this.name = value;
		}
		
	     @WhereSQL(sql="name=:User_name")
		public java.lang.String getName() {
			return this.name;
		}
		
		public void setAccount(java.lang.String value) {
			    if(StringUtils.isNotBlank(value)){
				 value=value.trim();
				}
			this.account = value;
		}
		
	     @WhereSQL(sql="account=:User_account")
		public java.lang.String getAccount() {
			return this.account;
		}
		public void setPassword(java.lang.String value) {
			    if(StringUtils.isNotBlank(value)){
				 value=value.trim();
				}
			this.password = value;
		}
		
	     @WhereSQL(sql="password=:User_password")
		public java.lang.String getPassword() {
			return this.password;
		}
		
		
		public void setSex(java.lang.String value) {
			    if(StringUtils.isNotBlank(value)){
				 value=value.trim();
				}
			this.sex = value;
		}
		
	     @WhereSQL(sql="sex=:User_sex")
		public java.lang.String getSex() {
			return this.sex;
		}
		
		public void setMobile(java.lang.String value) {
			    if(StringUtils.isNotBlank(value)){
				 value=value.trim();
				}
			this.mobile = value;
		}
		
	     @WhereSQL(sql="mobile=:User_mobile")
		public java.lang.String getMobile() {
			return this.mobile;
		}
		public void setEmail(java.lang.String value) {
			    if(StringUtils.isNotBlank(value)){
				 value=value.trim();
				}
			this.email = value;
		}
		
	     @WhereSQL(sql="eamil=:User_email")
		public java.lang.String getEmail() {
			return this.email;
		}
		
		public void setActive(java.lang.Integer value) {
			this.active = value;
		}
		
	     @WhereSQL(sql="active=:User_active")
		public java.lang.Integer getActive() {
			return this.active;
		}
	     @WhereSQL(sql="userType=:User_userType")
		public Integer getUserType() {
			return userType;
		}

		public void setUserType(Integer userType) {
			this.userType = userType;
		}
		@WhereSQL(sql="age=:User_age")
		public java.lang.Integer getAge() {
			return age;
		}

		public void setAge(java.lang.Integer age) {
			this.age = age;
		}
		@WhereSQL(sql="IDcard=:User_idCard")
		public java.lang.String getIdCard() {
			return idCard;
		}

		public void setIdCard(java.lang.String idCard) {
			this.idCard = idCard;
		}
		@WhereSQL(sql="bak1=:User_bak1")
		public java.lang.String getBak1() {
			return bak1;
		}

		public void setBak1(java.lang.String bak1) {
			this.bak1 = bak1;
		}
		@WhereSQL(sql="bak2=:User_bak2")
		public java.lang.String getBak2() {
			return bak2;
		}

		public void setBak2(java.lang.String bak2) {
			this.bak2 = bak2;
		}
		@WhereSQL(sql="bak3=:User_bak3")
		public java.lang.String getBak3() {
			return bak3;
		}

		public void setBak3(java.lang.String bak3) {
			this.bak3 = bak3;
		}
		@WhereSQL(sql="bak4=:User_bak4")
		public java.lang.String getBak4() {
			return bak4;
		}

		public void setBak4(java.lang.String bak4) {
			this.bak4 = bak4;
		}
		@WhereSQL(sql="bak5=:User_bak5")
		public java.lang.String getBak5() {
			return bak5;
		}

		public void setBak5(java.lang.String bak5) {
			this.bak5 = bak5;
		}

		public String toString() {
			return new StringBuilder()
			.append("编号[").append(getId()).append("],")
			.append("姓名[").append(getName()).append("],")
			.append("账号[").append(getAccount()).append("],")
			.append("密码[").append(getPassword()).append("],")
			.append("性别[").append(getSex()).append("],")
			.append("手机号码[").append(getMobile()).append("],")
			.append("邮箱[").append(getEmail()).append("],")
			.append("是否有效,0否,1是[").append(getActive()).append("],")
			.toString();
		}
		
		public int hashCode() {
			return new HashCodeBuilder()
				.append(getId())
				.toHashCode();
		}
		
		public boolean equals(Object obj) {
			if(obj instanceof User == false){
				return false;
			} 
			if(this == obj){
				return true;
			} 
			User other = (User)obj;
			return new EqualsBuilder()
				.append(getId(),other.getId())
				.isEquals();
		}


		@Transient
		public List<Role> getUserRoles() {
			return userRoles;
		}

		public void setUserRoles(List<Role> userRoles) {
			this.userRoles = userRoles;
		}

		public String getWeixinId() {
			return weixinId;
		}

		public void setWeixinId(String weixinId) {
			this.weixinId = weixinId;
		}
		@Transient
		public List<UserOrg> getManagerOrgs() {
			return managerOrgs;
		}

		public void setManagerOrgs(List<UserOrg> managerOrgs) {
			this.managerOrgs = managerOrgs;
		}
		@Transient
		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		@Transient
		public String getLizhiremark() {
			return lizhiremark;
		}

		public void setLizhiremark(String lizhiremark) {
			this.lizhiremark = lizhiremark;
		}
		@Transient
		public BigDecimal getPayDay() {
			return payDay;
		}

		public void setPayDay(BigDecimal payDay) {
			this.payDay = payDay;
		}

		public String getOauserId() {
			return oauserId;
		}

		public void setOauserId(String oauserId) {
			this.oauserId = oauserId;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
	
	
	
}

	
