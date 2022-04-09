package cn.com.yan.service;

import cn.com.yan.entity.User;
import cn.com.yan.entity.dto.LoginUser;
import cn.com.yan.entity.vo.CommonResult;
import cn.com.yan.mapper.LoginMapper;
import cn.com.yan.mapper.MenuMapper;
import cn.com.yan.utils.LoginUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService {
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    MenuMapper menuMapper;

    public CommonResult login(LoginUser loginUser, HttpSession httpSession) {
        User user = loginMapper.getUser(loginUser);
        if(user == null){
            return CommonResult.fail("登录失败");
        }
        LoginUtils.put(user);
        List<String> menus = menuMapper.getMenus(user.getRole());
        user.setMenus(menus);
        httpSession.setAttribute("user",user);
        return CommonResult.success(user);
    }

    public CommonResult logout(Long id) {
        LoginUtils.remove(id);
        return CommonResult.success("退出成功");
    }

    public CommonResult getUserInfo(HttpSession httpSession) {
        User user = LoginUtils.getSessionUser(httpSession);
        return CommonResult.success(user);
    }

    public CommonResult getUserInfos(HttpSession httpSession) {
        User user = LoginUtils.getSessionUser(httpSession);
        List<User> list = new ArrayList<>();
        list.add(user);
        IPage<User> page = new Page<>();
        page.setRecords(list);
        page.setTotal(1);
        return CommonResult.success(page);
    }
}
