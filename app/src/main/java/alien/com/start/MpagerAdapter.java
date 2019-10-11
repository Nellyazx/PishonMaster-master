package alien.com.start;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MpagerAdapter extends PagerAdapter
{
    private int[] layout;
    private LayoutInflater layoutInflater;
    private Context context;
    public MpagerAdapter(int[]layouts,Context context)
    {
        this.context = context;
        this.layout = layouts;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return layout.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }
    public Object instantiateItem(ViewGroup container,int position)
    {
        View view = layoutInflater.inflate(layout[position],container,false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
