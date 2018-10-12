package org.springrain.cms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springrain.cms.directive.CmsChannelListDirective;
import org.springrain.cms.entity.CmsChannel;
import org.springrain.cms.entity.CmsChannelContent;
import org.springrain.cms.entity.CmsLink;
import org.springrain.cms.entity.CmsSite;
import org.springrain.cms.entity.CmsTemplate;
import org.springrain.cms.service.ICmsChannelService;
import org.springrain.cms.service.ICmsLinkService;
import org.springrain.cms.service.ICmsSiteService;
import org.springrain.cms.service.ICmsTemplateService;
import org.springrain.cms.util.DirectiveUtils;
import org.springrain.frame.util.Enumerations;
import org.springrain.frame.util.Finder;
import org.springrain.frame.util.GlobalStatic;
import org.springrain.frame.util.Page;
import org.springrain.system.service.BaseSpringrainServiceImpl;
import org.springrain.system.service.ITableindexService;

import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;



/**
 * TODO 在此加入类描述
 * @copyright {@link weicms.net}
 * @author springrain<Auto generate>
 * @version  2016-11-10 11:55:17
 * @see org.springrain.cms.entity.demo.service.impl.CmsChannel
 */
@Service("cmsChannelService")
public class CmsChannelServiceImpl extends BaseSpringrainServiceImpl implements ICmsChannelService {

	@Resource
	private ITableindexService tableindexService;
	
	@Resource
	private ICmsLinkService cmsLinkService;
	
	
	@Resource
	private ICmsSiteService cmsSiteService;
	
	@Resource
	private ICmsTemplateService cmsTemplateService;
	
