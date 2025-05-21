package cn.itrf.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 7723201890343801972L;
    private String userAccount;
    private String userPassword;
}
