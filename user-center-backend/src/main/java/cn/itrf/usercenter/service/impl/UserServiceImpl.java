package cn.itrf.usercenter.service.impl;

import cn.itrf.usercenter.Mapper.UserMapper;
import cn.itrf.usercenter.common.ErrorCode;
import cn.itrf.usercenter.exception.BusinessException;
import cn.itrf.usercenter.model.domain.SafetyUser;
import cn.itrf.usercenter.model.domain.User;
import cn.itrf.usercenter.model.request.UpdateUserRequest;
import cn.itrf.usercenter.model.request.UserSearchRequest;
import cn.itrf.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static cn.itrf.usercenter.constant.UserConstant.*;

/**
 * @author itrf
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-05-15 15:14:09
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private  UserMapper userMapper;

    @Override
    public SafetyUser userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        checkAP(userAccount, userPassword);
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper1);
        if (count == 0) {
            log.info("登录账号不存在");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不存在");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("密码错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        }
        if(user.getUserStatus() == 1){
            log.info("当前账号封禁中");
            throw new BusinessException(ErrorCode.NO_AUTH,"账号已被封");
        }
        //脱敏
        SafetyUser safetyUser=getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //校验
        checkAP(userAccount, userPassword);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            log.info("注册账号已存在");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在");
        }

        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean result = this.save(user);
        if(!result){
            log.info("注册账号失败");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"账号创建失败");
        }
        return user.getId();
    }

    @Override
    public List<SafetyUser> queryUsers(UserSearchRequest selectUser, HttpServletRequest request) {
        if(!isAdmin(request)){
            log.info("越权行为，非管理员请求全部用户数据");
            throw new BusinessException(ErrorCode.NO_AUTH,"非管理员");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 动态添加查询条件
        if (selectUser.getId() != null) {
            queryWrapper.eq("id", selectUser.getId());
        }
        if (selectUser.getUsername() != null && !selectUser.getUsername().isEmpty()) {
            queryWrapper.like("username", selectUser.getUsername());
        }
        if (selectUser.getUserAccount() != null && !selectUser.getUserAccount().isEmpty()) {
            queryWrapper.eq("userAccount", selectUser.getUserAccount());
        }
        if (selectUser.getEmail() != null && !selectUser.getEmail().isEmpty()) {
            queryWrapper.eq("email", selectUser.getEmail());
        }
        if (selectUser.getPhone() != null && !selectUser.getPhone().isEmpty()) {
            queryWrapper.eq("phone", selectUser.getPhone());
        }
        if (selectUser.getUserStatus() != null) {
            queryWrapper.eq("userStatus", selectUser.getUserStatus());
        }
        if (selectUser.getUserRole() != null) {
            queryWrapper.eq("userRole", selectUser.getUserRole());
        }
        if (selectUser.getGender() != null) {
            queryWrapper.eq("gender", selectUser.getGender());
        }
        if (selectUser.getCreateTime() != null) {
            queryWrapper.eq("createTime", selectUser.getCreateTime());
        }
        if (selectUser.getUpdateTime() != null) {
            queryWrapper.eq("updateTime", selectUser.getUpdateTime());
        }
        // 调用 Mapper 查询
        List<User> userList = userMapper.selectList(queryWrapper);
        List<SafetyUser> safetyUserList = new ArrayList<>();
        for(User user:userList){
            SafetyUser safetyUser = getSafetyUser(user);
            safetyUserList.add(safetyUser);
        }
        return safetyUserList;
    }

    @Override
    public Boolean deleteUser(long id,HttpServletRequest request) {
        if(!isAdmin(request)){
            log.info("越权行为，非管理员删除账号");
            throw new BusinessException(ErrorCode.NO_AUTH,"非管理员");
        }
        if(id<0){
            log.info("删除账号的id小于0");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id小于0");
        }
        if(id==0){
            log.info("越权行为，试图删除超级管理员账号");
            throw new BusinessException(ErrorCode.NO_AUTH,"超级管理员禁止操作");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            log.info("要删除的账号不存在或已被删除");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public SafetyUser currentUser(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(USER_LOGIN_STATE);
        SafetyUser safetyUser = (SafetyUser) obj;
        long id = safetyUser.getId();
        User user = userMapper.selectById(id);
        if(user == null){
            log.info("未登录账号");
            throw new BusinessException(ErrorCode.NOT_LOGIN,"未登录");
        }
        return getSafetyUser(user);

    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        if(request.getSession().getAttribute(USER_LOGIN_STATE) == null){
            log.info("未登录账号");
            throw new BusinessException(ErrorCode.NOT_LOGIN,"未登录");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public Boolean updateUser(UpdateUserRequest inuser, HttpServletRequest request) {
        if (inuser == null) {
            log.info("更新参数为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (inuser.getId() == 0) {
            log.info("越权行为，试图更新超级管理员账号");
            throw new BusinessException(ErrorCode.NO_AUTH, "超级管理员禁止修改");
        }
        SafetyUser safetyUser = currentUser(request);
        if (!Objects.equals(safetyUser.getId(), inuser.getId())) {
            log.info("越权行为，试图更新其他账号信息");
            throw new BusinessException(ErrorCode.NO_AUTH,"越权行为，id不符");
        }
        String userAccount = safetyUser.getUserAccount();
        User user= new User();
        user.setId(inuser.getId());
        user.setUserAccount(inuser.getUserAccount());
        user.setUserPassword((inuser.getUserPassword() != null && inuser.getUserPassword().isEmpty()) ? null : inuser.getUserPassword());
        user.setEmail(inuser.getEmail());
        user.setPhone(inuser.getPhone());
        user.setAvatarUrl(inuser.getAvatarUrl());
        user.setGender(inuser.getGender());
        user.setUsername(inuser.getUsername());
        String newUserAccount = user.getUserAccount();
        checkUpdateUser(user);
        String tempAccount = userAccount + "@";
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userAccount", userAccount);
        updateWrapper.set("userAccount", tempAccount);
        userMapper.update(null, updateWrapper);
        try {
            // 2. 执行查询操作
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", newUserAccount);
            long count = userMapper.selectCount(queryWrapper);

            if (count > 0) {
                log.info("要更新的账号名已被占用");
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "此账号已被占用");
            }
        } finally {
            // 3. 恢复 userAccount 为原来的值
            UpdateWrapper<User> restoreWrapper = new UpdateWrapper<>();
            restoreWrapper.eq("userAccount", tempAccount);
            restoreWrapper.set("userAccount", userAccount);
            userMapper.update(null, restoreWrapper);
        }
        if (user.getUserPassword() != null) {
            String originPassword = user.getUserPassword();
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + originPassword).getBytes());
            user.setUserPassword(encryptPassword);
        }
        if(!updateById(user)){
            log.info("账号信息更新失败");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新错误");
        }
        return true;
    }
    @Override
    public Boolean AdminUpdateUser(UpdateUserRequest inuser ,HttpServletRequest request){
        if (inuser == null) {
            log.info("更新参数为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (inuser.getId() == 0) {
            log.info("越权行为，试图更新超级管理员账号");
            throw new BusinessException(ErrorCode.NO_AUTH, "超级管理员禁止修改");
        }
        SafetyUser safetyUser = currentUser(request);
        if (safetyUser.getUserRole()!=1){
            log.info("越权行为，非管理员试图更新其他用户信息");
            throw new BusinessException(ErrorCode.NO_AUTH,"无权限");
        }
        String userAccount = userMapper.selectById(inuser.getId()).getUserAccount();
        User user = new User();
        user.setId(inuser.getId());
        user.setUserAccount(inuser.getUserAccount());
        user.setUserPassword((inuser.getUserPassword() != null && inuser.getUserPassword().isEmpty()) ? null : inuser.getUserPassword());
        user.setEmail(inuser.getEmail());
        user.setPhone(inuser.getPhone());
        user.setAvatarUrl(inuser.getAvatarUrl());
        user.setGender(inuser.getGender());
        user.setUsername(inuser.getUsername());
        user.setUserRole(inuser.getUserRole());
        user.setUserStatus(inuser.getUserStatus());
        String newUserAccount = user.getUserAccount();
        checkUpdateUser(user);
        String tempAccount = userAccount + "@";
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userAccount", userAccount);
        updateWrapper.set("userAccount", tempAccount);
        userMapper.update(null, updateWrapper);
        try {
            // 2. 执行查询操作
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", newUserAccount);
            long count = userMapper.selectCount(queryWrapper);

            if (count > 0) {
                log.info("要更新的账号名已被占用");
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "此账号已被占用");
            }
        } finally {
            // 3. 恢复 userAccount 为原来的值
            UpdateWrapper<User> restoreWrapper = new UpdateWrapper<>();
            restoreWrapper.eq("userAccount", tempAccount);
            restoreWrapper.set("userAccount", userAccount);
            userMapper.update(null, restoreWrapper);
        }
        long id = user.getId();
        User checkuser = userMapper.selectById(id);
        if (checkuser == null) {
            log.info("要更新的账号不存在");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        int status= user.getUserStatus();
        int role= user.getUserRole();
        if(id<0){
            log.info("越权行为，id小于0");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id小于0");
        }
        if(id==0){
            log.info("越权行为，试图更改超级管理员信息");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"超级管理员禁止操作");
        }
        if (status != 0 && status != 1) {
            log.info("用户状态参数错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态参数错误");
        }
        if (role != 0 && role != 1) {
            log.info("用户权限参数错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "权限参数错误");
        }
        if (user.getUserPassword() != null) {
            String originPassword = user.getUserPassword();
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + originPassword).getBytes());
            user.setUserPassword(encryptPassword);
        }
        if(!updateById(user)){
            log.info("账号更新失败");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新错误");
        }
        return true;

    }
    private boolean checkAP(String userAccount, String userPassword) {
        // 校验账号密码是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            log.info("账号或密码为空");
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号或密码为空");
        }

        // 校验账号长度
        if (userAccount.length() < 4 || userAccount.length() > 20) {
            log.info("账号长度不符合标准");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度必须在4到20位之间");
        }

        // 校验账号是否包含特殊字符
        if (Pattern.compile("\\pP|\\pS|\\s+").matcher(userAccount).find()) {
            log.info("账号存在特殊字符");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }

        // 校验密码长度
        if (userPassword.length() < 8 || userPassword.length() > 20) {
            log.info("密码长度不符合要求");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度必须在8到20位之间");
        }

        // 校验密码是否符合复杂性要求
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct}).{8,20}$";
        if (!Pattern.compile(passwordPattern).matcher(userPassword).find()) {
            log.info("密码不符合复杂性要求");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码必须包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符");
        }

        return true;
    }

    private void checkUpdateUser(User user){
        String username=user.getUsername();
        String email=user.getEmail();
        String phone=user.getPhone();
        String userAccount=user.getUserAccount();
        String userPassword=user.getUserPassword();
        String avatatUrl=user.getAvatarUrl();
        int gender=user.getGender();
        if (userPassword != null ) {
            // 校验密码长度
            if (userPassword.length() < 8 || userPassword.length() > 20) {
                log.info("密码长度不符合要求");
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度必须在8到20位之间");
            }

            // 校验密码是否符合复杂性要求
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct}).{8,20}$";
            if (!Pattern.compile(passwordPattern).matcher(userPassword).find()) {
                log.info("密码不符合复杂性要求");
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码必须包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符");
            }
        }
        // 校验账号长度
        if (userAccount.length() < 4 || userAccount.length() > 20) {
            log.info("账号长度不符合要求");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度必须在4到20位之间");
        }

        // 校验账号是否包含特殊字符
        if (Pattern.compile("\\pP|\\pS|\\s+").matcher(userAccount).find()) {
            log.info("账号含有特殊字符");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
        if (username.length() < 2 || username.length() > 6) {
            log.info("用户名长度不符合要求");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名长度必须在2到6位之间");
        }
        String EMAIL_REGEX = "^(?![^@]+@[^@]+@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!(email != null && email.matches(EMAIL_REGEX))){
            log.info("邮箱格式错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"邮箱不符合格式");
        }
        String PHONE_REGEX = "^1[3-9]\\d{9}$";
        if (!(phone != null && phone.matches(PHONE_REGEX))){
            log.info("手机号格式错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"手机号不符合格式");
        }
        if (gender != 0 && gender != 1) {
            log.info("性别参数错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别参数错误");
        }
        String URL_REGEX = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?(\\?[^\\s]*)?$";
        if (!(avatatUrl != null && avatatUrl.matches(URL_REGEX))){
            log.info("头像参数错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"头像参数错误");
        }

    }



    private boolean isAdmin(HttpServletRequest request){
        Object obj = request.getSession().getAttribute(USER_LOGIN_STATE);
        SafetyUser user = (SafetyUser) obj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
    private SafetyUser getSafetyUser(User originUser){
        SafetyUser safetyUser = new SafetyUser();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        return safetyUser;
    }

}




