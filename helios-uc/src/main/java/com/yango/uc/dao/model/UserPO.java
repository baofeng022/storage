package com.yango.uc.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yango.common.dao.model.BasePO;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangbf
 * @since 2019-07-10
 */
@TableName("uc_user")
public class UserPO extends BasePO {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String mobile;

    private String jobNo;

    private String nickname;

    /**
     * 性别M:男W:女
     */
    private String sex;

    /**
     * 邮箱
     */
    private String email;

    private String position;

    private Long deptId;

    private String dataPermissionType;

    private Long leaderId;

    private String userNo;

    private String status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    public String getDataPermissionType() {
        return dataPermissionType;
    }

    public void setDataPermissionType(String dataPermissionType) {
        this.dataPermissionType = dataPermissionType;
    }
    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }
    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserPO{" +
        "username=" + username +
        ", password=" + password +
        ", mobile=" + mobile +
        ", jobNo=" + jobNo +
        ", nickname=" + nickname +
        ", sex=" + sex +
        ", email=" + email +
        ", position=" + position +
        ", deptId=" + deptId +
        ", dataPermissionType=" + dataPermissionType +
        ", leaderId=" + leaderId +
        ", userNo=" + userNo +
        ", status=" + status +
        "}";
    }
}
