package cn.com.yan.utils;

import cn.com.yan.entity.User;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class LoginUtils {
    public static HashMap<Long, User> userInfoMap = new HashMap<>();

    public static void put (User user){
        userInfoMap.put(user.getId(),user);
    }


    public static User get (Long id){
        return userInfoMap.get(id);
    }

    public static User remove (Long id){
        return userInfoMap.remove(id);
    }

    public static User getSessionUser(HttpSession httpSession) {
        Object user = httpSession.getAttribute("user");
        if(user == null){
            return null;
        }
        return (User) user;
    }
}
