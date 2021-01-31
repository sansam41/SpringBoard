package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.controller.BoardController;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping
@AllArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{

	@Setter(onMethod_=@Autowired )
	private BoardMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;
	
	@Override
	public void register(BoardVO board) {
		// TODO Auto-generated method stub
		log.info("register........"+board);
		mapper.insertSelectKey(board);
		
		if(board.getAttachList()==null||board.getAttachList().size()<=0) {
			return;
		}
		board.getAttachList().forEach(attach->{
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	}

	@Override
	public BoardVO get(long bno) {
		// TODO Auto-generated method stub
		log.info("getBoard..........");
		return mapper.read(bno);
	}

	@Transactional
	@Override
	public boolean modify(BoardVO board) {
		// TODO Auto-generated method stub
		log.info("modify..........");
		attachMapper.deleteAll(board.getBno());
		boolean modifyResult=mapper.update(board)==1;
		if(modifyResult&&board.getAttachList()!=null&&board.getAttachList().size()>0) {
			board.getAttachList().forEach(attach->{
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}
		return modifyResult;
	}

	@Transactional
	@Override
	public boolean remove(long bno) {
		// TODO Auto-generated method stub
		log.info("remove..........");
		attachMapper.deleteAll(bno);
		return mapper.delete(bno)==1;
	}

//	@Override
//	public List<BoardVO> getList() {
//		// TODO Auto-generated method stub
//		log.info("getList..........");
//		return mapper.getList();
//	}
	
	@Override
	public List<BoardVO> getList(Criteria cri){
		log.info("get list with criteria: " + cri);
		return mapper.getListWithPaging(cri);
	}
	
	@Override
	public int getTotal(Criteria cri) {
		return mapper.getTotalCount(cri);
	}
	
	@Override
	public List<BoardAttachVO> getAttachList(Long bno){
		log.info("get Attach list by bno "+bno);
		return attachMapper.findByBno(bno);
	}
	
}
