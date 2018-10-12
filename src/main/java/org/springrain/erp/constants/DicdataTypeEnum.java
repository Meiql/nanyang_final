package org.springrain.erp.constants;

/**
 * 业务类型
 * 
 * @author wulei
 *
 */
public enum DicdataTypeEnum {
	统筹类型("统筹类型", "tctype"), 跳转类型("跳转类型", "truntype"), 民族类型("民族类型", "minzu"),
	学历类型("学历类型", "xueli"), 级别("级别", "grade"), 公司("公司", "company"),
	统筹缴纳地("统筹缴纳地", "tcjnd"), 岗位("岗位", "gangwei"),
	入职城市("入职城市","rzcs"), 用工类型("用工类型", "yonggong"), 
	合同类型("合同类型", "hetong"),政治面貌("政治面貌","zzmm"),费用类型("费用类型","fytype"),银行管理("银行管理","bank"),
	工资增减项类型("工资增减项类型","gzzjxlx"),员工工资查询时间("员工工资查询时间","gzcxsj"),不生成工资员工("不生成工资员工","nosalary"),
	招生方式("招生方式","zsfs"),发送邮件时间("发送邮件时间","sendDate"),接收邮箱("接收邮箱","receiveEmail"),学位("学位","xuewei"),
	资产类型("资产类型","zichan"),资产类别("资产类别","zcleibie");

	// 成员变量
	private String name;
	private String value;

	// 构造方法
	private DicdataTypeEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}

	// 普通方法
	public static String getName(String value) {
		for (DicdataTypeEnum c : DicdataTypeEnum.values()) {
			if (c.getValue().equals(value)) {
				return c.name;
			}
		}
		return null;
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
