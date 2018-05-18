package com.hao.newcode.DAO;

import com.hao.newcode.Model.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsDAO {
	String TABLE_NAME="news";
	String INSERT_FIELDS="title,link,image,like_count,comment_count,created_date,user_id";
	String SELECT="id"+INSERT_FIELDS;
	@Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
			") values(#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
	int addNews(News news);
	List<News> selectByUserIdAndOffset(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);

}
