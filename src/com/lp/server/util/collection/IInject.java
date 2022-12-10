package com.lp.server.util.collection;

public interface IInject<T, S> {
	S inject(S sum, T each);
}
