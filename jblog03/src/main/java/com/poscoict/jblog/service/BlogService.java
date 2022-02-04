package com.poscoict.jblog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.BlogRepository;
import com.poscoict.jblog.repository.CategoryRepository;
import com.poscoict.jblog.repository.PostRepository;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;

@Service
public class BlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private CategoryRepository categoryRepository; 
	
	@Autowired
	private PostRepository postRepository;
	
	public Map<String,Object> getBlog(String blogId, Long paramNo) {

		Map<String, Object>map = new HashMap<>();
		
		Long categoryNo = paramNo;
		BlogVo blogVo = blogRepository.findBlog(blogId);
		
		if(blogVo == null) {
			return null;
		}
		
		List<CategoryVo> categoryList = categoryRepository.findCategoryAll(blogId);
		
		// 입력 안받으면 그냥 제일 처음 만들어진 카테고리를 불러온다.
		if(categoryNo == -1) {
			categoryNo = categoryList.get(0).getNo();
		}
		List<PostVo> postList = postRepository.findPostAll(categoryNo);
		
		map.put("blogVo", blogVo);
		map.put("categorylist", categoryList);
		map.put("postList", postList);
		
		return map;
	} 
	
}
