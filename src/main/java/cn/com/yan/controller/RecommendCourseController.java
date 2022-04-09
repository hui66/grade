package cn.com.yan.controller;

import cn.com.yan.entity.dto.PageInfo;
import cn.com.yan.entity.vo.CommonResult;
import cn.com.yan.service.CourseService;
import cn.com.yan.service.RecommendService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("recommend")
@Api(tags = "课程推荐")
public class RecommendCourseController {
    @Autowired
    CourseService recommendService;

    @PostMapping("course")
    public CommonResult recommendCourse(HttpServletRequest request, @RequestBody PageInfo pageInfo){
        return recommendService.recommendCourse(request,pageInfo.getRate());
    }
}
