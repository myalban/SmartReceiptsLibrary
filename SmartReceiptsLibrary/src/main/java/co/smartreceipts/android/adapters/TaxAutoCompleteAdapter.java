package co.smartreceipts.android.adapters;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.Vector;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import co.smartreceipts.android.model.TaxItem;
import co.smartreceipts.android.persistence.Preferences;

public class TaxAutoCompleteAdapter extends ArrayAdapter<TaxItem> implements TextWatcher, View.OnFocusChangeListener {
	
	private static final BigDecimal VAT_5_5 = new BigDecimal(5.5f);
	private static final BigDecimal VAT_10 = new BigDecimal(10);
	private static final BigDecimal VAT_20 = new BigDecimal(20);
	
	private final LayoutInflater mInflater;
	private final int mListItemId;
	private final TaxItem mDefaultValue;
	private final WeakReference<TextView> mPriceBox;
	private final WeakReference<AutoCompleteTextView> mTaxBox;
	private final Preferences mPreferences;
	
	/**
	 * The internal {@link List<T>} that the {@link ArrayAdapter<T>} uses to track entries caused
	 * some synchronization issues. I used a {@link Vector<T>} to resolve this.
	 */
	private final Vector<TaxItem> mData;
	
	public TaxAutoCompleteAdapter(Context context, TextView priceBox, AutoCompleteTextView taxBox) {
		this(context, priceBox, taxBox, null, 0);
	}
	
	public TaxAutoCompleteAdapter(Context context, TextView priceBox, AutoCompleteTextView taxBox, Preferences preferences, float defaultValue) {
		super(context, android.R.layout.two_line_list_item);
		mInflater = LayoutInflater.from(context);
		mData = new Vector<TaxItem>();
		mListItemId = android.R.layout.two_line_list_item;
		mDefaultValue = new TaxItem(defaultValue, preferences);
		mPriceBox = new WeakReference<TextView>(priceBox);
		mTaxBox = new WeakReference<AutoCompleteTextView>(taxBox);
		mPreferences = preferences;
		priceBox.addTextChangedListener(this);
		taxBox.setOnFocusChangeListener(this);
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public TaxItem getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean isEmpty() {
		return mData.isEmpty();
	}

	private static class MyViewHolder {
		public TextView top;
		public TextView bottom;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView priceBox = mPriceBox.get();
		if (priceBox == null || TextUtils.isEmpty(priceBox.getText())) {
			return null;
		}
		
		final MyViewHolder holder;
		if (convertView == null) {
			holder = new MyViewHolder();
			convertView = mInflater.inflate(mListItemId, parent, false);
			holder.top = (TextView) convertView.findViewById(android.R.id.text1);
			holder.bottom = (TextView) convertView.findViewById(android.R.id.text2);
			convertView.setTag(holder);
		}
		else {
			holder = (MyViewHolder) convertView.getTag();
		}
		TaxItem item = getItem(position);
		item.setPrice(priceBox.getText().toString());
		holder.top.setText(item.toString());
		holder.bottom.setText(item.getPercentAsString());
		return convertView;
	}

	@Override
	public void afterTextChanged(Editable editable) {
		// Method stub
	}

	@Override
	public void beforeTextChanged(CharSequence text, int start, int before, int count) {
		// Method stub
		
	}

	@Override
	public synchronized void onTextChanged(CharSequence text, int start, int before, int count) {
		if (text.length() > 0) {
			if (this.isEmpty()) { // If our adapter is empty, let's fill it up
				if (this.hasDefaultValue()) {
					mData.add(mDefaultValue);
				}
				mData.add(new TaxItem(VAT_5_5, mPreferences));
				mData.add(new TaxItem(VAT_10, mPreferences));
				mData.add(new TaxItem(VAT_20, mPreferences));
			}
			TextView taxBox = mTaxBox.get();
			if (this.hasDefaultValue() && taxBox != null) {
				mDefaultValue.setPrice(text.toString());
				taxBox.setText(mDefaultValue.toString());
			}
		}
		else {
			TextView taxBox = mTaxBox.get();
			if (this.hasDefaultValue() && taxBox != null) {
				mDefaultValue.setPrice("0");
				taxBox.setText(mDefaultValue.toString());
			}
			mData.clear();
		}
	}

	public boolean hasDefaultValue() {
		return BigDecimal.ZERO.compareTo(mDefaultValue.getPercent()) < 0;
	}

	@Override
	public synchronized void onFocusChange(View view, boolean hasFocus) {
		AutoCompleteTextView taxBox = mTaxBox.get();
		if (taxBox != null) {
			try {
				taxBox.showDropDown();
			}
			catch (Exception e) {
				// Ignore
			}
		}
	}

}
