package us.bojie.springdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import us.bojie.springdemo.entity.ResponseEntity;
import us.bojie.springdemo.entity.UserEntity;
import us.bojie.springdemo.service.UserService;
import us.bojie.springdemo.util.ResponseCode;

@RestController
@RequestMapping(value = "/user")
@Api(tags = {"Account"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestParam(value = "userName") @ApiParam("账号或手机号") String userName
            , @RequestParam(value = "password") @ApiParam("密码") String password, HttpServletRequest request) {
        List<UserEntity> list = userService.findUser(userName);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.of(ResponseCode.RC_ACCOUNT_INVALID);
        }
        UserEntity userEntity = null;
        for (UserEntity entity : list) {
            //判断密码是否正确
            if (bCryptPasswordEncoder.matches(password, entity.pwd)) {
                userEntity = entity;
                break;
            }
        }
        if (userEntity == null) {
            return ResponseEntity.of(ResponseCode.RC_PWD_INVALID);
        }
        if ("1".equals(userEntity.forbid)) {
            return ResponseEntity.of(ResponseCode.RC_USER_FORBID);
        }
        return ResponseEntity.success(userEntity).setMessage("login success");
    }

    @ApiOperation(value = "注册")
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public Object registration(@RequestParam(value = "userName") @ApiParam("账号或手机号") String userName,
                               @RequestParam(value = "password") @ApiParam("密码") String password,
                               @RequestParam(value = "imoocId") @ApiParam("慕课网用户ID") String imoocId,
                               @RequestParam(value = "orderId") @ApiParam("订单号") String orderId) {
        System.out.println("userName" + userName);
        userService.addUser(userName, bCryptPasswordEncoder.encode(password), imoocId, orderId);
        return ResponseEntity.successMessage("registration success.");
    }
}
