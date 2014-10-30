package com.idc.easycheck.check;

public class Payment {
	public Dish dish;
	public Costumer costumer;
	public float moneyCount;
	
	public Payment(Dish dish, Costumer costumer, float moneyCount) {
		this.dish = dish;
		this.costumer = costumer;
		this.moneyCount = moneyCount;
		
		dish.currentPaidCount += moneyCount;
		costumer.personalBill += moneyCount;
	}
}
