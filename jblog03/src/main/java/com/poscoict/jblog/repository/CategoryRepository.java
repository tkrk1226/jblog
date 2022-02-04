package com.poscoict.jblog.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscoict.jblog.vo.CategoryVo;

@Repository
public class CategoryRepository {

	@Autowired
	private SqlSession sqlSession;
	
	public Boolean init(String strId) {
		return 1 == sqlSession.insert("category.init", strId);
	}

	public Boolean insert(CategoryVo categoryVo) {
		return 1 == sqlSession.insert("category.insert", categoryVo);
	}
	
	public List<CategoryVo> findCategoryAll(String blogId) {
		return sqlSession.selectList("category.findCategoryAll", blogId);
	}

}
