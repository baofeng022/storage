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
@TableName("uc_role")
public class RolePO extends BasePO {

    private static final long serialVersionUID = 1L;

    private String roleName;

    private String roleDes;

    private String roleNo;

    private String status;

    
    public RolePO() {}
    
    
	public RolePO(Long id, String roleName) {
		 this.id = id;
		 this.roleName = roleName;
	}
    
    
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleDes() {
        return roleDes;
    }

    public void setRoleDes(String roleDes) {
        this.roleDes = roleDes;
    }
    public String getRoleNo() {
        return roleNo;
    }

    public void setRoleNo(String roleNo) {
        this.roleNo = roleNo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RolePO{" +
        "roleName=" + roleName +
        ", roleDes=" + roleDes +
        ", roleNo=" + roleNo +
        ", status=" + status +
        "}";
    }
}
