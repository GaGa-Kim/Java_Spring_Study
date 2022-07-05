package com.example.examplespring.mvc.repository;

import com.example.examplespring.mvc.domain.Board;
import com.example.examplespring.mvc.parameter.BoardParameter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 게시판 Repository
 * @author gagyeong
 */
@Repository
public interface BoardRepository {

    List<Board> getList(); // 전체 조회
    Board get(int boardSeq); // 단건 조회
    void save(BoardParameter board); // 등록
    void update(BoardParameter board); // 수정
    void delete(int boardSeq); // 삭제

}
