package com.idc.easycheck.check;

import java.security.InvalidKeyException;

public class Order /* implements Serializable */{
//	private static final long serialVersionUID = 0xDEADBEEFL;

	public Menu menu;

	public Order() {
		this.menu = new Menu();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Dish dish : this.menu.dishes) {
			builder.append(String
					.format("%s: %s\n", dish.dishName, dish.amount));
		}
		return builder.toString();
	}

	public void addDish(String dishName) throws InvalidKeyException {
		Dish dish = this.menu.getDish(dishName);
		dish.amount++;
	}
	//
	// private void writeObject(ObjectOutputStream stream) throws IOException {
	// stream.writeObject(this.order);
	// }
	//
	// @SuppressWarnings("unchecked")
	// private void readObject(ObjectInputStream stream) throws IOException,
	// ClassNotFoundException {
	// Object obj = stream.readObject();
	//
	// if (obj instanceof HashMap) {
	// this.order = (ArrayList<Dish>) obj;
	// } else {
	// throw new ClassNotFoundException("Could not readObject!");
	// }
	// }
}