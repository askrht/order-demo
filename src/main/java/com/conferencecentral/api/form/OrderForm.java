package com.conferencecentral.api.form;

import java.util.List;
import com.google.common.collect.ImmutableList;

public class OrderForm {
    private List<OrderQty> orderQty;

    @SuppressWarnings("unused")
	private OrderForm() {}

    public OrderForm(List<OrderQty> orderQty) {
        this.orderQty = orderQty == null ? null : ImmutableList.copyOf(orderQty);
    }

	public List<OrderQty> getOrderQty() {
		return orderQty == null ? null : ImmutableList.copyOf(orderQty);
	}

}


