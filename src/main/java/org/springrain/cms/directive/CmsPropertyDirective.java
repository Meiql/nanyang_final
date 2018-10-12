package org.springrain.cms.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springrain.cms.entity.CmsProperty;
import org.springrain.cms.service.ICmsPropertyService;
import org.springrain.cms.util.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("cmsPropertyDirective")
public class CmsPropertyDirective extends AbstractCMSDirective {
	
	@Resource
	private ICmsPropertyService cmsPropertyService;
	
	/**
	 * 模板名称
	 */
	private static final String TPL_NAME = "cms_property";
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String businessId = DirectiveUtils.getString("businessId", params);
		String code = DirectiveUtils.getString("code", params);
		CmsProperty queryBean = new CmsProperty();
		queryBean.setBusinessId(businessId);
		queryBean.setCode(code);
		CmsProperty property;
		try {
			property = cmsPropertyService.queryForObject(queryBean);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			property = new CmsProperty();
		}
		env.setVariable("property", DirectiveUtils.wrap(property));
		if (body != null) {
			body.render(env.getOut());
		}
	}
	
	@PostConstruct
	public void  registerFreeMarkerVariable(){
		setFreeMarkerSharedVariable(TPL_NAME, this);
	}
	
}
