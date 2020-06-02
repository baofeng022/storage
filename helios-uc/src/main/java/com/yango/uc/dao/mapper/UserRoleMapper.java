package com.yango.uc.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.uc.dao.model.UserRolePO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-17
 */
public interface UserRoleMapper extends BaseMapper<UserRolePO> {

	@Delete("delete from uc_user_role where user_id = #{userId}")
	void deleteByUserId(@Param("userId") Long userId);

	@Select("select * from uc_user_role where role_id = #{roleId}")
	List<UserRolePO> findAllByRoleId(@Param("roleId") Long roleId);
	
}
