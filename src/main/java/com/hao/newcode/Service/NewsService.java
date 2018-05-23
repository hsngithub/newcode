package com.hao.newcode.Service;

import com.hao.newcode.DAO.NewsDAO;
import com.hao.newcode.Model.News;
import com.hao.newcode.Util.newcodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import static java.nio.file.Files.copy;

@Service
public class NewsService {
	@Autowired
	private NewsDAO newsDAO;
	public List<News> getLatestNews(int userId, int offset, int limit){
		return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
	}
	public int addNews(News news){
		newsDAO.addNews(news);
		return news.getId();
	}
	public String saveImage(MultipartFile file) throws IOException{
		int dotPos=file.getOriginalFilename().lastIndexOf(".");
		if(dotPos<0)
			return null;
		String fileExt=file.getOriginalFilename().substring(dotPos+1).toLowerCase();
		if(!newcodeUtil.isFileAllowed(fileExt))
			return null;
		String filename= UUID.randomUUID().toString().replace("-","")+"."+fileExt;
		Files.copy(file.getInputStream(),new File(newcodeUtil.IMAGE_DIR+filename).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		return newcodeUtil.newcode_DOMAIN+"image?name="+filename;
	}
}
