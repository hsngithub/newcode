package com.hao.newcode.Model;

import org.springframework.stereotype.Component;

@Component//将其标志为spring管理的Bean
public class HostHolder {
	private static ThreadLocal<User> users=new ThreadLocal<User>();
	public User getUser(){
		return users.get();//让users调用get方法，得到users里的用户数据
	}
	public void setUsers(User user){
		users.set(user);//给users 设置数据 将当前使用访问的用户放到本地线程的users里
	}
	public void clean(){
		users.remove();//users只保存当前使用的用户 每一个用户访问都会生成一个users对象
	}
}
