package com.poscoict.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.BlogRepository;
import com.poscoict.jblog.repository.CategoryRepository;
import com.poscoict.jblog.repository.UserRepository;
import com.poscoict.jblog.vo.UserVo;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private CategoryRepository categoryRepository; 
	
	
	public Boolean join(UserVo userVo) {
		Boolean result = false;
		String strId = userVo.getId();

		Boolean userResult = userRepository.insert(userVo);
		Boolean blogResult = blogRepository.insert(strId);
		Boolean categoryResult = categoryRepository.init(strId);
		
		if(userResult && blogResult && categoryResult) {
			result = true;
		}
		
		return result;
	}

	public UserVo getUser(UserVo userVo) {
		return userRepository.findByIdAndPassword(userVo);
	}
	
}
