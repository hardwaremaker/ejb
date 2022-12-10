package com.lp.server.util.fastlanereader.service.query;

public class DslStringValue implements FilterDslStringValue {

	private FilterKriterium fk;
	
	public DslStringValue(FilterKriterium fk) {
		this.fk = fk ;
	}
	
	@Override
	public FilterKriterium build() {
		return fk ;
	}
	
	@Override
	public FilterDslStringValue ignoreCase() {
		fk.setBIgnoreCase(true);
		return this ;
	}
}
