package com.poscoict.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.PostRepository;
import com.poscoict.jblog.vo.PostVo;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;
	
	public Boolean addPost(PostVo postVo) {
		return postRepository.insert(postVo);
	}

	public Boolean deletePost(String blogId, Long categoryNo, Long postNo) {
		
		if(blogId == null || categoryNo == null) {
			return false;
		}
		
		return postRepository.delete(blogId, categoryNo, postNo);
	}
}
