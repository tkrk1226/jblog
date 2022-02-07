package com.poscoict.jblog.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.CategoryRepository;
import com.poscoict.jblog.vo.CategoryVo;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<CategoryVo> getCategoryNoAndNameList(String blogId) {
		return categoryRepository.findCategoryNoAndName(blogId);
	}
	
	public Boolean addCategory(CategoryVo categoryVo) {
		return categoryRepository.insert(categoryVo);
	}
	
	public List<CategoryVo> showCategory(String blogId){
		return categoryRepository.findCategoryAndPostnum(blogId);
	}
	
	public Boolean deleteCategory(String blogId, Long categoryNo) {
		
		if(blogId == null || categoryNo == null) {
			return false;
		}
		
		List<Long> categoryNoList = categoryRepository.findCategoryNo(blogId);
		
		for(Long checkNo : categoryNoList) {
			if(checkNo == categoryNo) {				
				return categoryRepository.delete(categoryNo);
			}
		}
		return false;
	}
}
