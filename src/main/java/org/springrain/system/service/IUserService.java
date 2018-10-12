package org.springrain.system.service;

import java.util.List;

import org.springrain.frame.util.Page;
import org.springrain.system.entity.User;

/**
 * TODO 在此加入类描述
 * @copyright {@link weicms.net}
 * @author springrain<Auto generate>
 * @version  2013-07-06 16:03:00
 * @see org.springrain.springrain.service.User
 */
public interface IUserService extends IBaseSpringrainService {
/**
	 * 保存 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	String saveUser(User entity) throws Exception;

	 /**
     * 更新
     * @param entity
     * @return
     * @throws Exception
     */
	Integer updateUser(User entity) throws Exception;
	
	/**
	 * 根据用户Id 删除用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String deleteUserById(String userId) throws Exception;

	
	
	
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 * @throws Exception
	 */
	User findUserById(Object id) throws Exception;
    

	
	void updateRoleUser(String userId,String roleIds)throws Exception;

	/**
	 * 根据账号查找
	 * @param account
	 * @return
	 * @throws Exception
	 */
	User findUserByAccount(String account) throws Exception;
	/**
	 * 查找用户 可以写公共的
	 * @param user
	 * @return
	 * @throws Exception
	 */
	List<User> finderUserForList(User user,Page page)throws Exception;

	
	Integer findUserByName(String name) throws Exception;
	/**
	 * 通过username 查找user
	 * @param name
	 * @return
	 * @throws Exception
	 */
	List<User> finderUserByUserName(String name,String name2) throws Exception;
}
