package  org.springrain.cms.web.s;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springrain.cms.entity.CmsChannel;
import org.springrain.cms.service.ICmsChannelService;
import org.springrain.cms.service.ICmsLinkService;
import org.springrain.cms.service.ICmsSiteService;
import org.springrain.cms.service.ICmsTemplateService;
import org.springrain.frame.common.SessionUser;
import org.springrain.frame.util.GlobalStatic;
import org.springrain.frame.util.Page;
import org.springrain.frame.util.ReturnDatas;
import org.springrain.frame.util.property.MessageUtils;


/**
 * TODO 在此加入类描述
 * @copyright {@link weicms.net}
 * @author springrain<Auto generate>
 * @version  2017-01-11 11:12:18
 * @see org.springrain.cms.entity.CmsChannel
 */
@Controller
@RequestMapping(value="/s/channel/{siteId}/{businessId}")
public class ChannelController  extends SiteBaseController {
	@Resource
	private ICmsChannelService cmsChannelService;
	@Resource
	private ICmsSiteService cmsSiteService;
	@Resource
	private ICmsLinkService cmsLinkService;
	
	@Resource
	private ICmsTemplateService cmsTemplateService;
	
	
	
	
	   
	/**
	 * 列表数据,调用listjson方法,保证和app端数据统一
	 * 
	 * @param request
	 * @param model
	 * @param cmsChannel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model,CmsChannel cmsChannel,@PathVariable String siteId,@PathVariable String businessId) 
			throws Exception {
		ReturnDatas returnObject = listjson(request, model, cmsChannel,siteId,businessId);
		model.addAttribute(GlobalStatic.returnDatas, returnObject);
		return "/u/"+siteId+"/s/channelList";
		//return jump(siteId, businessId,Enumerations.CmsLinkModeType.站长后台列表.getType(), request, model);
	}
	
	/**
	 * json数据,为APP提供数据
	 * 
	 * @param request
	 * @param model
	 * @param cmsChannel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list/json")
	@ResponseBody 
	public ReturnDatas listjson(HttpServletRequest request, Model model,CmsChannel cmsChannel,@PathVariable String siteId,@PathVariable String businessId) throws Exception{
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		Page page=newPage(request);
		List<CmsChannel> datas = cmsChannelService.findChanelByCmsChannel(siteId, businessId,cmsChannel,page);
		model.addAttribute("siteId",siteId);
		model.addAttribute("businessId", businessId);
		returnObject.setPage(page);
		returnObject.setData(datas);
		returnObject.setQueryBean(cmsChannel);
		return returnObject;
	}
		/**
	 * 查看操作,调用APP端lookjson方法
	 */
	@RequestMapping(value = "/look")
	public String look(Model model,HttpServletRequest request,HttpServletResponse response,@PathVariable String siteId,@PathVariable String businessId)  throws Exception {
		ReturnDatas returnObject = lookjson(model, request, response,siteId,businessId);
		model.addAttribute(GlobalStatic.returnDatas, returnObject);
		return "/u/"+siteId+"/s/channelLook";
		//return jump(siteId,businessId,Enumerations.CmsLinkModeType.站长后台查看.getType(),request,model);
	}

	
	/**
	 * 查看的Json格式数据,为APP端提供数据
	 */
	@RequestMapping(value = "/look/json")
	@ResponseBody 
	public ReturnDatas lookjson(Model model,HttpServletRequest request,HttpServletResponse response,@PathVariable String siteId,@PathVariable String businessId) throws Exception {
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		java.lang.String id=request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
		  CmsChannel cmsChannel = cmsChannelService.findCmsChannelById(id);
		   returnObject.setData(cmsChannel);
		}else{
		returnObject.setStatus(ReturnDatas.ERROR);
		}
		return returnObject;
		
	}
	
	
	/**
	 * 新增/修改 操作吗,返回json格式数据
	 * 
	 */
	@RequestMapping("/update")
	@ResponseBody 
	public ReturnDatas saveorupdate(Model model,CmsChannel cmsChannel,HttpServletRequest request,HttpServletResponse response,@PathVariable String siteId,@PathVariable String businessId) throws Exception{
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		returnObject.setMessage(MessageUtils.UPDATE_SUCCESS);
		try {
			cmsChannel.setSiteId(siteId);
			cmsChannel.setPid(businessId);
			cmsChannel.setPositionLevel(2);
			
			
			java.lang.String id =cmsChannel.getId();
			if(StringUtils.isBlank(id)){
			  cmsChannel.setId(null);
			}
			
			cmsChannelService.saveorupdate(cmsChannel);
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			returnObject.setStatus(ReturnDatas.ERROR);
			returnObject.setMessage(MessageUtils.UPDATE_ERROR);
		}
		return returnObject;
	
	}
	/**
	 * 进入修改页面,APP端可以调用 lookjson 获取json格式数据
	 */
	@RequestMapping(value = "/update/pre")
	public String updatepre(Model model,HttpServletRequest request,HttpServletResponse response,@PathVariable String siteId,@PathVariable String businessId)  throws Exception{
		ReturnDatas returnObject = lookjson(model, request, response,siteId,businessId);
		Map<String, Object> map = new HashMap<>();
		map.put("siteList", cmsSiteService.findSiteByUserId(SessionUser.getUserId()));
		returnObject.setMap(map);
		model.addAttribute(GlobalStatic.returnDatas, returnObject);
		
		model.addAttribute("siteId", siteId);
		model.addAttribute("businessId", businessId);
		return "/u/"+siteId+"/s/channelCru";
		//return jump(siteId,businessId,Enumerations.CmsLinkModeType.站长后台更新.getType(),request,model);
		
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping(value="/delete")
	@ResponseBody 
	public  ReturnDatas delete(HttpServletRequest request,@PathVariable String siteId,@PathVariable String businessId) throws Exception {

			// 执行删除
		try {
		java.lang.String id=request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
				CmsChannel findById = cmsChannelService.findById(id, CmsChannel.class);
				findById.setActive(0);
				cmsChannelService.update(findById);
				return new ReturnDatas(ReturnDatas.SUCCESS,
						MessageUtils.DELETE_SUCCESS);
			} else {
				return new ReturnDatas(ReturnDatas.WARNING,
						MessageUtils.DELETE_WARNING);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new ReturnDatas(ReturnDatas.WARNING, MessageUtils.DELETE_WARNING);
	}
	
}
