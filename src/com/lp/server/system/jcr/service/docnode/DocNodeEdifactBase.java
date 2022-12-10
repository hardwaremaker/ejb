package com.lp.server.system.jcr.service.docnode;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;

public class DocNodeEdifactBase extends DocNodeFolder {
	private static final long serialVersionUID = 3714499219246132336L;

	private PersonalDto personalDto;
	private Integer partnerId;
	private String artCnr;
	private String partnerName;
	private String mandantCnr;
	
	protected DocNodeEdifactBase(String belegart, String artCnr, PersonalDto personalDto) {
		super(belegart);
		this.mandantCnr = personalDto.getMandantCNr();
		this.artCnr = artCnr;
		this.personalDto = personalDto;
		this.partnerId = this.personalDto.getPartnerIId();
		this.partnerName = getPartnername(this.personalDto.getPartnerDto());
	}
	
	protected DocNodeEdifactBase(Node node) {
		super(node);
	}
	
	protected PersonalDto getPersonalDto() {
		return this.personalDto;
	}
	
	public String getArtCnr() {
		return this.artCnr;
	}
	
	private String getPartnername(PartnerDto partner) {
		return (partner.getCName1nachnamefirmazeile1() + (partner
				.getCName2vornamefirmazeile2() == null ? "" : (" " + partner
				.getCName2vornamefirmazeile2())));
	}

	@Override
	protected void loadFromImpl(Node node)
			throws RepositoryException {
		artCnr = node.getProperty(NODEPROPERTY_ARTCNR).getString();
		partnerName = node.getProperty(NODEPROPERTY_SHOWINGVALUE).getString();
		partnerId = new Long(node.getProperty(NODEPROPERTY_HELPERIID).getLong()).intValue();
		mandantCnr = node.getProperty(NODEPROPERTY_MANDANTCNR).getString();
	}

	@Override
	protected void persistToImpl(Node node) throws RepositoryException {
		node.setProperty(NODEPROPERTY_SHOWINGVALUE, partnerName);
		node.setProperty(NODEPROPERTY_ARTCNR, artCnr);
		node.setProperty(NODEPROPERTY_HELPERIID, partnerId);
		node.setProperty(NODEPROPERTY_MANDANTCNR, mandantCnr);
	}

	@Override
	public List<DocNodeBase> getHierarchy() {
		return new HeliumDocPath()
				.add(new DocNodeLiteral(mandantCnr))
				.add(new DocNodeFolder(super.asPath()))
				.add(new DocNodeFolder(getArtCnr()))
				.add(new DocNodeLiteral(String.valueOf(partnerId), partnerName))
				.asDocNodeList(); 
	}
}
