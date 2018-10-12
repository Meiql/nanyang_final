package org.springrain.cms.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springrain.cms.util.DirectiveUtils;
import org.springrain.system.entity.User;
import org.springrain.system.service.IUserService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("cmsUserDirective")
public class CmsUserDirective extends AbstractCMSDirective {
	
	private static final String TPL_NAME = "cms_user";
	@Resource
	private IUserService userService;
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String userId = DirectiveUtils.getString("id", params);
		User user;
		try {
			user = userService.findUserById(userId);
			user.setPassword("");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			user = new User();
		}
		
		env.setVariable("user", DirectiveUtils.wrap(user));
		if (body != null) { 
			body.render(env.getOut());  
		}
	}
	
	@PostConstruct
	public void registerFreeMarkerVariable() {
		setFreeMarkerSharedVariable(TPL_NAME, this);
	}
	
}
