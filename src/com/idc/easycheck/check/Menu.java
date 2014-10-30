package com.idc.easycheck.check;

import java.security.InvalidKeyException;
import java.util.LinkedList;

public class Menu {
	
	public LinkedList<Dish> dishes;
	
	public Menu() {
		this.dishes = new LinkedList<Dish>();
		
		this.populate();
	}
	
	private void populate() {
		this.dishes.add(new Dish("Burger", 45));
		this.dishes.add(new Dish("Chips", 10));
		this.dishes.add(new Dish("Fish", 45));
		this.dishes.add(new Dish("Fish_and_Chips", 50));
		this.dishes.add(new Dish("Beer", 24));
		this.dishes.add(new Dish("Cold_Beer", 27));
	}
	
	public Dish getDish(String dishName) throws InvalidKeyException {
		for (Dish dish : this.dishes) {
			if (dish.dishName.equals(dishName)) {
				return dish;
			}
		}
		
		throw new InvalidKeyException(dishName);
	}
}