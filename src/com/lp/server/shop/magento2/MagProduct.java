package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagProduct implements Serializable, IMagCustomAttribute {
	private static final long serialVersionUID = 2869541193908579753L;

	private Integer id;
	private String sku;
	private String name;
	private BigDecimal price;
	private List<Map<String,Object>> customAttributes;
	private MagProductExtensionAttributes extensionAttributes;
	private String typeId;
	private Integer attributeSetId;
	private Integer status;
	private Integer visibility;
	private List<MagProductMediaGalleryEntry> mediaGalleryEntries;
	private List<MagTierPriceEntry> tierPrices;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public List<Map<String,Object>> getCustomAttributes() {
		return customAttributes;
	}
	
	@JsonProperty("custom_attributes")
	public void setCustomAttributes(List<Map<String,Object>> customAttributes) {
		this.customAttributes = customAttributes;
	}
	
	@JsonIgnore
	public void addAttribute(String key, Object value) {
		Map<String, Object> entry = new HashMap<String, Object>();
		entry.put("attribute_code", key);
		entry.put("value", value);
		
		if(customAttributes == null) {
			customAttributes = new ArrayList<Map<String,Object>>();
		} else {
			removeAttribute(key);
		}
		
		customAttributes.add(entry);
	}
	
	@JsonIgnore
	public void removeAttribute(String key) {
		if(customAttributes == null) {
			return;
		}

		for(int i = 0; i < customAttributes.size(); i++) {
			Map<String, Object> m = customAttributes.get(i);
			if(key.equals(m.get("attribute_code"))) {
				customAttributes.remove(i);
				break;
			}
		}
	}

	@JsonIgnore
	public Object getAttribute(String key) {
		if(customAttributes == null) {
			 return null;
		}
		
		for (Map<String, Object> entry : customAttributes) {
			String mk = (String) entry.get("attribute_code");
			if(key.equals(mk)) {
				return entry.get("value");
			}
		}
		
		return null;
	}
	
	public MagProductExtensionAttributes getExtensionAttributes() {
		return extensionAttributes;
	}
	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(MagProductExtensionAttributes extensionAttributes) {
		this.extensionAttributes = extensionAttributes;
	}
	public String getTypeId() {
		return typeId;
	}
	@JsonProperty("type_id")
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public Integer getAttributeSetId() {
		return attributeSetId;
	}
	
	@JsonProperty("attribute_set_id")	
	public void setAttributeSetId(Integer attributeSetId) {
		this.attributeSetId = attributeSetId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getVisibility() {
		return visibility;
	}
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	public List<MagProductMediaGalleryEntry> getMediaGalleryEntries() {
		return mediaGalleryEntries;
	}
	@JsonProperty("media_gallery_entries")
	public void setMediaGalleryEntries(List<MagProductMediaGalleryEntry> mediaGalleryEntries) {
		this.mediaGalleryEntries = mediaGalleryEntries;
	}
	public List<MagTierPriceEntry> getTierPrices() {
		return tierPrices;
	}
	public void setTierPrices(List<MagTierPriceEntry> tierPrices) {
		this.tierPrices = tierPrices;
	}
}
