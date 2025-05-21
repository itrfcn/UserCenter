package cn.itrf.usercenter.controller;


import cn.itrf.usercenter.common.BaseResponse;
import cn.itrf.usercenter.common.ResultUtil;
import cn.itrf.usercenter.model.domain.SafetyUser;
import cn.itrf.usercenter.model.request.UpdateUserRequest;
import cn.itrf.usercenter.model.request.UserLoginRequest;
import cn.itrf.usercenter.model.request.UserRegisterRequest;
import cn.itrf.usercenter.model.request.UserSearchRequest;
import cn.itrf.usercenter.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController  //这个类里面所有的请求的接口返回值，响应的数据类型都是application json
@RequestMapping("/user")
//@CrossOrigin(origins = {"http://em.z7z.me"},allowCredentials = "true")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("/login")
    public BaseResponse<SafetyUser> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        SafetyUser result = userService.userLogin(userAccount,userPassword,request);
        return ResultUtil.sucess(result,"登录成功");
    }

    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        String userPassword = userRegisterRequest.getUserPassword();
        String userAccount = userRegisterRequest.getUserAccount();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtil.sucess(result,"注册成功");

    }
    @PostMapping("/search")
    public BaseResponse<List<SafetyUser> > search(@RequestBody UserSearchRequest userSearchRequest, HttpServletRequest request) {
        List<SafetyUser> result= userService.queryUsers(userSearchRequest,request);
        return ResultUtil.sucess(result,"查询成功");
    }
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(long id,HttpServletRequest request) {
        Boolean result = userService.deleteUser(id,request);
        return ResultUtil.sucess(result,"删除成功");
    }
    @GetMapping("current")
    public BaseResponse<SafetyUser> current(HttpServletRequest request){
        SafetyUser result = userService.currentUser(request);
        return ResultUtil.sucess(result);
    }
    @GetMapping("logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        Boolean reslut = userService.userLogout(request);
        return ResultUtil.sucess(reslut,"退出成功");
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UpdateUserRequest updateUserRequest,HttpServletRequest request){
        Boolean result = userService.updateUser(updateUserRequest,request);
        return ResultUtil.sucess(result,"更新成功");
    }
    @PostMapping("/updateAdmin")
    public BaseResponse<Boolean> updateUserAdmin(@RequestBody UpdateUserRequest updateUserRequest,HttpServletRequest request){
        Boolean result = userService.AdminUpdateUser(updateUserRequest,request);
        return ResultUtil.sucess(result,"更新成功");
    }
}
