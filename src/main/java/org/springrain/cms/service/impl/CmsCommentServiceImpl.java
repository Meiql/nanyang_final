package org.springrain.cms.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springrain.cms.entity.CmsComment;
import org.springrain.cms.service.ICmsCommentService;
import org.springrain.frame.entity.IBaseEntity;
import org.springrain.frame.util.Finder;
import org.springrain.frame.util.GlobalStatic;
import org.springrain.frame.util.JsonUtils;
import org.springrain.frame.util.Page;
import org.springrain.frame.util.ThreadPoolManager;
import org.springrain.system.service.BaseSpringrainServiceImpl;
import org.springrain.weixin.sdk.common.api.IWxMpConfig;
import org.springrain.weixin.sdk.common.api.IWxMpConfigService;
import org.springrain.weixin.sdk.common.exception.WxErrorException;
import org.springrain.weixin.sdk.mp.api.IWxMpMaterialService;
import org.springrain.weixin.sdk.mp.api.IWxMpService;


/**
 * 
 * @copyright {@link weicms.net}
 * @author springrain<Auto generate>
 * @version  2016-11-10 11:55:18
 * @see org.springrain.cms.entity.demo.service.impl.CmsComment
 */
@Service("cmsCommentService")
public class CmsCommentServiceImpl extends BaseSpringrainServiceImpl implements ICmsCommentService {
	
	@Resource
	private IWxMpConfigService wxMpConfigService;
	@Resource
	private IWxMpMaterialService wxMpMaterialService;
	@Resource
	private IWxMpService wxMpService;
	
	
	
	private void clearCache(String siteId)throws Exception{
		
		 super.cleanCache(siteId);
	}
	
	
	
    @Override
	public String  save(Object entity ) throws Exception{
    	 CmsComment cmsComment=(CmsComment) entity;
    	 String _id = super.save(cmsComment).toString();
    	 
    	 clearCache(cmsComment.getSiteId());
    	 
    	 
	     return _id;
	}

    @Override
	public String  saveorupdate(Object entity ) throws Exception{
	      CmsComment cmsComment=(CmsComment) entity;
	      
	      String _id = super.saveorupdate(cmsComment).toString();
	      clearCache(cmsComment.getSiteId());
	      
		 return _id;
	}
	
	@Override
    public Integer update(IBaseEntity entity ) throws Exception{
	 CmsComment cmsComment=(CmsComment) entity;
      Integer update = super.update(cmsComment,true);
      clearCache(cmsComment.getSiteId());
    	return update;
    }
    @Override
	public CmsComment findCmsCommentById(String id) throws Exception{
	 return super.findById(id,CmsComment.class);
	}
	
/**
 * 列表查询,每个service都会重载,要把sql语句封装到service中,Finder只是最后的方案
 * @param finder
 * @param page
 * @param clazz
 * @param o
 * @return
 * @throws Exception
 */
        @Override
    public <T> List<T> findListDataByFinder(Finder finder, Page page, Class<T> clazz,
			Object o) throws Exception{
			 return super.findListDataByFinder(finder,page,clazz,o);
			}
	/**
	 * 根据查询列表的宏,导出Excel
	 * @param finder 为空则只查询 clazz表
	 * @param ftlurl 类表的模版宏
	 * @param page 分页对象
	 * @param clazz 要查询的对象
	 * @param o  querybean
	 * @return
	 * @throws Exception
	 */
		@Override
	public <T> File findDataExportExcel(Finder finder,String ftlurl, Page page,
			Class<T> clazz, Object o)
			throws Exception {
			 return super.findDataExportExcel(finder,ftlurl,page,clazz,o);
		}
	@SuppressWarnings("rawtypes")
	public <T> File findDataExportExcel(Finder finder,String ftlurl, Page page,
			Class<T> clazz, Object o,Map map)
			throws Exception {
			 return super.findDataExportExcel(finder,ftlurl,page,clazz,o,map);
		}

	@Override
	public Integer findCommentsNumByBusinessId(String siteId,String businessId) throws Exception {
		
		
		String cacheKey="cmsCommentService_findCommentsNumByBusinessId_"+siteId+"_"+businessId;
		
		Integer num=super.getByCache(siteId, cacheKey, Integer.class);
		if(num!=null){
			return num;
		}
		
		
		
		Finder finder = new Finder("SELECT COUNT(*) FROM ")
					.append(Finder.getTableName(CmsComment.class))
					.append(" WHERE businessId=:businessId");
		finder.setParam("businessId", businessId);
		 num = super.queryForObject(finder,Integer.class);
		if(num==null){
			num=0;
		}
		
		super.putByCache(siteId, cacheKey, num);
		
	     return num;
		
	}

