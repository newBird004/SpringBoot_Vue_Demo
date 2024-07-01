package com.lantu.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lantu.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2024-06-29
 */
public interface UserService extends IService<User> {
    Map<String, Object> login(User user);
    public Map<String, Object> getUserInfo(String token);
    public void logout(String token);

}
