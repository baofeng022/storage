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
@TableName("uc_permission")
public class PermissionPO extends BasePO {

    private static final long serialVersionUID = 1L;

    private String permissionName;

    private String permissionCode;

    private Long parentId;

    private String path;

    private String icon;

    private Integer sort;

    private String type;

    private String fullPermId;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getFullPermId() {
        return fullPermId;
    }

    public void setFullPermId(String fullPermId) {
        this.fullPermId = fullPermId;
    }

    @Override
    public String toString() {
        return "PermissionPO{" +
        "permissionName=" + permissionName +
        ", permissionCode=" + permissionCode +
        ", parentId=" + parentId +
        ", path=" + path +
        ", icon=" + icon +
        ", sort=" + sort +
        ", type=" + type +
        ", fullPermId=" + fullPermId +
        "}";
    }
}
