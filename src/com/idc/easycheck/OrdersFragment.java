package com.idc.easycheck;

import java.security.InvalidKeyException;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idc.easycheck.check.Check;
import com.idc.easycheck.check.Checks;
import com.idc.easycheck.check.Costumer;
import com.idc.easycheck.check.Costumers;
import com.idc.easycheck.check.Dish;
import com.idc.easycheck.check.Payment;
import com.idc.easycheck.util.ExtendedImageView;

public class OrdersFragment extends Fragment {

	public static final String TAG = "OrdersFragment";
	public ArrayList<ExtendedImageView> extendedImageViews;

	LinearLayout mRoot;
	LinearLayout mOrdersListContainer;
	
	private OrderUpdateCallback mOrderUpdateListener;

	public static OrdersFragment newInstance() {
		OrdersFragment ordersFragment = new OrdersFragment();
		return ordersFragment;
	}
	
	public void setOrderUpdateListener(OrderUpdateCallback listener) {
		mOrderUpdateListener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRoot = (LinearLayout) inflater.inflate(
				R.layout.orders_fragment_layout, container, false);
		mOrdersListContainer = (LinearLayout) mRoot
				.findViewById(R.id.ordersListContainer);

		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		populateOrders();
	}

	private void populateOrders() {
		try {
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getApplicationContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);

			for (Dish dish : Checks.getCheck(0).ordersByDish.keySet()) {
				RelativeLayout orderLayout = (RelativeLayout) inflater.inflate(R.layout.order_layout, null);
				((ImageView) orderLayout.findViewById(R.id.ivDishPhoto)).setImageResource(Dish.DISH_PHOTO_RES_ID_MAP.get(dish.dishName));
				((TextView) orderLayout.findViewById(R.id.tvFoodName)).setText(dish.amount + "X " + dish.dishName + ", " + dish.price + " each");
				((TextView) orderLayout.findViewById(R.id.tvFoodPrice)).setText("Total: " + (dish.price * dish.amount) + " ILS");

				LinearLayout consumersContainerLayout = (LinearLayout) orderLayout.findViewById(R.id.consumersContainer);

				for (Payment payment : Checks.getCheck(0).ordersByDish.get(dish)) {
					LinearLayout consumerOfDishLayout = (LinearLayout) inflater.inflate(R.layout.consumer_of_dish_layout, null); 
					((ImageView) consumerOfDishLayout.findViewById(R.id.ivConsumerColour)).setImageResource(payment.costumer.fullResID);
					String paymentString = Float.toString(payment.moneyCount);
					paymentString = paymentString.length() < 4 ? paymentString : paymentString.substring(0, 4);
					((TextView) consumerOfDishLayout.findViewById(R.id.tvConsumerPayment)).setText(paymentString + " ILS");
					consumersContainerLayout.addView(consumerOfDishLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				}

				View container = orderLayout.findViewById(R.id.consumersOfDishesLayoutContainer);
				ImageView changePaymentsImage = (ImageView) container.findViewById(R.id.changePayments);
				changePaymentsImage.setClickable(true);
				changePaymentsImage.setOnClickListener(changePaymentsClickListener);
				changePaymentsImage.setTag(dish);

				((ProgressBar) orderLayout.findViewById(R.id.pbPaid)).setProgress((int)((dish.currentPaidCount  / dish.price / dish.amount)*100));
				ImageView ivCheck = (ImageView) orderLayout.findViewById(R.id.ivCheck); 
				if (dish.currentPaidCount == dish.price * dish.amount) {
					ivCheck.setVisibility(View.VISIBLE);
				} else {
					ivCheck.setVisibility(View.GONE);
				}
				
				mOrdersListContainer.addView(orderLayout, new ViewGroup.LayoutParams(1000, 1100));
			}

		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private ImageView createImageView(Context context, LinearLayout layout, int resource) {
		ImageView imageView = new ImageView(context);

		imageView.setImageResource(resource);
		imageView.setClickable(true);
		imageView.setOnClickListener(imageClickListener);

		return imageView;
	}

	private ExtendedImageView createExtendedImageView(Context context, LinearLayout layout, Costumer costumer, boolean isFull) {
		ImageView fullImageView = createImageView(context, layout, costumer.fullResID);
		ImageView emptyImageView = createImageView(context, layout, costumer.emptyResID);

		// Do not touch without the approval of John :(
		FrameLayout container = new FrameLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 15, 20, 15);
		container.setLayoutParams(params);
		// End of "Do not touch"

		container.addView(fullImageView);
		container.addView(emptyImageView);

		if (isFull) {
			fullImageView.setVisibility(View.VISIBLE);
			emptyImageView.setVisibility(View.GONE);
		} else {
			fullImageView.setVisibility(View.GONE);
			emptyImageView.setVisibility(View.VISIBLE);
		}

		layout.addView(container);

		return new ExtendedImageView(costumer, fullImageView, emptyImageView, isFull);
	}

	private OnClickListener changePaymentsClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Context context = (Context) getActivity();
			final Dialog dialog = new Dialog(context);

			Dish dish = (Dish) v.getTag();
			Check check = null;
			try {
				check = Checks.getCheck(0);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.costumer_payment_dialog_layout, null);

			LinearLayout imagesLayout = (LinearLayout) layout.findViewById(R.id.imagesLayout);
			TextView tvTitle = (TextView) layout.findViewById(R.id.tvPaymentTitle);
			tvTitle.setText(String.format("Paying for %dx %s:", dish.amount, dish.dishName));

			extendedImageViews = new ArrayList<ExtendedImageView>();
			for (Costumer costumer: Costumers.costumers) {
				boolean isFull = false;
				for (Payment payment : check.ordersByDish.get(dish)) {
					if (payment.costumer.equals(costumer)) {
						isFull = true;
					}
				}
				extendedImageViews.add(createExtendedImageView(context, imagesLayout, costumer, isFull));
			}

			TextView tvAccept = (TextView) layout.findViewById(R.id.tvAccept);
			tvAccept.setTag(v.getTag());
			tvAccept.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Dish dish = (Dish) v.getTag();
					Check check = null;
					try {
						check = Checks.getCheck(0);
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					int amountPaying = 0;
					for (ExtendedImageView extended : extendedImageViews) {
						if (extended.isFull) {
							amountPaying++;
						}
					}
					check.clearPaymentsForDisk(dish);
					
					for (ExtendedImageView extended : extendedImageViews) {
						if (extended.isFull) {
							float amount = (float) ((dish.price * dish.amount)) / amountPaying;
							check.addPayment(dish, extended.costumer, amount);
						}
					}

					mOrdersListContainer.removeAllViews();
					populateOrders();
					mOrderUpdateListener.onUpdate();
					dialog.dismiss();
				}
			});

			LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.1f);	        
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(layout, param);
			dialog.show();
		}
	};

	private OnClickListener imageClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final ImageView imageView = (ImageView) v;

			for (ExtendedImageView extended : extendedImageViews) {
				if (extended.isImageContained(imageView)) {

					ImageView oldImage = extended.getCurrent();
					oldImage.setVisibility(View.GONE);

					extended.switchState();

					ImageView newImage = extended.getCurrent();
					newImage.setVisibility(View.VISIBLE);

					return;
				}
			}
		}
	};

}
