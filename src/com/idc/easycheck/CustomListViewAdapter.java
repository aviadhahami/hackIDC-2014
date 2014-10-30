package com.idc.easycheck;

import java.security.InvalidKeyException;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.idc.easycheck.check.Costumer;
import com.idc.easycheck.check.Costumers;

public class CustomListViewAdapter extends ArrayAdapter<String> {

	Context context;
	String[] items;

	public CustomListViewAdapter(Context context, int resourceId, String[] items) {
		super(context, resourceId, items);
		this.context = context;
		this.items = items;
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView imageView;
		TextView costumerName;
		TextView personalBill;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.costumerName = (TextView) convertView
					.findViewById(R.id.costumerName);
			holder.personalBill = (TextView) convertView
					.findViewById(R.id.personalBill);
			holder.imageView = (ImageView) convertView.findViewById(R.id.color);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String bill = null;
		try {
			Costumer costumer = Costumers.getCostumer(items[position]);
//			bill = costumer.getPersonalBill() + " + " + costumer.tipPercent;
			if (costumer.tipPercent != 0) {
				bill = String.format("%.2f + %.2f", costumer.getPersonalBill() ,costumer.personalBill * costumer.tipPercent / 100);
			} else {
				bill = String.format("%.2f", costumer.getPersonalBill());
			}
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			// wtf this shit
			e1.printStackTrace();
		}

		holder.personalBill.setText(bill);
		holder.costumerName.setText(items[position]);

		try {
			holder.imageView.setBackgroundResource(Costumers.getCostumer(
					items[position]).fullResID);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}
}