package org.springrain.system.service;

import java.util.List;
import java.util.Map;

import org.springrain.frame.util.Page;
import org.springrain.system.entity.DicData;
/**
 * TODO 在此加入类描述
 * @copyright {@link springrain}
 * @author weicms.net<Auto generate>
 * @version  2013-07-31 15:56:45
 * @see org.springrain.springrain.service.DicData
 */
public interface IDicDataService extends IBaseSpringrainService {
	
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 * @throws Exception
	 */
	DicData findDicDataById(Object id) throws Exception;
	
	/**
	 * 根据pathKey和Id查找对象
	 * @param id
	 * @param pathtypekey
	 * @return
	 * @throws Exception
	 */
	DicData findDicDataById(String id,String pathtypekey) throws Exception;
	/**
	 * 根据pathKey查询所有字典
	 * @param pathtypekey
	 * @return
	 * @throws Exception
	 */
	List<DicData> findListDicData(String pathtypekey,Page page,DicData dicData) throws Exception;
	
	/**
	 * 根据Id和pathKey删除字典
	 * @param id
	 * @param pathtypekey
	 * @throws Exception
	 */
	void deleteDicDataById(String id,String pathtypekey) throws Exception;
	
	/**
	 * 根据Ids和pathKey删除字典
	 * @param ids
	 * @param pathtypekey
	 * @throws Exception
	 */
	void deleteDicDataByIds(List<String> ids,String pathtypekey) throws Exception;
	/**
	 * 根据pathkey添加或者修改字典
	 * @param dicData
	 * @param pathtypekey
	 * @return
	 * @throws Exception
	 */
	String saveorupdateDicData(DicData dicData, String pathtypekey)
			throws Exception;
	/**
	 *  从缓存中查询name
	 * @param id
	 * @param pathtypekey
	 * @return
	 * @throws Exception
	 */
	String findCacheNameById(String id,String pathtypekey)throws Exception;
	
	/**
	 * 根据编码和typekey找到字典
	 * @param code
	 * @param pathtypekey
	 * @return
	 * @throws Exception
	 */
	DicData findByCodeAndTypeKey(String code,String pathtypekey) throws Exception;
	
	//得到字典类型下所有直属类型
	Map<String, DicData> getAlldicData(String dictType)throws Exception;
	
	Map<String, DicData> findGongziConfigure() throws Exception;
	
	Map<String, DicData> findHetongConfigure() throws Exception;
	
	/**
	 * 根据pathKey查询所有字典
	 * @param pathtypekey
	 * @return
	 * @throws Exception
	 */
	List<DicData> findDicDataForList(String pathtypekey,Page page,DicData dicData) throws Exception;
	
}
