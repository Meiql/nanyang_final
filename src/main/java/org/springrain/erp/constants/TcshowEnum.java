package org.springrain.erp.constants;

/**
 * 
 * 
 * 
 *
 */
public class TcshowEnum {
	/**
	 * tc桌面展示
	 * @author Administrator
	 *
	 */
	public enum tcDeskShowEnum{
		员工统筹公积金("员工统筹公积金","tcbz"),统筹月度数据("统筹月度数据","tcrecord"),工资代扣代缴报表("工资代扣代缴报表","gzdkdj"),社保公积金缴纳报表("社保公积金缴纳报表","tcjnrecord")
		,代扣代缴与社保差异("代扣代缴与社保差异","tcChayi");
		
		private String name;
		
		private String value;
		
		private tcDeskShowEnum(String name,String value){
			this.name = name ;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		
	}

	
	public enum tcShowPropetryEnum{
		缴费基数("缴费基数","radices"),账号("账号","insuranceAccount"),个人缴费("个人缴费","insurancePersonal"),单位缴费("单位缴费","insuranceCompany")
		,缴费开始时间("缴费开始时间","insurancePaymentDate"),缴费结束时间("缴费结束时间","efficientDate")
		,代扣代缴公司部分("代扣代缴公司部分","insuranceCompanyDk"),代扣代缴个人部分("代扣代缴个人部分","insurancePersonalDk")
		,社保缴纳公司部分("社保缴纳公司部分","insuranceCompanyJn"),社保缴纳个人部分("社保缴纳个人部分","insurancePersonalJn")
		,公司部分差额("公司部分差额","insuranceCompanyCe"),个人部分差额("个人部分差额","insurancePersonalCe");
		
		private String name;
		
		private String value;
		
		private tcShowPropetryEnum(String name,String value){
			this.name = name ;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		
	}
	

}
