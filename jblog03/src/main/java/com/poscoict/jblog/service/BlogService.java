package com.poscoict.jblog.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poscoict.jblog.repository.BlogRepository;
import com.poscoict.jblog.repository.CategoryRepository;
import com.poscoict.jblog.repository.PostRepository;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;

@Service
public class BlogService {

	private static final String SAVE_PATH = "/upload-images";
	private static final String URL_BASE = "/images";
	
	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private CategoryRepository categoryRepository; 
	
	@Autowired
	private PostRepository postRepository;
	
	public Map<String, Object> getBlog(String blogId, Long paramCategoryNo, Long paramPostNo) {

		Map<String, Object>map = new HashMap<>();
		
		Long categoryNo = paramCategoryNo;
		Long postNo = paramPostNo;
		
//		BlogVo blogVo = blogRepository.findBlog(blogId);
//		
//		if(blogVo == null) {
//			return null;
//		}
		
		List<CategoryVo> categoryList = categoryRepository.findCategoryAll(blogId);	
		
		if(categoryNo != null) {
			Long categoryNoCheck = categoryRepository.findCategoryNo(blogId, categoryNo);
			if(!categoryNo.equals(categoryNoCheck)) {			
				categoryNo = null;
			}
		}

		// 입력 안받거나 잘못된 값이 들어온 경우 그냥 제일 처음 만들어진 카테고리를 불러온다.
		if(categoryNo == null) {
			categoryNo = categoryList.get(0).getNo();
		}
		
		List<PostVo> postList = postRepository.findPostAll(categoryNo);
		
		if(postNo == null && postList.size() > 0) {
			postNo = postList.get(0).getNo();
		}
		
		PostVo postVo = postRepository.findPost(postNo);
		
//		map.put("blogVo", blogVo);
		map.put("categorylist", categoryList);
		map.put("postList", postList);
		map.put("postVo", postVo);
		
		return map;
	} 
	
	public Map<String, Object> getBlog(String blogId, Long paramCategoryNo) {

		Map<String, Object>map = new HashMap<>();
		
		Long categoryNo = paramCategoryNo;
		
//		BlogVo blogVo = blogRepository.findBlog(blogId);
//		
//		if(blogVo == null) {
//			return null;
//		}
		
		List<CategoryVo> categoryList = categoryRepository.findCategoryAndPostnum(blogId);		
		
		if(categoryNo != null) {
			Long categoryNoCheck = categoryRepository.findCategoryNo(blogId, categoryNo);	
			if(!categoryNo.equals(categoryNoCheck)) {			
				categoryNo = null;
			}
		}
		
		// 입력 안받거나 잘못된 값이 들어온 경우 그냥 제일 처음 만들어진 카테고리를 불러온다.
		if(categoryNo == null) {
			categoryNo = categoryList.get(0).getNo();
		}
				
		List<PostVo> postList = postRepository.findPostAll(categoryNo);
		
		Long categoryCount = categoryRepository.findCategoryCount(blogId);
		
//		map.put("blogVo", blogVo);
		map.put("categoryList", categoryList);
		map.put("postList", postList);
		map.put("categoryCount", categoryCount);
		
		return map;
	}

	public BlogVo getBlog(String blogId) {
		BlogVo blogVo = blogRepository.findBlog(blogId);
		
		if(blogVo == null) {
			return null;
		}
	
		return blogVo;
	}	
	
	public Boolean updateBlog(BlogVo blogVo, MultipartFile multipartFile) {

		String url = restore(multipartFile);
		
		if(url != null) {
			blogVo.setLogo(url);
		}
		
		if(blogRepository.update(blogVo)) {
			return true;
		} else {
			return false;
		}		
	}
	
	public Boolean deleteCategoryOrPost(String blogId, Long categoryNo, Long postNo) {
		Boolean result = false;
		
		if(categoryNo == null && postNo == null) {
			return false;
		}
		
		// 잘못된 카테고리 삭제 시도
		Long categoryNoCheck = categoryRepository.findCategoryNo(blogId, categoryNo);	
		if(!categoryNo.equals(categoryNoCheck)) {			
			return false;
		}
		
		// post or category 삭제 확인
		if(postNo == null) {
			// category에 2개 이상인 경우만 카테고리 삭제 가능
			if(categoryRepository.findCategoryCount(blogId) > 1 && postRepository.findPostCountByCategoryNo(categoryNo) == 0) {		
				result = categoryRepository.delete(blogId, categoryNo);
			}
		// postNo 있는 경우 삭제
		} else {
			result = postRepository.delete(blogId, categoryNo, postNo);
		}		
		
		return result;
	}
	
	public String restore(MultipartFile multipartFile) {
		String url = null;
		
		try {
		if(multipartFile.isEmpty()) {
			return url;
		}
		
		String originFileName = multipartFile.getOriginalFilename();
		String extName = originFileName.substring(originFileName.lastIndexOf('.') + 1); // jpg (확장자만)
		String saveFileName = generateSaveFileName(extName);
		long fileSize = multipartFile.getSize();
		System.out.println("# originFileName : " + originFileName);
		System.out.println("# fileSize : " + fileSize);
		System.out.println("# saveFileName : " + saveFileName);
		
		byte[] data = multipartFile.getBytes(); // error catch
		
		OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
		os.write(data);
		os.close();
		
		url = URL_BASE + "/" + saveFileName;
		
		} catch(IOException ex) {
			throw new RuntimeException("file upload error : " + ex);
		}		
		return url;
	}

	private String generateSaveFileName(String extName) {
		String filename = "";
		Calendar calendar = Calendar.getInstance(); // new 필요하지 않음

		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += "." + extName;
		
		return filename;
	}
}
