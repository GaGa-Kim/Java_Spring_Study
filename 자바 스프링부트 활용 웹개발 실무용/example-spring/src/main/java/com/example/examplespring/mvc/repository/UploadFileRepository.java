package com.example.examplespring.mvc.repository;

import com.example.examplespring.mvc.domain.UploadFile;
import com.example.examplespring.mvc.parameter.UploadFileParameter;
import org.springframework.stereotype.Repository;

/**
 * 업로드 파일 Repository
 * @author gagyeong
 */
@Repository
public interface UploadFileRepository {

    void save(UploadFileParameter uploadFile);

    UploadFile get(int uploadFileSeq);
}
