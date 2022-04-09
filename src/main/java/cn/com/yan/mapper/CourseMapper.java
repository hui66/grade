package cn.com.yan.mapper;

import cn.com.yan.entity.Course;
import cn.com.yan.entity.UserCourse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    IPage<Course> getChoiceCourses(Page<Course> coursePage, @Param("userId") Long userId);

    IPage<Course> getMyCourse(Page<Course> coursePage, @Param("userId")Long userId);

    List<UserCourse> getMyGrade(Long id);

    List<Course> getRecommend(Long id);

    void addCourses(@Param("userId") Long userId, @Param("idList") List<String> idList);

    void cancelCourses(@Param("userId")Long userId, @Param("idList")List<String> idList);
}
