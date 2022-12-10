package com.lp.server.util.bean;

import javax.naming.Context;

import com.lp.server.util.FacLookup;

public class HvLocalBean<C> extends HvAbstractBean<C> {

	public HvLocalBean(Class<?> beanClass, Class<C> interfaceClass) {
		super(beanClass, interfaceClass);
	}
	
	@Override
	protected C getOrComputeBean(Context context, Class<?> beanClass, Class<C> interfaceClass) {
		return FacLookup.lookupLocalBeanless(context, beanClass, interfaceClass);
	}
}
