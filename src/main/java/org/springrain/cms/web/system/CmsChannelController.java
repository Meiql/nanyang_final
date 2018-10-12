package  org.springrain.cms.web.system;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springrain.cms.entity.CmsChannel;
import org.springrain.cms.entity.CmsLink;
import org.springrain.cms.entity.CmsSite;
import org.springrain.cms.entity.CmsTemplate;
import org.springrain.cms.service.ICmsChannelService;
import org.springrain.cms.service.ICmsLinkService;
import org.springrain.cms.service.ICmsSiteService;
import org.springrain.cms.service.ICmsTemplateService;
import org.springrain.frame.common.SessionUser;
import org.springrain.frame.controller.BaseController;
import org.springrain.frame.util.Enumerations;
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
@RequestMapping(value="/system/cms/channel")
public class CmsChannelController  extends BaseController {
	@Resource
	private ICmsChannelService cmsChannelService;
	@Resource
	private ICmsSiteService cmsSiteService;
	@Resource
	private ICmsLinkService cmsLinkService;
	
	@Resource
	private ICmsTemplateService cmsTemplateService;
	private String listurl="/cms/channel/channelList";
	
	
	
	
	   
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
	public String list(HttpServletRequest request, Model model,CmsChannel cmsChannel) 
			throws Exception {
		ReturnDatas returnObject = listjson(request, model, cmsChannel);
		model.addAttribute(GlobalStatic.returnDatas, returnObject);
		return listurl;
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
	public ReturnDatas listjson(HttpServletRequest request, Model model,CmsChannel cmsChannel) throws Exception{
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		List<CmsChannel> treeList = new ArrayList<>();
		//查询站点列表
		Page page = newPage(request);
		List<CmsSite> siteTreeList = cmsSiteService.findListDataByFinder(null, page, CmsSite.class, cmsChannel);
		for (CmsSite cmsSite : siteTreeList) {
			String siteId = cmsSite.getId();
			List<CmsChannel> channelList = cmsChannelService.findTreeByPid(null, siteId);
			if (channelList != null){
				treeList.addAll(channelList);
				for (CmsChannel channel : channelList) {
					if(StringUtils.isBlank(channel.getPid()))
						channel.setPid(siteId);
				}
			}
			
			//将site转为channel
			CmsChannel tmpChannel = new CmsChannel(siteId);
			tmpChannel.setName(cmsSite.getName());
			treeList.add(tmpChannel);
		}
		returnObject.setData(treeList);
		return returnObject;
	}
	
	@RequestMapping("/list/export")
	public void listexport(HttpServletRequest request,HttpServletResponse response, Model model,CmsChannel cmsChannel) throws Exception{
		// ==构造分页请求
		Page page = newPage(request);
	
		File file = cmsChannelService.findDataExportExcel(null,listurl, page,CmsChannel.class,cmsChannel);
		String fileName="cmsChannel"+GlobalStatic.excelext;
		downFile(response, file, fileName,true);
		return;
	}
	
		/**
	 * 查看操作,调用APP端lookjson方法
	 */
	@RequestMapping(value = "/look")
	public String look(Model model,HttpServletRequest request,HttpServletResponse response)  throws Exception {
		ReturnDatas returnObject = lookjson(model, request, response);
		model.addAttribute(GlobalStatic.returnDatas, returnObject);
		return "/cms/channel/channelLook";
	}

	
	/**
	 * 查看的Json格式数据,为APP端提供数据
	 */
	@RequestMapping(value = "/look/json")
	@ResponseBody 
	public ReturnDatas lookjson(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
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
	public ReturnDatas saveorupdate(Model model,CmsChannel cmsChannel,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		returnObject.setMessage(MessageUtils.UPDATE_SUCCESS);
		try {
		
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
	 * 新增/修改 操作吗,返回json格式数据
	 * 
	 */
	@RequestMapping("/updateCmsLinks")
	@ResponseBody 
	public ReturnDatas updateCmsLinks(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		returnObject.setMessage(MessageUtils.UPDATE_SUCCESS);
		try {
			String businessId = request.getParameter("businessId");
			String siteId = request.getParameter("siteId");
			
			//找站点
			CmsSite cmsSite = cmsSiteService.findById(siteId, CmsSite.class);
			
			//此处可再次进行数据权限审核，待写。
			//可根据当前人的部门，是否有对站点及栏目修改的权限
			//待写
			if(cmsSite==null){
				returnObject.setStatus(ReturnDatas.ERROR);
				returnObject.setMessage(MessageUtils.UPDATE_ERROR);
				return returnObject;
			}
			//PC    后台模板指定
			String pc_ftlListPath_id=request.getParameter("pc_ftlListPath_id");//后台列表模板路径
			String pc_ftlUpdatePath_id=request.getParameter("pc_ftlUpdatePath_id");//后台更新模板路径
			String pc_ftlLookPath_id=request.getParameter("pc_ftlLookPath_id");//后台查看模板路径
			String pc_ftlListPath=request.getParameter("pc_ftlListPath");//后台列表模板路径
			String pc_ftlUpdatePath=request.getParameter("pc_ftlUpdatePath");//后台更新模板路径
			String pc_ftlLookPath=request.getParameter("pc_ftlLookPath");//后台查看模板路径
			//pc   前台
			String pc_childFtl=request.getParameter("pc_childFtl");//前台栏目子类继承路径（默认和前台列表一致）
			String pc_frontFtl=request.getParameter("pc_frontFtl");//前台列表模板路径
			String pc_frontFtl_id=request.getParameter("pc_frontFtl_id");//前台列表模板路径
			String pc_frontDomFtl=request.getParameter("pc_frontDomFtl");//前台列表DOM模板路径
			String pc_frontDomFtl_id=request.getParameter("pc_frontDomFtl_id");//前台列表DOM模板路径
			String pc_nodeftlPath=request.getParameter("pc_nodeftlPath");//添加内容(子节点)时前台访问详情模板
			String pc_jumpType=request.getParameter("pc_jumpType");//前台跳转方式
			String pc_link=request.getParameter("pc_link");//前台访问链接
			String pc_loginuser=request.getParameter("pc_loginuser");//登陆访问
			
			String pc_update=request.getParameter("pc_update");//是否更新旧内容
			String wap_update=request.getParameter("wap_update");//是否更新旧内容
			
			//wap  如果是wap的话，此处就为空喽nothing serious
			String wap_childFtl=request.getParameter("wap_childFtl");//前台栏目子类继承路径（默认和前台列表一致）
			String wap_frontFtl=request.getParameter("wap_frontFtl");//前台列表模板路径
			String wap_frontFtl_id=request.getParameter("wap_frontFtl_id");//前台列表模板路径
			String wap_frontDomFtl=request.getParameter("wap_frontDomFtl");//前台列表DOM模板路径
			String wap_frontDomFtl_id=request.getParameter("wap_frontDomFtl_id");//前台列表DOM模板路径
			String wap_nodeftlPath=request.getParameter("wap_nodeftlPath");//添加内容(子节点)时前台访问详情模板
			String wap_jumpType=request.getParameter("wap_jumpType");//前台跳转方式
			String wap_link=request.getParameter("wap_link");//前台访问链接
			String wap_loginuser=request.getParameter("wap_loginuser");//登陆访问
			List<CmsTemplate> cmsTemplates=cmsTemplateService.findTplsBySiteIdAndThemeType(siteId, null);
			List<CmsLink> cmsLinks=new ArrayList<CmsLink>();
			CmsLink tempCmsLink=null;
			if(CollectionUtils.isNotEmpty(cmsTemplates)){
				if(CollectionUtils.isNotEmpty(cmsTemplates)){
					for(CmsTemplate e:cmsTemplates){
						if(e.getModelType()==Enumerations.CmsLinkModeType.前台.getType()||e.getModelType()==Enumerations.CmsLinkModeType.站长后台.getType()
			    				||e.getModelType()==Enumerations.CmsLinkModeType.前台内容.getType()||e.getModelType()==Enumerations.CmsLinkModeType.前台栏目继承页.getType()){
			    			continue;
			    		}
						if(e.getThemeType()==Enumerations.ThemeSiteType.pc.getType()&&e.getModelType()==Enumerations.CmsLinkModeType.站长后台列表.getType()){
							tempCmsLink=new CmsLink(pc_ftlListPath_id,pc_ftlListPath,Enumerations.CmsLinkModeType.站长后台列表.getType(),Enumerations.ThemeSiteType.pc.getType());
						}
						if(e.getThemeType()==Enumerations.ThemeSiteType.pc.getType()&&e.getModelType()==Enumerations.CmsLinkModeType.站长后台更新.getType()){
							tempCmsLink=new CmsLink(pc_ftlUpdatePath_id,pc_ftlUpdatePath,Enumerations.CmsLinkModeType.站长后台更新.getType(),Enumerations.ThemeSiteType.pc.getType());
						}
						if(e.getThemeType()==Enumerations.ThemeSiteType.pc.getType()&&e.getModelType()==Enumerations.CmsLinkModeType.站长后台查看.getType()){
							tempCmsLink=new CmsLink(pc_ftlLookPath_id,pc_ftlLookPath,Enumerations.CmsLinkModeType.站长后台查看.getType(),Enumerations.ThemeSiteType.pc.getType());
						}
						if(e.getThemeType()==Enumerations.ThemeSiteType.pc.getType()&&e.getModelType()==Enumerations.CmsLinkModeType.前台栏目.getType()){
							tempCmsLink=new CmsLink(pc_frontFtl_id,pc_frontFtl,Enumerations.CmsLinkModeType.前台栏目.getType(),Enumerations.ThemeSiteType.pc.getType());
							tempCmsLink.setChildftlfile(pc_childFtl);
							tempCmsLink.setNodeftlfile(pc_nodeftlPath);
							tempCmsLink.setJumpType(Integer.valueOf(pc_jumpType));
							tempCmsLink.setLoginuser(Integer.valueOf(pc_loginuser));
							tempCmsLink.setLink(pc_link);
							if(StringUtils.isNoneBlank(pc_update)){
								tempCmsLink.setUpdateOld(1);
							}else{
								tempCmsLink.setUpdateOld(0);
							}
							
						}
						if(e.getThemeType()==Enumerations.ThemeSiteType.pc.getType()&&e.getModelType()==Enumerations.CmsLinkModeType.前台栏目DOM.getType()){
							tempCmsLink=new CmsLink(pc_frontDomFtl_id,pc_frontDomFtl,Enumerations.CmsLinkModeType.前台栏目DOM.getType(),Enumerations.ThemeSiteType.pc.getType());
						}
						if(e.getThemeType()==Enumerations.ThemeSiteType.mp.getType()&&e.getModelType()==Enumerations.CmsLinkModeType.前台栏目.getType()){
							tempCmsLink=new CmsLink(wap_frontFtl_id,wap_frontFtl,Enumerations.CmsLinkModeType.前台栏目.getType(),Enumerations.ThemeSiteType.mp.getType());
							tempCmsLink.setChildftlfile(wap_childFtl);
							tempCmsLink.setNodeftlfile(wap_nodeftlPath);
							tempCmsLink.setJumpType(Integer.valueOf(wap_jumpType));
							tempCmsLink.setLoginuser(Integer.valueOf(wap_loginuser));
							tempCmsLink.setLink(wap_link);
							if(StringUtils.isNoneBlank(wap_update)){
								tempCmsLink.setUpdateOld(1);
							}else{
								tempCmsLink.setUpdateOld(0);
							}
						}
						if(e.getThemeType()==Enumerations.ThemeSiteType.mp.getType()&&e.getModelType()==Enumerations.CmsLinkModeType.前台栏目DOM.getType()){
							tempCmsLink=new CmsLink(wap_frontDomFtl_id,wap_frontDomFtl,Enumerations.CmsLinkModeType.前台栏目DOM.getType(),Enumerations.ThemeSiteType.mp.getType());
						}
						tempCmsLink.setBusinessId(businessId);
						tempCmsLink.setSiteId(siteId);
						cmsLinks.add(tempCmsLink);
						tempCmsLink=null;
					}
				}
			}
			cmsChannelService.saveLinkCms(cmsLinks,businessId,siteId);//此处用cmslink即可，上面已经用  主题 过滤了一遍了 
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			returnObject.setStatus(ReturnDatas.ERROR);
			returnObject.setMessage(MessageUtils.UPDATE_ERROR+":"+e.getMessage());
		}
		return returnObject;
	
	}
	/**
	 * 新增/修改 操作吗,返回json格式数据
	 * 
	 */
	@RequestMapping("/tplcmslink/update")
	@ResponseBody 
	public ReturnDatas tplcmslinkUpdate(Model model,CmsChannel cmsChannel,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		String siteId = request.getParameter("siteId");
		if(StringUtils.isEmpty(siteId)){
			returnObject.setStatus(ReturnDatas.ERROR);
			return returnObject;
		}
		CmsSite cmsSite=cmsChannelService.findById(siteId, CmsSite.class);
		
		return returnObject;
	
	}
	
	/**
	 * 进入修改页面,APP端可以调用 lookjson 获取json格式数据
	 */
	@RequestMapping(value = "/update/pre")
	public String updatepre(Model model,HttpServletRequest request,HttpServletResponse response)  throws Exception{
		ReturnDatas returnObject = lookjson(model, request, response);
		Map<String, Object> map = new HashMap<>();
		map.put("siteList", cmsSiteService.findSiteByUserId(SessionUser.getUserId()));
		returnObject.setMap(map);
		model.addAttribute(GlobalStatic.returnDatas, returnObject);
		return "/cms/channel/channelCru";
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping(value="/delete")
	@ResponseBody 
	public  ReturnDatas delete(HttpServletRequest request) throws Exception {

			// 执行删除
		try {
		java.lang.String id=request.getParameter("id");
		java.lang.String siteId=request.getParameter("siteId");
		if(StringUtils.isNotBlank(id)&StringUtils.isNoneBlank(siteId)){
				cmsChannelService.deleteByIdAndSiteId(id,siteId);
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
	
	/**
	 * 删除多条记录
	 * 
	 */
	@RequestMapping("/delete/more")
	@ResponseBody 
	public ReturnDatas deleteMore(HttpServletRequest request, Model model) {
		String records = request.getParameter("records");
		if(StringUtils.isBlank(records)){
			 return new ReturnDatas(ReturnDatas.ERROR,
					MessageUtils.DELETE_ALL_FAIL);
		}
		String[] rs = records.split(",");
		if (rs == null || rs.length < 1) {
			return new ReturnDatas(ReturnDatas.ERROR,
					MessageUtils.DELETE_NULL_FAIL);
		}
		try {
			List<String> ids = Arrays.asList(rs);
			cmsChannelService.deleteByIds(ids,CmsChannel.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ReturnDatas(ReturnDatas.ERROR,
					MessageUtils.DELETE_ALL_FAIL);
		}
		return new ReturnDatas(ReturnDatas.SUCCESS,
				MessageUtils.DELETE_ALL_SUCCESS);
	}
}
