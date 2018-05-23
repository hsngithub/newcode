package com.hao.newcode.Controller;

import com.hao.newcode.Model.HostHolder;
import com.hao.newcode.Model.News;
import com.hao.newcode.Service.NewsService;
import com.hao.newcode.Service.QiniuService;
import com.hao.newcode.Service.UserService;
import com.hao.newcode.Util.newcodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.Date;

@Controller
public class NewsController {
	private static final Logger logger= LoggerFactory.getLogger(NewsController.class);
	@Autowired
	NewsService newsService;
	@Autowired
	QiniuService qiniuService;
	@Autowired
	HostHolder hostHolder;
	@Autowired
	UserService userService;

	@RequestMapping(path={"/uploadImage/"},method = {RequestMethod.POST})
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file){
		try{
			String url= newsService.saveImage(file);
//			String url=qiniuService.saveImage(file);
			if(url==null){
				return newcodeUtil.getJson(1,"上传失败");
			}
			return newcodeUtil.getJson(0,url);

		}catch(Exception e){
			logger.error("上传图片失败"+e.getMessage());
			return newcodeUtil.getJson(1,"上传失败");
		}

	}

	@RequestMapping(path={"/image"},method = {RequestMethod.GET})
	@ResponseBody
	public void BrowseImage(@RequestParam("name") String imagename, HttpServletResponse response){
		try{
			response.setContentType("image/jpeg");
			StreamUtils.copy(new FileInputStream(newcodeUtil.IMAGE_DIR+imagename),
					response.getOutputStream());

		}catch(Exception e){
			logger.error("读取图片错误"+imagename+e.getMessage());
		}
	}
	@RequestMapping(path={"/user/addNews"},method = {RequestMethod.POST})
	@ResponseBody
	public String addNews(@RequestParam("image") String image,
						  @RequestParam("title") String title,
						  @RequestParam("link") String link){
		try{
			News news=new News();
			news.setImage(image);
			news.setCreatedDate(new Date());
			news.setLink(link);
			news.setTitle(title);
			if(hostHolder.getUser()!=null){
				news.setUserId(hostHolder.getUser().getId());
			}else{
				news.setUserId(3);
			}
			newsService.addNews(news);
			return newcodeUtil.getJson(0);
		}catch(Exception e){
				logger.error("添加资讯失败"+e.getMessage());
				return newcodeUtil.getJson(1,"发布资讯失败");
		}


	}
}
