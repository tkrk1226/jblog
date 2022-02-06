package com.poscoict.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscoict.jblog.vo.BlogVo;

@Repository
public class BlogRepository {

	@Autowired
	private SqlSession sqlSession;
	
	public Boolean insert(String strId) {
		BlogVo blogVo = new BlogVo();
		blogVo.setTitle(strId + "의 블로그");
		blogVo.setLogo("/images/default.jpg");
		blogVo.setUserId(strId);
		return 1 == sqlSession.insert("blog.insert", blogVo);
	}

	public BlogVo findBlog(String blogId) {
		return sqlSession.selectOne("blog.findBlog", blogId);
	}

	public boolean update(BlogVo blogVo) {
		return 1 == sqlSession.update("blog.update", blogVo);
	}

}
