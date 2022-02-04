package com.poscoict.jblog.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscoict.jblog.security.AuthUser;
import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;
import com.poscoict.jblog.vo.UserVo;

@Controller
@RequestMapping("/{blogId}")
public class BlogController {

	@Autowired
	private BlogService blogService;
	
	@RequestMapping(value="")
	public String main(@PathVariable("blogId") String blogId,
			@RequestParam(value = "categoryNo", required = true, defaultValue = "-1") Long categoryNo,
			Model model, @AuthUser UserVo authUser) {
		
		Map<String, Object> map = blogService.getBlog(blogId, categoryNo);
		
		if(map == null) {
			return "redirect:/";
		}
				
		model.addAllAttributes(map);
		
		return "blog/blog-main";
	}
	
	// 뒤에 어드민 기능들 활용할 때 Path 다시 지정해줘야함
	
	@RequestMapping(value={"/admin", "/admin/basic"}, method=RequestMethod.GET)
	public String basic(@PathVariable("blogId") String blogId) {
		
		return "blog/blog-admin-basic";
	}
	
	@RequestMapping(value="/admin/basic", method=RequestMethod.POST)
	public String basic(@PathVariable("blogId") String blogId, BlogVo blogVo) {
		
		return "redirect:/blog/admin/basic";
	}
	
	@RequestMapping(value="/admin/category", method=RequestMethod.GET)
	public String category(@PathVariable("blogId") String blogId) {
		
		return "blog/blog-admin-category";
	}
	
	@RequestMapping(value="/admin/category", method=RequestMethod.POST)
	public String category(@PathVariable("blogId") String blogId, CategoryVo categoryVo,
			@AuthUser UserVo authUser) {
		
		return "redirect:/" + authUser.getId() + "/admin/category";
	}
	
	@RequestMapping(value="/admin/write", method=RequestMethod.GET)
	public String write(@PathVariable("blogId") String blogId) {
		
		return "blog/blog-admin-write";
	}
	
	@RequestMapping(value="/admin/write", method=RequestMethod.POST)
	public String write(@PathVariable("blogId") String blogId, PostVo postVo,
			@AuthUser UserVo authUser) {
		
		return "redirect:/" + authUser.getId() + "/admin";
	}	
	
}
