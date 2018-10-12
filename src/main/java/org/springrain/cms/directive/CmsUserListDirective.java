package org.springrain.cms.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springrain.cms.util.DirectiveUtils;
import org.springrain.erp.constants.ErpStateEnum;
import org.springrain.system.entity.User;
import org.springrain.system.service.IUserService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("cmsUserListDirective")
public class CmsUserListDirective extends AbstractCMSDirective {
	
	private static final String TPL_NAME = "user_list";
	@Resource
	private IUserService userService;
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String activeStr = DirectiveUtils.getString("active", params);
		String userTypeStr = DirectiveUtils.getString("userTypeStr", params);
		Integer active = null;
		Integer userType = null;
		if(StringUtils.isNotBlank(activeStr)){
			active = Integer.parseInt(activeStr);
		}
		if(StringUtils.isNotBlank(userTypeStr)){
			userType = Integer.parseInt(userTypeStr);
		}
		List<User> datas = null;
		try {
			User u = new User();
			u.setActive(active);
			u.setUserType(userType);
			datas = userService.finderUserForList(u,null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		env.setVariable(DirectiveUtils.OUT_LIST, DirectiveUtils.wrap(datas));
		if (body != null) { 
			body.render(env.getOut());  
		}
	}
	
	@PostConstruct
	public void registerFreeMarkerVariable() {
		setFreeMarkerSharedVariable(TPL_NAME, this);
	}
	
}
