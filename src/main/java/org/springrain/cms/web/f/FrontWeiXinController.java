package org.springrain.cms.web.f;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springrain.cms.service.ICmsContentService;
import org.springrain.frame.util.Enumerations;
import org.springrain.frame.util.Enumerations.OrgType;
import org.springrain.frame.util.GlobalStatic;
import org.springrain.frame.util.Page;
import org.springrain.frame.util.ReturnDatas;


@Controller
@RequestMapping(value="/f/mp/{siteId}")
public class FrontWeiXinController extends FrontBaseController {
	
	@Resource
	private ICmsContentService cmsContentService;

	@RequestMapping("/index")
	public  String index(@PathVariable String siteId,HttpServletRequest request, Model model) 
			throws Exception {
		
	  return jump(siteId, siteId, OrgType.mp.name(), request, model,Enumerations.CmsLinkModeType.前台.getType(),Enumerations.ThemeSiteType.mp.getType());
			
	}
	
	/**
	 * 映射栏目页面
	 * @throws Exception 
	 * */
	@RequestMapping("/{businessId}")
	public String channel(@PathVariable String siteId,@PathVariable String businessId,HttpServletRequest request,Model model,Map<String, Object> params) throws Exception{
		ReturnDatas returnDatas = ReturnDatas.getSuccessReturnDatas();
		Page page = newPage(request);
		returnDatas.setPage(page);
		returnDatas.setQueryBean(params);
		model.addAttribute(GlobalStatic.returnDatas, returnDatas);
		Integer modelType=Enumerations.CmsLinkModeType.前台栏目.getType();
		if(businessId.startsWith("c_")){
			modelType=Enumerations.CmsLinkModeType.前台内容.getType();
		}
		return jump(siteId, businessId, OrgType.mp.name(), request, model,modelType,Enumerations.ThemeSiteType.mp.getType());
	}
	
	/**
	 * 映射栏目页面
	 * @throws Exception 
	 * */
	@RequestMapping("/{businessId}/dom")
	public String channelDom(@PathVariable String siteId,@PathVariable String businessId,HttpServletRequest request,Model model,Map<String, Object> params) throws Exception{
		ReturnDatas returnDatas = ReturnDatas.getSuccessReturnDatas();
		Page page = newPage(request);
		returnDatas.setPage(page);
		returnDatas.setQueryBean(params);
		model.addAttribute(GlobalStatic.returnDatas, returnDatas);
		// 额外参数
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		Enumeration<String> keyNames = request.getParameterNames();
		while(keyNames.hasMoreElements()){
			String key = keyNames.nextElement();
			paramsMap.put(key, request.getParameter(key));
		}
		model.addAttribute("paramsMap", paramsMap);
		return jump(siteId, businessId, OrgType.mp.name(), request, model,Enumerations.CmsLinkModeType.前台栏目DOM.getType(),Enumerations.ThemeSiteType.mp.getType());
	}
}
