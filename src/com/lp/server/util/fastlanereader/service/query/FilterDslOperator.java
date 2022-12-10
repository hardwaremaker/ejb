package com.lp.server.util.fastlanereader.service.query;

import java.sql.Timestamp;
import java.util.Collection;



public interface FilterDslOperator extends FilterDslEnd {
	FilterDslStringValue  equal(String value) ;
	FilterDslStringValue  in(String[] values) ;
	FilterDslStringValue  inString(Collection<String> values) ;
	FilterDslLikeStringValue like(String value) ;
	FilterDslStringValue  lt(Timestamp ts);

	FilterDslIntegerValue equal(Integer value) ;
	FilterDslIntegerValue in(Integer[] values) ;
	FilterDslIntegerValue inInteger(Collection<Integer> values) ;
	FilterDslIntegerValue lt(Integer value);
	FilterDslIntegerValue ltOrNull(Integer value);
	FilterDslIntegerValue lessEqual(Integer value);
	FilterDslIntegerValue gt(Integer value);
	FilterDslIntegerValue greaterEqual(Integer value);
	
//	FilterDslValue  in(List<String> values) ;

	FilterDslValue  isNull() ;
	FilterDslValue isNotNull() ;
	
	FilterDslOperator not() ;
}
