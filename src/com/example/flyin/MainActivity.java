package com.example.flyin;

import java.io.File;
import java.lang.reflect.Method;

import com.yg.image.filter.ui.FilterActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private static final int REQUEST_GALLERY = 1;
	private static final int REQUEST_CAMERA = 0;
	
	private Animation animation;
	private RelativeLayout top_holder;
	private RelativeLayout bottom_holder;
	private RelativeLayout step_number;
	private Uri imageUri;
	private boolean click_status = true;

	public static Bitmap bitmap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		top_holder = (RelativeLayout) findViewById(R.id.yg_select_picture_top_holder);
		bottom_holder = (RelativeLayout) findViewById(R.id.yg_select_picture_bottom_holder);
		step_number = (RelativeLayout) findViewById(R.id.yg_select_picture_step_number);
	}

	@Override
	protected void onStart() 
	{
		overridePendingTransition(0, 0);
		flyIn();
		super.onStart();
	}
	
	private void flyIn()
	{
		click_status = true;
		
		animation = AnimationUtils.loadAnimation(this, R.anim.holder_top);
		top_holder.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(this, R.anim.holder_bottom);
		bottom_holder.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(this, R.anim.step_number);
		step_number.startAnimation(animation);
	}

	@Override
	protected void onStop() 
	{
		overridePendingTransition(0, 0);
		super.onStop();
	}

	public void startGallery(View view)
	{
		flyOut("displayGallery");
	}

	public void startCamera(View view) 
	{
		flyOut("displayCamera");
	}
	
	private void flyOut(final String methodName)
	{
		if (click_status)
		{
			click_status = false;
			
			animation = AnimationUtils.loadAnimation(this, R.anim.step_number_back);
			step_number.startAnimation(animation);
	
			animation = AnimationUtils.loadAnimation(this, R.anim.holder_top_back);
			top_holder.startAnimation(animation);
	
			animation = AnimationUtils.loadAnimation(this, R.anim.holder_bottom_back);
			bottom_holder.startAnimation(animation);
	
			animation.setAnimationListener(new AnimationListener() 
			{
				@Override
				public void onAnimationStart(Animation arg0) {
				}
	
				@Override
				public void onAnimationRepeat(Animation arg0) {
				}
	
				@Override
				public void onAnimationEnd(Animation arg0) 
				{
					callMethod(methodName);
				}
			});
		}
	}
	
	@SuppressWarnings("unused")
	private void displayGallery()
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) 
		{
			Intent intent = new Intent();
			intent.setType("image/jpeg");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, REQUEST_GALLERY);
		} 
		else 
			Toast.makeText(getApplicationContext(), "Error: your SD-Card is not available.", Toast.LENGTH_SHORT).show();
	}
	
	@SuppressWarnings("unused")
	private void displayCamera() 
	{
		imageUri = getOutputMediaFile();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, REQUEST_CAMERA);
	}
	
	private Uri getOutputMediaFile()
	{
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "Camera Pro");
		values.put(MediaStore.Images.Media.DESCRIPTION, "www.appsroid.org");
		return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == REQUEST_CAMERA)
		{
			try
			{
				if (resultCode == RESULT_OK) 
					displayPhotoActivity();
				else 
					getApplicationContext().getContentResolver().delete(imageUri, null, null);
			} 
			catch (Exception e)
			{
				Toast.makeText(getApplicationContext(), "Image not found.(Camera)", Toast.LENGTH_SHORT).show();
			}
		}
		else if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY)
		{
			try 
			{
				imageUri = data.getData();
				displayPhotoActivity();
			} 
			catch (Exception e) 
			{
				Toast.makeText(getApplicationContext(), "Image not found.(Gallery)", Toast.LENGTH_SHORT).show();
			}
		}
		else if (requestCode == REQUESTCODE_CROP && resultCode == RESULT_OK)
		{
			try
			{
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), IMAGE_URI);

				Intent it = new Intent(MainActivity.this, FilterActivity.class);
				startActivity(it);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private void displayPhotoActivity() 
	{
		startPhotoZoom(imageUri);
	}
	
	private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "IMAGE_URI"));
	private static final int REQUESTCODE_CROP = 2;
	
	private void startPhotoZoom(Uri uri) 
	{  
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/*");  
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪  
        intent.putExtra("crop", "true");  
        // aspectX aspectY 是宽高的比例  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("outputX", 300); 
        intent.putExtra("outputY", 300);  
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, REQUESTCODE_CROP);  
    }

	private void callMethod(String methodName)
	{
		if (methodName.equals("finish")) 
		{
			overridePendingTransition(0, 0);
			finish();
		}
		else 
		{
			try 
			{
				Method method = getClass().getDeclaredMethod(methodName);
				method.invoke(this, new Object[] {});
			}
			catch (Exception e) {}
		}
	}

	@Override
	public void onBackPressed() 
	{
		flyOut("finish");
		super.onBackPressed();
	}
}
