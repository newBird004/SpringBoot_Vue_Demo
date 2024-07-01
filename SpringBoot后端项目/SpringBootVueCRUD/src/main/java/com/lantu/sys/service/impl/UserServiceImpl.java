package com.lantu.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lantu.sys.entity.User;
import com.lantu.sys.mapper.UserMapper;
import com.lantu.sys.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2024-06-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    RedisTemplate redisTemplate;

    public Map<String, Object> login(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        wrapper.eq(User::getUsername,user.getUsername());
        wrapper.eq(User::getPassword,user.getPassword());
        User loginUser = this.getOne(wrapper);
        if(loginUser != null){
            Map<String, Object> data = new HashMap<>();
            String key = "user::" + UUID.randomUUID();
            data.put("token", key);    // 待优化，最终方案jwt
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);
            return data;
        }
        return null;
    }
    public Map<String, Object> getUserInfo(String token) {
        // 从redis查询token
        Object obj = redisTemplate.opsForValue().get(token);
        // 反序列化
        User user = JSON.parseObject(JSON.toJSONString(obj),User.class);
        if(user != null){
            Map<String, Object> data =  new HashMap<>();
            data.put("name",user.getUsername());
            data.put("avatar",user.getAvatar());
            List<String> roleList = this.getBaseMapper().getRoleNamesByUserId(user.getId());
            data.put("roles", roleList);
            return data;
        }
        return null;
    }
    public void logout(String token) {
        redisTemplate.delete(token);
    }

}
