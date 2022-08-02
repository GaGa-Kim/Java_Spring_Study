package com.example.examplespring.framework.data.web;

import com.example.examplespring.framework.data.domain.MySQLPageRequest;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * MySQL 쿼리 페이징 LIMIT, OFFSET 값을 자동 계산하여 MysqlPageRequest 클래스에 담아서 컨트롤러에서 받을 수 있게 함
 * @author gagyeong
 */
public class MySQLRequestHandleMethodArgumentResolver implements HandlerMethodArgumentResolver {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String DEFAULT_PARAMETER_PAGE = "page";
    private static final String DEFAULT_PARAMETER_SIZE = "size";
    private static final int DEFAULT_SIZE = 20;

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        
        // 현재 페이지 정보
        int page = NumberUtils.toInt(request.getParameter(DEFAULT_PARAMETER_PAGE), 1);

        // 리스트 갯수 정보
        int offset = NumberUtils.toInt(request.getParameter(DEFAULT_PARAMETER_SIZE), DEFAULT_SIZE);

        // 시작 지점 계산
        int limit = (offset * page) - offset;
        logger.info("page : {}", page);
        logger.info("limit : {}, offset : {}", limit, offset);
        return new MySQLPageRequest(page, offset, limit, offset);
    }
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return MySQLPageRequest.class.isAssignableFrom(methodParameter.getParameterType());
    }
}
