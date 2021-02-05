package org.zerock.mapper;

import org.zerock.domain.AuthVO;
import org.zerock.domain.MemberVO;

public interface MemberMapper {
	public MemberVO read(String userid);
	public int chkId(String userid);
	public void insertMember(MemberVO vo);
	public void insertAuth(AuthVO vo);
}
