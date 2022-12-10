package com.lp.server.util.bean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.util.EJBExceptionLP;

public abstract class HvAbstractBean<C> {
	private final Lazy<Context> context;
	private final Lazy<C> bean;
//	private final Class<?> beanClass;
//	private final Class<C> interfaceClass;
	
	private final ISupplier<C> beanSupplier;
	private final ISupplier<Context> contextSupplier;
	
	protected HvAbstractBean(final Class<?> beanClass, final Class<C> interfaceClass) {
		context = new Lazy<Context>();
		bean = new Lazy<C>();
//		this.beanClass = beanClass;
//		this.interfaceClass = interfaceClass;
		
		beanSupplier = new ISupplier<C>() {
			public C get() {
				return getOrComputeBean(getOrComputeContext(), beanClass, interfaceClass);
			}
		};
		
		contextSupplier = new ISupplier<Context>() {
			public Context get() {
				try {
					return new InitialContext();
				} catch(NamingException e) {
					throw new EJBExceptionLP(e);
				}
			}			
		};
	}
	
	public C get() {
		return bean.getOrCompute(beanSupplier);
	}
	
	protected abstract C getOrComputeBean(
			Context context, Class<?> beanClass, Class<C> interfaceClass);
	
	private Context getOrComputeContext() {
		return context.getOrCompute(contextSupplier);
	}
}
