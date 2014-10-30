package com.idc.easycheck.check;

import java.security.InvalidKeyException;

import android.util.SparseArray;

public class Orders {
	static private SparseArray<Order> orders = new SparseArray<Order>();
	static private int currentOrderID = 0;
	
	public static int addOrder(Order order) {
		orders.put(currentOrderID, order);
		currentOrderID++;
		return currentOrderID;
	}
	
	public static Order getOrder(int orderID) throws InvalidKeyException {
		Order ret = orders.get(orderID);
		if (null == ret) {
			throw new InvalidKeyException(String.format("%d", orderID));
		}
		return ret;
	}
}