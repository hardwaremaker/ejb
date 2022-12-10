package com.lp.server.system.jcr.service.docnode;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class DocNodeGeschaeftsjahr extends DocNodeFolder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8167638166504804710L;
	
	private Integer geschaeftsjahr;

	public DocNodeGeschaeftsjahr(Integer jahr) {
		super(BELEGART_GESCHAEFTSJAHR);
		geschaeftsjahr = jahr;
	}

	public DocNodeGeschaeftsjahr(Node node) {
		super(node);
	}

	@Override
	public String asVisualPath() {
		return geschaeftsjahr.toString();
	}

	@Override
	public String asPath() {
		return geschaeftsjahr.toString();
	}

	@Override
	protected void persistToImpl(Node node) throws RepositoryException {
		super.persistToImpl(node);

		node.setProperty(NODEPROPERTY_GESCHAEFTSJAHR, geschaeftsjahr);
	}

	@Override
	protected void loadFromImpl(Node node) throws RepositoryException {
		super.loadFromImpl(node);
		
		geschaeftsjahr = new Long(node.getProperty(NODEPROPERTY_GESCHAEFTSJAHR).getLong()).intValue();
	}

	
}
