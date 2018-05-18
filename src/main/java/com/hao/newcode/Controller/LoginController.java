package com.hao.newcode.Controller;

import com.hao.newcode.Service.UserService;
import com.hao.newcode.Util.newcodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	UserService userService;
	@RequestMapping(path={"/reg"},method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String reg(Model model, @RequestParam("username") String username,
						@RequestParam("password") String password,
						@RequestParam("remberme") int rember,
						HttpServletResponse response){
		try{
			Map<String, Object> objectMap = userService.register(username, password);
			if(objectMap.containsKey("ticket")){
				Cookie ck=new Cookie("ticket",objectMap.get("ticket").toString());
				ck.setPath("/");
				if(rember > 0){
					ck.setMaxAge(3600*24*5);
				}
				response.addCookie(ck);
				return newcodeUtil.getJson(0,"注册成功");
			}else {
				return newcodeUtil.getJson(1,objectMap);
			}
		}catch(Exception e){
			logger.error("注册异常"+e.getMessage());
			return newcodeUtil.getJson(1,"注册异常");
		}
	}

	@RequestMapping(path={"/log"},method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String log(Model model, @RequestParam("username") String username,
					  @RequestParam("password") String password,
					  @RequestParam(value="remberme",defaultValue="0") int rember){
		try{
			Map<String, Object> objectMap = userService.register(username, password);
			if(objectMap.containsKey("ticket")){
				Cookie ck=new Cookie("ticket",objectMap.get("ticket").toString());
				ck.setPath("/");
				if(rember > 0){
					ck.setMaxAge(3600*24*5);
				}
				return newcodeUtil.getJson(0,"注册成功");
			}else {
				return newcodeUtil.getJson(1,objectMap);
			}
		}catch(Exception e){
			logger.error("注册异常"+e.getMessage());
			return newcodeUtil.getJson(1,"注册异常");
		}
	}
	@RequestMapping(path={"/logout"},method = {RequestMethod.GET,RequestMethod.POST})

	public String logout(@CookieValue("ticket") String ticket){
		userService.logout(ticket);
		return "redirect:/";
	}
}
