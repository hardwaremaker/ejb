package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagOrderExtensionAttributes implements Serializable {
	private static final long serialVersionUID = -703505776887417665L;

	private List<MagShippingAssignment> shippingAssignments;

	public List<MagShippingAssignment> getShippingAssignments() {
		return shippingAssignments;
	}

	@JsonProperty("shipping_assignments")
	public void setShippingAssignments(List<MagShippingAssignment> shippingAssignments) {
		this.shippingAssignments = shippingAssignments;
	}

}
