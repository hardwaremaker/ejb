package com.lp.server.util.fastlanereader.service.query;

public interface FilterDslLikeStringValue extends FilterDslEnd {
	enum Wildcard {
		LEADING,
		TRAILING,
		BOTH,
		EXTENDED
	}
	FilterDslEnd wildcard(Wildcard position) ;
	
	FilterDslEnd leading() ;
	FilterDslEnd trailing() ;
	FilterDslEnd both() ;
	
	FilterDslLikeStringValue ignoreCase() ;	
}
