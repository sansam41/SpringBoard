package org.zerock.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@ContextConfiguration(classes= {org.zerock.config.RootConfig.class})
@Log4j
public class BoardServiceTests {
	@Setter(onMethod_=@Autowired)
	private BoardService service;
	
	@Test
	public void testExist() {
		log.info(service);
		assertNotNull(service);
	}
	
	@Test
	public void testRegister() {
		BoardVO board=new BoardVO();
		board.setTitle("boardservice를 통해 제목 작성");
		board.setContent("boardservice를 통해 내용 작성");
		board.setWriter("boardservice를 통해 작가 작성");
		
		service.register(board);
		
		log.info("생성된 게시물 번호"+board.getBno());
	}
	
	//@Test
	//public void testGetList() {
	//	log.info("get List");
	//	service.getList().forEach(board->log.info(board));
	//}
	
	@Test
	public void testGetList() {
		log.info("get list with paging: ");
		service.getList(new Criteria(2,10)).forEach(board->log.info(board));
	}
	
	@Test
	public void testGet() {
		log.info("get board");
		service.get(1L);
	}
	

	@Test
	public void testRemove() {
		log.info("Remove");
		service.remove(2L);
	}
	
	@Test
	public void testModify() {
		BoardVO board=service.get(1L);
		if(board==null)
			return;
		board.setTitle("boardServiace를 통해 제목 수정");
		log.info("MODIFY RESULT: "+service.modify(board));
	}
	

}
