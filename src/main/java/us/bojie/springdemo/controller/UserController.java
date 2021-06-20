package us.bojie.springdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import us.bojie.springdemo.service.UserService;

@RestController
@RequestMapping(value = "/user")
@Api(tags = {"Account"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ApiOperation(value = "注册")
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public Object registration(@RequestParam(value = "userName") @ApiParam("账号或手机号") String userName,
                               @RequestParam(value = "password") @ApiParam("密码") String password,
                               @RequestParam(value = "imoocId") @ApiParam("慕课网用户ID") String imoocId,
                               @RequestParam(value = "orderId") @ApiParam("订单号") String orderId) {
        System.out.println("userName" + userName);
        userService.addUser(userName, bCryptPasswordEncoder.encode(password), imoocId, orderId);
        return "registration success.";
    }
}
