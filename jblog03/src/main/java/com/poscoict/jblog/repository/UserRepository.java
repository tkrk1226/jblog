package com.poscoict.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscoict.jblog.vo.UserVo;

@Repository
public class UserRepository {

	@Autowired
	private SqlSession sqlSession;

	public Boolean insert(UserVo userVo) {
		return 1 == sqlSession.insert("user.insert", userVo);
	}
	
	public UserVo findByIdAndPassword(UserVo userVo) {
		return sqlSession.selectOne("user.findByIdAndPassword", userVo);
	}
	
	
}
