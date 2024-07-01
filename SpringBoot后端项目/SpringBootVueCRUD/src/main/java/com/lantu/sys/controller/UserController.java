package com.lantu.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.bean.Result;
import com.lantu.sys.entity.User;
import com.lantu.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yy
 * @since 2024-06-29
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user){
        Map<String,Object> data = userService.login(user);
        if(data != null){
            return Result.success(data);
        }
        return Result.fail(20002,"用户名或密码错误");
    }


    @GetMapping("/info")
    public Result<?> getUserInfo(@RequestParam("token") String token){
        Map<String,Object> data = userService.getUserInfo(token);
        if(data != null){
            return Result.success(data);
        }
        return Result.fail(20003,"用户信息获取失败");
    }


    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success("注销成功");
    }
    @GetMapping("/list")
    public Result<?> getUserListPage(@RequestParam(value = "username", required = false) String username,

                                     @RequestParam("pageNo") Long pageNo,
                                     @RequestParam("pageSize") Long pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        wrapper.eq(!StringUtils.isEmpty(username), User::getUsername, username);
        Page<User> page = new Page<>(pageNo, pageSize);
        userService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return Result.success(data);
    }

    @PostMapping("/add")
    public Result<?> addUser(@RequestBody  User user) {
        userService.save(user);
        Map<String, Object> data = new HashMap<>();

        return Result.success(data);
    }
    @PutMapping
    public Result<?> updateUser(@RequestBody  User user) {
        user.setPassword(null);
        userService.updateById(user);
        Map<String, Object> data = new HashMap<>();
        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result<User> getByUserId(@PathVariable("id")  Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result<User> deleteByUserId(@PathVariable("id")  Integer id) {
        userService.removeById(id);
        return Result.success();
    }

}