	public void deleteByIdAndSiteId(String  id,String siteId) throws Exception {
		List<CmsChannel> datas = findTreeByPid(id, siteId);
		if(CollectionUtils.isNotEmpty(datas)){
			for(CmsChannel e:datas){
				deleteById(e.getId(), CmsChannel.class);
			}	
		}
		super.deleteById(id, CmsChannel.class);
	}
	@Override
	public Object saveorupdate(Object entity) throws Exception {
		CmsChannel channel = (CmsChannel) entity;
		evictByKey(GlobalStatic.cacheKey, "cmsChannelService_findListDataByFinder");
		
		
		evictByKey(channel.getSiteId(), "cmsChannelService_findTreeByPid_null_"+channel.getSiteId());
		if(StringUtils.isBlank(channel.getId())){
			return this.saveChannel(channel);
		}else{
			return this.updateChannel(channel);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findListDataByFinder(Finder finder, Page page,
			Class<T> clazz, Object queryBean) throws Exception {
		List<CmsChannel> channelList;
		if(page.getPageIndex()==1){
			channelList = getByCache(GlobalStatic.cacheKey, "cmsChannelService_findListDataByFinder", List.class,page);
			if(CollectionUtils.isEmpty(channelList)){
				channelList = super.findListDataByFinder(finder, page, CmsChannel.class, queryBean);
				putByCache(GlobalStatic.cacheKey, "cmsChannelService_findListDataByFinder", channelList,page);
			}
		}else{
			channelList =  super.findListDataByFinder(finder, page, CmsChannel.class, queryBean);
		}
		return (List<T>) channelList;
	}
	
    @Override
	public String  saveChannel(CmsChannel cmsChannel) throws Exception{
    	if(cmsChannel==null){
    		return null;
    	}
        String siteId=cmsChannel.getSiteId();
        if(StringUtils.isBlank(siteId)){
        	return null;
        }
        Integer siteType=cmsSiteService.findSiteTypeById(siteId);
        if(siteType==null){
        	return null;
        }
	    String id= tableindexService.updateNewId(CmsChannel.class);
	    if(StringUtils.isEmpty(id)){
	    	return null;
	    }
	    //2017-4-13 新增栏目时id增加站点后缀 如：h_10009-s_10007
	    id=id+"-"+siteId;
	    cmsChannel.setId(id);
	    //pid和siteId一致设空
	    if(siteId.equals(cmsChannel.getPid())){
	    	cmsChannel.setPid(null);
	    }
	    String _c=	findChannelNewComcode(id, cmsChannel.getPid(),siteId);
	    cmsChannel.setComcode(_c);
	    super.save(cmsChannel);
	    //手机端链接
	    CmsSite site=findById(siteId, CmsSite.class);
	  	List<CmsTemplate> wapdatas=cmsTemplateService.findTplsBySiteIdAndThemeType(siteId, Enumerations.ThemeSiteType.mp.getType());
	  	//PC端链接
	  	List<CmsTemplate> pcdatas=cmsTemplateService.findTplsBySiteIdAndThemeType(siteId, Enumerations.ThemeSiteType.pc.getType());
	  	List<CmsTemplate> alls=new ArrayList<CmsTemplate>();
	  	if(CollectionUtils.isNotEmpty(wapdatas)){
	  		alls.addAll(wapdatas);
	  	}
		if(CollectionUtils.isNotEmpty(pcdatas)){
			alls.addAll(pcdatas);
	  	}
	  	CmsLink cmsLink;
	  	CmsChannel  superCmsChannel;//父类
	  	String pid = cmsChannel.getPid();
	  	if(cmsChannel.getSiteId().equals(pid)||StringUtils.isEmpty(pid)){
	  		superCmsChannel=null;
	  	}else{
	  		superCmsChannel=super.findById(pid, CmsChannel.class);
	  		
	  	}
	    if(CollectionUtils.isNotEmpty(alls)){
	    	for(CmsTemplate e:alls){
	    		if(e.getModelType()==Enumerations.CmsLinkModeType.前台.getType()||e.getModelType()==Enumerations.CmsLinkModeType.站长后台.getType()
	    				||e.getModelType()==Enumerations.CmsLinkModeType.前台内容.getType()||e.getModelType()==Enumerations.CmsLinkModeType.前台栏目继承页.getType()){
	    			continue;
	    		}
	    		cmsLink=new CmsLink();
		    	cmsLink.setModelType(e.getModelType());
		    	cmsLink.setBusinessId(id);
		    	cmsLink.setSiteId(siteId);
		    	cmsLink.setName(cmsChannel.getName());
		    	cmsLink.setJumpType(0);//默认就走ftl，想修改，再回来修改
		    	cmsLink.setLookcount(1);
		    	cmsLink.setStatichtml(0);//默认不静态化
		 	    cmsLink.setActive(1);//默认可以使用
		 	    cmsLink.setSortno(1);
		 	    cmsLink.setLoginuser(0);//默认不用登录 
		 	    
		 	   String _index="";
		 	   if(e.getModelType()==Enumerations.CmsLinkModeType.前台栏目.getType()){
		 		   _index="/f/"+Enumerations.ThemeSiteType.getThemeSiteType(e.getThemeType())+"/"+siteId+"/"+id;
		 		   if(superCmsChannel!=null){
		 			   //父类存在取父类
				 		cmsLink.setChildftlfile(cmsLinkService.findLinkBySiteBusinessIdModelType(siteId, superCmsChannel.getId(), Enumerations.CmsLinkModeType.前台栏目.getType(),e.getThemeType()).getChildftlfile());
				 		cmsLink.setNodeftlfile(cmsLinkService.findLinkBySiteBusinessIdModelType(siteId, superCmsChannel.getId(), Enumerations.CmsLinkModeType.前台栏目.getType(),e.getThemeType()).getNodeftlfile());
		 		   }else{
		 			  	//父类不存在的话，取主题
		 			  	cmsLink.setNodeftlfile("/u/"+siteId+cmsTemplateService.findCmsTemplateByThemeIdAndModelType(site.getThemeId(), Enumerations.CmsLinkModeType.前台内容.getType(), e.getThemeType()).getFtlfile());
		 			  	cmsLink.setChildftlfile("/u/"+siteId+cmsTemplateService.findCmsTemplateByThemeIdAndModelType(site.getThemeId(), Enumerations.CmsLinkModeType.前台栏目继承页.getType(),e.getThemeType()).getFtlfile());
		 			 }
		 	   }else if(e.getModelType()==Enumerations.CmsLinkModeType.前台栏目DOM.getType()){
		 		  _index="/f/"+Enumerations.ThemeSiteType.getThemeSiteType(e.getThemeType())+"/"+siteId+"/"+id+"/dom";
		 	   }else if(e.getModelType()==Enumerations.CmsLinkModeType.站长后台列表.getType()){
		 		  _index="/s/"+siteId+"/"+id+"/content/list";
		 	   }else if(e.getModelType()==Enumerations.CmsLinkModeType.站长后台更新.getType()){
		 		  _index = "/s/"+siteId+"/"+id+"/content/update/pre";
		 	   }else if(e.getModelType()==Enumerations.CmsLinkModeType.站长后台查看.getType()){
		 		  _index = "/s/"+siteId+"/"+id+"/content/look";
		 	   }
		 	   
			   cmsLink.setDefaultLink(_index);
			   cmsLink.setLink(_index);
			   
			   if(superCmsChannel!=null){
				   if(e.getModelType()==Enumerations.CmsLinkModeType.前台栏目.getType()){
					   cmsLink.setFtlfile(cmsLinkService.findLinkBySiteBusinessIdModelType(siteId, superCmsChannel.getId(), e.getModelType(),e.getThemeType()).getChildftlfile());
				   }else{
					   cmsLink.setFtlfile(cmsLinkService.findLinkBySiteBusinessIdModelType(siteId, superCmsChannel.getId(), e.getModelType(),e.getThemeType()).getFtlfile());
				   }
			   }else{
				   cmsLink.setFtlfile("/u/"+siteId+e.getFtlfile());
			   }
			   cmsLink.setThemeType(e.getThemeType());
			   cmsLinkService.save(cmsLink);
	    	}
	    }
	    evictByKey(siteId, "cmsChannelService_findTreeByPid_"+cmsChannel.getPid()+"_"+siteId);
	    putByCache(siteId,"cmsChannelService_findTreeByPid_"+cmsChannel.getPid()+"_"+siteId, cmsChannel);
	    return id;
	}
	@Override
    public Integer updateChannel(CmsChannel cmsChannel) throws Exception{
		if(cmsChannel==null){
    		return null;
    	}
		String id=cmsChannel.getId();
		String pid=cmsChannel.getPid();
		String siteId=cmsChannel.getSiteId();
		
		if(StringUtils.isBlank(siteId)||StringUtils.isBlank(id)){
			return null;
		}
		  //pid和siteId一致设空
	    if(siteId.equals(cmsChannel.getPid())){
	    	cmsChannel.setPid("");
	    }
		
		Finder f_old_c=Finder.getSelectFinder(CmsChannel.class, "comcode").append(" WHERE id=:id ").setParam("id", id);
		
		String old_c=super.queryForObject(f_old_c, String.class);
		
		String new_c=findChannelNewComcode(id, pid,siteId);
		
		if(new_c.equalsIgnoreCase(old_c)){//编码没有改变
			evictByKey(siteId, "cmsChannelService_findTreeByPid_"+pid+"_"+siteId);
			return super.update(cmsChannel,true);
		}
		cmsChannel.setComcode(new_c);
		Integer update = super.update(cmsChannel,true);
		//级联更新
		Finder f_s_list=Finder.getSelectFinder(CmsChannel.class, "id,comcode").append(" WHERE comcode like :comcode and id<>:id ").setParam("comcode", old_c+"%").setParam("id", id);
	    List<CmsChannel> list = super.queryForList(f_s_list, CmsChannel.class);
	    if(CollectionUtils.isEmpty(list)){
	    	 return update;
	    }
		
	    for(CmsChannel ch:list){
	    	String _id=ch.getId();
		    String _c=	findChannelNewComcode(_id, id,siteId);
		    ch.setComcode(_c);
		    ch.setPid(id);
	    }
		super.update(list,true);
		super.cleanCache(siteId);
	    return update;

    }
    @Override
	public CmsChannel findCmsChannelById(String id) throws Exception{
    	return findById(id,CmsChannel.class);
	}
    
    
    
    @SuppressWarnings("unchecked")
	@Override
	public List<CmsChannel> findTreeByPid(String pid,String siteId) throws Exception {
    	if(StringUtils.isBlank(siteId)){
    		return null;
    	}
    	
    	List<CmsChannel> channelList = null;//getByCache(siteId, "cmsChannelService_findTreeByPid_"+pid+"_"+siteId, List.class);
    	if(CollectionUtils.isNotEmpty(channelList)){
    		return channelList;
    	}
    	
    	
    	
    	 
    		Finder finder=Finder.getSelectFinder(CmsChannel.class).append("  WHERE active=:active and siteId=:siteId ").setParam("siteId", siteId).setParam("active", 1);
            
            if(StringUtils.isNotBlank(pid)){
            	finder.append(" and comcode like :comcode and id<>:pid ").setParam("comcode", "%,"+pid+",%").setParam("pid", pid);
            }
            finder.append(" order by sortno asc ");
            
            channelList=super.queryForList(finder, CmsChannel.class);
    		if(CollectionUtils.isEmpty(channelList)){
    			return null;
    		}
    		List<CmsChannel> wrapList=new ArrayList<>();
    		diguiwrapList(channelList, wrapList, null);
    		putByCache(siteId, "cmsChannelService_findTreeByPid_"+pid+"_"+siteId,wrapList);
    		return wrapList;
    	
	}
	@Override
	public List<CmsChannel> findTreeChannel(String siteId) throws Exception {
		return findTreeByPid(null, siteId);
	}

	@Override
	public String findChannelNewComcode(String channelId,String pid,String siteId) throws Exception {
		
		if(StringUtils.isBlank(channelId)||StringUtils.isBlank(siteId)){
			return null;
		}
		
		String comcode=null;
		Finder f_p_c=Finder.getSelectFinder(CmsChannel.class, "comcode").append(" WHERE id=:id and siteId=:siteId ").setParam("siteId", siteId).setParam("id", pid);
		String p_c=super.queryForObject(f_p_c, String.class);
		//如果没有上级部门
		if(StringUtils.isEmpty(p_c)){
			p_c=",";
		}
		
		comcode=p_c+channelId+",";
		
		return comcode;
	}
	private List<CmsChannel> diguiwrapList(List<CmsChannel> from,List<CmsChannel> tolist,String parentId){
		if(CollectionUtils.isEmpty(from)){
			return null;
		}
		
		for(int i=0;i<from.size();i++){
			CmsChannel m=from.get(i);
			if(m==null){
				continue;
			}
			String pid=m.getPid();
			if((pid==null)&&(parentId!=null)){
				continue;
			}
			if((parentId==m.getPid())||(m.getPid().equals(parentId))){
				List<CmsChannel> leaf=new ArrayList<>();
				m.setLeaf(leaf);
				tolist.add(m);
			    diguiwrapList(from, leaf, m.getId());
			}
		}
		
		return tolist;
	}
	
	@Override
	public <T> T findById(Object id, Class<T> clazz) throws Exception {
		// TODO Auto-generated method stub
		return super.findById(id, clazz);
	}
	
	@Override
	public List<CmsChannel> findListByIdsForTag(List<String> asList,
			String siteId, Map params) throws Exception{
		if(StringUtils.isEmpty(siteId) || asList == null || asList.size() <= 0){
			return null;
		}
		Finder finder = Finder.getSelectFinder(CmsChannel.class);
		finder.append(" where id in (:ids) and siteId = :siteId ")
			.setParam("ids", asList).setParam("siteId", siteId);
		
		// 常用参数（表与实体类直接对应的字段）
		/*CmsChannel queryBean = new CmsChannel();
		if(params!=null){
			BeanUtils.populate(queryBean, params);
			super.getFinderWhereByQueryBean(finder, queryBean);
		}*/
		
		List<CmsChannel> channelList = super.queryForList(finder, CmsChannel.class);
		return channelList;
	}

	@Override
	public List<CmsChannel> findListForTag(Map params, Environment env,
			String siteId) throws Exception {
		if(StringUtils.isEmpty(siteId)){
			return null;
		}
		Finder finder = Finder.getSelectFinder(CmsChannel.class);
		finder.append(" where siteId = :siteId ").setParam("siteId", siteId);
		
		
		if(params==null){
			finder.append(" and pid is null ");
			finder.append(getOrderSql(0));
			return super.queryForList(finder, CmsChannel.class, null);
		}
		
		
		// 常用参数（表与实体类直接对应的字段）
		CmsChannel queryBean = new CmsChannel();
		BeanUtils.populate(queryBean, params);
		super.getFinderWhereByQueryBean(finder, queryBean);
		
		// 父类ID集合,不存在时只获取顶级栏目
		String pids = DirectiveUtils.getString("pids", params);
		if(StringUtils.isNotEmpty(pids)){
			List<String> pidList = Arrays.asList(pids.split(CmsChannelListDirective.PARAM_SPLIT));
			finder.append(" and pid in (:pids) ").setParam("pids", pidList);
		}else{
			finder.append(" and pid is null ");
		}
		
		
		
		
		

		// 分页
		Page page = null;
		if(params.get("page") != null){
			StringModel stringModel = (StringModel) params.get("page");
			page = (Page) stringModel.getAdaptedObject(Page.class);
			// 修改分页大小
			Integer pageSize = DirectiveUtils.getInt("pageSize", params);
			if(pageSize!=null){
				page.setPageSize(pageSize);
			}
		}
		
		// 排序
		Integer orderBy = DirectiveUtils.getInt(CmsChannelListDirective.PARAM_ORDER_BY, params);
		if (orderBy == null) {
			orderBy = 0;
		}
		finder.append(getOrderSql(orderBy));
		
		return super.queryForList(finder, CmsChannel.class, page);
	}
	
	/**
	 * 获取排序sql
	 * @param orderBy
	 * @return
	 */
	private String getOrderSql(int orderBy){
		String orderSql = "";
		switch (orderBy) {
		case 0: // 默认排序
			orderSql = " order by sortno ";
			break;
		case 1: // name 名称 升序
			orderSql = " order by name asc ";
			break;
		case 2: // name 名称 降序
			orderSql = " order by name desc ";
			break;
		case 3: // lookcount 打开次数 升序
			orderSql = " order by lookcount asc ";
			break;
		case 4: // lookcount 打开次数 降序
			orderSql = " order by lookcount desc ";
			break;
		case 5: // active 状态 升序
			orderSql = " order by active asc ";
			break;
		case 6: // active 状态 降序
			orderSql = " order by active desc ";
			break;
		case 7: // hasContent 是否有内容 升序
			orderSql = " order by hasContent asc ";
			break;
		case 8: // hasContent 是否有内容 降序
			orderSql = " order by hasContent desc ";
			break;
		default:
			break;
		}
		return orderSql;
	}

	@Override
	public CmsChannel findCmsChannelById(String id, String siteId) throws Exception{
		Finder finder = Finder.getSelectFinder(CmsChannel.class);
		finder.append(" where id=:id and siteId=:siteId ")
			.setParam("id", id).setParam("siteId", siteId);
		return super.queryForObject(finder, CmsChannel.class);
	}

	@Override
	public Integer saveLinkCms(List<CmsLink> cmsLinks,String channelId,String siteId) throws Exception {
		if(CollectionUtils.isEmpty(cmsLinks)){
			return null;
		}
		CmsLink link = null;
		List<CmsLink> saveDatas=new ArrayList<CmsLink>();
		for(CmsLink e:cmsLinks){
			if(!e.getSiteId().equals(siteId)||!e.getBusinessId().equals(channelId)){
				throw new  Exception("错误提示：data is wrong,the datas is not in the siteId or channelId ");
			}
			link=cmsLinkService.findById(e.getId(), CmsLink.class);
			if(link==null||link.getModelType()!=e.getModelType()||link.getThemeType()!=e.getThemeType()){
				throw new  Exception("错误提示：data is wrong,can't find the the link datas or the link's modeltype can't match the given datas ");
			}
			if(Enumerations.CmsLinkModeType.前台栏目.getType()==e.getModelType()){
				if(!link.getNodeftlfile().equals(e.getNodeftlfile())){
					//修改之前的文章   相当于对 之前的文章 换前台模板
					if(e.getUpdateOld()!=null&&e.getUpdateOld()==1){
						Finder finder=Finder.getUpdateFinder(CmsLink.class," ftlfile=:ftlfile where siteId=:siteId  and modelType=:modelType and themeType=:themeType and businessId in(select contentId from ")
								.append(Finder.getTableName(CmsChannelContent.class))
								.append(" as t where t.channelId=:channelId")
								.append(")");
						finder.setParam("channelId", link.getBusinessId());
						finder.setParam("ftlfile", e.getNodeftlfile()).setParam("siteId", siteId).setParam("modelType", "12").setParam("themeType", e.getThemeType());
						super.update(finder);
					}
					/*
					//临时开关处理  开始     只保留 40的nodefltlfile  此处方法为处理老数据，部署完后可注释掉
					finder=Finder.getUpdateFinder(CmsLink.class," nodeftlfile=null where siteId=:siteId and businessId=:businessId and modelType <> 10 ");
					finder.setParam("siteId", siteId).setParam("businessId", channelId);
					super.update(finder);
					//临时开关处理  结束
					 */
					//修改现有
					link.setNodeftlfile(e.getNodeftlfile());
					cmsLinkService.saveorupdate(link);
				}
				link.setLink(e.getLink());
				link.setJumpType(e.getJumpType());
				link.setLoginuser(e.getLoginuser());
				link.setChildftlfile(e.getChildftlfile());
			}
			link.setFtlfile(e.getFtlfile());
			saveDatas.add(link);
		}
		super.update(saveDatas);
		super.cleanCache(siteId);
	    return 0;
	}

	@Override
	public List<CmsChannel> findChanelByPid(String site, String pid) throws Exception {
		CmsChannel channel=new CmsChannel();
		channel.setSiteId(site);
		channel.setPid(pid);
		return super.findListDataByFinder(null, null, CmsChannel.class, channel);
				
	}
	
	@Override
	public List<CmsChannel> findChanelByCmsChannel(String site, String pid,CmsChannel cmsChannel,Page page) throws Exception {
		Finder finder=Finder.getSelectFinder(CmsChannel.class).append(" where 1=1");
		cmsChannel.setSiteId(site);
		cmsChannel.setPid(pid);
		super.getFinderWhereByQueryBean(finder, cmsChannel);
		return super.queryForList(finder, CmsChannel.class, page);
				
	}
	
}
