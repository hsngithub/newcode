package com.hao.newcode.Controller;

import com.hao.newcode.Model.News;
import com.hao.newcode.Model.User;
import com.hao.newcode.Model.ViewObject;
import com.hao.newcode.Service.NewsService;
import com.hao.newcode.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomeController {
	@Autowired
	UserService userService;
	@Autowired
	NewsService newsService;
	@RequestMapping(path={"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
	public String index(Model model, @RequestParam(value="pop",defaultValue = "0") int pop){
		model.addAttribute("vos",getNews(0,0,10));
		model.addAttribute("pop",pop);
		List<ViewObject> vos = getNews();
		return "home";
	}
	private Map<String,List<ViewObject>> getNews(int userId, int offset, int limit) {

		List<News> newsList = newsService.getLatestNews(userId, offset, limit);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//使用TreeMap，重写排序按倒序排列[使用jdk1.8方法]
		Map<String,List<ViewObject>> allDateMap = new TreeMap<>(Comparator.reverseOrder());

		for (News news : newsList) {
			Date createdDate = news.getCreatedDate();
			String formatDate = sdf.format(createdDate);
			allDateMap.put(formatDate, new ArrayList<>());
		}
		for (News news : newsList) {
			String createdDate = sdf.format(news.getCreatedDate());
			List<ViewObject> viewObjectList = allDateMap.get(createdDate);
			ViewObject vo = new ViewObject();
			vo.set("news", news);
			vo.set("user", userService.getUser(news.getUserId()));
			viewObjectList.add(vo);
		}

		return allDateMap;
	}

	public List<ViewObject> getNews(){
		Map<String, List<ViewObject>> map = new TreeMap<>(Comparator.reverseOrder());
		List<ViewObject> list = new ArrayList<>();
		List<News> latestNews = newsService.getLatestNews(0, 0, 10);
		if (latestNews != null && latestNews.size() > 0) {
			for (News news : latestNews) {
				ViewObject viewObject = new ViewObject();
				User user = userService.getUser(news.getUserId());
				viewObject.set("user",user);
				viewObject.set("news",news);
				list.add(viewObject);
			}
		}

		return list;
	}
}
