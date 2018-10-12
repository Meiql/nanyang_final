package org.springrain.system.web;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springrain.cms.util.SiteUtils;
import org.springrain.frame.common.SessionUser;
import org.springrain.frame.controller.BaseController;
import org.springrain.frame.shiro.FrameAuthenticationToken;
import org.springrain.frame.util.DateUtils;
import org.springrain.frame.util.DesSecUtils;
import org.springrain.frame.util.GlobalStatic;
import org.springrain.frame.util.QrCodeUtils;
import org.springrain.frame.util.ReturnDatas;
import org.springrain.frame.util.SecUtils;
import org.springrain.system.entity.User;
import org.springrain.system.service.IUserService;

@Controller
@RequestMapping(value = "/system")
public class SystemLoginController extends BaseController {
	@Resource
	IUserService userService;
	@Resource
	private CacheManager cacheManager;
	
	private final Integer ERROR_TIME_SHOWYZM=2;
	private final String BACKPWDSTRSEC="backpwd_strSec";

	/**
	 * 首页的映射
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/")
	public String index() throws Exception {
		return redirect + "/system/index";
	}

	/**
	 * 首页的映射
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public String index(Model model, HttpSession session,
			HttpServletRequest request) throws Exception {
		String siteId = request.getParameter("systemSiteId");
		if (StringUtils.isNotBlank(siteId)) {
			model.addAttribute("systemSiteId", siteId);
		}
		return "/system/index";
	}

	/**
	 * 渲染登录/login的界面展示,如果已经登录,跳转到首页,如果没有登录,就渲染login.html
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, HttpServletRequest request)
			throws Exception {
		return getLoginUrl(model, request, null);
	}

	private String getLoginUrl(Model model, HttpServletRequest request,
			String siteId) {

		// 判断用户是否登录
		if (SecurityUtils.getSubject().isAuthenticated()) {
			model.addAttribute(GlobalStatic.tokenKey, request.getSession()
					.getAttribute(GlobalStatic.tokenKey));
			return redirect + "/system/index";
		}
		// 默认赋值message,避免freemarker尝试从session取值,造成异常
		model.addAttribute("message", "");
		String url = request.getParameter("gotourl");
		if (StringUtils.isNotBlank(url)) {
			model.addAttribute("gotourl", url);
		}
		return "/system/login";
	}

	/**
	 * 处理登录提交的方法
	 * 
	 * @param currUser
	 *            自动封装当前登录人的 账号,密码,验证码
	 * @param session
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost(User currUser, HttpSession session, Model model,
			HttpServletRequest request) throws Exception {
		Subject user = SecurityUtils.getSubject();
		// 系统产生的验证码
		String code = (String) session
				.getAttribute(GlobalStatic.DEFAULT_CAPTCHA_PARAM);

		session.removeAttribute(GlobalStatic.DEFAULT_CAPTCHA_PARAM);

		String systemSiteId = request.getParameter("systemSiteId");
		if (StringUtils.isNotBlank(systemSiteId)) {
			model.addAttribute("systemSiteId", systemSiteId);
		}

		if (StringUtils.isNotBlank(code)) {
			code = code.toLowerCase();
		}
		// 用户产生的验证码
		String submitCode = WebUtils.getCleanParam(request,
				GlobalStatic.DEFAULT_CAPTCHA_PARAM);
		if (StringUtils.isNotBlank(submitCode)) {
			submitCode = submitCode.toLowerCase();
		}
		String gotourl = request.getParameter("gotourl");
		//验证码
		String yzm=request.getParameter("showyzm");
		if(StringUtils.isNotBlank(yzm)){
			model.addAttribute("showyzm",yzm);
		}
		 /**验证失败次数大于等于2次就显示验证码 然后进行验证码校验**/
		 Cache cache = cacheManager.getCache(GlobalStatic.springrainloginCacheKey);
		 Integer errorLogincount=cache.get(currUser.getAccount(), Integer.class);
		 if(errorLogincount!=null&&errorLogincount>=ERROR_TIME_SHOWYZM){ 
			 // 如果验证码不匹配,跳转到登录
			 if (StringUtils.isBlank(submitCode) || StringUtils.isBlank(code)
					 || !code.equals(submitCode)) {
				 model.addAttribute("message", "验证码错误!");
				 model.addAttribute("showyzm", "yes"); 
				 return "/system/login";
			 }
		 }
		
		// 通过账号和密码获取 UsernamePasswordToken token
		FrameAuthenticationToken token = new FrameAuthenticationToken(
				currUser.getAccount(), currUser.getPassword());

		String rememberme = request.getParameter("rememberme");
		if (StringUtils.isNotBlank(rememberme)) {
			token.setRememberMe(true);
		} else {
			token.setRememberMe(false);
		}

