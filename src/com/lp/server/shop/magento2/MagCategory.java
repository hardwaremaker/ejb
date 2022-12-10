package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagCategory implements Serializable, IMagCustomAttribute {
	private static final long serialVersionUID = -8569459175678607768L;
	
	private Integer id;
	private Integer parentId;
	private String name;
	private boolean isActive;
	private Integer position;
	private Integer level;
	private Integer productCount;
	private List<MagCategory> childrenData;
	private boolean includeInMenu;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String children;
	private String path;
	private List<Map<String,Object>> customAttributes;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getParentId() {
		return parentId;
	}
	@JsonProperty("parent_id")
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isActive() {
		return isActive;
	}
	@JsonProperty("is_active")
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Integer getProductCount() {
		return productCount;
	}
	@JsonProperty("product_count")
	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}
	
	public List<MagCategory> getChildrenData() {
		return childrenData;
	}
	@JsonProperty("children_data")
	public void setChildrenData(List<MagCategory> childrenData) {
		this.childrenData = childrenData;
	}
	
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	public boolean isIncludeInMenu() {
		return includeInMenu;
	}
	@JsonProperty("include_in_menu")
	public void setIncludeInMenu(boolean includeInMenu) {
		this.includeInMenu = includeInMenu;
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	@JsonProperty("created_at")
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	@JsonProperty("updated_at")
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getChildren() {
		return children;
	}
	public void setChildren(String children) {
		this.children = children;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
}
