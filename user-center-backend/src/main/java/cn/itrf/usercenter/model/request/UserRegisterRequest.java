package cn.itrf.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 7859313095868872507L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
