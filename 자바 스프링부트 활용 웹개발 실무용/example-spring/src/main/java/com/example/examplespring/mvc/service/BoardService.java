package com.example.examplespring.mvc.service;

import com.example.examplespring.mvc.domain.Board;
import com.example.examplespring.mvc.parameter.BoardSearchParameter;
import com.example.examplespring.mvc.repository.BoardRepository;
import com.example.examplespring.mvc.parameter.BoardParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 게시판 서비스
 * @author gagyeong
 */
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    /**
     * 목록 리턴
     * @return
     */
    public List<Board> getList(BoardSearchParameter parameter) {
        return boardRepository.getList(parameter);
    }

    /**
     * 상세 정보 리턴
     * @param boardSeq
     * @return
     */
    public Board get(int boardSeq) {
        return boardRepository.get(boardSeq);
    }

    /**
     * 등록/수정 처리
     * @param parameter
     */
    public void save(BoardParameter parameter) {
        // 조회하여 리턴된 정보
        Board board = boardRepository.get(parameter.getBoardSeq());
        if (board == null) {
            boardRepository.save(parameter);
        } else {
            boardRepository.update((parameter));
        }
    }

    /**
     * 단순 for 반복문을 이용한 등록 처리
     * @param list
     */
    public void saveList1(List<BoardParameter> list) {
        for (BoardParameter parameter : list) {
            boardRepository.save(parameter);
        }
    }

    /**
     * 100개씩 배열에 담아서 일괄 등록 처리
     * @param boardList
     */
    public void saveList2(List<BoardParameter> boardList) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("boardList", boardList);
        boardRepository.saveList(paramMap);
    }
    
    /**
     * 삭제 처리
     * @param boardSeq
     */
    public void delete(int boardSeq) {
        boardRepository.delete(boardSeq);
    }

}