	@Override
	public List<CmsComment> findCommentListByBusinessId(String siteId,String businessId,Page page) throws Exception {
//		String cacheKey="cmsCommentService_findCommentListByBusinessId_"+siteId+"_"+businessId;
//		List<CmsComment> commentList=super.getByCache(siteId, cacheKey, List.class,page);
//		if(commentList!=null){
//			return commentList;
//		}
		Finder finder = Finder.getSelectFinder(CmsComment.class).append(" WHERE businessId=:businessId");
		finder.setParam("businessId", businessId);
		List<CmsComment> commentList = super.queryForList(finder, CmsComment.class,page);
		if (commentList == null){
			commentList = new ArrayList<>();
		}
		
//		super.putByCache(siteId, cacheKey, commentList,page);
		
		return commentList;
	}

	@Override
	public String saveWechatFile(String serverIds,String siteType,String siteId,String businessId) throws WxErrorException, IOException {
		IWxMpConfig wxMpConfig = wxMpConfigService.findWxMpConfigById(siteId);
		wxMpConfig.setTmpDirFile(GlobalStatic.tempRootpath);
		String[] serverIdArr = StringUtils.split(serverIds, ",");
		List<Map<String, String>> pathList = new ArrayList<Map<String,String>>();
		
		File tmpDir = new File(GlobalStatic.tempRootpath);
		if(!tmpDir.exists())
			tmpDir.mkdirs();
		for (String serverId : serverIdArr) {
			File downLoadFile = wxMpMaterialService.mediaDownload(wxMpConfig, serverId);
			String formalPath = GlobalStatic.rootDir+"/upload/u/"+siteId+"/"+siteType+"/"+businessId+"/"+downLoadFile.getName();
			File formalFile = new File(formalPath);
			FileUtils.copyFile(downLoadFile, formalFile);
			StringBuilder tmpFilePath = new StringBuilder("");
			tmpFilePath.append("/upload/u/").append(siteId).append("/").append(siteType).append("/").append(businessId).append("/").append(downLoadFile.getName());
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put("path", tmpFilePath.toString());
			pathList.add(tmpMap);
		}
		
		String path = JsonUtils.writeValueAsString(pathList);
		if(StringUtils.isBlank(path)){
			return null;
		}
		return path;
	}
	
	@Override
	public String saveWechatFile(String serverIds,String siteType,String siteId,String businessId,int transcoding,String suffix,String type) throws WxErrorException, IOException {
		IWxMpConfig wxMpConfig = wxMpConfigService.findWxMpConfigById(siteId);
		wxMpConfig.setTmpDirFile(GlobalStatic.tempRootpath);
		String[] serverIdArr = StringUtils.split(serverIds, ",");
		List<Map<String, String>> pathList = new ArrayList<Map<String,String>>();
		
		File tmpDir = new File(GlobalStatic.tempRootpath);
		if(!tmpDir.exists())
			tmpDir.mkdirs();
		for (String serverId : serverIdArr) {
			File downLoadFile = wxMpMaterialService.mediaDownload(wxMpConfig, serverId);
			String formalPath = GlobalStatic.rootDir+"/upload/"+siteType+"/"+siteId+"/"+businessId+"/"+downLoadFile.getName();
			File formalFile = new File(formalPath);
			FileUtils.copyFile(downLoadFile, formalFile);
			StringBuilder tmpFilePath = new StringBuilder("");
			if(transcoding==1){//0不转型，1转型
				if(StringUtils.isBlank(suffix)){
					tmpFilePath.append("/upload/").append(siteType).append("/").append(siteId).append("/").append(businessId).append("/").append(downLoadFile.getName().substring(0,downLoadFile.getName().lastIndexOf("."))+".mp3");
				}else{
					tmpFilePath.append("/upload/").append(siteType).append("/").append(siteId).append("/").append(businessId).append("/").append(downLoadFile.getName().substring(0,downLoadFile.getName().lastIndexOf("."))+"."+suffix);
				}
				toTranscodingType(GlobalStatic.rootDir,formalPath,tmpFilePath.toString(),type);
			}else{
				tmpFilePath.append("/upload/").append(siteType).append("/").append(siteId).append("/").append(businessId).append("/").append(downLoadFile.getName());
			}
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put("path", tmpFilePath.toString());
			pathList.add(tmpMap);
		}
		
		String path = JsonUtils.writeValueAsString(pathList);
		if(StringUtils.isBlank(path)){
			return null;
		}
		return path;
	}
	
