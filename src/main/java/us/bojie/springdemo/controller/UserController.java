package us.bojie.springdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.bojie.springdemo.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public Object registration(@RequestParam(value = "userName") String userName,
                               @RequestParam(value = "password") String password,
                               @RequestParam(value = "imoocId") String imoocId,
                               @RequestParam(value = "orderId") String orderId) {
        System.out.println("userName" + userName);
        userService.addUser(userName, bCryptPasswordEncoder.encode(password), imoocId, orderId);
        return "registration success.";
    }
}
