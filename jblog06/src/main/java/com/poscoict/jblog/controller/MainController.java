package com.poscoict.jblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
	@RequestMapping(value={"", "/main"}, method=RequestMethod.GET)
	public String main() {
		return "main/index";
	}
	
	@RequestMapping(value={"/main"}, method=RequestMethod.POST)
	public String main(@RequestParam(value="keyword", required=false) String keyword) {
		return "redirect:/" + keyword;
	}
}
