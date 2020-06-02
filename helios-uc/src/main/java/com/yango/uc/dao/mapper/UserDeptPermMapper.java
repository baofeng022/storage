package com.yango.uc.dao.mapper;

import com.yango.uc.dao.model.UserDeptPermPO;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhangbf
 * @since 2019-12-18
 */
public interface UserDeptPermMapper extends BaseMapper<UserDeptPermPO> {

	@Select("select * from uc_user_dept_perm where user_id = #{userId} and is_valid = '1'")
	List<UserDeptPermPO> findAllByUserId(@Param("userId") Long userId);

	@Delete("delete from uc_user_dept_perm where user_id = #{userId}")
	void delByUserId(@Param("userId") Long userId);

}
