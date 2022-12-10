package com.lp.server.system.jcr.service.docnode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import com.lp.server.personal.service.PersonalDto;

public class DocNodeEdifactOrders extends DocNodeEdifactBase {
	private static final long serialVersionUID = 330550343324213717L;

	private Date importDate;
	private String formattedDate;
	
	public DocNodeEdifactOrders(PersonalDto personalDto, Date importDate) {
		super(BELEGART_EDIFACT, BELEGART_EDIFACTORDERS, personalDto);
		this.importDate = importDate;
		prepareFormattedDate();
	}
	
	public DocNodeEdifactOrders(Node node) {
		super(node);
	}
	
	@Override
	public String asVisualPath() {
		return formattedDate;
	}

	@Override
	public String asPath() {
		return String.valueOf(importDate.getTime());
	}
	
	private void prepareFormattedDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		formattedDate = df.format(importDate);		
	}
	
	@Override
	protected void persistToImpl(Node node) throws RepositoryException {
		super.persistToImpl(node);

		node.setProperty(NODEPROPERTY_LITERALVALUE, importDate.getTime());
	}

	@Override
	protected void loadFromImpl(Node node) throws RepositoryException {
		super.loadFromImpl(node);
		
		importDate = new Date(node.getProperty(NODEPROPERTY_LITERALVALUE).getLong());
		prepareFormattedDate();
	}

	@Override
	public List<DocNodeBase> getHierarchy() {
		List<DocNodeBase> all = super.getHierarchy();
		all.add(new DocNodeLiteral(asPath(), asVisualPath()));
		return all;
	}
}
