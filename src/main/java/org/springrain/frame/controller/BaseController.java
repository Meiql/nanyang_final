package org.springrain.frame.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springrain.frame.common.BaseLogger;
import org.springrain.frame.util.DateTypeEditor;
import org.springrain.frame.util.GlobalStatic;
import org.springrain.frame.util.OpenOfficeKit;
import org.springrain.frame.util.Page;

import freemarker.template.Template;

/**
 * 基础的Controller,所有的Controller必须继承此类
 * 
 * @copyright {@link weicms.net}
 * @author springrain<9iuorg@gmail.com>
 * @version 2013-03-19 11:08:15
 * @see org.springrain.frame.controller.BaseController
 * @param <T>
 */
// @Controller
public class BaseController extends BaseLogger {

	public static final String messageurl = "/common/message";

	public static final String message = "message";

	public static final String redirect = "redirect:";

	public static final String forward = "forward:";

	//@Resource
	//private CacheManager shiroCacheManager;

	/**
	 * 增加了@ModelAttribute的方法可以在本controller方法调用前执行,可以存放一些共享变量,如枚举值,或是一些初始化操作
	 */

	// @ModelAttribute("")
	public void init(Model model) {
		// model.addAttribute("now", new Date());
	}

	/**
	 * 初始化映射格式.
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(Date.class, new DateTypeEditor());
		
		/*
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					if (StringUtils.isNotBlank(value)) {
						try {
							setValue(new SimpleDateFormat(DateUtils.DATETIME_FORMAT).parse(value));
						} catch (Exception e) {
							setValue(new SimpleDateFormat(DateUtils.DATE_FORMAT).parse(value));
						}
					} else {
						setValue(null);
					}
				} catch (Exception e) {
					setValue(null);
					logger.error(e.getMessage(), e);
				}
			}
		
			 // public String getAsText() { return new
			 // SimpleDateFormat("yyyy-MM-dd").format((Date) getValue()); }
			 
		});
		
		
		

		binder.registerCustomEditor(Integer.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					if (StringUtils.isNotBlank(value)) {
						setValue(Integer.valueOf(value));
					} else {
						setValue(null);
					}
				} catch (Exception e) {
					setValue(null);
					logger.error(e.getMessage(), e);
				}
			}
		});
		binder.registerCustomEditor(Long.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					if (StringUtils.isNotBlank(value)) {
						setValue(Long.valueOf(value));
					} else {
						setValue(null);
					}
				} catch (Exception e) {
					setValue(null);
					logger.error(e.getMessage(), e);
				}
			}
		});
		binder.registerCustomEditor(Double.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					if (StringUtils.isNotBlank(value)) {
						setValue(Double.valueOf(value));
					} else {
						setValue(null);
					}
				} catch (Exception e) {
					setValue(null);
					logger.error(e.getMessage(), e);
				}
			}
		});

		binder.registerCustomEditor(BigDecimal.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					if (StringUtils.isNotBlank(value)) {
						setValue(new BigDecimal(value));
					} else {
						setValue(null);
					}
				} catch (Exception e) {
					setValue(null);
					logger.error(e.getMessage(), e);
				}
			}
		});
		
		*/

	}

	/*
	@ExceptionHandler
	public String exp(HttpServletRequest request, Exception e) {
		request.setAttribute("e", e);
		logger.error(e.getMessage(), e);
		return "/error";
	}
	*/

	/**
	 * 公共下载方法
	 * 
	 * @param response
	 * @param file
	 *            下载的文件
	 * @param fileName
	 *            下载时显示的文件名
	 * @return
	 * @throws Exception
	 */
	public HttpServletResponse downFile(HttpServletResponse response, File file, String fileName,
			boolean delFile) throws Exception {
		response.setContentType("application/x-download");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		OutputStream out = null;
		InputStream in = null;
		// 下面一步不可少
		fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.addHeader("Content-disposition", "attachment;filename=" + fileName);// 设定输出文件头

		try {
			out = response.getOutputStream();
			in = new FileInputStream(file);
			int len = in.available();
			byte[] b = new byte[len];
			in.read(b);
			out.write(b);
			out.flush();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("下载失败!");
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (delFile) {
				file.delete();
			}
		}
		return response;
	}
	/**
	 * 获取分页 page 对象
	 * @param request
	 * @return
	 */
	public Page newPage(HttpServletRequest request) {
		// ==获取分页信息
	 return 	newPage(request, "id", "desc");
	}
	/**
	 * 指定默认的排序字段和方法 返回page分页对象
	 * @param request
	 * @param defaultOrder
	 * @param defaultSort
	 * @return
	 */
	public Page newPage(HttpServletRequest request,String defaultOrder,String defaultSort) {
		// ==获取分页信息

		String str_pageIndex = request.getParameter("pageIndex");
		int pageIndex = NumberUtils.toInt(str_pageIndex, 1);
		String order = request.getParameter("order");
		String sort = request.getParameter("sort");
		
		if (StringUtils.isBlank(order)) {
			order = defaultOrder;
		}
		if (StringUtils.isBlank(sort)) {
			sort = defaultSort;
		}
		
		if (StringUtils.isBlank(order)) {
			order = "id";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "desc";
		}

		Page page = new Page(pageIndex);
		page.setOrder(order);
		page.setSort(sort);
		return page;
	}
	
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addModelParameter(HttpServletRequest request,Model model){
		Map<String,String[]> map=request.getParameterMap();  
	    Set<Map.Entry<String,String[]>> keSet=map.entrySet();  
	    for(Iterator itr=keSet.iterator();itr.hasNext();){  
	    	
	        Map.Entry<String,String[]> me=(Map.Entry<String,String[]>)itr.next();  
	        String key=me.getKey();  
	        String[] value=me.getValue(); 
	        if(value==null||value.length==0){
	        	continue;
	        }else if(value.length==1){
	        	model.addAttribute(key, value[0]);
	        }else{
	        	model.addAttribute(key, value);
	        }
	        
	  
	     }  
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public File createExceFile(Template template, Map map) throws Exception {
		
		map.put(GlobalStatic.exportexcel, true);// 设置导出excel变量
		
		String fileName = UUID.randomUUID().toString();
		String tempFFilepath = GlobalStatic.tempRootpath + "/" + fileName
				+ "/freemarker.html";
		String tempExcelpath = GlobalStatic.tempRootpath + "/" + fileName + "/"
				+ fileName + GlobalStatic.excelext;
		File tempfdir = new File(GlobalStatic.tempRootpath + "/" + fileName);
		if (tempfdir.exists() == false) {
			tempfdir.mkdirs();
		}

		File ffile = new File(tempFFilepath);

		File excelFile = new File(tempExcelpath);
		boolean first = true;
		boolean end = false;
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(ffile), "UTF-8"));
			template.process(map, out);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("生成freemarker页面错误");
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}

		FileInputStream in = null;
		BufferedReader br = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			in = new FileInputStream(ffile);
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			fos = new FileOutputStream(excelFile, true);
			osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			if (first) {// 如果是第一次,输出编码格式,防止 office 乱码
				bw.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
			}
			String line = null;

			boolean iswrite = false;
			while ((line = br.readLine()) != null) {
				if (StringUtils.isBlank(line))
					continue;

				line = line.trim();
				if (line.startsWith("<!--first_") && first == false) {
					iswrite = false;
					continue;
				}
				if (line.startsWith("<!--last_") && end == false) {
					iswrite = false;
					continue;
				}

				if ("<!--first_start_export-->".equals(line)) {
					iswrite = first;
					continue;

				} else if ("<!--last_start_export-->".equals(line)) {
					iswrite = end;
					continue;

				} else if ("<!--first_start_no_export-->".equals(line)) {
					iswrite = false;
					continue;

				} else if ("<!--first_end_no_export-->".equals(line)) {
					iswrite = true;
					continue;

				} else if ("<!--start_no_export-->".equals(line)) {
					iswrite = false;
					continue;
				} else if ("<!--start_export-->".equals(line)) {
					iswrite = true;
					continue;
				} else if ("<!--last_start_export-->".equals(line)) {
					iswrite = true;
					continue;
				} else if ("<!--last_end_export-->".equals(line)) {
					iswrite = false;
					continue;
				} else if (line.startsWith("<!--end_")) {// 不包含需要输出的内容
					if ("<!--end_no_export-->".equals(line)) {// 特殊标签,不输出内容
						iswrite = true;
					} else {
						iswrite = false;
					}
					continue;
				}

				if (iswrite) {// 如果是写入标签
					bw.write(line);
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("追加xlsx内容错误");
		} finally {
			if (bw != null)
				bw.close();
			if (osw != null)
				osw.close();
			if (fos != null)
				fos.close();
			if (br != null)
				br.close();
			if (in != null)
				in.close();
		}
		// excel转化
		try {
			String newuid = UUID.randomUUID().toString();
			File excelnew = new File(GlobalStatic.tempRootpath + "/" + fileName
					+ "/" + newuid + GlobalStatic.excelext); 
			OpenOfficeKit.cvtXls(excelFile, excelnew);
			return excelnew;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return excelFile;
	}

}
