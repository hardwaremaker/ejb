package com.lp.server.util.collection;

import java.util.ArrayList;
import java.util.Collection;


public class CollectionTools {

	public static <T> T detect(Collection<T> collection, IDetect<T> detector) {
		for (T element : collection) {
			if(detector.accept(element)) {
				return element ;
			}
		}
		
		return null ;
	}

	public static <T> T detect(Collection<T> collection, IDetectIfNone<T> detector) {
		for (T element : collection) {
			if(detector.accept(element)) {
				return element ;
			}
		}
		
		return detector.ifNone() ;
	}

	public static <T> T detect(T[] collection, IDetect<T> detector) {
		for (T element : collection) {
			if(detector.accept(element)) {
				return element ;
			}
		}
		
		return null ;
	}

	public static <T> T detect(T[] collection, IDetectIfNone<T> detector) {
		for (T element : collection) {
			if(detector.accept(element)) {
				return element ;
			}
		}
		
		return detector.ifNone() ;
	}
	
	public static <T, S> S inject(S initialValue, Collection<T> collection, IInject<T, S> injector){
		S sum = initialValue;
		for (T each: collection) {
			sum = injector.inject(sum, each) ; 
		}
		
		return sum;
	}

	public static <T, S> S inject(S initialValue, T[] collection, IInject<T, S> injector){
		S sum = initialValue;
		for (T each: collection) {
			sum = injector.inject(sum, each) ; 
		}
		
		return sum;
	}
	
	public static <T> Collection<T> reject(Collection<T> collection, IReject<T> rejector) {
		Collection<T> result = new ArrayList<T>();
		for(T each: collection) {
			if(!rejector.reject(each)) {
				result.add(each);
			}
		}
		return result;
	}
	
	public static <T> Collection<T> select(Collection<T> collection, ISelect<T> selector) {
		Collection<T> result = new ArrayList<T>();
		for(T each: collection) {
			if(selector.select(each)) {
				result.add(each);
			}
		}
		return result;
	}
	
	public static <T> Collection<T> select(T[] collection, ISelect<T> selector) {
		Collection<T> result = new ArrayList<T>();
		for(T each: collection) {
			if(selector.select(each)) {
				result.add(each);
			}
		}
		return result;
	}
}
