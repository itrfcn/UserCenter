package cn.itrf.usercenter.model.request;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UpdateUserRequest implements Serializable {
    private static final long serialVersionUID = -9147742139489245237L;
    private Long id;
    private String username;
    private String userAccount;
    private String avatarUrl;
    private Integer gender;
    private String userPassword;
    private String phone;
    private String email;
    private Integer userStatus;
    private Integer userRole;
}
