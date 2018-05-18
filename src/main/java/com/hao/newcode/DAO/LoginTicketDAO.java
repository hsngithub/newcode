package com.hao.newcode.DAO;

import com.hao.newcode.Model.LoginTicket;
import com.hao.newcode.Model.News;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import sun.security.krb5.internal.Ticket;

import java.util.List;
@Component
@Mapper
public interface LoginTicketDAO {
	String TABLE_NAME="LoginTicket";
	String INSERT_FIELDS="user_id,expired,status,ticket";
	String SELECT="id"+INSERT_FIELDS;
	@Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
			") values(#{userId},#{expired},#{status},#{ticket})"})
	int addTicket(LoginTicket ticket);
	@Select({"select",SELECT,"from",TABLE_NAME,"where ticket=#{ticket}"})
	LoginTicket selectByTicket(String ticket);
	@Update({"update",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
	void updateStatus(@Param("ticket") String ticket,@Param("status") int status);

}
