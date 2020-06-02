package com.yango.uc.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yango.common.dao.model.BasePO;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangbf
 * @since 2019-12-18
 */
@TableName("uc_user_dept_perm")
public class UserDeptPermPO extends BasePO {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long deptId;

    private String fullDeptId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    public String getFullDeptId() {
        return fullDeptId;
    }

    public void setFullDeptId(String fullDeptId) {
        this.fullDeptId = fullDeptId;
    }

    @Override
    public String toString() {
        return "UserDeptPermPO{" +
        "userId=" + userId +
        ", deptId=" + deptId +
        ", fullDeptId=" + fullDeptId +
        "}";
    }
}
