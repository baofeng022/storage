package com.yango.uc.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.uc.dao.model.PermissionPO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-17
 */
public interface PermissionMapper extends BaseMapper<PermissionPO> {

	/**
	 * 根据用户id查询出对应的权限
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select "
			+ " e.* "
			+ " from uc_user a ,uc_user_role b , uc_role c, uc_role_permission d, uc_permission e "
			+ " where a.id = b.user_id and b.role_id = c.id and c.id = d.role_id and d.permission_id = e.id "
			+ " and b.is_valid = '1' and c.is_valid = '1' and d.is_valid = '1' and e.is_valid = '1' "
			+ " and a.id = #{userId} ")
	List<PermissionPO> findAllPermissionByUserId(@Param("userId") Long userId);
	
	@Select("select * from uc_permission e where e.is_valid = '1'")
	List<PermissionPO> findAllPermission();
	
	@Select("select " + 
			"e.* " + 
			"from uc_role_permission d,uc_permission e " + 
			"where d.permission_id = e.id and d.is_valid = '1' and e.is_valid = '1' and d.role_id= #{roleId} ")
	List<PermissionPO> findAllPermissionByRoleId(@Param("roleId") Long roleId);
	
}
