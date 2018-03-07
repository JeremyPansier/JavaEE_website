package com.website.tools.authentication;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * The logger interceptor defined in the beans.xml.</br>
 *
 * @author Jérémy Pansier
 */
@Logged
@Interceptor
public class LoggerInterceptor {

	/**
	 * The method to invoke around the type tagged with {@link Logged} annotation.
	 *
	 * @param invocationContext the invocation context
	 * @return the result of the subsequent invocation processing
	 * @throws Exception the exception thrown by the subsequent invocation processing
	 */
	@AroundInvoke
	public Object aroundInvocation(final InvocationContext invocationContext) throws Exception {
		System.out.println("Hello");
		return invocationContext.proceed();
	}
}
