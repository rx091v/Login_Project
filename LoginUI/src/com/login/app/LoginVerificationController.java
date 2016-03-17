package com.login.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.login.verification.client.SaaJSoapClient;

@Controller
@RequestMapping("/login")
public class LoginVerificationController {

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("userPojo") UserPojo userPojo,
			BindingResult result, ModelMap model) {
		
		/*
		 * CALL A WEB SERVICE
		 * CREATE A SESSION
		 */
		String response = "";
		try {
			SaaJSoapClient client = new SaaJSoapClient();
			response = client.authentication(userPojo.getUsername(), userPojo.getPassword());
			System.err.println("WEB SERVICE RETURNED _________ " +  response);
			model.addAttribute("message", response );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Success";
	}

}
