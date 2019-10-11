package alien.com.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import alien.com.pishongroceries.R;


public class GlideClient {
    public static  void downloadImage(Context c, String imageUrl, ImageView img)
    {
        if(imageUrl != null && imageUrl.length()>0)
        {
            Glide.with(c)
                    .load(imageUrl) .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.baseline_sync_black_48dp)
                    .into(img);

        }else {
            Glide.with(c)
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_error_outline_black_48dp)
                    .into(img);
        }
    }
}
