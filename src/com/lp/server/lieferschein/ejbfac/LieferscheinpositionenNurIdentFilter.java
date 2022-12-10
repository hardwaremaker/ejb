package com.lp.server.lieferschein.ejbfac;

import java.io.Serializable;

import org.apache.commons.collections.Predicate;

import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;

public class LieferscheinpositionenNurIdentFilter implements Predicate,
		Serializable {
	private static final long serialVersionUID = -5617572280308975044L;

	@Override
	public boolean evaluate(Object arg0) {
		if (arg0 instanceof LieferscheinpositionDto) {
			LieferscheinpositionDto pos = (LieferscheinpositionDto) arg0;
			return LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT
					.equals(pos.getLieferscheinpositionartCNr());
		}

		return false;
	}
}
