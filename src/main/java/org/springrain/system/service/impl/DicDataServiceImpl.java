package org.springrain.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springrain.erp.constants.DicdataTypeEnum;
import org.springrain.erp.constants.ErpStateEnum;
import org.springrain.frame.entity.IBaseEntity;
import org.springrain.frame.util.Finder;
import org.springrain.frame.util.GlobalStatic;
import org.springrain.frame.util.Page;
import org.springrain.system.entity.DicData;
import org.springrain.system.service.BaseSpringrainServiceImpl;
import org.springrain.system.service.IDicDataService;

/**
 * TODO 在此加入类描述
 * 
 * @copyright {@link springrain}
 * @author weicms.net<Auto generate>
 * @version 2013-07-31 15:56:45
 * @see org.springrain.springrain.service.impl.DicData
 */
@Service("dicDataService")
public class DicDataServiceImpl extends BaseSpringrainServiceImpl implements
		IDicDataService {

	@Override
	public String save(Object entity) throws Exception {
		DicData dicData = (DicData) entity;
		return (String) super.save(dicData);
	}

	@Override
	@CacheEvict(value = GlobalStatic.cacheKey, key = "'findListDicData_'+#pathtypekey")
	public String saveorupdateDicData(DicData dicData, String pathtypekey)
			throws Exception {
		if (StringUtils.isBlank(pathtypekey) || dicData == null) {
			return null;
		}
		String typeKey = dicData.getTypekey();
		if (!pathtypekey.equals(typeKey)) {
			return null;
		}

		return super.saveorupdate(dicData).toString();
	}

	@Override
	public Integer update(IBaseEntity entity) throws Exception {
		DicData dicData = (DicData) entity;
		return super.update(dicData);
	}

	@Override
	public DicData findDicDataById(Object id) throws Exception {
		return super.findById(id, DicData.class);
	}

	/**
	 * 列表查询,每个service都会重载,要把sql语句封装到service中,Finder只是最后的方案
	 * 
	 * @param finder
	 * @param page
	 * @param clazz
	 * @param o
	 * @return
	 * @throws Exception
	 */
	@Override
	public <T> List<T> findListDataByFinder(Finder finder, Page page,
			Class<T> clazz, Object o) throws Exception {
		if (finder == null) {
			finder = Finder.getSelectFinder(DicData.class)
					.append(" WHERE 1=1 ");
		}
		if (o != null) {
			getFinderWhereByQueryBean(finder, o);
		}

		return super.findListDataByFinder(finder, page, clazz, o);
	}

	@Override
	public DicData findDicDataById(String id, String pathtypekey)
			throws Exception {
		if (StringUtils.isBlank(id) || StringUtils.isBlank(pathtypekey)) {
			return null;
		}
		Finder finder = Finder.getSelectFinder(DicData.class).append(
				"  where id=:id and  typekey=:typekey   order by name ");
		finder.setParam("typekey", pathtypekey).setParam("id", id);
		DicData dicData = super.queryForObject(finder, DicData.class);
		return dicData;
	}

	@Override
	// @Cacheable(value = GlobalStatic.cacheKey, key =
	// "'findListDicData_'+#pathtypekey")
	public List<DicData> findListDicData(String pathtypekey, Page page,
			DicData dicData) throws Exception {
		if (StringUtils.isBlank(pathtypekey)) {
			return null;
		}
		Finder finder = Finder.getSelectFinder(DicData.class).append(
				" WHERE typekey=:typekey ");
		finder.setParam("typekey", pathtypekey);
		/*if(dicData != null){
			if(dicData.getActive() != null){
				finder.append(" and active = :active ").setParam("active", dicData.getActive());
			}
		}*/
		if(dicData == null || dicData.getActive() == null){
			finder.append(" and active = :active ").setParam("active", ErpStateEnum.stateEnum.是.getValue());
		}else{
			finder.append(" and active = :active ").setParam("active", dicData.getActive());
			if(StringUtils.isNotEmpty(dicData.getName())){
				finder.append(" and name = :name ").setParam("name",dicData.getName());	
			}
		}
		finder.append(" order by sortno desc ");
		return super.queryForList(finder, DicData.class, page);
	}

	@Override
	@CacheEvict(value = GlobalStatic.cacheKey, key = "'findListDicData_'+#pathtypekey")
	public void deleteDicDataById(String id, String pathtypekey)
			throws Exception {

		if (StringUtils.isBlank(id) || StringUtils.isBlank(pathtypekey)) {
			return;
		}

		Finder finder = Finder.getDeleteFinder(DicData.class).append(
				" where id=:id and  typekey=:typekey  ");
		finder.setParam("typekey", pathtypekey).setParam("id", id);
		super.update(finder);

	}

	@Override
	@CacheEvict(value = GlobalStatic.cacheKey, key = "'findListDicData_'+#pathtypekey")
	public void deleteDicDataByIds(List<String> ids, String pathtypekey)
			throws Exception {
		if (CollectionUtils.isEmpty(ids) || StringUtils.isBlank(pathtypekey)) {
			return;
		}

		Finder finder = Finder.getDeleteFinder(DicData.class).append(
				" where id in(:ids) and  typekey=:typekey  ");
		finder.setParam("typekey", pathtypekey).setParam("ids", ids);
		super.update(finder);

	}

	@Override
	public String findCacheNameById(String id, String pathtypekey)
			throws Exception {
		List<DicData> findListDicData = findListDicData(pathtypekey, null, null);
		if (CollectionUtils.isEmpty(findListDicData) || StringUtils.isBlank(id)) {
			return null;
		}
		for (DicData dicData : findListDicData) {
			if (dicData.getId().equals(id)) {
				return dicData.getName();
			}
		}

		return null;
	}

	@Override
	public DicData findByCodeAndTypeKey(String code, String pathtypekey)
			throws Exception {
		if (StringUtils.isBlank(code) || StringUtils.isBlank(pathtypekey)) {
			return null;
		}
		Finder finder = Finder.getSelectFinder(DicData.class);
		finder.append(" where 1 = 1 ");
		finder.append(" and code = :code and typekey = :typekey ")
				.setParam("code", code).setParam("typekey", pathtypekey);
		List<DicData> datas = super.queryForList(finder, DicData.class);
		if (CollectionUtils.isEmpty(datas)) {
			return null;
		}
		return datas.get(0);
	}

	@Override
	public Map<String, DicData> getAlldicData(String dictType) throws Exception {
		// 得到字典类型下所有直属类型
		Map<String, DicData> map = new HashMap<String, DicData>();
		Finder finder = Finder.getSelectFinder(DicData.class);
		finder.append(" where 1 = 1 ");
		if (StringUtils.isNoneEmpty(dictType)) {
			finder.append(" and typekey = :typekey ").setParam("typekey",
					dictType);
		}
		List<DicData> list = queryForList(finder, DicData.class);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		for (DicData dict : list)
			map.put(dict.getId().toString(), dict);
		return map;
	}

	@Override
	public Map<String, DicData> findGongziConfigure() throws Exception {
		Map<String, DicData> map = new HashMap<String, DicData>();
		
		List<DicData> chakanDatas = findListDicData(DicdataTypeEnum.员工工资查询时间.getValue(), null, null);
		List<DicData> bushengchengDatas = findListDicData(DicdataTypeEnum.不生成工资员工.getValue(), null, null);
		
		DicData chakanData = null;
		DicData bushengchengData = null;
		
		if(!CollectionUtils.isEmpty(chakanDatas)){
			chakanData = chakanDatas.get(0);
		}
		if(!CollectionUtils.isEmpty(bushengchengDatas)){
			bushengchengData = bushengchengDatas.get(0);
		}
		
		map.put("chakanData", chakanData);
		map.put("bushengchengData", bushengchengData);
		return map;
	}

	@Override
	public Map<String, DicData> findHetongConfigure() throws Exception {
		Map<String, DicData> map = new HashMap<String, DicData>();
		
		List<DicData> sendDatas = findListDicData(DicdataTypeEnum.发送邮件时间.getValue(), null, null);
		List<DicData> jieshouDatas = findListDicData(DicdataTypeEnum.接收邮箱.getValue(), null, null);
		
		DicData sendData = null;
		DicData jieshouData = null;
		
		if(!CollectionUtils.isEmpty(sendDatas)){
			sendData = sendDatas.get(0);
		}
		if(!CollectionUtils.isEmpty(jieshouDatas)){
			jieshouData = jieshouDatas.get(0);
		}
		
		map.put("sendDate", sendData);
		map.put("receiveEmail", jieshouData);
		return map;
	}

	@Override
	public List<DicData> findDicDataForList(String pathtypekey, Page page,
			DicData dicData) throws Exception {
		Finder finder = Finder.getSelectFinder(DicData.class);
		finder.append(" where 1 = 1 ");
		if (StringUtils.isNotBlank(pathtypekey)) {
			finder.append(" and typekey = :typekey ").setParam("typekey",
					pathtypekey);
		}
		if(dicData.getActive()!=null){
			finder.append(" and active = :active ").setParam("active",
					dicData.getActive());
		}
		if(StringUtils.isNotEmpty(dicData.getCode())){
			finder.append(" and code = :code ").setParam("code",
					dicData.getCode());
		}
		if(StringUtils.isNotEmpty(dicData.getName())){
			finder.append(" and name like :name ").setParam("name",
					"%"+dicData.getName()+"%");
		}
		
		return super.queryForList(finder, DicData.class, page);
	}

	
	
}
