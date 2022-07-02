package com.example.examplespring.mvc.repository;

import com.example.examplespring.mvc.domain.Board;
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
    void save(Board board); // 등록
    void update(Board board); // 수정
    void delete(int boardSeq); // 삭제

}
