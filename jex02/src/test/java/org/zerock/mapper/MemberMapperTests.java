package org.zerock.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.AuthVO;
import org.zerock.domain.MemberVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {org.zerock.config.RootConfig.class,org.zerock.config.SecurityConfig.class})
@Log4j
public class MemberMapperTests {
	
	@Setter(onMethod_=@Autowired)
	private MemberMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private PasswordEncoder pwencoder;
	
	@Test
	public void testRead() {
		MemberVO vo=mapper.read("sansam41");
		log.info(vo);
		
		vo.getAuthList().forEach(authVO->log.info(authVO));
	}
	
	@Test
	public void testinsertmember() {
		MemberVO vo=new MemberVO();

		vo.setUserid("sansam41");
		vo.setUsername("승덱이");
		vo.setUserpw(pwencoder.encode("1234"));

		
		mapper.insertMember(vo);
		log.info(vo);
		
		AuthVO Avo=new AuthVO();
		
		Avo.setUserid("sansam41");
		Avo.setAuth("ROLE_MEMBER");
		mapper.insertAuth(Avo);
		log.info(Avo);
	}
	@Test
	public void testinsertAuth() {
		AuthVO Avo=new AuthVO();
		
		Avo.setUserid("sansam41");
		Avo.setAuth("ROLE_MEMBER");
		mapper.insertAuth(Avo);
		log.info(Avo);
	
	}
	@Test
	public void passwd() {
		String pw=pwencoder.encode("1234");
		System.out.println(pw);
	}
	
	@Test
	public void chkId() {
		mapper.chkId("sansam41");
		
	}
}
