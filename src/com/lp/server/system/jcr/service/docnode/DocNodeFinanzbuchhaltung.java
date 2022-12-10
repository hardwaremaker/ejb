package com.lp.server.system.jcr.service.docnode;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class DocNodeFinanzbuchhaltung extends DocNodeFolder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1497644870099300327L;
	private int iGeschaeftsjahr;
	private String sMandant;
	
	public DocNodeFinanzbuchhaltung(String mandantCNr, int geschaeftsjahr) {
		super(BELEGART_FINANZBUCHHALTG);
		sMandant = mandantCNr;
		iGeschaeftsjahr = geschaeftsjahr;
	}

	public DocNodeFinanzbuchhaltung(Node node) {
		super(node);
	}

	@Override
	public List<DocNodeBase> getHierarchy() {
		return new HeliumDocPath().add(new DocNodeLiteral(sMandant))
				.add(this)
				.add(new DocNodeGeschaeftsjahr(iGeschaeftsjahr))
//				.add(new DocNodeLiteral(String.valueOf(iGeschaeftsjahr)))
				.asDocNodeList();
	}

	@Override
	protected void persistToImpl(Node node) throws RepositoryException {
		super.persistToImpl(node);

		node.setProperty(NODEPROPERTY_MANDANTCNR, sMandant);
	}

	@Override
	protected void loadFromImpl(Node node) throws RepositoryException {
		super.loadFromImpl(node);
		
		sMandant = node.getProperty(NODEPROPERTY_MANDANTCNR).getString();
	}
	
}
