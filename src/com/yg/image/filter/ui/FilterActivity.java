package com.yg.image.filter.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.flyin.SelectImageActivity;
import com.example.flyin.R;

public class FilterActivity extends Activity
{
	private ImageView imageView;
	private FilterPreviewCache filterCache;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		
		imageView = (ImageView)findViewById(R.id.yg_filter_img);
		filterCache = new FilterPreviewCache(SelectImageActivity.bitmap);
		
		setupDialogActionBar();
	}

	public void onClickEffectButton(View view)
	{
		filterCache.applyFilterByTag(view.getTag().toString(), imageView);
	}
	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x00, 0x00, 0x00)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.yg_filter_actionbar);
		
		LinearLayout back = (LinearLayout)findViewById(R.id.yg_filter_actionbar_back);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FilterActivity.this.finish();
			}
		});
	}
}