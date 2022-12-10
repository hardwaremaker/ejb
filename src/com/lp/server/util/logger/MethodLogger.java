package com.lp.server.util.logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class MethodLogger {
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());

	@AroundInvoke
	public Object logging(InvocationContext ctx) throws Exception {
		long t1 = System.currentTimeMillis();

		String name = ctx.getMethod().getDeclaringClass().getName();
		String method = ctx.getMethod().getName();
		String sig = name + "." + method;
		String params = "\tParameter: (" + paramsAsString(ctx.getParameters()) + ")";
		try {
			myLogger.info(sig + params + " ...");

			Object result = ctx.proceed();
			long now = System.currentTimeMillis();

			myLogger.info(sig + " => (" + asString(result, 30) + ")" + params + "\tDauer: " + (now - t1) + "ms.");
			return result;
		} catch (Exception e) {
			myLogger.error(sig + params + " throwed:", e);
			throw e;
		}
	}

	private String shortenString(String value, int maxLength) {
		return value == null 
				? "<null>" 
				: value.length() > maxLength ? (value.substring(0, maxLength - 3) + "...") : value;
	}

	private String asString(Object o, int maxLength) {
		return o == null ? "null" : shortenString(o.toString(), maxLength);
	}

	private String paramsAsString(Object[] params) {
		if (params == null) {
			return "";
		}
		StringBuffer s = new StringBuffer();
		for (Object param : params) {
			if (s.length() > 0) {
				s.append(", ");
			}
			s.append(asString(param, 80));
		}
		return s.toString();
	}
}
