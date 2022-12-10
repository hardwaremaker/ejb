package com.lp.server.util.fastlanereader.service.query;

public class DslIntegerValue implements FilterDslIntegerValue {

	private FilterKriterium fk ;
	
	public DslIntegerValue(FilterKriterium fk) {
		this.fk = fk ;
	}
	
	@Override
	public FilterKriterium build() {
		return fk ;
	}
}
