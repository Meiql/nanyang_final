package org.springrain.frame.util.property;

/**
 * db配置文件
 * 
 * @author wulei
 *
 */
public class HostPropertyUtil {
	public static PropertyFile property = null;

	static {
		property = new PropertyFile("hosturl");
	}

	public static String getValue(String key) {
		return property.getString(key);
	}

}
