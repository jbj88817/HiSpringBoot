package us.bojie.springdemo.controller;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import us.bojie.springdemo.config.NeedLogin;
import us.bojie.springdemo.entity.ResponseEntity;
import us.bojie.springdemo.entity.UserEntity;
import us.bojie.springdemo.service.UserService;
import us.bojie.springdemo.util.DataUtil;
import us.bojie.springdemo.util.ResponseCode;
import us.bojie.springdemo.util.UserRedisUtil;

@RestController
@RequestMapping(value = "/user")
@Api(tags = {"Account"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private StringRedisTemplate redisTemplate;

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
        UserRedisUtil.addUser(redisTemplate, request.getSession(), userEntity);
        return ResponseEntity.success(UserRedisUtil.getKey(request.getSession())).setMessage("login success.");
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

    @NeedLogin
    @ApiOperation(value = "登出")
    @RequestMapping(value = "/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserRedisUtil.removeUser(redisTemplate, session);
        return ResponseEntity.successMessage("logout success.");
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户列表")
    public ResponseEntity getUserList(@RequestParam(value = "pageIndex", defaultValue = "1") @ApiParam("起始页码从1开始") int pageIndex
            , @RequestParam(value = "pageSize") @ApiParam("每页显示的数量") int pageSize
    ) {
        PageHelper.startPage(pageIndex, pageSize);
        List<UserEntity> list = userService.getUserList();
        return ResponseEntity.success(DataUtil.getPageData(list));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation("用户管理")
    public ResponseEntity updateUser(@ApiParam(name = "用户ID") @PathVariable String id
            , @RequestParam(value = "forbid") @ApiParam(name = "是否禁止") String forbid) {
        userService.updateUser(id, forbid);
        return ResponseEntity.successMessage("操作成功");
    }
}
