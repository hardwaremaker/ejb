package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagProductMediaGalleryEntry implements Serializable {
	private static final long serialVersionUID = -2663439538119454702L;

	private Integer id;
	private String mediaType;
	private String label;
	private Integer position;
	private boolean disabled;
	private List<String> types;
	private String file;
	private MediaGalleryContent content;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMediaType() {
		return mediaType;
	}
	@JsonProperty("media_type")
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public MediaGalleryContent getContent() {
		return content;
	}
	public void setContent(MediaGalleryContent content) {
		this.content = content;
	}
}
