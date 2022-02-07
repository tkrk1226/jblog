package com.poscoict.jblog.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscoict.jblog.vo.PostVo;

@Repository
public class PostRepository {

	@Autowired
	private SqlSession sqlSession;

	public List<PostVo> findPostAll(Long categoryNo) {
		return sqlSession.selectList("post.findPostAll", categoryNo);
	}

	public PostVo findPost(Long postNo) {
		return sqlSession.selectOne("post.findPost", postNo);
	}
	
	public Boolean insert(PostVo postVo) {
		return 1 == sqlSession.insert("post.insert", postVo);
	}

	public Boolean delete(String blogId, Long categoryNo, Long postNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("blogId", blogId);
		map.put("categoryNo", categoryNo);
		map.put("postNo", postNo);
		return 1 == sqlSession.delete("post.delete", map);
	}
}
