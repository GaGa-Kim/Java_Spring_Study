package com.example.examplespring.configuration.session;

import com.example.examplespring.mvc.domain.SessionMember;
import org.springframework.stereotype.Component;

@Component
public class HttpSessionMember extends AbstractHttpSession<SessionMember> {

    @Override
    protected String name() {
        return "SESSION_MEMBER";
    }
}
