package com.example.interceptors;

import com.example.common.Const;
import com.example.common.ResponseCode;
import com.example.pojo.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import utils.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class PortalLoginCheckInterceptor implements HandlerInterceptor {
    //在请求到达Controller之前, true:不拦截请求，false:拦截请求
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            //登陆成功
            return true;
        }

        //用户未登录，重写response
        try{
            response.reset();
            response.addHeader("Content-Type","application/json;charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            ServerResponse serverResponse = ServerResponse.createServerResponseByFail(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
            ObjectMapper objectMapper = new ObjectMapper();
            String info = objectMapper.writeValueAsString(serverResponse);
            printWriter.write(info);
            printWriter.flush();
            printWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    //请求处理完成后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //客户端接收到响应后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
