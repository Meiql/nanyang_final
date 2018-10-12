package org.springrain.system.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springrain.cms.directive.AbstractCMSDirective;
import org.springrain.cms.util.DirectiveUtils;
import org.springrain.erp.constants.ErpStateEnum;
import org.springrain.system.entity.DicData;
import org.springrain.system.service.IDicDataService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("dicDataListDirective")
public class DicDataListDirective extends AbstractCMSDirective {
	
	private static final String TPL_NAME = "dicdata_list";
	
	private static final String PARAM_TYPEKEY = "typekey";
	
	@Resource
	private IDicDataService dicDataService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		try {
			List<DicData> datas;
			String typekey = DirectiveUtils.getString(PARAM_TYPEKEY, params);
			DicData data = new DicData();
			data.setActive(Integer.parseInt(ErpStateEnum.stateEnum.是.getValue()));
			datas = dicDataService.findListDicData(typekey, null, data);
			
			env.setVariable(DirectiveUtils.OUT_LIST, DirectiveUtils.wrap(datas));
			if(body!=null){
				body.render(env.getOut());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@PostConstruct
	public void registerFreeMarkerVariable() {
		setFreeMarkerSharedVariable(TPL_NAME, this);
	}
}
