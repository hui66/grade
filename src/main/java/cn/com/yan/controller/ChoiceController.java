package cn.com.yan.controller;

import cn.com.yan.entity.User;
import cn.com.yan.entity.dto.PageInfo;
import cn.com.yan.entity.vo.CommonResult;
import cn.com.yan.service.CourseService;
import cn.com.yan.utils.LoginUtils;
import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("choice")
@Api(tags = "自主选课")
public class ChoiceController {
    @Autowired
    CourseService courseService;

    /**
     * 全局的去掉现有的课程为所有的课程
     * @return
     */
    @PostMapping("/get")
    public CommonResult choiceCourse(@RequestBody PageInfo pageInfo, HttpServletRequest request){
        User user = LoginUtils.getSessionUser(request.getSession());
        if(user == null){
            return CommonResult.fail("请先登录");
        }
        return courseService.choiceCourse(pageInfo,Long.valueOf(user.getId()));
    }

    /**
     * 全局的去掉现有的课程为所有的课程
     * @return
     */
    @PostMapping("/add")
    public CommonResult addCourse(HttpServletRequest request,@RequestBody PageInfo pageInfo){
        User user = LoginUtils.getSessionUser(request.getSession());
        if(user == null){
            return CommonResult.fail("请先登录");
        }
        return courseService.addCourses(Long.valueOf(user.getId()),pageInfo.getId());
    }

    /**
     * 取消指定课程
     * @return
     */
    @PostMapping("/cancel")
    public CommonResult cancel(HttpServletRequest request,@RequestBody PageInfo pageInfo){
        User user = LoginUtils.getSessionUser(request.getSession());
        if(user == null){
            return CommonResult.fail("请先登录");
        }
        return courseService.cancelCourses(Long.valueOf(user.getId()),pageInfo.getId());
    }
}
