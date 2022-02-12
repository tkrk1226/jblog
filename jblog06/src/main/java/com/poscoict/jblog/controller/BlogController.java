package com.poscoict.jblog.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.poscoict.jblog.security.Auth;
import com.poscoict.jblog.security.AuthUser;
import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.service.CategoryService;
import com.poscoict.jblog.service.PostService;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;
import com.poscoict.jblog.vo.UserVo;

@Controller
@RequestMapping("/{blogId:(?!assets|images).*}")
public class BlogController {

	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private PostService postService;
	
	// 메인 페이지
	@RequestMapping(value={"", "/{categoryNo:^[0-9]+$}", "/{categoryNo:^[0-9]+$}/{postNo:^[0-9]+$}"})
	public String main(@PathVariable("blogId") String blogId,
			@PathVariable(required = false) Long categoryNo,
			@PathVariable(required = false) Long postNo,
			Model model, @AuthUser UserVo authUser) {
		
		// 메인페이지에 필요한 내용들
		Map<String, Object> map = blogService.getBlog(blogId, categoryNo, postNo);		
		model.addAllAttributes(map);
		
		return "blog/blog-main";
	}
	
	// 어드민 페이지(1), 관리자 정보 수정
	@Auth
	@RequestMapping(value={"/admin", "/admin/basic"}, method=RequestMethod.GET)
	public String basic(@PathVariable("blogId") String blogId, @AuthUser UserVo authUser) {
				
		return "blog/blog-admin-basic";
	}

	// 어드민 페이지(1), 관리자 정보 수정
	@Auth
	@RequestMapping(value="/admin/basic", method=RequestMethod.POST)
	public String basic(@PathVariable("blogId") String blogId, BlogVo blogVo,
			@RequestParam(value="logo-file") MultipartFile multipartFile, @AuthUser UserVo authUser) {
		
		Boolean result = blogService.updateBlog(blogVo, multipartFile);
		
		return "redirect:/" + authUser.getId() + "/admin/basic";
	}
	
	// 어드민 페이지(2), 카테고리 및 글 관리
	@Auth
	@RequestMapping(value={"/admin/category", "/admin/category/{categoryNo:^[0-9]+$}"}, method=RequestMethod.GET)
	public String category(@PathVariable("blogId") String blogId, Model model,
			@PathVariable(required = false) Long categoryNo, @AuthUser UserVo authUser) {
		
		Map<String, Object> map = blogService.getBlog(authUser.getId(), categoryNo);	
		model.addAllAttributes(map);
		
		return "blog/blog-admin-category";
	}
	
	// 어드민 페이지(2), 카테고리 및 글 관리
	@Auth
	@RequestMapping(value="/admin/category/add", method=RequestMethod.POST)
	public String addCategory(@PathVariable("blogId") String blogId, CategoryVo categoryVo,
			@AuthUser UserVo authUser) {
		
		// 카테고리 추가
		categoryService.addCategory(categoryVo);
		
		return "redirect:/" + authUser.getId() + "/admin/category";
	}
	
	// 어드민 페이지(2), category or post 삭제
	@Auth
	@RequestMapping(value= {"/admin/category/delete", "/admin/category/delete/{categoryNo:^[0-9]+$}", "/admin/category/delete/{categoryNo:^[0-9]+$}/{postNo:^[0-9]+$}"})
	public String delete(@PathVariable("blogId") String blogId,
			@PathVariable(required = false) Long categoryNo,
			@PathVariable(required = false) Long postNo,
			@AuthUser UserVo authUser) {
				
		Boolean result = blogService.deleteCategoryOrPost(authUser.getId(), categoryNo, postNo);

		return "redirect:/" + authUser.getId() + "/admin/category";
	}
	
	// 어드민 페이지(3), 글 작성
	@Auth
	@RequestMapping(value="/admin/write", method=RequestMethod.GET)
	public String write(@PathVariable("blogId") String blogId, Model model, @AuthUser UserVo authUser) {
				
		model.addAttribute("categoryNoAndNameList", categoryService.getCategoryNoAndNameList(blogId));
		
		return "blog/blog-admin-write";
	}
	
	// 어드민 페이지(3), 글 작성
	@Auth
	@RequestMapping(value="/admin/write", method=RequestMethod.POST)
	public String write(@PathVariable("blogId") String blogId, PostVo postVo,
			@AuthUser UserVo authUser) {
		
		Boolean result = postService.addPost(postVo);
		return "redirect:/" + authUser.getId() + "/admin/category";
	}	
	
}
