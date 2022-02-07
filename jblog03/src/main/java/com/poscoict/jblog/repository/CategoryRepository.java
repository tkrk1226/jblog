package com.poscoict.jblog.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
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

	public Boolean delete(Long categoryNo) {
		return 1 == sqlSession.delete("category.delete", categoryNo);
	}
	
	public List<CategoryVo> findCategoryAll(String blogId) {
		return sqlSession.selectList("category.findCategoryAll", blogId);
	}
	
	public List<Long> findCategoryNo(String blogId){
		return sqlSession.selectList("category.findCategoryNo", blogId);
	}

	public List<CategoryVo> findCategoryNoAndName(String blogId){
		return sqlSession.selectList("category.findCategoryNoAndName", blogId);
	}
	
	public List<CategoryVo> findCategoryAndPostnum(String blogId) {
		return sqlSession.selectList("category.findCategoryAndPostnum", blogId);
	}
	
}
