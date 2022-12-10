package com.lp.server.util.fastlanereader.service.query;

public class DslLikeStringValue implements FilterDslLikeStringValue {

	private FilterKriterium fk ;
	
	public DslLikeStringValue(FilterKriterium fk) {
		this.fk = fk ;
	}
	
	@Override
	public FilterKriterium build() {
		return fk ;
	}

	@Override
	public FilterDslEnd wildcard(Wildcard position) {
		fk.value = "'" + percentImpl(position) + "'" ;
		return new DslEnd(fk) ;
	}
	
	private String percentImpl(Wildcard position) {
		if(position == Wildcard.LEADING) {
			return  "%" + fk.value ;
		}
		if(position == Wildcard.TRAILING) {
			return fk.value + "%" ;
		}
		if(position == Wildcard.BOTH) {
			return "%" + fk.value + "%" ;
		}
		
		return fk.value ;
	}

	@Override
	public FilterDslEnd leading() {
		return wildcard(Wildcard.LEADING);
	}

	@Override
	public FilterDslEnd trailing() {
		return wildcard(Wildcard.TRAILING);
	}

	@Override
	public FilterDslEnd both() {
		return wildcard(Wildcard.BOTH);
	}
	
	@Override
	public FilterDslLikeStringValue ignoreCase() {
		fk.setBIgnoreCase(true);
		return this ;
	}	
}
