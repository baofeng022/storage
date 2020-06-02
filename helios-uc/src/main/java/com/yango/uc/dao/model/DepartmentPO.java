package com.yango.uc.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yango.common.dao.model.BasePO;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-18
 */
@TableName("uc_department")
public class DepartmentPO extends BasePO {

    private static final long serialVersionUID = 1L;

    private String deptName;

    private String deptType;

    private String deptNo;

    private Long parentId;

    private String fullDeptId;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }
    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public String getFullDeptId() {
        return fullDeptId;
    }

    public void setFullDeptId(String fullDeptId) {
        this.fullDeptId = fullDeptId;
    }

    @Override
    public String toString() {
        return "DepartmentPO{" +
        "deptName=" + deptName +
        ", deptType=" + deptType +
        ", deptNo=" + deptNo +
        ", parentId=" + parentId +
        ", fullDeptId=" + fullDeptId +
        "}";
    }
}
