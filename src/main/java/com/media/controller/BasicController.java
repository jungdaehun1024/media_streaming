package com.media.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BasicController {

	@GetMapping("/basic")
	public String basic() {
		return "basic";
	}
	


}
