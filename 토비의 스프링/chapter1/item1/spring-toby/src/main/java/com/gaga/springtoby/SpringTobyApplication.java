package com.gaga.springtoby;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.domain.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

/**
 * 테스트용 main() 메소드
 */
@SpringBootApplication
public class SpringTobyApplication {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		// UserDao 오브젝트를 생성한다.
		UserDao userDao = new UserDao();

		// 프로퍼티에 값 넣는다.
		User user = new User();
		user.setId("gaga");
		user.setName("김가경");
		user.setPassword("gagakim");

		// add() 메소드를 이용해 DB에 등록해본다.
		userDao.add(user);

		System.out.println(user.getId() + " 등록 성공");

		// get() 메소드를 이용해 DB에 저장된 결과를 가져와 본다.
		User user2 = userDao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());

		System.out.println(user2.getId() + " 조회 성공");
	}
}
