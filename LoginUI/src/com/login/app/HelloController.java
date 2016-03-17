package com.login.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/credentials")
public class HelloController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView("LoginPage", "userPojo", new UserPojo());

	}
}
