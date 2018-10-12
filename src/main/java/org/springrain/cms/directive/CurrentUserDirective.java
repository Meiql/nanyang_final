package org.springrain.cms.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springrain.cms.util.DirectiveUtils;
import org.springrain.frame.common.SessionUser;
import org.springrain.system.entity.User;
import org.springrain.system.service.IUserService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 当前用户信息标签
 * @author Administrator
 *
 */
@Component("currentUserDirective")
public class CurrentUserDirective  extends AbstractCMSDirective {
	
	private static final String TPL_NAME = "currentUser";
	@Resource
	private IUserService userService;
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		User currentUser = null;
		try {
			currentUser = userService.findUserById(SessionUser.getUserId());
		} catch (Exception e) {
			logger.error("根据userId获取当前用户信息发生错误", e);
		}
		env.setVariable("user", DirectiveUtils.wrap(currentUser));
		if(body!=null){
			body.render(env.getOut());
		}
		
	}
	
	@PostConstruct
	public void registerFreeMarkerVariable() {
		setFreeMarkerSharedVariable(TPL_NAME, this);
	}
}
