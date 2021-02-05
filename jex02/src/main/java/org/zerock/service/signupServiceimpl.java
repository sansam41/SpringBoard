package org.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.domain.AuthVO;
import org.zerock.domain.MemberVO;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.MemberMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping
@AllArgsConstructor
@Service
public class signupServiceimpl implements signupService{
	
	@Setter(onMethod_ = @Autowired)
	MemberMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private PasswordEncoder pwencoder;
	
	@Override
	public boolean checkId(String userid) {
		// TODO Auto-generated method stub
		if(mapper.chkId(userid)==1) {
			return true;
		}
		else
			return false;
		
	}
	
	
	@Transactional
	@Override
	public void insert(MemberVO vo) {

		vo.setUserpw(pwencoder.encode(vo.getUserpw()));
		
		
		AuthVO Avo=new AuthVO();
		Avo.setUserid(vo.getUserid());
		Avo.setAuth("ROLE_MEMBER");

		mapper.insertMember(vo);
		mapper.insertAuth(Avo);
	}

}
