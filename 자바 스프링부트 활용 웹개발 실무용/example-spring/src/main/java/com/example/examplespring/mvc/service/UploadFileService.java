package com.example.examplespring.mvc.service;

import com.example.examplespring.mvc.domain.UploadFile;
import com.example.examplespring.mvc.parameter.UploadFileParameter;
import com.example.examplespring.mvc.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 업로드 파일 서비스
 * @author gagyeong
 */
@Service
public class UploadFileService {

    @Autowired
    private UploadFileRepository uploadFileRepository;

    /**
     * 등록 처리
     * @param parameter
     */
    public void save(UploadFileParameter parameter) {
        uploadFileRepository.save(parameter);
    }

    public UploadFile get(int uploadFileSeq) {
        return uploadFileRepository.get(uploadFileSeq);
    }
}
