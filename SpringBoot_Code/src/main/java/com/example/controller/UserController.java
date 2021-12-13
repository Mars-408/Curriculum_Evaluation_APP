package com.example.controller;

import com.example.common.Const;
import com.example.common.ResponseCode;
import com.example.pojo.User;
import com.example.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ServerResponse;

import javax.servlet.http.HttpSession;

@RestController //所有方法的返回值都是json
public class UserController {

    @Autowired
    IUserService userService;
    //登录
    @RequestMapping(value = "/portal/user/login.do", method = RequestMethod.POST)
    @CrossOrigin
    public ServerResponse login(@RequestParam String username, @RequestParam String password, HttpSession session){
        ServerResponse serverResponse = userService.loginLogic(username, password);
        if(serverResponse.isSuccess()){
            //判断用户是否登录成功
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    //注册
    @RequestMapping(value = "/portal/user/register.do")
    public ServerResponse register(User user){
        return userService.registerLogic(user);
    }

    //修改用户信息
    @RequestMapping(value = "/portal/user/update.do")
    public ServerResponse updateUser(User user, HttpSession session){
        //判断用户是否登录
        User userInfo = (User)session.getAttribute(Const.CURRENT_USER);
        if(userInfo == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        if(user==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }
        user.setUsername(userInfo.getUsername());

        ServerResponse serverResponse = userService.updateUserLogic(user);
        if(serverResponse.isSuccess()){//更新session中的用户信息
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }

    //修改密码
    @RequestMapping(value = "/portal/user/alterPass.do")
    public ServerResponse alterUserPassword(User user, HttpSession session,String prePassword, String currentPassword){
        //判断用户是否登录
        User userInfo = (User)session.getAttribute(Const.CURRENT_USER);
        if(userInfo == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        if(user==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }
        user.setUsername(userInfo.getUsername());

        ServerResponse serverResponse = userService.alterPassword(user.getUsername(),prePassword,currentPassword);
        if(serverResponse.isSuccess()){//更新session中的用户信息
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }

    //忘记密码(重置密码)
    @RequestMapping(value = "/portal/user/resetPassword.do")
    public ServerResponse resetPassword(String username, String NewPassword){
        ServerResponse serverResponse = userService.resetPassword(username, NewPassword);
        return serverResponse;
    }

    //注销
    @RequestMapping(value = "/portal/user/logout.do")
    public ServerResponse deleteUser(User user,HttpSession session){
        //判断用户是否登录
        User userInfo = (User)session.getAttribute(Const.CURRENT_USER);
        user.setUsername(userInfo.getUsername());

        return userService.deleteUserLogic(user.getUsername());
    }
}
