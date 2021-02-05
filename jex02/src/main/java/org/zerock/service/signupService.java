package org.zerock.service;

import org.zerock.domain.MemberVO;

public interface signupService {
	public boolean checkId(String userid);

	public void insert(MemberVO vo);

}
