package org.springrain.erp.shiro;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import org.springrain.cms.util.SiteUtils;
import org.springrain.frame.common.SessionUser;
import org.springrain.frame.util.GlobalStatic;
import org.springrain.system.entity.User;
import org.springrain.system.service.IUserService;

/**
 * 微信登陆校验
 * 
 * @author Administrator
 *
 */
@Component("wxcheck")
public class WxCheckFilter extends UserFilter {
	@Resource
	private IUserService userService;
	private String wxcheckpage = "/system/wxcheck";

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {
		HttpServletResponse rep = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;

		String springraintoken = req.getParameter("springraintoken");
		// 判断用户是否需要校验登录
		String userid = SessionUser.getUserId();
		User user = userService.findById(userid, User.class);
		String ischeckwx = user.getBak1();
		String checkpage = SiteUtils.getBaseURL(req) + wxcheckpage
				+ "?springraintoken=" + springraintoken;
		if (StringUtils.isNotBlank(ischeckwx)&&"on".equals(ischeckwx)) { 
			if (req.getSession().getAttribute("CHECKWXTOKEN") != null) {
				return true;
			} 
			Cache cache = userService.getCache(GlobalStatic.CACHEID_WXLOGINCHECK);
			Object openId = cache.get(user.getId(),String.class);
			if (openId == null) {
				// 如果没有openId 去扫描页面
				rep.sendRedirect(checkpage);
				return false;
			} else {
				if (StringUtils.isBlank(user.getWeixinId())) {
					// 第一次扫描
					user.setWeixinId(openId.toString());
					userService.update(user);
				} else {
					if (!user.getWeixinId().equals(openId)) {
						// 微信认证失败
						cache.evict(user.getId());
						rep.sendRedirect(checkpage + "&msg=error");
						return false;
					}
				}
				req.getSession().setAttribute("CHECKWXTOKEN", "y");
			}
			cache.evict(user.getId());
		}
		return true;
	}
}
