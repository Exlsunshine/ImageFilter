package com.yg.image.filter.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.yg.image.filter.filters.BlockPrintFilter;
import com.yg.image.filter.filters.ColorQuantizeFilter;
import com.yg.image.filter.filters.ComicFilter;
import com.yg.image.filter.filters.EdgeFilter;
import com.yg.image.filter.filters.IImageFilter;
import com.yg.image.filter.filters.MistFilter;
import com.yg.image.filter.filters.OldPhotoFilter;
import com.yg.image.filter.filters.RaiseFrameFilter;
import com.yg.image.filter.filters.SaturationModifyFilter;
import com.yg.image.filter.filters.SepiaFilter;
import com.yg.image.filter.filters.SoftGlowFilter;
import com.yg.image.filter.filters.VintageFilter;
import com.yg.image.filter.filters.ZoomBlurFilter;

public class FilterPreviewCache
{
	/**
	 * key   represents the filter name<br>
	 * value represents the path of the image which has been applied with the filter.
	 */
	private HashMap<String, String> filterCache;
	private List<IImageFilter> avaliableFilters;
	private Bitmap target;
	
	/**
	 * Filter preview class.
	 * @param target the bitmap which you want to apply filter to.
	 */
	public FilterPreviewCache(Bitmap target)
	{
		this.target = target;
		avaliableFilters = new ArrayList<IImageFilter>();
		filterCache = new HashMap<String, String>();
		loadFilters();
	}
	
	/**
	 * Apply specific filter, and show the filter result on imageview.
	 * @param tag the filter's tag.
	 * @param imageview the imageview which will show the filter result.
	 */
	synchronized public void applyFilterByTag(String tag, ImageView imageview)
	{
		if (filterCache.containsKey(tag))
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.inInputShareable = true;
			
			imageview.setImageBitmap(BitmapFactory.decodeFile(filterCache.get(tag), options));
		}
		else
		{
			IImageFilter filter = avaliableFilters.get(Integer.parseInt(tag) - 1);
			new ProcessImageTask(filter, target, tag, filterCache, imageview).execute();
		}
	}
	
	/**
	 * Get the path of the bitmap file which has been applied by the specific filter.
	 * @param tag filter's tag
	 * @return path of the bitmap file<br>
	 * null indicates we can not find the file with the given tag.
	 */
	public String getFilterBmpPath(String tag)
	{
		return filterCache.get(tag);
	}
	
	private void loadFilters() 
	{
		avaliableFilters.add(new ZoomBlurFilter(30));
		avaliableFilters.add(new SoftGlowFilter(10, 0.1f, 0.1f));
		avaliableFilters.add(new RaiseFrameFilter(20));
		avaliableFilters.add(new ComicFilter());
		avaliableFilters.add(new EdgeFilter());
		avaliableFilters.add(new BlockPrintFilter());
		avaliableFilters.add(new MistFilter());
		avaliableFilters.add(new SaturationModifyFilter());
		avaliableFilters.add(new ColorQuantizeFilter());
		avaliableFilters.add(new VintageFilter());
		avaliableFilters.add(new OldPhotoFilter());
		avaliableFilters.add(new SepiaFilter());
	}
}
