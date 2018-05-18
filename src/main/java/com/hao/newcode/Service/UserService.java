package com.hao.newcode.Service;

import com.hao.newcode.DAO.LoginTicketDAO;
import com.hao.newcode.DAO.UserDAO;
import com.hao.newcode.Model.LoginTicket;
import com.hao.newcode.Model.User;
import com.hao.newcode.Util.newcodeUtil;
import freemarker.template.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private LoginTicketDAO loginTicketDAO;
	public User getUser(int id){
		return userDAO.selectById(id);
	}
	public Map<String,Object> register(String username,String password){
		Map<String,Object> map=new HashMap<>();
		if(StringUtils.isEmpty(username)){
			map.put("msgname","用户名不能为空");
			return map;}
		if(StringUtils.isEmpty(password)){
			map.put("msgpassword","密码不能为空");
			return map;
		}
		User user=userDAO.selectByName(username);
		if(user!=null){
			map.put("namerepete","用户名已经注册");
			return map;
		}
		user=new User();
		user.setName(username);
		user.setSalt(UUID.randomUUID().toString().substring(0,5));
		String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
		user.setHeadUrl(head);
		user.setPassword(newcodeUtil.MD5(password+user.getSalt()));
		userDAO.addUser(user);
		String ticket=addTicket(user.getId());
		map.put("ticket",ticket);
		return map;
	}
	public Map<String,Object> login(String username,String password){
		Map<String,Object> map=new HashMap<>();
		if(StringUtils.isEmpty(username)){
			map.put("msgname","用户名不能为空");
			return map;}
		if(StringUtils.isEmpty(password)){
			map.put("msgpassword","密码不能为空");
			return map;
		}
		User user=userDAO.selectByName(username);
		if(user==null){
			map.put("namenull","用户名不存在");
			return map;
		}
		if(!newcodeUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
			map.put("falsepsw","密码不正确");
			return map;
		}
		String ticket=addTicket(user.getId());
		map.put("ticket",ticket);
		return map;
	}
	private String addTicket(int userId){
		LoginTicket ti=new LoginTicket();
		ti.setUserId(userId);
		Date date=new Date();
		date.setTime(date.getTime()+1000*3600*24);
		ti.setExpired(date);
		ti.setStatus(0);
		ti.setTicket(UUID.randomUUID().toString().replace("_",""));
		loginTicketDAO.addTicket(ti);
		return ti.getTicket();
	}
	public void logout(String ticket){

		loginTicketDAO.updateStatus(ticket,1);

	}
}
