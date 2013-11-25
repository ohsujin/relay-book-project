package com.example.staggeredgridviewdemo;

import java.util.ArrayList;

import relay.book.intentdemob2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.staggeredgridviewdemo.loader.ImageLoader;
import com.example.staggeredgridviewdemo.views.ScaleImageView;


public class StaggeredAdapter extends ArrayAdapter<String> {

	private ImageLoader mLoader;
	private ArrayList<ImageItem> book_list = new ArrayList<ImageItem>();;
	static int i = 0;
	
	public StaggeredAdapter(Context context, int textViewResourceId,
			String[] objects, ArrayList<ImageItem> arrayList) {
		super(context, textViewResourceId, objects);
		mLoader = new ImageLoader(context);
		this.book_list = arrayList;
	

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.row_staggered_demo,null);

			holder = new ViewHolder();
			holder.imageView = (ScaleImageView) convertView.findViewById(R.id.imageView1);
			holder.Title = (TextView) convertView.findViewById(R.id.title);
			holder.Writer = (TextView) convertView.findViewById(R.id.writer);
			holder.Price = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
			
			
		}

		holder = (ViewHolder) convertView.getTag();

		ImageItem item = book_list.get(position);

		holder.Title.setText(item.getTitle());// 항목을 불러와야됨
		holder.Writer.setText(item.getWriter());
		holder.Price.setText(item.getPrice());

		mLoader.DisplayImage(getItem(position), holder.imageView);

		/*판매중or예약중선택 시작*/
		
		ImageView finger_img1 = (ImageView)convertView.findViewById(R.id.finger_img);
		ImageView finger_img2 = (ImageView)convertView.findViewById(R.id.finger_img2);
		
		if(item.getActive() == 0)
		{
			finger_img2.setVisibility(View.VISIBLE);
		} else if(item.getActive() == 1){
			finger_img1.setVisibility(View.VISIBLE);
		}
		/*끝*/
		
		return convertView;
	}

	static class ViewHolder {
		ScaleImageView imageView;
		TextView Title;
		TextView Writer;
		TextView Price;

	}
}