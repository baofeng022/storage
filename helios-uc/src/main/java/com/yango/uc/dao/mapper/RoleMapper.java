package com.yango.uc.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.uc.dao.model.RolePO;
import com.yango.uc.web.vo.RoleVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-17
 */
public interface RoleMapper extends BaseMapper<RolePO> {

	/**
	 * 根据用户id查询出对应的角色
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select "
			+ "c.* "
			+ " from uc_user a ,uc_user_role b , uc_role c "
			+ " where a.id = b.user_id and b.role_id = c.id "
			+ " and b.is_valid = '1' and c.is_valid = '1' "
			+ " and a.id = #{userId} ")
	List<RolePO> findAllRoleByUserId(@Param("userId") Long userId);

	/**
	 * 根据角色名查出相应的角色
	 * 
	 * @param roleName
	 * @return
	 */
	@Select("select * from uc_role where role_name = #{roleName} and is_valid = '1'")
	RolePO findUniqueByRolename(@Param("roleName") String roleName);

	/**
	 * 根据条件查询出列表
	 * 
	 * @param params
	 * @return
	 */
	List<RoleVo> selectPageByCond(Map<String, Object> params);

	/**
	 * 根据条件查询出count
	 * 
	 * @param params
	 * @return
	 */
	Integer selectCountByCond(Map<String, Object> params);
	
	
	/**
	 * 查询出最大的角色编号
	 * 
	 * @return
	 */
	@Select("select max(role_no) from uc_role")
	String queryMaxRoleNo();
	
}
