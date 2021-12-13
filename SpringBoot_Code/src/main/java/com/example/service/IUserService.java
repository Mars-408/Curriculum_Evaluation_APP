package com.example.service;

import com.example.pojo.User;
import utils.ServerResponse;

import javax.servlet.http.HttpSession;

public interface IUserService {

    /**
     * 登录
     **/
    public ServerResponse loginLogic(String username, String password);
    /**
     * 注册
     */
    public ServerResponse registerLogic(User user);

    /**
     * 修改
     */
    public ServerResponse updateUserLogic(User user);

    /**
     * 修改密码
     */
    public ServerResponse alterPassword(String username, String prePassword, String currentPassword);

    /**
     * 重置密码
     */
    public ServerResponse resetPassword(String username, String NewPassword);

    /**
     * 注销
     */
    public ServerResponse deleteUserLogic(String username);
}
