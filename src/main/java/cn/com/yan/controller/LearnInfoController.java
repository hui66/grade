package cn.com.yan.controller;

import cn.com.yan.entity.User;
import cn.com.yan.entity.dto.PageInfo;
import cn.com.yan.entity.vo.CommonResult;
import cn.com.yan.service.CourseService;
import cn.com.yan.utils.LoginUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("learnInfo")
@Api(tags = "学情分析")
public class LearnInfoController {
    @Autowired
    CourseService courseService;

    /**
     * 学情分析
     * @return
     */
    @PostMapping("/get")
    public CommonResult learnInfo(HttpServletRequest request){
        User user = LoginUtils.getSessionUser(request.getSession());
        if(user == null){
            return CommonResult.fail("请先登录");
        }
        return courseService.learnInfo(user);
    }
}