		try {
			// 会调用 shiroDbRealm 的认证方法
			// org.springrain.frame.shiro.ShiroDbRealm.doGetAuthenticationInfo(AuthenticationToken)

			user.login(token);
		} catch (UnknownAccountException e) {
			logger.error(e.getMessage(), e);
			model.addAttribute("message", "账号不存在!");
			if (StringUtils.isNotBlank(gotourl)) {
				model.addAttribute("gotourl", gotourl);
			}
			return "/system/login";
		} catch (IncorrectCredentialsException e) { 
			logger.error(e.getMessage(), e);
			model.addAttribute("message", "密码错误!");
			 /*********密码错误次数大于等于2次显示验证码********/
			 if(errorLogincount!=null&&errorLogincount>=ERROR_TIME_SHOWYZM-1){
				 model.addAttribute("showyzm", "yes");
			 }
			 /******************/
			if (StringUtils.isNotBlank(gotourl)) {
				model.addAttribute("gotourl", gotourl);
			}
			return "/system/login";
		} catch (LockedAccountException e) {
			logger.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
			if (StringUtils.isNotBlank(gotourl)) {
				model.addAttribute("gotourl", gotourl);
			}
			return "/system/login";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			model.addAttribute("message", "未知错误,请联系管理员.");
			if (StringUtils.isNotBlank(gotourl)) {
				model.addAttribute("gotourl", gotourl);
			}
			return "/system/login";
		}

		if (StringUtils.isBlank(gotourl)) {
			gotourl = "/system/index";
		}
		// 设置tokenkey
		String springraintoken = "system_" + SecUtils.getUUID();
		session.setAttribute(GlobalStatic.tokenKey, springraintoken);
		model.addAttribute(GlobalStatic.tokenKey, springraintoken);

