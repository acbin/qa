package com.bingo.qa;

import com.bingo.qa.dao.QuestionDAO;
import com.bingo.qa.dao.UserDAO;
import com.bingo.qa.model.Question;
import com.bingo.qa.model.User;
import com.bingo.qa.util.QaUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QaApplicationTests {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private QuestionDAO questionDAO;

	@Test
	public void testInsertUser() {
		for (int i = 1; i < 11; ++i) {
			User u = new User();
			Random r = new Random();
			u.setName("User" + String.valueOf(i));
			u.setPassword("xxx");
			u.setSalt("");
			u.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", r.nextInt(1000)));
			userDAO.addUser(u);

			Question question = new Question();
			question.setCommentCount(i);
			question.setContent("You are the apple of my eye~~~~~" + i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000 *3600 * (i - 1));
			question.setCreatedDate(date);
			question.setTitle("This is title " + i);
			question.setUserId(i);
			questionDAO.addQuestion(question);

		}

	}

	@Test
	public void testSelectUser() {
		User u = userDAO.selectById(2);
		System.out.println(u);
	}

	@Test
	public void testUpdateUser() {
		User u = userDAO.selectById(2);
		u.setPassword("bingo");
		userDAO.updatePassword(u);
	}

	@Test
	public void testDelete() {
		userDAO.deleteById(12);

	}

	@Test
	public void testAvatar() {
		try {
			QaUtil.createIdenticon(1, "bb", 200);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
