package cn.itrf.usercenter;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.itrf.usercenter.Mapper")
public class UserCenterBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterBackendApplication.class, args);
    }

}
