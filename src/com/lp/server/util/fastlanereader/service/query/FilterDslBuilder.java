package com.lp.server.util.fastlanereader.service.query;

public class FilterDslBuilder implements FilterDslStart {
	private FilterKriterium fk ;
	
	@Override
	public FilterDslOperator field(String fieldName) {
		fk = new FilterKriterium(fieldName, true, "", null, false) ;
		return new DslOperator(fk);
	}
	
	public static FilterDslOperator create(String fieldName) {
		FilterKriterium fk = new FilterKriterium(fieldName, true, "", "", false) ;
		return new DslOperator(fk);
	}
}
