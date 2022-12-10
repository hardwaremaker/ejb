package com.lp.server.util.fastlanereader.service.query;

import java.sql.Timestamp;
import java.util.Collection;

import com.lp.util.Helper;

public class DslOperator implements FilterDslOperator {

	private FilterKriterium fk ;
	private boolean invert ;
	
	public DslOperator(FilterKriterium fk) {
		this.fk = fk ;
		this.invert = false ;
	}
	
	public DslOperator(FilterKriterium fk, boolean invert) {
		this.fk = fk ;
		this.invert = invert ;
	}
	
	@Override
	public FilterKriterium build() {
		return fk ;
	}

	@Override
	public FilterDslStringValue equal(String value) {
		fk.value = "'" + value + "'";
		fk.operator += invert ? FilterKriterium.OPERATOR_NOT_EQUAL : FilterKriterium.OPERATOR_EQUAL ;
		return new DslStringValue(fk);
	}

	@Override
	public FilterDslLikeStringValue like(String value) {
		fk.value = value ;
		fk.operator += (invert ? "NOT " : "") + FilterKriterium.OPERATOR_LIKE ;	
		return new DslLikeStringValue(fk);
	}

	@Override
	public FilterDslIntegerValue equal(Integer value) {
		fk.value = value.toString();
		fk.operator += invert ? FilterKriterium.OPERATOR_NOT_EQUAL : FilterKriterium.OPERATOR_EQUAL ;
		return new DslIntegerValue(fk) ;
	}

	@Override
	public FilterDslIntegerValue gt(Integer value) {
		fk.value = value.toString();
		fk.operator += (invert ? "NOT " : "") + FilterKriterium.OPERATOR_GT ;
		return new DslIntegerValue(fk) ;
	}

	@Override
	public FilterDslIntegerValue lt(Integer value) {
		fk.value = value.toString();
		fk.operator += (invert ? "NOT " : "") + FilterKriterium.OPERATOR_LT ;
		return new DslIntegerValue(fk) ;
	}

	@Override
	public FilterDslIntegerValue ltOrNull(Integer value) {
		fk.value = value.toString() + " OR " + fk.kritName + " IS NULL)";
		fk.operator += (invert ? "NOT " : "") + FilterKriterium.OPERATOR_LT ;
		fk.kritName = "(" + fk.kritName;
		return new DslIntegerValue(fk) ;
	}
	
	@Override
	public FilterDslIntegerValue lessEqual(Integer value) {
		fk.value = value.toString();
		fk.operator += (invert ? "NOT " : "") + FilterKriterium.OPERATOR_LTE ;
		return new DslIntegerValue(fk) ;
	}

	@Override
	public FilterDslIntegerValue greaterEqual(Integer value) {
		fk.value = value.toString();
		fk.operator += (invert ? "NOT " : "") + FilterKriterium.OPERATOR_GTE ;
		return new DslIntegerValue(fk) ;
	}
	
	@Override
	public FilterDslValue isNull() {
		fk.value = "" ;
		fk.operator += FilterKriterium.OPERATOR_IS 
						+ " " + FilterKriterium.OPERATOR_NULL ;
		return new DslValue(fk);
	}

	@Override
	public FilterDslValue isNotNull() {
		fk.value = "" ;
		fk.operator = FilterKriterium.OPERATOR_IS 
						+ " " + FilterKriterium.OPERATOR_NOT_NULL ;
		return new DslValue(fk);
	}
	
	@Override
	public FilterDslOperator not() {
		fk.value = "" ;
//		fk.operator += " " + FilterKriterium.OPERATOR_NOT + " " ;
		return new DslOperator(fk, true);
	}
	
	private String buildInOperator(StringBuffer sb) {
		return sb.length() == 0 ? "" :  ((invert ? " NOT" : "") + " IN (" + sb.toString() + ")") ;
	}
	
	private void appendInValue(StringBuffer sb, String value) {
		if(sb.length() > 0) {
			sb.append(',');
		}
		sb.append("'") ;
		sb.append(value) ;
		sb.append("'") ;		
	}

	private void appendInValue(StringBuffer sb, Integer value) {
		if(sb.length() > 0) {
			sb.append(',');
		}
		sb.append(value) ;
	}
	
	@Override
	public FilterDslStringValue in(String[] values) {
		StringBuffer sb = new StringBuffer() ;
		for (String value : values) {
			appendInValue(sb, value);
		}
		fk.operator += buildInOperator(sb) ;
		fk.value = "" ;
		return new DslStringValue(fk);
	}
	
	@Override
	public FilterDslStringValue inString(Collection<String> values) {
		StringBuffer sb = new StringBuffer() ;
		for (String value : values) {
			appendInValue(sb, value);
		}
		fk.operator += buildInOperator(sb) ;
		fk.value = "" ;
		return new DslStringValue(fk);
	}
	
	@Override
	public FilterDslIntegerValue in(Integer[] values) {
		StringBuffer sb = new StringBuffer() ;
		for (Integer value : values) {
			appendInValue(sb, value);
		}
		fk.operator += buildInOperator(sb) ;
		fk.value = "" ;
		return new DslIntegerValue(fk);
	}	
	
	@Override
	public FilterDslIntegerValue inInteger(Collection<Integer> values) {
		StringBuffer sb = new StringBuffer() ;
		for (Integer value : values) {
			appendInValue(sb, value);
		}
		fk.operator += buildInOperator(sb) ;
		fk.value = "" ;
		return new DslIntegerValue(fk);
	}
	
	@Override
	public FilterDslStringValue lt(Timestamp value) {
		fk.value = "'" + Helper.formatTimestampWithSlashes(value) + "'";
		fk.operator += invert ? FilterKriterium.OPERATOR_GTE : FilterKriterium.OPERATOR_LT ;
		return new DslStringValue(fk);
	}
}