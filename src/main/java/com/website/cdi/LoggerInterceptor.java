package com.website.cdi;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Logged
@Interceptor
public class LoggerInterceptor {

    @AroundInvoke
    public Object aroundInvocation(InvocationContext invocationContext) throws Exception {
        System.out.println("Hello");
        return invocationContext.proceed();

    }
}