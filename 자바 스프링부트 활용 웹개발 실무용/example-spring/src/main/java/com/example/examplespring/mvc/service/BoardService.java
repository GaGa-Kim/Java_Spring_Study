package com.example.examplespring.mvc.service;

import com.example.examplespring.mvc.domain.Board;
import com.example.examplespring.mvc.repository.BoardRepository;
import com.example.examplespring.mvc.parameter.BoardParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Board> getList() {
        return boardRepository.getList();
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
     * 삭제 처리
     * @param boardSeq
     */
    public void delete(int boardSeq) {
        boardRepository.delete(boardSeq);
    }

}
