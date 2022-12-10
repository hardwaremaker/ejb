package com.lp.server.artikel.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "WebshopItemImageEntry")
public class WebshopItemImageEntry implements Serializable {
	private static final long serialVersionUID = -2944106489193960855L;
	
	private Integer id ;
	private byte[] image ;
	private String name ;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
