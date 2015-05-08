package com.yg.image.filter.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.flyin.MainActivity;
import com.example.flyin.R;

public class FilterActivity extends Activity
{
	private ImageView imageView;
	FilterPreviewCache filterCache;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		
		imageView = (ImageView)findViewById(R.id.yg_filter_img);
		filterCache = new FilterPreviewCache(MainActivity.bitmap);
	}
	
	public void onClickEffectButton(View view)
	{
		filterCache.applyFilterByTag(view.getTag().toString(), imageView);
	}
}