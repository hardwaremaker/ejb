package com.lp.server.util.fastlanereader.service.query;

public class DslEnd implements FilterDslEnd {

	private FilterKriterium fk ;
	
	public DslEnd(FilterKriterium fk) {
		this.fk = fk ;
	}
	
	@Override
	public FilterKriterium build() {
		return fk ;
	}

}
