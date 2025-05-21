package cn.itrf.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserSearchRequest implements Serializable {
    private static final long serialVersionUID = 839110220299831171L;

    private Long id;

    private String username;

    private String userAccount;

    private Integer gender;

    private String phone;

    private String email;

    private Integer userStatus;

    private Date createTime;

    private Date updateTime;

    private Integer userRole;

}
