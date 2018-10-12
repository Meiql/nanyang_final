package  org.springrain.cms.web.system;

import java.io.File;
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
import org.springrain.cms.entity.CmsTemplate;
import org.springrain.cms.entity.CmsTheme;
import org.springrain.cms.service.ICmsLinkService;
import org.springrain.cms.service.ICmsTemplateService;
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
 * @version  2017-04-12 14:55:49
 * @see org.springrain.cms.base.web.CmsTemplate
 */
@Controller
@RequestMapping(value="/system/template")
public class CmsTemplateController  extends BaseController {
	@Resource
	private ICmsTemplateService cmsTemplateService;
	@Resource
	private ICmsLinkService cmsLinkService;
	private String listurl="/cms/template/templateList";
	
	
	   
	/**
	 * 列表数据,调用listjson方法,保证和app端数据统一
	 * 
	 * @param request
	 * @param model
	 * @param cmsTemplate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model,CmsTemplate cmsTemplate) 
			throws Exception {
		ReturnDatas returnObject = listjson(request, model, cmsTemplate);
		model.addAttribute(GlobalStatic.returnDatas, returnObject);
		return listurl;
	}
	
	/**
	 * json数据,为APP提供数据
	 * 
	 * @param request
	 * @param model
	 * @param cmsTemplate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ajax/tpl")
	@ResponseBody   
	public  ReturnDatas loadTpl(HttpServletRequest request, Model model,CmsTemplate cmsTemplate) throws Exception{
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		String chanelId = request.getParameter("chanelId");
		String siteId = request.getParameter("siteId");
		//手机端链接
		List<CmsTemplate> wapdatas=cmsTemplateService.findTplsBySiteIdAndThemeType(siteId, Enumerations.ThemeSiteType.mp.getType());
		//PC端链接
		List<CmsTemplate> pcdatas=cmsTemplateService.findTplsBySiteIdAndThemeType(siteId, Enumerations.ThemeSiteType.pc.getType());
		if(CollectionUtils.isNotEmpty(wapdatas)){
			//找是的的cmslink
			if(CollectionUtils.isNotEmpty(wapdatas)){
				for(CmsTemplate e:wapdatas){
					e.setCmsLink(cmsLinkService.findLinkBySiteBusinessIdModelType(siteId, chanelId, e.getModelType(),e.getThemeType()));
				}
			}
		}
		if(CollectionUtils.isNotEmpty(pcdatas)){
			//找是的的cmslink
			if(CollectionUtils.isNotEmpty(pcdatas)){
				for(CmsTemplate e:pcdatas){
					e.setCmsLink(cmsLinkService.findLinkBySiteBusinessIdModelType(siteId, chanelId, e.getModelType(),e.getThemeType()));
				}
			}
		}	
		Map<String, Object> maps=new HashMap<String,Object>();
		maps.put("wap", wapdatas);
		maps.put("pc", pcdatas);
		returnObject.setMap(maps);
		return returnObject;
	}
	/**
	 * json数据,为APP提供数据
	 * 
	 * @param request
	 * @param model
	 * @param cmsTemplate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list/json")
	@ResponseBody   
	public  ReturnDatas listjson(HttpServletRequest request, Model model,CmsTemplate cmsTemplate) throws Exception{
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		// ==构造分页请求
		Page page = newPage(request);
		// ==执行分页查询
		cmsTemplate.setFrameTableAlias("tpl");
		List<CmsTemplate> datas=cmsTemplateService.findListDataByFinder(null,page,CmsTemplate.class,cmsTemplate);
		CmsTheme cmsTheme = cmsTemplateService.findById(cmsTemplate.getThemeId(), CmsTheme.class);
		if(cmsTheme!=null){
			cmsTemplate.setThemeName(cmsTheme.getName());
		}
		returnObject.setQueryBean(cmsTemplate);
		returnObject.setPage(page);
		returnObject.setData(datas);
		return returnObject;
	}
	@RequestMapping("/list/export")
	public void listexport(HttpServletRequest request,HttpServletResponse response, Model model,CmsTemplate cmsTemplate) throws Exception{
		// ==构造分页请求
		Page page = newPage(request);
	
		File file = cmsTemplateService.findDataExportExcel(null,listurl, page,CmsTemplate.class,cmsTemplate);
		String fileName="cmsTemplate"+GlobalStatic.excelext;
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
		return "/cms/template/templateLook";
	}

	
	/**
	 * 查看的Json格式数据,为APP端提供数据
	 */
	@RequestMapping(value = "/look/json")
	@ResponseBody      
	public ReturnDatas lookjson(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		java.lang.String id=request.getParameter("id");
		String themeId = request.getParameter("themeId");
		CmsTemplate cmsTemplate=new CmsTemplate();
		cmsTemplate.setThemeId(themeId);
		if(StringUtils.isNotBlank(id)){
			cmsTemplate = cmsTemplateService.findCmsTemplateById(id);
		}else{
			returnObject.setStatus(ReturnDatas.ERROR);
		}
		returnObject.setData(cmsTemplate);
		return returnObject;
		
	}
	
	
	/**
	 * 新增/修改 操作吗,返回json格式数据
	 * 
	 */
	@RequestMapping("/update")
	@ResponseBody      
	public ReturnDatas saveorupdate(Model model,CmsTemplate cmsTemplate,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ReturnDatas returnObject = ReturnDatas.getSuccessReturnDatas();
		returnObject.setMessage(MessageUtils.UPDATE_SUCCESS);
		try {
		
			java.lang.String id =cmsTemplate.getId();
			if(StringUtils.isBlank(id)){
			  cmsTemplate.setId(null);
			}
		
			cmsTemplateService.saveorupdate(cmsTemplate);
			
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
	public String updatepre(Model model,HttpServletRequest request,HttpServletResponse response)  throws Exception{
		ReturnDatas returnObject = lookjson(model, request, response);
		model.addAttribute(GlobalStatic.returnDatas, returnObject);
		return "/cms/template/templateCru";
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
		if(StringUtils.isNotBlank(id)){
				cmsTemplateService.deleteById(id,CmsTemplate.class);
				return new ReturnDatas(ReturnDatas.SUCCESS,MessageUtils.DELETE_SUCCESS);
			} else {
				return new ReturnDatas(ReturnDatas.WARNING,MessageUtils.DELETE_WARNING);
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
			 return new ReturnDatas(ReturnDatas.ERROR,MessageUtils.DELETE_ALL_FAIL);
		}
		String[] rs = records.split(",");
		if (rs == null || rs.length < 1) {
			return new ReturnDatas(ReturnDatas.ERROR,MessageUtils.DELETE_NULL_FAIL);
		}
		try {
			List<String> ids = Arrays.asList(rs);
			cmsTemplateService.deleteByIds(ids,CmsTemplate.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ReturnDatas(ReturnDatas.ERROR,MessageUtils.DELETE_ALL_FAIL);
		}
		return new ReturnDatas(ReturnDatas.SUCCESS,MessageUtils.DELETE_ALL_SUCCESS);
		
		
	}

}
