package org.springrain.erp.constants;

/**
 * 
 * 
 * 
 *
 */
public class TcCodeTypeEnum {
	/**
	 * tc桌面展示
	 * @author Administrator
	 *
	 */
	public enum tcFyTypeEnum{
		社保("社保","社保"),
		代扣代缴("代扣代缴","代扣代缴");
		private String name;
		
		private String value;
		
		private tcFyTypeEnum(String name,String value){
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
