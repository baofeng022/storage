package com.yango.uc.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.uc.dao.model.UserPO;
import com.yango.uc.web.vo.UserVo;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-17
 */
public interface UserMapper extends BaseMapper<UserPO> {

	/**
	 * 根据用户名查询出用户
	 * 
	 * @param username
	 * @return
	 */
	@Select("select * from uc_user where username = #{username}")
	UserPO findUniqueByUsername(@Param("username") String username);

	/**
	 * 根据条件查询出列表
	 * 
	 * @param params
	 * @return
	 */
	List<UserVo> selectPageByCond(Map<String, Object> params);

	/**
	 * 根据条件查询出count
	 * 
	 * @param params
	 * @return
	 */
	Integer selectCountByCond(Map<String, Object> params);

	/**
	 * 查询出部门下所有员工
	 * 
	 * @param deptId
	 * @return
	 */
	@Select("select * from uc_user where dept_id = #{deptId} and is_valid = '1'")
	List<UserPO> selectListByDeptId(@Param("deptId") Long deptId);

	/**
	 * 查询出最大的员工编号
	 * 
	 * @return
	 */
	@Select("select max(user_no) from uc_user")
	String queryMaxUserNo();

	/**
	 * 根据fullDeptId来查出所属部门及以下的员工
	 * 
	 * @param fullDeptId
	 * @return
	 */
	@Select("select a.* from uc_user a left join uc_department b on a.dept_id = b.id where a.is_valid = '1' and b.is_valid = '1' and b.full_dept_id like '${fullDeptId}%'")
	List<UserPO> selectAllSubUserListByFullDeptId(@Param("fullDeptId") String fullDeptId);
	
	@Select("select * from uc_user where is_valid = '1'")
	List<UserPO> findAll();
	
}
