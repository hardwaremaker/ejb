package com.lp.server.system.jcr.ejbfac;

import javax.ejb.Local;
import javax.jcr.Node;
import javax.jcr.NodeIterator;

@Local
public interface DmsNodeVisitor {
	
	void initializeVisit();
	void doneVisit();
	
	void start() ;
	void stop() ;
	
	/**
	 * Eine Ebene tiefer
	 */
	void push(String path) ;
	
	/**
	 * eine Ebene hoeher
	 */
	void pop() ;
	
	/**
	 * Beginn der Iteration ueber die Nodes der aktuellen Ebene
	 * 
	 * @param nodeIterator ist einzig und alleine fuer READ-ONLY Zwecke
	 */
	void startIterate(NodeIterator nodeIterator) ;
	
	void stopIterator() ;
	
	void visitPath(Node n) ;
	void visitData(Node n) ;
}
