package org.zerock.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.expression.ParseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.MemberVO;
import org.zerock.service.BoardService;
import org.zerock.service.signupService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
public class CommonController {

		signupService signupservice;
		@GetMapping("/accessError")
		public void accessDenied(Authentication auth,Model model) {
			log.info("access Denied: "+ auth);
			model.addAttribute("msg","ACCESS DENIED");
		}
		
		@GetMapping("/customLogin")
		public void loginInput(String error,String logout,Model model) {
			log.info("error: "+error);
			log.info("logout: "+logout);
			
			if(error!=null) {
				model.addAttribute("error", "Login Error Check your Account");
			}
			
			if(logout!=null) {
				model.addAttribute("logout","Logout!!");
			}
		}
		
		@GetMapping("/customLogout")
		public void logoutGET() {
			log.info("custom logout");
		}
		
		@GetMapping("/Test")
		public void test() {
			
		}
		
		@RequestMapping(value="/idchek",method=RequestMethod.POST)
		@ResponseBody
		public String id_Check(HttpServletRequest request) {
			String userid= request.getParameter("userid");
			System.out.println(signupservice.checkId(userid));
			//return signupservice.checkId(userid);
			if(signupservice.checkId(userid))
				return userid;
			else
				return null;
			
		}
		
		@GetMapping("/signup")
		public void signup() {
			
		}
		
		@PostMapping("/signup")
		public String register(MemberVO vo,RedirectAttributes rttr) {
			log.info("====================================");
			log.info(vo.getUserid());
			log.info(vo.getUserpw());
			log.info(vo.getUsername());
			signupservice.insert(vo);
			return "redirect:/board/list";
		}
		

}
