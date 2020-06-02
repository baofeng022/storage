package com.yango.uc.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.uc.dao.model.RolePermissionPO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-17
 */
public interface RolePermissionMapper extends BaseMapper<RolePermissionPO> {

	@Delete("delete from uc_role_permission where role_id = #{roleId}")
	void deleteByRoleId(@Param("roleId") Long roleId);
	
	@Select("select * from uc_role_permission where role_id = #{roleId} and is_valid = '1'")
	List<RolePermissionPO> findAllByRoleId(@Param("roleId") Long roleId);
	
}
