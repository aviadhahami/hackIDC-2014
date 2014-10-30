package com.idc.easycheck.check;

import java.security.InvalidKeyException;
import java.util.ArrayList;

public class Costumers {
	static public ArrayList<Costumer> costumers = new ArrayList<Costumer>();

	public static void addCostumer(Costumer costumer) {
		costumers.add(costumer);
	}

	public static int getAmount() {
		return costumers.size();
	}

	public static Costumer getCostumer(String name) throws InvalidKeyException {
		for (Costumer costumer : costumers) {
			if (costumer.name.equals(name)) {
				return costumer;
			}
		}
		throw new InvalidKeyException(name);
	}
}

