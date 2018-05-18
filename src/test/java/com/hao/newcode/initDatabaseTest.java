package com.hao.newcode;

import com.hao.newcode.DAO.NewsDAO;
import com.hao.newcode.DAO.UserDAO;
import com.hao.newcode.Model.News;
import com.hao.newcode.Model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = NewcodeApplication.class)
@Sql("/init-schema.sql")
public class initDatabaseTest {
	@Autowired
	UserDAO userDAO;
	@Autowired
	NewsDAO newsDAO;
	@Test
	public void initData(){
		Random random=new Random();
		for(int i=0;i<10;i++){
			User user=new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("USER%d",i));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);

			News news=new News();
			news.setTitle(String.format("Title{%d}",i));
			news.setCommentCount(i);
			news.setUserId(i+1);
			news.setLikeCount(i+1);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png",random.nextInt(1000)));
			news.setLink(String.format("http://www.nowcoder.com/%d.html",i));
			Date date=new Date();
			date.setTime(date.getTime()+1000*3600*5*i);
			news.setCreatedDate(date);
			newsDAO.addNews(news);


		}
	}
}
