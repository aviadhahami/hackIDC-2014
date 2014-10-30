package com.idc.easycheck.check;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;

public class Check {
	public HashMap<Dish, ArrayList<Payment>> ordersByDish;
	public HashMap<Costumer, ArrayList<Payment>> ordersByCostumer;
	
	public Check(int tableID, Order order) {

		this.ordersByDish = new HashMap<Dish, ArrayList<Payment>>();

		for (Dish dish : order.menu.dishes) {
			if (dish.amount > 0) {
				this.ordersByDish.put(dish, new ArrayList<Payment>());
			}
		}
		
		this.ordersByCostumer = new HashMap<Costumer, ArrayList<Payment>>();
		for (Costumer costumer : Costumers.costumers) {
			this.ordersByCostumer.put(costumer, new ArrayList<Payment>());
		}
	}
	
	public Dish getDish(String dishName) throws InvalidKeyException {
		for (Dish dish : this.ordersByDish.keySet()) {
			if (dish.dishName.equals(dishName)) {
				return dish;
			}
		}
		
		throw new InvalidKeyException(dishName);
	}
	
	public void addPayment(Dish dish, Costumer costumer, float amount) {
		Payment payment = new Payment(dish, costumer, amount);
		
		this.ordersByDish.get(dish).add(payment);
		this.ordersByCostumer.get(costumer).add(payment);
	}
	
	public float getCurrentPaidSum() {
		float sum = 0;
		
		for (ArrayList<Payment> payments : this.ordersByDish.values()) {
			for (Payment payment : payments) {
				sum += payment.moneyCount;
			}
		}
		
		return sum;
	}
	
	public float getCurrentWantedSum() {
		float sum = 0;
		
		for (Dish dish : this.ordersByDish.keySet()) {
			sum += (dish.amount * dish.price);
		}
		
		return sum;
	}

	public void clearPaymentsForDisk(Dish dish) {
		this.ordersByDish.get(dish).clear();
		
		HashMap<Costumer, ArrayList<Payment>> newOrdersByCostumer = new HashMap<Costumer, ArrayList<Payment>>();
		
		for (Costumer costumer : this.ordersByCostumer.keySet()) {
			ArrayList<Payment> newPayments = new ArrayList<Payment>();
			for (Payment payment : this.ordersByCostumer.get(costumer)) {
				if (!payment.dish.equals(dish)) {
					newPayments.add(payment);
				} else {
					dish.currentPaidCount = 0;
					costumer.personalBill -= payment.moneyCount;
				}
			}
			this.ordersByCostumer.get(costumer).clear();
			newOrdersByCostumer.put(costumer, newPayments);
		}
		
		this.ordersByCostumer.clear();
		this.ordersByCostumer = null;
		this.ordersByCostumer = newOrdersByCostumer;
	}
}