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

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class HomeController {
	@Autowired
	UserService userService;
	@Autowired
	NewsService newsService;
	@RequestMapping(path={"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
	public String index(Model model){
		List<ViewObject> vos = getNews();
		model.addAttribute("vos",vos);
		return "home";
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
