package com.idc.easycheck;

import java.security.InvalidKeyException;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.idc.easycheck.check.Checks;
import com.idc.easycheck.check.Costumer;
import com.idc.easycheck.check.Costumers;

public class CustomersFragment extends Fragment {

	public static final String TAG = "CustomersFragment";
	ListView listView;
	LinearLayout mRoot;
	// ProgressBar billProgressBar;
	int billProgress = 0;
	Dialog mainCustomerDialog;
	CustomPersonalListViewAdapter myAdapt;
	
	public void updatePayments() {
		((CustomListViewAdapter) ((ListView) mRoot.findViewById(R.id.customersListView)).getAdapter()).notifyDataSetChanged();
		updateFinalBills();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Created Dialog and adapter for later use per user click
		mainCustomerDialog = new Dialog(getActivity());
		myAdapt = new CustomPersonalListViewAdapter(getActivity());
		mainCustomerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mRoot = (LinearLayout) inflater.inflate(
				R.layout.customers_fragment_layout, container, false);
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final ListView list = (ListView) mRoot.findViewById(R.id.customersListView);
		
		// dummy string array for the names
		String[] dummyNames = new String[Costumers.getAmount()];
		ArrayList<Costumer> dummyArray1 = Costumers.costumers;
		for (int i = 0; i < dummyNames.length; i++) {
			dummyNames[i] = dummyArray1.get(i).getCostumerName();
		}

		// working on custom ArrayAdapter
		CustomListViewAdapter adapter2 = new CustomListViewAdapter(
				getActivity(), R.layout.list_item, dummyNames);

		// on click listener for the list
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				if (mainCustomerDialog == null) {
					mainCustomerDialog = new Dialog(getActivity());
				}
				if (myAdapt == null) {
					myAdapt = new CustomPersonalListViewAdapter(getActivity());
				}
				final String customerName = ((TextView) v
						.findViewById(R.id.costumerName)).getText().toString();
				try {
					myAdapt.setCurrentPayments(Checks.getCheck(0).ordersByCostumer
							.get(Costumers.getCostumer(customerName)));
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mainCustomerDialog.setContentView(R.layout.customer_dialog);
				ViewGroup contentView = (ViewGroup) mainCustomerDialog
						.findViewById(R.id.customerDialogLayout);

				ListView list = (ListView) contentView
						.findViewById(R.id.listViewPopup);
				
				Window window = mainCustomerDialog.getWindow();
				WindowManager.LayoutParams windowParams = window.getAttributes();
				View anchor = contentView.findViewById(R.id.anchor);
				windowParams.x = 250;
				window.setAttributes(windowParams);
				window.setBackgroundDrawable(new ColorDrawable(0));
				
//				anchor.setX(v.getX());
				anchor.setY(v.getY() + 20);
				
				
				//SeekBar
				SeekBar seekBar = (SeekBar) contentView.findViewById(R.id.customerTipSeekBar);
				final TextView tvTip = (TextView) contentView.findViewById(R.id.costumerCurrentTip);
				final TextView tvTotal = (TextView) contentView.findViewById(R.id.customerSumOnTheGo);
				
				try {
//					tvTip.setText(String.valueOf( cleanPayment + cleanPayment/10 ));
					tvTip.setText(String.format(" %d%% ", Costumers.getCostumer(customerName).tipPercent));
					seekBar.setProgress(Costumers.getCostumer(customerName).tipPercent);
					
					double cleanPayment = Costumers.getCostumer(customerName).personalBill;
					if (seekBar.getProgress() != 0) {
						tvTotal.setText(String.format(" %.2f ",cleanPayment + cleanPayment*seekBar.getProgress()/100 ));
					} else {
						tvTotal.setText(String.format(" %.2f ",cleanPayment));
					}
					
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						tvTip.setText(String.format(" %d%% ", progress));
						try {
							Costumers.getCostumer(customerName).tipPercent = progress;
							double cleanPayment = Costumers.getCostumer(customerName).personalBill;
							if (progress != 0) {
								tvTotal.setText(String.format(" %.2f ",cleanPayment + cleanPayment*progress/100 ));
							} else {
								tvTotal.setText(String.format(" %.2f ",cleanPayment));
							}
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				mainCustomerDialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						updatePayments();
					}
				});
				
				mainCustomerDialog.show();
				
				list.setAdapter(myAdapt);

			}
		});
		list.setAdapter(adapter2);

		// handling the check sum
		updateFinalBills();
	}

	/*
	 * public void updateProgressBar(View v) { billProgressBar = (ProgressBar)
	 * mRoot.findViewById(R.id.billProgressBar); double totalSum = 0; double
	 * coveredSum = 0; try { totalSum =
	 * Checks.getCheck(0).getCurrentWantedSum(); coveredSum =
	 * Checks.getCheck(0).getCurrentPaidSum(); } catch (InvalidKeyException e) {
	 * // TODO Auto-generated catch block // some comment e.printStackTrace(); }
	 * coveredSum = 40.23; int currentSum = (int) ((coveredSum / totalSum) *
	 * 100); billProgressBar.setProgress(currentSum); }
	 */

	public void updateFinalBills() {
		TextView total = (TextView) mRoot.findViewById(R.id.totalTextView);
		TextView covered = (TextView) mRoot.findViewById(R.id.coveredTextView);
		TextView balance = (TextView) mRoot.findViewById(R.id.balanceTextView);
		ProgressBar billProgressBar = (ProgressBar) mRoot
				.findViewById(R.id.billProgressBar); 

		double totalSum = 0.0;
		double coveredSum = 0.0;
		try {
			totalSum = Checks.getCheck(0).getCurrentWantedSum();
			coveredSum = Checks.getCheck(0).getCurrentPaidSum();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// update the prices etc.
		total.setText(Double.toString(totalSum));
		covered.setText(Double.toString(coveredSum));

		String balanceStr = Double.toString(totalSum - coveredSum);
		
		balance.setText(balanceStr.length() < 6 ? balanceStr : balanceStr
				.substring(0, 6));
		billProgressBar.setProgress((int) (totalSum - coveredSum));
	}

}
