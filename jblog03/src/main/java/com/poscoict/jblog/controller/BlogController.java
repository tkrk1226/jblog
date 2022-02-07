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
	@RequestMapping(value={"", "/{categoryNo}", "/{categoryNo}/{postNo}"})
	public String main(@PathVariable("blogId") String blogId,
			@PathVariable(required = false) Long categoryNo,
			@PathVariable(required = false) Long postNo,
			Model model, @AuthUser UserVo authUser) {
		
		// 메인페이지에 필요한 내용들
		Map<String, Object> map = blogService.getBlog(blogId, categoryNo, postNo);
		
		// 반환이 제대로 되지 않으면 main으로...
		if(map == null) {
			return "redirect:/";
		}
				
		model.addAllAttributes(map);
		
		return "blog/blog-main";
	}
	
	// 어드민 페이지(1), 관리자 정보 수정
	@RequestMapping(value={"/admin", "/admin/basic"}, method=RequestMethod.GET)
	public String basic(@PathVariable("blogId") String blogId, Model model, @AuthUser UserVo authUser) {

		// 관리자 확인
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		BlogVo blogVo = blogService.getBlog(blogId);
		
		// 블로그 확인
		if(blogVo == null) {
			return "redirect:/";
		}
		
		model.addAttribute("blogVo", blogVo);
		
		return "blog/blog-admin-basic";
	}

	// 어드민 페이지(1), 관리자 정보 수정
	@RequestMapping(value="/admin/basic", method=RequestMethod.POST)
	public String basic(@PathVariable("blogId") String blogId, BlogVo blogVo,
			@RequestParam(value="logo-file") MultipartFile multipartFile, @AuthUser UserVo authUser) {
		
		// 관리자 확인
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		Boolean result = blogService.updateBlog(blogVo, multipartFile);
		
		return "redirect:/" + authUser.getId() + "/admin/basic";
	}
	
	// 어드민 페이지(2), 카테고리 및 글 관리
	@RequestMapping(value={"/admin/category", "/admin/category/{categoryNo}"}, method=RequestMethod.GET)
	public String category(@PathVariable("blogId") String blogId, Model model,
			@PathVariable(required = false) Long categoryNo, @AuthUser UserVo authUser) {
		
		// 관리자 확인
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		Map<String, Object> map = blogService.getBlog(authUser.getId(), categoryNo);
		
		// 맵 Null인 경우 메인으로
		if(map == null) {
			return "redirect:/";
		}
				
		model.addAllAttributes(map);
		
		return "blog/blog-admin-category";
	}
	
	// 어드민 페이지(2), 카테고리 및 글 관리
	@RequestMapping(value="/admin/category/add", method=RequestMethod.POST)
	public String addCategory(@PathVariable("blogId") String blogId, CategoryVo categoryVo,
			@AuthUser UserVo authUser) {
		
		// 관리자 확인
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		// 카테고리 추가
		categoryService.addCategory(categoryVo);
		
		return "redirect:/" + authUser.getId() + "/admin/category";
	}
	
	// 어드민 페이지(2), category or post 삭제
	@RequestMapping(value= {"/admin/category/delete/{categoryNo}", "/admin/category/delete/{categoryNo}/{postNo}"})
	public String delete(@PathVariable("blogId") String blogId,
			@PathVariable("categoryNo") Long categoryNo,
			@PathVariable(required = false) Long postNo,
			@AuthUser UserVo authUser) {

		// 관리자 확인
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}

		Boolean result = false;

		// post or category 삭제 확인
		if(postNo == null) {
			// category에 2개 이상인 경우만 카테고리 삭제 가능
			if(categoryService.getCategoryCount(authUser.getId()) > 1) {
				result = categoryService.deleteCategory(authUser.getId(), categoryNo);
			}
			else {
				return "redirect:/" + authUser.getId() + "/admin/category";
			}
		// postNo 있는 경우 삭제
		} else {
			result = postService.deletePost(authUser.getId(), categoryNo, postNo);
		}
		
		// 동작이 잘못된 경우 다시 메인으로...
		if(!result) {
			return "redirect:/";
		}
		
		return "redirect:/" + authUser.getId() + "/admin/category";
	}
	
	// 어드민 페이지(3), 글 작성
	@RequestMapping(value="/admin/write", method=RequestMethod.GET)
	public String write(@PathVariable("blogId") String blogId, Model model, @AuthUser UserVo authUser) {
		
		// 관리자 확인
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		BlogVo blogVo = blogService.getBlog(blogId);
		
		// 블로그 확인
		if(blogVo == null) {
			return "redirect:/";
		}
		
		model.addAttribute("categoryNoAndNameList", categoryService.getCategoryNoAndNameList(blogId));
		model.addAttribute("blogVo", blogVo);
		
		return "blog/blog-admin-write";
	}
	
	// 어드민 페이지(3), 글 작성
	@RequestMapping(value="/admin/write", method=RequestMethod.POST)
	public String write(@PathVariable("blogId") String blogId, PostVo postVo,
			@AuthUser UserVo authUser) {
		
		// 관리자 확인
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		Boolean result = postService.addPost(postVo);
		return "redirect:/" + authUser.getId() + "/admin/category";
	}	
	
}
