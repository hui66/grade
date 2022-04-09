package cn.com.yan.controller;

import cn.com.yan.entity.User;
import cn.com.yan.entity.dto.LoginUser;
import cn.com.yan.entity.vo.CommonResult;
import cn.com.yan.service.LoginService;
import cn.com.yan.utils.LoginUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/")
@Api(tags = "登录")
public class LoginController {
    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public CommonResult login(@RequestBody LoginUser loginUser, HttpSession httpSession){
        return loginService.login(loginUser,httpSession);
    }

    @PostMapping("/user/info")
    public CommonResult getUserInfo(HttpSession httpSession){
        return loginService.getUserInfo(httpSession);
    }

    @PostMapping("/user/infos")
    public CommonResult getUserInfos(HttpSession httpSession){
        return loginService.getUserInfos(httpSession);
    }

    @PostMapping("/logout")
    public CommonResult logout(HttpServletRequest request){
        User user = LoginUtils.getSessionUser(request.getSession());
        if(user == null){
            return CommonResult.fail("请先登录");
        }
        return loginService.logout(user.getId());
    }

}
