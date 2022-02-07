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
	
	@RequestMapping(value={"", "/{categoryNo}", "/{categoryNo}/{postNo}"})
	public String main(@PathVariable("blogId") String blogId,
			@PathVariable(required = false) Long categoryNo,
			@PathVariable(required = false) Long postNo,
			Model model, @AuthUser UserVo authUser) {
		
		Map<String, Object> map = blogService.getBlog(blogId, categoryNo, postNo);
		
		if(map == null) {
			return "redirect:/";
		}
				
		model.addAllAttributes(map);
		
		return "blog/blog-main";
	}
	
	// 뒤에 어드민 기능들 활용할 때 Path 다시 지정해줘야함
	
	@RequestMapping(value={"/admin", "/admin/basic"}, method=RequestMethod.GET)
	public String basic(@PathVariable("blogId") String blogId, Model model, @AuthUser UserVo authUser) {

		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		BlogVo blogVo = blogService.getBlog(blogId);
		
		if(blogVo == null) {
			return "redirect:/";
		}
		
		model.addAttribute("blogVo", blogVo);
		
		return "blog/blog-admin-basic";
	}
	
	@RequestMapping(value="/admin/basic", method=RequestMethod.POST)
	public String basic(@PathVariable("blogId") String blogId, BlogVo blogVo,
			@RequestParam(value="logo-file") MultipartFile multipartFile, @AuthUser UserVo authUser) {
		
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		Boolean result = blogService.updateBlog(blogVo, multipartFile);
		
		return "redirect:/" + authUser.getId() + "/admin/basic";
	}
	
	@RequestMapping(value={"/admin/category", "/admin/category/{categoryNo}"}, method=RequestMethod.GET)
	public String category(@PathVariable("blogId") String blogId, Model model,
			@PathVariable(required = false) Long categoryNo, @AuthUser UserVo authUser) {
		
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		Map<String, Object> map = blogService.getBlog(authUser.getId(), categoryNo);
		
		if(map == null) {
			return "redirect:/";
		}
				
		model.addAllAttributes(map);
		
		return "blog/blog-admin-category";
	}
	
	@RequestMapping(value="/admin/category/add", method=RequestMethod.POST)
	public String addCategory(@PathVariable("blogId") String blogId, CategoryVo categoryVo,
			@AuthUser UserVo authUser) {
		
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		categoryService.addCategory(categoryVo);
		
		return "redirect:/" + authUser.getId() + "/admin/category";
	}
	
	@RequestMapping(value= {"/admin/category/delete/{categoryNo}", "/admin/category/delete/{categoryNo}/{postNo}"})
	public String delete(@PathVariable("blogId") String blogId,
			@PathVariable("categoryNo") Long categoryNo,
			@PathVariable(required = false) Long postNo,
			@AuthUser UserVo authUser) {

		Boolean result = false;
		
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		if(postNo == null) {
			if(categoryService.getCategoryCount(authUser.getId()) > 1) {
			result = categoryService.deleteCategory(authUser.getId(), categoryNo);
			}
			else {
				return "redirect:/" + authUser.getId() + "/admin/category";
			}
		} else {
			result = postService.deletePost(authUser.getId(), categoryNo, postNo);
		}
		
		if(!result) {
			return "redirect:/";
		}
		
		return "redirect:/" + authUser.getId() + "/admin/category";
	}
		
	@RequestMapping(value="/admin/write", method=RequestMethod.GET)
	public String write(@PathVariable("blogId") String blogId, Model model, @AuthUser UserVo authUser) {
		
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		BlogVo blogVo = blogService.getBlog(blogId);
		
		if(blogVo == null) {
			return "redirect:/";
		}
		
		model.addAttribute("categoryNoAndNameList", categoryService.getCategoryNoAndNameList(blogId));
		model.addAttribute("blogVo", blogVo);
		
		return "blog/blog-admin-write";
	}
	
	@RequestMapping(value="/admin/write", method=RequestMethod.POST)
	public String write(@PathVariable("blogId") String blogId, PostVo postVo,
			@AuthUser UserVo authUser) {
		
		if(authUser == null || !blogId.equals(authUser.getId())) {
			return "redirect:/";
		}
		
		Boolean result = postService.addPost(postVo);
		return "redirect:/" + authUser.getId() + "/admin/category";
	}	
	
}
