package org.springrain.frame.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springrain.frame.util.IPUtils;
import org.springrain.frame.util.SecUtils;
import org.springrain.frame.util.property.DbPropertyUtil;

/**
 * 任务调度
 * 
 */

@Component("taskjob")
public class TaskJobFilter extends BaseUserFilter {
	public TaskJobFilter() {
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// 获取IP地址
		HttpServletRequest req = (HttpServletRequest) request;
		String ip = IPUtils.getClientAddress(req);
		String ips = DbPropertyUtil.getValue("taskjob.ips");
		if (StringUtils.isBlank(ips) || StringUtils.isBlank(ip)) {
			return false;
		}
		if ((ips).indexOf(ip + ",") == -1) {
			return false;
		}
		String token = DbPropertyUtil.getValue("taskjob.token");
		String channel = request.getParameter("channel");
		String time = request.getParameter("time");
		String sign = request.getParameter("sign");

		StringBuffer str = new StringBuffer();
		if (StringUtils.isNotBlank(channel)) {
			str.append(channel);
		}
		if (StringUtils.isNotBlank(time)) {
			str.append(time);
		}
		if (StringUtils.isNotBlank(token)) {
			str.append(token);
		}
		String md5str = SecUtils.encoderByMd5With32Bit(str.toString());
		if (!md5str.equals(sign)) {
			return false;
		}
		return true;

	}

}
