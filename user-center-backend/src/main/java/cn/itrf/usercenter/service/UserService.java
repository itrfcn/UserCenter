package cn.itrf.usercenter.service;

import cn.itrf.usercenter.model.domain.SafetyUser;
import cn.itrf.usercenter.model.domain.User;
import cn.itrf.usercenter.model.request.UpdateUserRequest;
import cn.itrf.usercenter.model.request.UserSearchRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author admin
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-05-15 15:14:09
*/
public interface UserService extends IService<User> {
    SafetyUser userLogin(String userAccount, String userPassword, HttpServletRequest request);
    /**
     *
     * @param userAccount 账号
     * @param password 密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    long userRegister(String userAccount, String password, String checkPassword);

    List<SafetyUser> queryUsers(UserSearchRequest selectUser, HttpServletRequest request);

    Boolean deleteUser(long id, HttpServletRequest request);

    SafetyUser currentUser(HttpServletRequest request);

    Boolean userLogout(HttpServletRequest request);

    Boolean updateUser(UpdateUserRequest user, HttpServletRequest request);


    Boolean AdminUpdateUser(UpdateUserRequest inuser ,HttpServletRequest request);
}

