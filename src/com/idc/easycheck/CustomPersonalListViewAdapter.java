package com.idc.easycheck;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.idc.easycheck.check.Dish;
import com.idc.easycheck.check.Payment;

public class CustomPersonalListViewAdapter extends BaseAdapter {
	Context myContext;

	ArrayList<Payment> mPayments;

	public CustomPersonalListViewAdapter(Context context) {
		super();
		myContext = context;
	}

	public void setCurrentPayments(ArrayList<Payment> payments) {
		mPayments = payments;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPayments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mPayments.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	private class ViewHolder {
		ImageView productThumb;
		TextView productName;
		TextView productPrice;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) myContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.personal_items_list, null);
			holder = new ViewHolder();
			holder.productName = (TextView) convertView
					.findViewById(R.id.productName);
			holder.productPrice = (TextView) convertView
					.findViewById(R.id.productPrice);
			holder.productThumb = (ImageView) convertView
					.findViewById(R.id.productThumb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.productThumb.setImageResource(Dish.DISH_PHOTO_RES_ID_MAP
				.get(mPayments.get(position).dish.dishName));

		holder.productName.setText(mPayments.get(position).dish.dishName);
		holder.productPrice
				.setText(String.valueOf(mPayments.get(position).moneyCount));

		return convertView;
	}

}