		return redirect + gotourl;
	}

	/**
	 * 退出
	 * 
	 * @param request
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			subject.logout();
		}
		return redirect + "/system/login";
	}

	/**
	 * 打开微信校验页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/wxcheck")
	public String wxcheck(HttpServletRequest request) {
		request.setAttribute("springraintoken",
				request.getParameter("springraintoken"));
		request.setAttribute("msg", request.getParameter("msg"));
		// 跳转地址
		String tourl = SiteUtils.getBaseURL(request)
				+ "/system/wxcheck/result?"+GlobalStatic.WXCHECK_USERID+"=" + SessionUser.getUserId();
		String siteId = "1";
		String oathurl = SiteUtils.getBaseURL(request) + "/mp/mpautologin/"
				+ siteId + "/oauth2";
		//request.setAttribute("wxurl", oathurl + "?url=" + tourl); 
		String wxurl=oathurl + "?url=" + tourl;
		String rootpath=request.getSession().getServletContext().getRealPath("/");
		String picpath="/upload/qrpic/"+SecUtils.getUUID()+".jpg";
		File qrFile=new File(rootpath+picpath);
		if(!qrFile.exists()){
			qrFile.mkdirs();
		}
		try {
			//生成二维码
			QrCodeUtils.genQrCode(wxurl, qrFile); 
			request.setAttribute("qrimgurl", picpath);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return "/system/user/wxcheck";
	}

	/**
	 * ajax轮询查询是否已扫描
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ajax/wxcheck")
	@ResponseBody
	public ReturnDatas ajaxwxcheck(HttpServletRequest request, Model model)
			throws Exception {
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		Object openId = userService.getCache(GlobalStatic.CACHEID_WXLOGINCHECK).get(
				SessionUser.getUserId(),String.class);
		returnObject.setData(openId);
		return returnObject;
	}

	/**
	 * 显示微信扫描结果
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/wxcheck/result")
	public String wxcheckmore(HttpServletRequest request) {
		try {
			String suid = request.getParameter(GlobalStatic.WXCHECK_USERID);
			Object openId = request.getSession().getAttribute(GlobalStatic.SESSION_OPENID);
			if (StringUtils.isNotBlank(suid)) {
				User u = userService.findById(suid, User.class);
				if (StringUtils.isBlank(u.getWeixinId())) {
					request.setAttribute("msg", "success");
				} else {
					if (openId.equals(u.getWeixinId())) {
						request.setAttribute("msg", "success");
					} else {
						request.setAttribute("msg", "error");
					}
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/system/user/wxcheckresult";
	}
	
	
	/**
	 * ajax发送加密字符串给员工邮箱
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/ajax/sendpwdbymail")
	@ResponseBody
	public ReturnDatas sendpwdbymail(HttpServletRequest request, Model model, HttpSession session) {
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		try {
			// 系统产生的验证码
			String code = (String) session
					.getAttribute(GlobalStatic.DEFAULT_CAPTCHA_PARAM);
			session.removeAttribute(GlobalStatic.DEFAULT_CAPTCHA_PARAM);
			// 用户产生的验证码
			String submitCode = WebUtils.getCleanParam(request,
					GlobalStatic.DEFAULT_CAPTCHA_PARAM);
			if (StringUtils.isNotBlank(submitCode)) {
				submitCode = submitCode.toLowerCase();
			}
			 // 如果验证码不匹配,跳转到登录
			 if (StringUtils.isBlank(submitCode) || StringUtils.isBlank(code)
					 || !code.equals(submitCode)) {
					returnObject.setStatus(ReturnDatas.ERROR);
					returnObject.setMessage("验证码错误");
					return returnObject;
			 }
			
			//账号校验
			String account=request.getParameter("account");
			if(StringUtils.isBlank(account)){
				returnObject.setStatus(ReturnDatas.ERROR);
				returnObject.setMessage("账号不能为空");
				return returnObject;
			}
			 
			User u=userService.findUserByAccount(account);
			if(u==null){
				returnObject.setStatus(ReturnDatas.ERROR);
				returnObject.setMessage("输入的账号不存在");
				return returnObject;
			}
			
			String strIn=SecUtils.getUUID()+","+account+","+String.valueOf(new Date().getTime());
			String strSec=new DesSecUtils().encrypt(strIn); 
			//放入缓存
			userService.cleanCache(BACKPWDSTRSEC); 
			userService.putByCache(BACKPWDSTRSEC, account, strSec);
			
			String linkurl=SiteUtils.getBaseURL(request)+"/system/backpwdchange?checkstr="+strSec;
			//发送邮件
			Map map = new HashMap();
			map.put("linkurl", linkurl);
			map.put("timestr", DateUtils.convertDate2String("yyyy-MM-dd HH:mm:ss", new Date()));
			
		} catch (Exception e) {
			returnObject.setStatus(ReturnDatas.ERROR);
			logger.error(e.getMessage(),e);
		}
		return returnObject;
	}
	
	
	/**
	 * ajax更新用户密码
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ajax/changpassword")
	@ResponseBody
	public ReturnDatas changpassword(HttpServletRequest request, Model model,HttpSession session) {
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		try {	
			Object currentaccount=session.getAttribute("backpwd_current_account");
			if(currentaccount==null){
				returnObject.setStatus(ReturnDatas.ERROR);
				returnObject.setMessage("当前账号已超时，请重新找回密码！");
				return returnObject;
		    }
			String account=currentaccount.toString();
			
			String newpwd=request.getParameter("newpwd");
			if(StringUtils.isBlank(newpwd)){
				returnObject.setStatus(ReturnDatas.ERROR);
				returnObject.setMessage("密码不能为空！");
				return returnObject;
			}
		    //修改密码
			User u=userService.findUserByAccount(account);
			if(u==null){
				returnObject.setStatus(ReturnDatas.ERROR);
				returnObject.setMessage("账号信息错误！");
				return returnObject;
			}
			u.setPassword(SecUtils.encoderByMd5With32Bit(newpwd));
			userService.update(u);
			//移除账号信息
			userService.cleanCache(BACKPWDSTRSEC); 
			session.removeAttribute("backpwd_current_account");
			
		} catch (Exception e) {
			returnObject.setStatus(ReturnDatas.ERROR);
			logger.error(e.getMessage(),e);
		}
		return returnObject;
	}
	
	/**
	 * 找回密码
	 * 
	 * @param request
	 */
	@RequestMapping(value = "/backpwd")
	public String backpwd(HttpServletRequest request) {
		return "/system/user/backpwd";
	}
	
	/**
	 * 修改密码页面
	 * 
	 * @param request
	 */
	@RequestMapping(value = "/backpwdchange")
	public String backpwdchange(HttpServletRequest request, Model model,HttpSession session) {
		String pageurl="/system/user/backpwdchange";
		try {
			String checkstr=request.getParameter("checkstr");
			if(StringUtils.isBlank(checkstr)){
				model.addAttribute("validateError", "非法请求：链接地址不完整！");
				return pageurl;
			}
			String useraccount=null;
			String timestr=null;
			String desstr=new DesSecUtils().decrypt(checkstr);
			if(StringUtils.isNotBlank(desstr)){
				String[] ss=desstr.split(",");
				if(ss!=null&&ss.length==3){
					useraccount=ss[1];
					timestr=ss[2];
				}
			}
			if(StringUtils.isBlank(useraccount)){
				model.addAttribute("validateError", "非法请求：账号信息不正确！");
				return pageurl;
			}
			
			//校验码是否正确
			String sessionstr =userService.getByCache(BACKPWDSTRSEC, useraccount, String.class);
			if(sessionstr!=null&&sessionstr.toString().equals(checkstr)){
				if(new Date().getTime()-NumberUtils.toLong(timestr)>5*60*1000){
					model.addAttribute("validateError", "链接时间超时！");
					return pageurl;
				}
			}else{
				model.addAttribute("validateError", "非法请求：验证码不正确或链接地址已失效！");
				return pageurl;
			}
			
			session.setAttribute("backpwd_current_account", useraccount);
			model.addAttribute("currentaccount", useraccount);
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return pageurl;
	}

}