	/** 
	 * 将上传的音频转格式 
	 * @param webroot 项目的根目录 
	 * @param sourcePath 文件的相对地址 
	 */  
	public static void toTranscodingType(final String webroot, final String sourcePath,final String targetPath,final String type){  
		 //File file = new File(sourcePath);  
	    //String targetPath = sourcePath+".mp3";//转换后文件的存储地址，直接将原来的文件名后加mp3后缀名 
		ThreadPoolManager.addThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 Runtime run = null;    
				 try {
					Thread.sleep(10000l);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				    try {    
				        run = Runtime.getRuntime();    
				        long start=System.currentTimeMillis();
				        Process p;
				      //  System.out.println("/usr/bin/ffmpeg -i "+sourcePath+" -acodec libmp3lame "+webroot+targetPath);
				        if(StringUtils.isBlank(type)){
				        	p=run.exec("/usr/bin/ffmpeg -i "+sourcePath+" -acodec libmp3lame "+webroot+targetPath);//执行ffmpeg.exe,前面是ffmpeg.exe的地址，中间是需要转换的文件地址，后面是转换后的文件地址。-i是转换方式，意思是可编码解码，mp3编码方式采用的是libmp3lame
				        }else{
				        	p=run.exec("/usr/bin/ffmpeg -i "+sourcePath+" -acodec "+type+" "+webroot+targetPath);//执行ffmpeg.exe,前面是ffmpeg.exe的地址，中间是需要转换的文件地址，后面是转换后的文件地址。-i是转换方式，意思是可编码解码，mp3编码方式采用的是libmp3lame
				        }
				        p.waitFor();
				        //释放进程    
				        p.getOutputStream().close();    
				        p.getInputStream().close();    
				        p.getErrorStream().close();    
				        long end=System.currentTimeMillis();    
				        System.out.println(sourcePath+" convert success, costs:"+(end-start)+"ms");    
				        //删除原来的文件    
				        //if(file.exists()){    
				            //file.delete();    
				        //}    
				    } catch (Exception e) {    
				        e.printStackTrace();    
				    }finally{    
				        //run调用lame解码器最后释放内存    
				        run.freeMemory();    
				    }  
			}
		});
	}

	@Override
	public void deleteCommentById(String id,String siteId,String businessId) throws Exception {
		super.deleteById(id, CmsComment.class);
		clearCache(siteId);
	}

	@Override
	public void deleteByCommentIds(List<String> ids,String siteId,String businessId) throws Exception {
		super.deleteByIds(ids, CmsComment.class);
		clearCache(siteId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CmsComment> findListByOpenId(String openId, String siteId,
			String businessId, Integer type) throws Exception{
		if(StringUtils.isEmpty(openId) || StringUtils.isEmpty(siteId) || StringUtils.isEmpty(businessId)){
			return null;
		}
		
		String cacheKey=siteId+"_"+openId+"_"+businessId; 
		
		List<CmsComment> commentList=super.getByCache(siteId, cacheKey, List.class);
		if(commentList!=null){
			return commentList;
		}
		
		
		
		Finder finder = Finder.getSelectFinder(CmsComment.class);
		finder.append(" where siteId=:siteId and businessId=:businessId and userId=:openId ")
			.setParam("siteId", siteId).setParam("businessId", businessId)
			.setParam("openId", openId);
		if(type != null){
			finder.append(" and type = :type ").setParam("type", type);
		}
		finder.append(" order by createDate desc ");
		
		
		commentList=super.queryForList(finder,CmsComment.class);
		
		 
		if (commentList == null){
			commentList = new ArrayList<>();
		}
		
		super.putByCache(siteId, cacheKey, commentList);
	    
	    
		return commentList;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<CmsComment> findCommentList(String siteId, CmsComment cmsComment, Page page) throws Exception {
		
		
		String cacheKey=siteId+"_"+page+cmsComment.getBak2()==null?"null":page.toString()+"_"+cmsComment.toString()+"_"+cmsComment.getBak2();
		
		List<CmsComment> commentList=super.getByCache(siteId, cacheKey, List.class,page);
		if(commentList!=null){
			return commentList;
		}
		
		Finder finder = Finder.getSelectFinder(CmsComment.class).append(" WHERE 1=1 ");
		super.getFinderWhereByQueryBean(finder, cmsComment);
		super.getFinderOrderBy(finder, page);
	    commentList = super.queryForList(finder, CmsComment.class,page);
	    
		if (commentList == null){
			commentList = new ArrayList<>();
		}
		
		super.putByCache(siteId, cacheKey, commentList,page);
	    
	    
		return commentList;
	}
	
}
