package com.example.examplespring.mvc.controller;

import com.example.examplespring.configuration.GlobalConfig;
import com.example.examplespring.configuration.exception.BaseException;
import com.example.examplespring.configuration.http.BaseResponse;
import com.example.examplespring.configuration.http.BaseResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 파일 컨트롤러
 * @author gagyeong
 */
@RestController
@RequestMapping("/file")
@Api(tags = "파일 API")
public class FileController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GlobalConfig config;

    /**
     * 업로드 리턴
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "업로드", notes = "")
    public BaseResponse<Boolean> save(@RequestParam("uploadFile") MultipartFile multipartFile) {
        logger.debug("multipartFile : {}", multipartFile);
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BaseException(BaseResponseCode.DATA_IS_NULL);
        }

        String uploadFilePath = config.getUploadFilePath();
        logger.debug("uploadFilePath : {}", uploadFilePath);
        String prefix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1, multipartFile.getOriginalFilename().length());
        String filename = UUID.randomUUID().toString() + "." + prefix;
        logger.info("filename : {}", filename);

        // 저장될 파일 상위경로의 폴더가 없다면 생성
        File folder = new File(uploadFilePath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        String pathname = uploadFilePath + filename;
        // 파일 생성
        File dest = new File(pathname);
        try {
            multipartFile.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("e", e);
        }

        return new BaseResponse<Boolean>(true);
    }

}
