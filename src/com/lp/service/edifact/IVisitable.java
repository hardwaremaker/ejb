package com.lp.service.edifact;

public interface IVisitable {
	void accept(IVisitor visitor);
}
