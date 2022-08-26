package com.example.examplespring.mvc.controller;

import com.example.examplespring.configuration.GlobalConfig;
import com.example.examplespring.configuration.exception.BaseException;
import com.example.examplespring.configuration.http.BaseResponse;
import com.example.examplespring.configuration.http.BaseResponseCode;
import com.example.examplespring.mvc.parameter.UploadFileParameter;
import com.example.examplespring.mvc.service.UploadFileService;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @Autowired
    private UploadFileService uploadFileService;

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

        // 날짜폴더를 추가
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        String uploadFilePath = config.getUploadFilePath() + currentDate + "/";
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
        String resourcePathname = config.getUploadResourcePath() + currentDate + "/" + filename;
        // 파일 생성
        File dest = new File(pathname);
        try {
            multipartFile.transferTo(dest);
            // 파일 업로드된 후 DB에 저장
            UploadFileParameter parameter = new UploadFileParameter();
            // 컨텐츠 종류
            parameter.setContentType(multipartFile.getContentType());
            // 원본 파일명
            parameter.setOriginalFilename(multipartFile.getOriginalFilename());
            // 저장 파일명
            parameter.setFilename(filename);
            // 실제 파일 저장 경로
            parameter.setPathname(pathname);
            // static resource 접근 경로
            parameter.setResourcePathname(resourcePathname);
            uploadFileService.save(parameter);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("e", e);
        }

        return new BaseResponse<Boolean>(true);
    }

}
