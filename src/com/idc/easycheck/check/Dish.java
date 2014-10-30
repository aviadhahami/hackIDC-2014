package com.idc.easycheck.check;

import java.util.HashMap;


public class Dish {
	
	public static HashMap<String, Integer> DISH_PHOTO_RES_ID_MAP;
	
	public String dishName;
	public float price;
	public int amount;
	public float currentPaidCount;
	
	public Dish(String dishName, int price) {
		this.dishName = dishName;
		this.price = price;
		this.amount = 0;
		this.currentPaidCount = 0;
	}
}