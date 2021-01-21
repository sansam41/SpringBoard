package org.zerock.mapper;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {org.zerock.config.RootConfig.class})
@Log4j
public class ReplyMapperTests {
	
	private Long[] bnoArr= {30L,31L,32L,33L,34L,35L};
	
	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	@Test
	public void testMapper() {
		log.info(mapper);
	}
	
	@Test
	public void testCreate() {
		IntStream.rangeClosed(1,10).forEach(i->{
			ReplyVO vo=new ReplyVO();
			
			vo.setBno(bnoArr[i%5]);
			vo.setReply("댓글 테스트"+i);
			vo.setReplyer("replyer"+i);
			
			mapper.insert(vo);
		});
	}
	
	@Test
	public void testRead() {
		Long tartgetRno=5L;
		ReplyVO vo=mapper.read(tartgetRno);
		log.info(vo);
	}
	
	@Test
	public void testDelete() {
		Long targetRno=1L;
		mapper.delete(targetRno);
	}
	
	@Test
	public void testUpdate() {
		Long targetRno=10L;
		ReplyVO vo=mapper.read(targetRno);
		vo.setReply("update reply ");
		int count=mapper.update(vo);
		log.info("Update count: "+count);
	}
	
	@Test
	public void testList() {
		Criteria cri =new Criteria();
		List<ReplyVO> replies=mapper.getListWithPaging(cri, bnoArr[1]);
		
		replies.forEach(reply->log.info(reply));
	}
	
	@Test
	public void testList2(){
		Criteria cri= new Criteria(2,10);
		List<ReplyVO> replies=mapper.getListWithPaging(cri, 1L);
		replies.forEach(reply->log.info(reply));
	}
	
}
