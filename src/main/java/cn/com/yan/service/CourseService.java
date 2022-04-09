package cn.com.yan.service;

import cn.com.yan.constants.Constant;
import cn.com.yan.entity.Course;
import cn.com.yan.entity.LearnGraph;
import cn.com.yan.entity.User;
import cn.com.yan.entity.UserCourse;
import cn.com.yan.entity.dto.Ability;
import cn.com.yan.entity.dto.LearnDTO;
import cn.com.yan.entity.dto.PageInfo;
import cn.com.yan.entity.vo.CommonResult;
import cn.com.yan.mapper.CourseMapper;
import cn.com.yan.utils.GradeUtils;
import cn.com.yan.utils.LoginUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    CourseMapper courseMapper;

    public CommonResult choiceCourse(PageInfo pageInfo, Long userId) {
        Page<Course> coursePage = new Page<Course>(1,500);
        IPage<Course> courses = courseMapper.getChoiceCourses(coursePage,userId);
        return CommonResult.success(courses);
    }

    public CommonResult getMyCourse(Long userId) {
        return CommonResult.success(courseMapper.getMyCourse(new Page<Course>(1,500),userId));
    }

    public CommonResult getMygrade(Integer page, Integer size,HttpSession httpSession) {
        User user = LoginUtils.getSessionUser(httpSession);
        if(user == null){
            return CommonResult.fail("请先登录");
        }
        List<UserCourse> userCourseList = getMyGradeValues(user);
        IPage<UserCourse> userCourseIPage = new Page<>();
        userCourseIPage.setTotal(userCourseList.size());
        userCourseIPage.setRecords(userCourseList);
        CommonResult commonResult = CommonResult.success(userCourseIPage);
        return commonResult;
    }

    private List<UserCourse> getMyGradeValues(User user) {
        List<UserCourse> userCourseList = courseMapper.getMyGrade(user.getId());
        //计算所有成绩
        for(UserCourse userCourse:userCourseList){
            userCourse.setTotalScore(GradeUtils.gradeSum(userCourse.getNormalGrade(),userCourse.getEndGrade()));
            userCourse.setCourseRate(GradeUtils.getCourseRate(userCourse.getTotalScore()));
            userCourse.setRealArtAbility(GradeUtils.skillCal(userCourse.getCourseRate(),userCourse.getArtAbility()));
            userCourse.setRealCodeAbility(GradeUtils.skillCal(userCourse.getCourseRate(),userCourse.getCodeAbility()));
            userCourse.setRealPracticeAbility(GradeUtils.skillCal(userCourse.getCourseRate(),userCourse.getPracticeAbility()));
            userCourse.setRealCreateAbility(GradeUtils.skillCal(userCourse.getCourseRate(),userCourse.getCreateAbility()));
            userCourse.setRealTheoryAbility(GradeUtils.skillCal(userCourse.getCourseRate(),userCourse.getTheoryAbility()));
        }
        return userCourseList;
    }

    public CommonResult recommendCourse(HttpServletRequest request, String rate) {
        IPage<Course> iPage = new Page();
        User user = LoginUtils.getSessionUser(request.getSession());
        if(user == null){
            return CommonResult.fail("请先登录");
        }
        //获取已有课程得分
        List<UserCourse> userCourseList = getMyGradeValues(user);
        //进行评级
        String courseRate = sumAllScores(userCourseList);
        if (courseRate.equals("0.0")) {
            iPage.setTotal(0);
            iPage.setRecords(new ArrayList<>());
            return CommonResult.success(iPage);
        }

        IPage<Course> courseIPage = courseMapper.getChoiceCourses(new Page<>(1,500), user.getId());
        List<Course> courseList = courseIPage.getRecords();

                //需要推荐的课程
        List<Course> recommends = new ArrayList<>();
        List<Course> firstRecommends = new ArrayList<>();
        addRecommendCoursesList(userCourseList, courseRate, courseList, firstRecommends,rate);
        addRecommendForUserCourseList(userCourseList, courseRate, firstRecommends);
        recommends.addAll(firstRecommends);

        //需要推荐的课程
        List<Course> secondReommends = new ArrayList<>();
        courseList.removeAll(firstRecommends);
        addRecommendCoursesList(userCourseList, courseRate, courseList, secondReommends, rate);
        addRecommendForUserCourseList(userCourseList, courseRate, secondReommends);
        recommends.addAll(secondReommends);

        //需要推荐的课程
        List<Course> thirdRecommends = new ArrayList<>();
        courseList.removeAll(secondReommends);
        addRecommendCoursesList(userCourseList, courseRate, courseList, thirdRecommends, rate);
        addRecommendForUserCourseList(userCourseList, courseRate, thirdRecommends);
        recommends.addAll(thirdRecommends);

        //需要推荐的课程
        List<Course> forthRecommends = new ArrayList<>();
        courseList.removeAll(secondReommends);
        addRecommendCoursesList(userCourseList, courseRate, courseList, forthRecommends, rate);
        addRecommendForUserCourseList(userCourseList, courseRate, forthRecommends);
        recommends.addAll(forthRecommends);

        //需要推荐的课程
        List<Course> fifthRecommends = new ArrayList<>();
        courseList.removeAll(forthRecommends);
        addRecommendCoursesList(userCourseList, courseRate, courseList, fifthRecommends, rate);
        addRecommendForUserCourseList(userCourseList, courseRate, fifthRecommends);
        recommends.addAll(fifthRecommends);

        iPage.setTotal(recommends.size());
        iPage.setRecords(recommends);
        return CommonResult.success(iPage);
    }

    private void addRecommendForUserCourseList(List<UserCourse> userCourseList, String courseRate, List<Course> recommendCourses) {
        for(Course course: recommendCourses){
            UserCourse userCourse = new UserCourse();
            userCourse.setRealArtAbility(GradeUtils.skillCal(courseRate,course.getArtAbility()));
            userCourse.setRealCodeAbility(GradeUtils.skillCal(courseRate,course.getCodeAbility()));
            userCourse.setRealPracticeAbility(GradeUtils.skillCal(courseRate,course.getPracticeAbility()));
            userCourse.setRealCreateAbility(GradeUtils.skillCal(courseRate,course.getCreateAbility()));
            userCourse.setRealTheoryAbility(GradeUtils.skillCal(courseRate,course.getTheoryAbility()));
            userCourseList.add(userCourse);
        }
    }

    private void addRecommendCoursesList(List<UserCourse> userCourseList, String courseRate, List<Course> courseList, List<Course> recommendCourses, String rate) {
        //计算现在差的最多的指标
        Ability abilityGup = getAbilityGap(userCourseList,rate);
        //对数据进行分组过滤并排序
        List<Course> artList = getArtList(courseList,abilityGup);
        List<Course> practiceList = getPracticeList(courseList,abilityGup);
        List<Course> theoryList = getTheoryList(courseList,abilityGup);
        List<Course> codeList = getCodeList(courseList,abilityGup);
        List<Course> createList = getCreateList(courseList,abilityGup);

        //开始推荐
        switch (abilityGup.getAbilityGap()){
            case "code":
                addCodeAbilityRecommends(courseRate, abilityGup, codeList, recommendCourses);
                break;
            case "art":
                addArtAbility(courseRate, abilityGup, artList, recommendCourses);
                break;
            case "theory":
                addTheoryAbility(courseRate, abilityGup, theoryList, recommendCourses);
                break;
            case "create":
                addCreateAbility(courseRate, abilityGup, createList, recommendCourses);
                break;
            case "practice":
                addPracticeAbility(courseRate, abilityGup, practiceList, recommendCourses);
                break;
            default:
                break;

        }
    }

    private void addPracticeAbility(String courseRate, Ability abilityGup, List<Course> practiceList, List<Course> recommendCourses) {
        String codeGap = abilityGup.getRealPracticeAbilityTotal();
        BigDecimal total = new BigDecimal(codeGap);
        for(Course course: practiceList){
            recommendCourses.add(course);
            total = total.subtract(new BigDecimal(course.getPracticeAbility())
                            .multiply(new BigDecimal(courseRate)));
            if(total.compareTo(new BigDecimal(0))<=0){
                break;
            }
        }
    }

    private void addCreateAbility(String courseRate, Ability abilityGup, List<Course> createList, List<Course> recommendCourses) {
        String codeGap = abilityGup.getRealCreateAbilityTotal();
        BigDecimal total = new BigDecimal(codeGap);
        for(Course course: createList){
            recommendCourses.add(course);
            total = total
                    .subtract(new BigDecimal(course.getCreateAbility())
                            .multiply(new BigDecimal(courseRate)));
            if(total.compareTo(new BigDecimal(0))<=0){
                break;
            }
        }
    }

    private void addTheoryAbility(String courseRate, Ability abilityGup, List<Course> theoryList, List<Course> recommendCourses) {
        String codeGap = abilityGup.getRealTheoryAbilityTotal();
        BigDecimal total = new BigDecimal(codeGap);
        for(Course course: theoryList){
            recommendCourses.add(course);
            total = total
                    .subtract(new BigDecimal(course.getTheoryAbility())
                            .multiply(new BigDecimal(courseRate)));
            if(total.compareTo(new BigDecimal(0))<=0){
                break;
            }
        }
    }

    private void addArtAbility(String courseRate, Ability abilityGup, List<Course> artList, List<Course> recommendCourses) {
        String artGap = abilityGup.getRealArtAbilityTotal();
        BigDecimal total = new BigDecimal(artGap);
        for(Course course: artList){
            recommendCourses.add(course);
            total = total
                    .subtract(new BigDecimal(course.getArtAbility())
                            .multiply(new BigDecimal(courseRate)));
            if(total.compareTo(new BigDecimal(0))<=0){
                break;
            }
        }
    }

    private void addCodeAbilityRecommends(String courseRate, Ability abilityGup, List<Course> codeList, List<Course> recommendCourses) {
        String codeGap = abilityGup.getRealCodeAbilityTotal();
        BigDecimal total = new BigDecimal(codeGap);
        for (Course course : codeList) {
            recommendCourses.add(course);
            total = total.subtract(new BigDecimal(course.getCodeAbility())
                    .multiply(new BigDecimal(courseRate)));
            if (total.compareTo(new BigDecimal(0)) <= 0) {
                break;
            }
        }
    }

    private List<Course> getCreateList(List<Course> courseList, Ability abilityGup) {
        //不管其他满不满
        List<Course> list = courseList.stream().filter(course -> !course.getCreateAbility().equals("0")).collect(Collectors.toList());
        //不要满的数据
        List<Course> list1 = new ArrayList<>();
        for(Course course:list1) {
            if (new BigDecimal(abilityGup.getRealTheoryAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getTheoryAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealCodeAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getCodeAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealArtAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getArtAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealPracticeAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getPracticeAbility().equals("0")) {
                    continue;
                }
            }
            if (!course.getCreateAbility().equals("0")) {
                list1.add(course);
            }


        }
        if (list1.size() != 0) {
            list = list1;
            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    int size1 = 0;
                    int size2 = 0;
                    BigDecimal sum1 = new BigDecimal(0);
                    BigDecimal sum2 = new BigDecimal(0);
                    if(new BigDecimal(o1.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getCreateAbility()));
                    }
                    if(new BigDecimal(o1.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getArtAbility()));
                    }
                    if(new BigDecimal(o1.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getTheoryAbility()));
                    }
                    if(new BigDecimal(o1.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getPracticeAbility()));
                    }
                    if(new BigDecimal(o1.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getCodeAbility()));
                    }

                    if(new BigDecimal(o2.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getCreateAbility()));
                    }
                    if(new BigDecimal(o2.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getArtAbility()));
                    }
                    if(new BigDecimal(o2.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getTheoryAbility()));
                    }
                    if(new BigDecimal(o2.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getPracticeAbility()));
                    }
                    if(new BigDecimal(o2.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getCodeAbility()));
                    }
                    return size2 - size1==0?(sum2.compareTo(sum1)) == 0?new BigDecimal(o2.getCreateAbility()).compareTo(new BigDecimal(o1.getCreateAbility())):sum2.compareTo(sum1):size2 - size1;
                }
            });
        }
        else {

            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    BigDecimal b1 = new BigDecimal(o1.getArtAbility());
                    BigDecimal b2 = new BigDecimal(o2.getArtAbility());
                    return b1.compareTo(b2);
                }
            });
        }
        return list;
    }

    private List<Course> getCodeList(List<Course> courseList, Ability abilityGup) {
        List<Course> list = courseList.stream().filter(course -> !course.getCodeAbility().equals("0")).collect(Collectors.toList());
        //不要满的数据
        List<Course> list1 = new ArrayList<>();
        for(Course course:list1){
            if(new BigDecimal(abilityGup.getRealTheoryAbilityTotal()).compareTo(new BigDecimal(0))<0){
                if(!course.getTheoryAbility().equals("0")){
                    continue;
                }
            }
            if(new BigDecimal(abilityGup.getRealCreateAbilityTotal()).compareTo(new BigDecimal(0))<0){
                if(!course.getCreateAbility().equals("0")){
                    continue;
                }
            }
            if(new BigDecimal(abilityGup.getRealArtAbilityTotal()).compareTo(new BigDecimal(0))<0){
                if(!course.getArtAbility().equals("0")){
                    continue;
                }
            }
            if(new BigDecimal(abilityGup.getRealPracticeAbilityTotal()).compareTo(new BigDecimal(0))<0){
                if(!course.getPracticeAbility().equals("0")){
                    continue;
                }
            }
            if(!course.getCodeAbility().equals("0")){
                list1.add(course);
            }
        }
        if (list1.size() != 0) {
            list = list1;
            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    int size1 = 0;
                    int size2 = 0;
                    BigDecimal sum1 = new BigDecimal(0);
                    BigDecimal sum2 = new BigDecimal(0);
                    if(new BigDecimal(o1.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getCreateAbility()));
                    }
                    if(new BigDecimal(o1.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getArtAbility()));
                    }
                    if(new BigDecimal(o1.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getTheoryAbility()));
                    }
                    if(new BigDecimal(o1.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getPracticeAbility()));
                    }
                    if(new BigDecimal(o1.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getCodeAbility()));
                    }

                    if(new BigDecimal(o2.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getCreateAbility()));
                    }
                    if(new BigDecimal(o2.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getArtAbility()));
                    }
                    if(new BigDecimal(o2.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getTheoryAbility()));
                    }
                    if(new BigDecimal(o2.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getPracticeAbility()));
                    }
                    if(new BigDecimal(o2.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o1.getCodeAbility()));
                    }
                    return size2 - size1==0?(sum2.compareTo(sum1)) == 0?new BigDecimal(o2.getCodeAbility()).compareTo(new BigDecimal(o1.getCodeAbility())):sum2.compareTo(sum1):size2 - size1;
                }
            });
        }else {
            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    BigDecimal b1 = new BigDecimal(o1.getCodeAbility());
                    BigDecimal b2 = new BigDecimal(o2.getCodeAbility());
                    return b1.compareTo(b2);
                }
            });
        }
        return list;
    }

    private List<Course> getTheoryList(List<Course> courseList, Ability abilityGup) {
        List<Course> list = courseList.stream().filter(course -> !course.getTheoryAbility().equals("0")).collect(Collectors.toList());
        //不要满的数据
        List<Course> list1 = new ArrayList<>();
        for(Course course:courseList) {
            if (new BigDecimal(abilityGup.getRealCodeAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getCodeAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealCreateAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getCreateAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealArtAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getArtAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealPracticeAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getPracticeAbility().equals("0")) {
                    continue;
                }
            }
            if (!course.getTheoryAbility().equals("0")) {
                list1.add(course);
            }

        }
        if (list1.size() != 0) {
            list = list1;
            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    int size1 = 0;
                    int size2 = 0;
                    BigDecimal sum1 = new BigDecimal(0);
                    BigDecimal sum2 = new BigDecimal(0);
                    if(new BigDecimal(o1.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        sum1=sum1.add(new BigDecimal(o1.getCreateAbility()));
                        size1 = size1 + 1;
                    }
                    if(new BigDecimal(o1.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getArtAbility()));
                    }
                    if(new BigDecimal(o1.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getTheoryAbility()));
                    }
                    if(new BigDecimal(o1.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getPracticeAbility()));
                    }
                    if(new BigDecimal(o1.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getCodeAbility()));
                    }

                    if(new BigDecimal(o2.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getCodeAbility()));
                    }
                    if(new BigDecimal(o2.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getArtAbility()));
                    }
                    if(new BigDecimal(o2.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getTheoryAbility()));
                    }
                    if(new BigDecimal(o2.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getPracticeAbility()));
                    }
                    if(new BigDecimal(o2.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getCodeAbility()));
                    }
                    return size2 - size1==0?((sum2.compareTo(sum1)) == 0?new BigDecimal(o2.getTheoryAbility()).compareTo(new BigDecimal(o1.getTheoryAbility())):sum2.compareTo(sum1)):size2 - size1;
                }
            });
        }else {

            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    BigDecimal b1 = new BigDecimal(o1.getTheoryAbility());
                    BigDecimal b2 = new BigDecimal(o2.getTheoryAbility());
                    return b1.compareTo(b2);
                }
            });
        }
        return list;
    }

    private List<Course> getPracticeList(List<Course> courseList, Ability abilityGup) {
        List<Course> list = courseList.stream().filter(course -> !course.getPracticeAbility().equals("0")).collect(Collectors.toList());
        //不要满的数据
        List<Course> list1 = new ArrayList<>();
        for(Course course:courseList) {
            if (new BigDecimal(abilityGup.getRealTheoryAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getTheoryAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealCreateAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getCreateAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealArtAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getArtAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealCodeAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getCodeAbility().equals("0")) {
                    continue;
                }
            }
            if (!course.getPracticeAbility().equals("0")) {
                list1.add(course);
            }
        }
        if (list1.size() != 0) {
            list = list1;
        }
        if (list1.size() != 0) {
            list = list1;
            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    int size1 = 0;
                    int size2 = 0;
                    BigDecimal sum1 = new BigDecimal(0);
                    BigDecimal sum2 = new BigDecimal(0);
                    if(new BigDecimal(o1.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getCreateAbility()));
                    }
                    if(new BigDecimal(o1.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getArtAbility()));
                    }
                    if(new BigDecimal(o1.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getTheoryAbility()));
                    }
                    if(new BigDecimal(o1.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getPracticeAbility()));
                    }
                    if(new BigDecimal(o1.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1=sum1.add(new BigDecimal(o1.getCodeAbility()));
                    }

                    if(new BigDecimal(o2.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getCreateAbility()));
                    }
                    if(new BigDecimal(o2.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getArtAbility()));
                    }
                    if(new BigDecimal(o2.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getTheoryAbility()));
                    }
                    if(new BigDecimal(o2.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getPracticeAbility()));
                    }
                    if(new BigDecimal(o2.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2=sum2.add(new BigDecimal(o2.getCodeAbility()));
                    }
                    return size2 - size1==0?(sum2.compareTo(sum1)) == 0?new BigDecimal(o2.getPracticeAbility()).compareTo(new BigDecimal(o1.getPracticeAbility())):sum2.compareTo(sum1):size2 - size1;
                }
            });
        }else {
            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    BigDecimal b1 = new BigDecimal(o1.getPracticeAbility());
                    BigDecimal b2 = new BigDecimal(o2.getPracticeAbility());
                    return b1.compareTo(b2);
                }
            });
        }
        return list;
    }

    private List<Course> getArtList(List<Course> courseList, Ability abilityGup) {
        List<Course> list = courseList.stream().filter(course -> !course.getArtAbility().equals("0")).collect(Collectors.toList());
        //不要满的数据
        List<Course> list1 = new ArrayList<>();
        for(Course course:list1) {
            if (new BigDecimal(abilityGup.getRealTheoryAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getTheoryAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealCreateAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getCreateAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealCodeAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getCodeAbility().equals("0")) {
                    continue;
                }
            }
            if (new BigDecimal(abilityGup.getRealPracticeAbilityTotal()).compareTo(new BigDecimal(0)) < 0) {
                if (!course.getPracticeAbility().equals("0")) {
                    continue;
                }
            }
            if (!course.getArtAbility().equals("0")) {
                list1.add(course);
            }

            if (list1.size() != 0) {
                list1 = list1;
            }
        }
        if (list1.size() != 0) {
            list = list1;
            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    int size1 = 0;
                    int size2 = 0;
                    BigDecimal sum1 = new BigDecimal(0);
                    BigDecimal sum2 = new BigDecimal(0);
                    if(new BigDecimal(o1.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1 = sum1.add(new BigDecimal(o1.getCreateAbility()));
                    }
                    if(new BigDecimal(o1.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1 = sum1.add(new BigDecimal(o1.getArtAbility()));
                    }
                    if(new BigDecimal(o1.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1 = sum1.add(new BigDecimal(o1.getTheoryAbility()));
                    }
                    if(new BigDecimal(o1.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1 = sum1.add(new BigDecimal(o1.getPracticeAbility()));
                    }
                    if(new BigDecimal(o1.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size1 = size1 + 1;
                        sum1 = sum1.add(new BigDecimal(o1.getCodeAbility()));
                    }

                    if(new BigDecimal(o2.getCreateAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2 = sum2.add(new BigDecimal(o2.getCreateAbility()));
                    }
                    if(new BigDecimal(o2.getArtAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2 = sum2.add(new BigDecimal(o2.getArtAbility()));
                    }
                    if(new BigDecimal(o2.getTheoryAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2 = sum2.add(new BigDecimal(o2.getTheoryAbility()));
                    }
                    if(new BigDecimal(o2.getPracticeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2 = sum2.add(new BigDecimal(o2.getPracticeAbility()));
                    }
                    if(new BigDecimal(o2.getCodeAbility()).compareTo(new BigDecimal("0")) == 1){
                        size2 = size2 + 1;
                        sum2 = sum2.add(new BigDecimal(o2.getCodeAbility()));
                    }
                    return size2 - size1==0?(sum2.compareTo(sum1)) == 0?new BigDecimal(o2.getArtAbility()).compareTo(new BigDecimal(o1.getArtAbility())):sum2.compareTo(sum1):size2 - size1;
                }
            });
        }else {
            list.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    BigDecimal b1 = new BigDecimal(o1.getArtAbility());
                    BigDecimal b2 = new BigDecimal(o2.getArtAbility());
                    return b1.compareTo(b2);
                }
            });
        }
        return list;
    }

    /**
     * 获取能力差值最多的能力
     * @param userCourseList
     * @param rate
     * @return
     */
    private Ability getAbilityGap(List<UserCourse> userCourseList, String rate) {
        if(rate == null || rate.equals("")){
            rate = "1";
        }
        BigDecimal art = new BigDecimal(0);
        BigDecimal practice = new BigDecimal(0);
        BigDecimal theory = new BigDecimal(0);
        BigDecimal code = new BigDecimal(0);
        BigDecimal create = new BigDecimal(0);
        for(UserCourse userCourse:userCourseList){
            art = art.add(new BigDecimal(userCourse.getRealArtAbility()));
            practice = practice.add(new BigDecimal(userCourse.getRealPracticeAbility()));
            theory = theory.add(new BigDecimal(userCourse.getRealTheoryAbility()));
            code = code.add(new BigDecimal(userCourse.getRealCodeAbility()));
            create = create.add(new BigDecimal(userCourse.getRealCreateAbility()));
        }
        //计算插值
        art = new BigDecimal(Constant.ART).multiply(new BigDecimal(rate)).subtract(art);
        practice = new BigDecimal(Constant.PRACTICE).multiply(new BigDecimal(rate)).subtract(practice);
        theory = new BigDecimal(Constant.THEORY).multiply(new BigDecimal(rate)).subtract(theory);
        code = new BigDecimal(Constant.CODE).multiply(new BigDecimal(rate)).subtract(code);
        create = new BigDecimal(Constant.CREATE).multiply(new BigDecimal(rate)).subtract(create);

        //5个值进行比较大小
        int artValue = art.setScale(1,RoundingMode.HALF_UP).intValue();
        int practiceValue = practice.setScale(1,RoundingMode.HALF_UP).intValue();
        int theoryValue = theory.setScale(1,RoundingMode.HALF_UP).intValue();
        int codeValue = code.setScale(1,RoundingMode.HALF_UP).intValue();
        int createValue = create.setScale(1,RoundingMode.HALF_UP).intValue();

        //进行排序，取最大差值的数据
        String value ="";
        int compareValue = 0;
        if(compareValue<artValue){
            value = Constant.ART_CODE;
            compareValue = artValue;
        }
        if(compareValue<practiceValue){
            value = Constant.PRACTICE_CODE;
            compareValue = practiceValue;
        }
        if(compareValue<theoryValue){
            value = Constant.THEORY_CODE;
            compareValue = theoryValue;
        }
        if(compareValue<codeValue){
            value = Constant.CODE_CODE;
            compareValue = codeValue;
        }
        if(compareValue<createValue){
            value = Constant.CREATE_CODE;
            compareValue = createValue;
        }
        Ability ability = new Ability(theory.setScale(2,RoundingMode.HALF_UP).toPlainString(),
                code.setScale(2,RoundingMode.HALF_UP).toPlainString(),
                practice.setScale(2,RoundingMode.HALF_UP).toPlainString(),
                create.setScale(2,RoundingMode.HALF_UP).toPlainString(),
                art.setScale(2,RoundingMode.HALF_UP).toPlainString(),
                value);
        return ability;

    }

    private String sumAllScores(List<UserCourse> userCourseList) {
        BigDecimal totalScores = new BigDecimal("0");
        if(userCourseList.size() != 0) {
            for (UserCourse userCourse : userCourseList) {
                totalScores = totalScores.add(new BigDecimal(userCourse.getTotalScore()));
            }
            totalScores = totalScores.divide(new BigDecimal(userCourseList.size()));
        }
        return  GradeUtils.getCourseRate(totalScores.setScale(1,RoundingMode.HALF_UP).toPlainString());
    }

    public CommonResult addCourses(Long valueOf, String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        courseMapper.addCourses(valueOf,idList);
        return CommonResult.success("选择成功");
    }

    public CommonResult cancelCourses(Long valueOf, String id) {
        List<String> idList = Arrays.asList(id.split(","));
        courseMapper.cancelCourses(valueOf,idList);
        return CommonResult.success("选择成功");
    }

    public CommonResult learnInfo(User user) {
        List<UserCourse> userCourseList = getMyGradeValues(user);

        LearnDTO theory = new LearnDTO("工程知识");
        LearnDTO code = new LearnDTO("设计/开发解决方案");
        LearnDTO practice = new LearnDTO("研究");
        LearnDTO create = new LearnDTO("个人和团队");
        LearnDTO art = new LearnDTO("项目管理");
        List<LearnDTO> learnDTOs = new ArrayList<>();

        learnDTOs.add(theory);
        learnDTOs.add(code);
        learnDTOs.add(practice);
        learnDTOs.add(create);
        learnDTOs.add(art);

        for(UserCourse userCourse:userCourseList){
            theory.setValue(new BigDecimal(userCourse.getRealTheoryAbility()).add(new BigDecimal(theory.getValue())).setScale(1,RoundingMode.HALF_UP).toPlainString());
            code.setValue(new BigDecimal(userCourse.getRealCodeAbility()).add(new BigDecimal(code.getValue())).setScale(1,RoundingMode.HALF_UP).toPlainString());
            practice.setValue(new BigDecimal(userCourse.getRealPracticeAbility()).add(new BigDecimal(practice.getValue())).setScale(1,RoundingMode.HALF_UP).toPlainString());
            create.setValue(new BigDecimal(userCourse.getRealCreateAbility()).add(new BigDecimal(create.getValue())).setScale(1,RoundingMode.HALF_UP).toPlainString());
            art.setValue(new BigDecimal(userCourse.getRealArtAbility()).add(new BigDecimal(art.getValue())).setScale(1,RoundingMode.HALF_UP).toPlainString());
        }
        learnDTOs.stream().forEach(learnDTO -> {
            learnDTO.setValue(new BigDecimal(learnDTO.getValue()).divide(new BigDecimal("100")).setScale(3,BigDecimal.ROUND_HALF_UP).toPlainString());
        });
        learnDTOs.sort(new Comparator<LearnDTO>() {
            @Override
            public int compare(LearnDTO o1, LearnDTO o2) {
                return new BigDecimal(o1.getValue()).compareTo(new BigDecimal(o2.getValue()));
            }
        });

        List<String> xValue = new ArrayList<>();
        List<String> yValue = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        learnDTOs.stream().forEach(learnDTO -> {
            xValue.add(learnDTO.getName());
            yValue.add(learnDTO.getValue());
            builder.append(learnDTO.getName()).append(",");
        });
        LearnGraph learnGraph = new LearnGraph(xValue,yValue,builder.toString(),user.getSuggest());
        return CommonResult.success(learnGraph);
    }
}
