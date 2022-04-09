package cn.com.yan.controller;

import cn.com.yan.entity.User;
import cn.com.yan.entity.vo.CommonResult;
import cn.com.yan.service.CourseService;
import cn.com.yan.utils.LoginUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "我的课程")
@RestController
@RequestMapping("mycourse")
public class MychoiceController {
    @Autowired
    CourseService courseService;

    @PostMapping("get")
    public CommonResult mycourse(HttpServletRequest request){
        User user = LoginUtils.getSessionUser(request.getSession());
        if(user == null){
            return CommonResult.fail("请先登录");
        }
        return courseService.getMyCourse(Long.valueOf(user.getId()));
    }
}
