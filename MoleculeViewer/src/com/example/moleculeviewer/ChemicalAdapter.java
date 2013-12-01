package com.example.moleculeviewer;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChemicalAdapter extends BaseAdapter {
	private ArrayList<Chemical> mList;
	private LayoutInflater mInflater;
	private boolean formulaSubtitle = true;
	
	static class ViewHolder {
		TextView text1;
		TextView text2;
	}

	public ChemicalAdapter(Context c, ArrayList<Chemical> items) {
		mList = items;
		
		mInflater = LayoutInflater.from(c);
	}
	
	public void setFormulaSubtitle(){
		formulaSubtitle = true;
	}
	
	public void setWeightSubtitle(){
		formulaSubtitle = false;
	}
	
	public void updateAdapter(ArrayList<Chemical> new_items){
		mList.clear();
		mList.addAll(new_items);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null) {
			convertView = mInflater.inflate(android.R.layout.simple_list_item_2, null);
			
			holder = new ViewHolder();
			holder.text1 = (TextView)convertView.findViewById(android.R.id.text1);
			holder.text2 = (TextView)convertView.findViewById(android.R.id.text2);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		Chemical molecule = mList.get(position);
		
		holder.text1.setText(molecule.name);
		System.out.println(molecule.name);
		if(formulaSubtitle){
			holder.text2.setText(molecule.formula);
			System.out.println(molecule.formula);
		}
		else{
			holder.text2.setText(molecule.molecular_weight + "u");
			System.out.println(molecule.molecular_weight + "u");
		}
		
		return convertView;
	}

}
