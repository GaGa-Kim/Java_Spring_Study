package com.example.examplespring.configuration.session;

import com.example.examplespring.mvc.domain.SessionNiceAuth;
import org.springframework.stereotype.Component;

@Component
public class HttpSessionNiceAuth extends AbstractHttpSession<SessionNiceAuth> {

    @Override
    protected String name() {
        return "SESSION_NICEAUTH";
    }
}
