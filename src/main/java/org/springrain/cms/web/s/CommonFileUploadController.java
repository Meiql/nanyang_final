package org.springrain.cms.web.s;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springrain.frame.util.GlobalStatic;
import org.springrain.frame.util.ReturnDatas;
import org.springrain.frame.util.SecUtils;

@Controller
@RequestMapping(value="/s/{siteId}/file")
public class CommonFileUploadController {
	/**
	 * 上传logo
	 * @throws IOException 
	 * */
	@RequestMapping(value="/logoupload",method=RequestMethod.POST)
	@ResponseBody 
	public  ReturnDatas logoUpload(HttpServletRequest request,@PathVariable String siteId) throws IOException{
		ReturnDatas returnDatas=ReturnDatas.getSuccessReturnDatas();
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		String businessId = request.getParameter("businessId");
	    List<Map<String, String>> fileList = uploadFile(multiRequest,siteId,businessId);
		returnDatas.setData(fileList);
		return returnDatas;
	}

	private List<Map<String, String>> uploadFile(
			MultipartHttpServletRequest multiRequest, String siteId,String businessId) throws IOException {
		Iterator<String> iter = multiRequest.getFileNames();
		List<Map<String, String>> fileList = new ArrayList<>(); 
		while(iter.hasNext()){
			MultipartFile tempFile = multiRequest.getFile(iter.next());
            int lastSplit=tempFile.getOriginalFilename().lastIndexOf(".");
            String prefix=tempFile.getOriginalFilename().substring(lastSplit+1);
           String fileName = tempFile.getOriginalFilename().substring(0, lastSplit);
			
			//String path = "/upload/"+siteId+"/"+businessId+"/"+SecUtils.getUUID()+tempFile.getOriginalFilename();
			String path = "/upload/u/"+siteId+"/"+businessId+"/"+SecUtils.getUUID()+"."+prefix;
    		File file = new File(GlobalStatic.rootDir+path);
    		if(!file.getParentFile().exists())
    			file.getParentFile().mkdirs();
    		if(!file.exists()){
    			boolean createNewFile = file.createNewFile();
    			if(!createNewFile){
    				return null;
    			}
    		}
    			
    		tempFile.transferTo(file);
    		Map<String, String> uploadFileMap = new HashMap<>();
    		uploadFileMap.put("name", fileName);
    		uploadFileMap.put("path", path);
    		uploadFileMap.put("size", file.getTotalSpace()+"");
    		uploadFileMap.put("prefix", prefix);
    		
    		// 添加title,url,orderby 
    		uploadFileMap.put("id", "");
    		uploadFileMap.put("title", "");
    		uploadFileMap.put("url", "");
    		uploadFileMap.put("orderby", "");
    		
    		fileList.add(uploadFileMap);
		}
		
		return fileList;
	}
}
