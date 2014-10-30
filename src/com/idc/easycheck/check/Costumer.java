package com.idc.easycheck.check;

public class Costumer {
	public String name;
	public int fullResID;
	public int emptyResID;
	public double personalBill;
	public int tipPercent;

	public Costumer(String name, int fullResID, int emptyResID, double bill) {
		this.name = name;
		this.fullResID = fullResID;
		this.emptyResID = emptyResID;
		this.personalBill = bill;
		tipPercent = 10;
	}

	public String getCostumerName() {
		return this.name;
	}

	public double getPersonalBill() {
		return this.personalBill;
	}

}
