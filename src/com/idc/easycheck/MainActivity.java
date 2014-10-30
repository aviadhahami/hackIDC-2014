package com.idc.easycheck;

import java.security.InvalidKeyException;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.idc.easycheck.check.Check;
import com.idc.easycheck.check.Checks;
import com.idc.easycheck.check.Costumer;
import com.idc.easycheck.check.Costumers;
import com.idc.easycheck.check.Dish;
import com.idc.easycheck.check.Order;
import com.idc.easycheck.check.Orders;

public class MainActivity extends FragmentActivity implements OrderUpdateCallback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// remove bars
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		// Creating new CustomersFragment, or retrieving one if exists
		CustomersFragment customersFragment = (CustomersFragment) getSupportFragmentManager()
				.findFragmentByTag(CustomersFragment.TAG); 
		if (customersFragment == null) {
			customersFragment = new CustomersFragment();
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.customersFragmentContainer,
							customersFragment, CustomersFragment.TAG).commit();
		}

		OrdersFragment ordersFragment = (OrdersFragment) getSupportFragmentManager()
				.findFragmentByTag(OrdersFragment.TAG);
		if (ordersFragment == null) {
			ordersFragment = OrdersFragment.newInstance();
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.ordersFragmentContainer, ordersFragment,
							OrdersFragment.TAG).commit();
		}
		
		ordersFragment.setOrderUpdateListener(this);

		// ViewServer.get(this).addWindow(this);
		
		populateCostumers();
		populateCheck();
//		populatePayments();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intro_screen, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ViewServer.get(this).removeWindow(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ViewServer.get(this).setFocusedWindow(this);
	}
	
	private void populateCheck() {
		Dish.DISH_PHOTO_RES_ID_MAP = new HashMap<String, Integer>();
		Dish.DISH_PHOTO_RES_ID_MAP.put("Beer", R.drawable.beer);
		Dish.DISH_PHOTO_RES_ID_MAP.put("Burger", R.drawable.burger);
		Dish.DISH_PHOTO_RES_ID_MAP.put("Chips", R.drawable.chips);
		Dish.DISH_PHOTO_RES_ID_MAP.put("Fish", R.drawable.heineken);
		
		Order order = new Order();
		try {
			order.addDish("Beer");
			order.addDish("Burger");
			order.addDish("Chips");
			order.addDish("Chips");
			order.addDish("Fish");
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		order.addOrder(dishName)
		
		//CreateOrder
		Orders.addOrder(order);
		Check check = new Check(0, order);
		Checks.addCheck(check);
	}
	
	private void populateCostumers() {
		Costumers.addCostumer(new Costumer("Tyrion Lannister", R.drawable.f1, R.drawable.e1, 0));
		Costumers.addCostumer(new Costumer("Jon Snow", R.drawable.f2, R.drawable.e2, 0));
		Costumers.addCostumer(new Costumer("Theon Greyjoy", R.drawable.f3, R.drawable.e3, 0));
		Costumers.addCostumer(new Costumer("Khaleesi", R.drawable.f4, R.drawable.e4, 0));
		Costumers.addCostumer(new Costumer("Arya Stark", R.drawable.f5, R.drawable.e5, 0));
	}
	
	private void populatePayments() {
		Check check = null;
		try {
			check = Checks.getCheck(0);

			check.addPayment(check.getDish("Chips"), Costumers.getCostumer("Arya Stark"), 2);
			check.addPayment(check.getDish("Chips"), Costumers.getCostumer("Jon Snow"), 2);

			check.addPayment(check.getDish("Burger"), Costumers.getCostumer("Theon Greyjoy"), 10);
			check.addPayment(check.getDish("Burger"), Costumers.getCostumer("Tyrion Lannister"), 20);

			check.addPayment(check.getDish("Beer"), Costumers.getCostumer("Theon Greyjoy"), 10);

			check.addPayment(check.getDish("Fish"), Costumers.getCostumer("Tyrion Lannister"), 30);
			check.addPayment(check.getDish("Fish"), Costumers.getCostumer("Khaleesi"), 15);
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpdate() {
		CustomersFragment f = (CustomersFragment) getSupportFragmentManager().findFragmentByTag(CustomersFragment.TAG);
		f.updatePayments();
	}
}
