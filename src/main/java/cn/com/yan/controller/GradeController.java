package cn.com.yan.controller;

import cn.com.yan.entity.vo.CommonResult;
import cn.com.yan.service.CourseService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("grade")
public class GradeController {
    @Autowired
    CourseService courseService;

    @PostMapping("/")
    public CommonResult getMyGrade(Integer page, Integer size, HttpSession httpSession){
        return courseService.getMygrade(page,size,httpSession);
    }

    @PostMapping("/infomation")
    public CommonResult MyInfomation(Integer page, Integer size, HttpSession httpSession){
        return courseService.getMygrade(page,size,httpSession);
    }
}
