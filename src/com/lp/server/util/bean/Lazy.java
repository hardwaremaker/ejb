package com.lp.server.util.bean;

import java.util.Objects;

public final class Lazy<T> {
	private volatile T value;
	
	public T getOrCompute(ISupplier<T> supplier) {
		final T result = value; // Just one volatile read
		return result == null ? maybeCompute(supplier) : result;
	}
	
	private synchronized T maybeCompute(ISupplier<T> supplier) {
		if(value == null) {
			value = Objects.requireNonNull(supplier.get());
		}
		return value;
	}
}
