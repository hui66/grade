package cn.com.yan.mapper;

import cn.com.yan.entity.User;
import cn.com.yan.entity.dto.LoginUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    User getUser(LoginUser loginUser);
}
