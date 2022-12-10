package com.lp.service.edifact;

import com.lp.service.edifact.errors.EdifactException;

public interface IValueSetter {
	void set(String value) throws EdifactException;
}
