package com.lp.server.util.bean;

import javax.naming.Context;

import com.lp.server.util.FacLookup;

public class HvBean<C> extends HvAbstractBean<C> {

	public HvBean(Class<?> beanClass, Class<C> interfaceClass) {
		super(beanClass, interfaceClass);
	}
	
	@Override
	protected C getOrComputeBean(Context context, Class<?> beanClass, Class<C> interfaceClass) {
		return FacLookup.lookupBeanless(context, beanClass, interfaceClass);
	}
}
