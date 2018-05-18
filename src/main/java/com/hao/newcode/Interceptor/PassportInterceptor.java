package com.hao.newcode.Interceptor;

import com.hao.newcode.DAO.LoginTicketDAO;
import com.hao.newcode.DAO.UserDAO;
import com.hao.newcode.Model.HostHolder;
import com.hao.newcode.Model.LoginTicket;
import com.hao.newcode.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/*
* 验证是否是已经登录的过的用户
* 过程：因为是请求是带cookie的 所以从cookie中得到下发的token
* 验证token是否为空，若为空调到注册页面
* 若不为空则与LoginTicket中的ticket相比较查看ticket是否为随意拼凑的
* 若检查结果为真 则保留用户信息在HostHolder中
* 其中用到有userDao.select,LoginTicketDao.select*/
@Component
public class PassportInterceptor implements HandlerInterceptor {
	@Autowired
	LoginTicketDAO loginTicketDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	HostHolder hostHolder;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String ticket=null;
		if(request.getCookies()!=null){
			for(Cookie cookie:request.getCookies()){
				if(cookie.getName().equals("ticket"))
					ticket=cookie.getValue();
				break;
			}
		}
		if(ticket!=null){
			LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);//将现在的ticket作为一个select的参数，如果不空则表里存在
			if(loginTicket==null&&loginTicket.getExpired().before(new Date())&&loginTicket.getStatus()!=0)//0表示有效
				return true;
			User user=userDAO.selectById(loginTicket.getUserId());
			hostHolder.setUsers(user);
		}
		return true;
	}

	@Override//验证已经是登录用户，在访问时“登录”改成“用户”
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null&&hostHolder.getUser()!=null){
			modelAndView.addObject("user",hostHolder.getUser());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
		hostHolder.clean();
	}
}
