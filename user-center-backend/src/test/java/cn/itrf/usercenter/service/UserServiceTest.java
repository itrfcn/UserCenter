package cn.itrf.usercenter.service;
import cn.itrf.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/*
 * 用户服务测试
 *
 * @author hza
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("itrf");
        user.setUserAccount("123");
        user.setAvatarUrl("https://img1.baidu.com/it/u=1645832847,2375824523&fm=253&fmt=auto&app=138&f=JPEG?w=480&h=480");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setEmail("123");
        user.setPhone("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }
    @Test
    void userRegister() {
        String userAccount = "itrf";
        String userPassword = "WASDwasd@123456";
        String checkPassword = "WASDwasd@123456";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result>0);
//
//        userAccount = "yu";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "itrf";
//        userPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "it rf";
//        userPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "dogitrf";
//        checkPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "itrf";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertTrue(result > 0);
    }
}