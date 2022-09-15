package com.example.examplespring.configuration.session;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class AbstractHttpSession<T> {

    /**
     * 세션에 사용되는 이름 (상속받은 클래스에서 세션에 사용될 이름을 가져오기 위함)
     * @return
     */
    protected abstract String name();

    /**
     * value를 세션에 저장
     * @param value
     */
    public void setAttribute(T value) {
        getSession().setAttribute(name(), value);
    }

    /**
     * 세션에 저장된 정보를 리턴
     * @return
     */
    @SuppressWarnings("unchecked")
    public T getAttribute() {
        return (T) getSession().getAttribute(name());
    }

    /**
     * 세션에 저장된 정보를 삭제
     */
    public void removeAttribute() {
        getSession().removeAttribute(name());
    }

    /**
     * 세션에 저장된 모든 정보 삭제 및 초기화
     */
    public void invalidate() {
        getSession().invalidate();
    }
    
    /**
     * HttpSession 리턴
     * @return
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * HttpServletRequest 리턴
     * @return
     */
    protected HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }
}
