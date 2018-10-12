package org.springrain.erp.constants;

/**
 * 
 * 
 * 
 *
 */
public class ErpStateEnum {

	// 通用状态状态
	public enum stateEnum {

		是("是", "1"), 否("否", "0");

		// 成员变量
		private String name;
		private String value;

		// 构造方法
		private stateEnum(String name, String value) {
			this.name = name;
			this.value = value;
		}

		// 普通方法
		public static String getName(String value) {
			for (tcstateEnum c : tcstateEnum.values()) {
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

	/**
	 * 劳动合同状态
	 * 
	 * @author Administrator
	 *
	 */
	public enum contractStateEnum {
		未过期("未过期", "1"), 已过期("已过期", "0");
		private String name;
		private String value;

		// 构造方法
		private contractStateEnum(String name, String value) {
			this.name = name;
			this.value = value;
		}

		// 普通方法
		public static String getName(String value) {
			for (contractStateEnum c : contractStateEnum.values()) {
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

	// 是否
	public enum gzZjxActiveEnum {

		是("是", "1"), 否("否", "0");

		// 成员变量
		private String name;
		private String value;

		// 构造方法
		private gzZjxActiveEnum(String name, String value) {
			this.name = name;
			this.value = value;
		}

		// 普通方法
		public static String getName(String value) {
			for (tcstateEnum c : tcstateEnum.values()) {
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

	public enum tcstateEnum {

		是("是", "1"), 否("否", "0");

		// 成员变量
		private String name;
		private String value;

		// 构造方法
		private tcstateEnum(String name, String value) {
			this.name = name;
			this.value = value;
		}

		// 普通方法
		public static String getName(String value) {
			for (tcstateEnum c : tcstateEnum.values()) {
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

	// 社保公积金类型
	public enum tcAccountTypeEnum {

		公积金("公积金", "1"), 社保("社保", "0");

		// 成员变量
		private String name;
		private String value;

		// 构造方法
		private tcAccountTypeEnum(String name, String value) {
			this.name = name;
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

	// 公积金字典ID
	public enum gongjijinId {
		公积金ID("公积金ID", "N000007"), 请假("请假", "N000013"), 加班("加班", "N000014");

		private String name;
		private String value;

		private gongjijinId(String name, String value) {
			this.name = name;
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

	// 统筹增减项是否使用
	public enum tcZjxStateEnum {
		是("是", "1"), 否("否", "0");

		// 成员变量
		private String name;
		private String value;

		// 构造方法
		private tcZjxStateEnum(String name, String value) {
			this.name = name;
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

	public enum userActiveEnum {
		在职("在职", 1), 离职("离职", 0);
		private String name;
		private int value;

		private userActiveEnum(String name, int value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

	}
	public enum userStateEnum {
		办理离职("办理离职", "0"), 工资生成("工资生成", "1"),人力确认停保("人力确认停保","2");
		private String name;
		private String value;

		private userStateEnum(String name, String value) {
			this.name = name;
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
	public enum userTypeEnum {
		系统账号("系统账号", 0), 员工("员工", 1);
		private String name;
		private int value;

		private userTypeEnum(String name, int value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

	}

	/**
	 * 查看工资
	 * 
	 * @author Administrator
	 *
	 */
	public enum operSalaryEnum {
		财务工资查询("财务工资查询", "all"), 员工工资查询("员工工资查询", "yuangong"), 工资发放("工资发放", "send"),个人工资查询("个人工资查询","me");

		private String name;

		private String value;

		private operSalaryEnum(String name, String value) {
			this.name = name;
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

	public enum stopPayEnum {
		停发("停止发放", 1), 撤销("已撤销", 0);

		private String name;
		private Integer value;

		private stopPayEnum(String name, Integer value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		public static String getName(Integer value) {
			for (stopPayEnum c : stopPayEnum.values()) {
				if (c.getValue() == value) {
					return c.name;
				}
			}
			return null;
		}

	}

	public enum userStopPayEnum {
		是("是", "是"), 否("否", "否");
		private String name;
		private String value;

		private userStopPayEnum(String name, String value) {
			this.name = name;
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

	public enum chuShiRoleEnum {
		初始角色("初始角色", "7740ec7a595e46b796538c3dfc8e1343");
		private String name;
		private String value;

		private chuShiRoleEnum(String name, String value) {
			this.name = name;
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

	public enum resumeTypeEnum {
		部门("部门", 0), 岗位("岗位", 1), 级别("级别", 2), 薪资("薪资", 3);

		private String name;
		private Integer value;

		private resumeTypeEnum(String name, Integer value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		public static String getName(Integer value) {
			for (resumeTypeEnum c : resumeTypeEnum.values()) {
				if (c.getValue() == value) {
					return c.name;
				}
			}
			return null;
		}

	}

	public enum adminIdEnum {
		admin("admin", "u_10001");
		private String name;
		private String value;

		private adminIdEnum(String name, String value) {
			this.name = name;
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

	public enum ZichanOperEnum {
		领用("领用"), 归还("归还"), 报损("报损"), 出售("出售");
		String state;

		private ZichanOperEnum(String state) {
			this.state = state;
		}

		public String getValue() {
			return state;
		}
	}

	// 通用状态状态
	public enum tcKKzeSourceEnum {

		统筹记录("统筹记录", "0"), 增减项("增减项", "1");

		// 成员变量
		private String name;
		private String value;

		// 构造方法
		private tcKKzeSourceEnum(String name, String value) {
			this.name = name;
			this.value = value;
		}

		// 普通方法
		public static String getName(String value) {
			for (tcstateEnum c : tcstateEnum.values()) {
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
	
	public enum userImportEnum {
		教育经历("教育经历", "education"), 证书("证书", "certificate"),基本信息("证书","baseinfo");
		private String name;
		private String value;

		// 构造方法
		private userImportEnum(String name, String value) {
			this.name = name;
			this.value = value;
		}

		// 普通方法
		public static String getName(String value) {
			for (contractStateEnum c : contractStateEnum.values()) {
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

}
