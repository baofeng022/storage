package com.yango.uc.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yango.common.dao.model.BasePO;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-17
 */
@TableName("uc_role_permission")
public class RolePermissionPO extends BasePO {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long permissionId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        return "RolePermissionPO{" +
        "roleId=" + roleId +
        ", permissionId=" + permissionId +
        "}";
    }
}
