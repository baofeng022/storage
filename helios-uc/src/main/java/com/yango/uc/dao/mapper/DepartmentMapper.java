package com.yango.uc.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.uc.dao.model.DepartmentPO;
import com.yango.uc.web.vo.DepartmentVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-17
 */
public interface DepartmentMapper extends BaseMapper<DepartmentPO> {

	/**
	 * 根据条件查询出列表
	 * 
	 * @param params
	 * @return
	 */
	List<DepartmentVo> selectPageByCond(Map<String, Object> params);

	/**
	 * 根据parentId查询出子节点
	 * 
	 * @param id
	 * @return
	 */
	@Select("select * from uc_department where parent_id = #{parentId} and is_valid = '1'")
	List<DepartmentPO> selectListByParentId(@Param("parentId") Long parentId);

	/**
	 * 查询最大部门编号
	 * 
	 * @return
	 */
	@Select("select max(dept_no) from uc_department")
	String queryMaxDeptNo();
	
	
	@Select("select * from uc_department where full_dept_id like '${fullDeptId}%' and is_valid = '1'")
	List<DepartmentPO> selectAllSubDeptByFullDeptId(@Param("fullDeptId") String fullDeptId);

}
