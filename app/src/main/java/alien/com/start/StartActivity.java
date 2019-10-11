package alien.com.start;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import alien.com.pishongroceries.R;
import alien.com.prefs.PreferenceManager;

public class StartActivity extends AppCompatActivity implements View.OnClickListener
{
    private ViewPager mPager;
    private int[] layout = {R.layout.firstslide,R.layout.slidetwo,R.layout.slidethree,R.layout.fourthslide};
    private MpagerAdapter mpagerAdapter;
    private LinearLayout Dots_layouts;
    private ImageView[] dots;
    private Button next,skip;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }
        //Setting Translucent Action Bar
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_start);
        mPager = findViewById(R.id.viewpager);
        mpagerAdapter = new MpagerAdapter(layout,this);
        mPager.setAdapter(mpagerAdapter);
        Dots_layouts = findViewById(R.id.dotslayout);
        createDots(0);
        next = findViewById(R.id.btnNext);
        skip = findViewById(R.id.btnSkip);
        next.setOnClickListener(this);
        skip.setOnClickListener(this);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if (position == layout.length - 1) {
                    next.setText("Start");
                    skip.setVisibility(View.INVISIBLE);
                } else {
                    next.setText("Next");
                    skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void createDots(int current_position)
    {
        if(Dots_layouts!=null)
        {
            Dots_layouts.removeAllViews();
        }
        dots = new ImageView[layout.length];
        for(int i=0;i<layout.length;i++)
        {
            dots[i]=new ImageView(this);
            if(i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_layouts.addView(dots[i],params);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnNext:
                loadNextSlide();
                break;
            case R.id.btnSkip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }
    }
    private void loadNextSlide()
    {
        int nextSlide = mPager.getCurrentItem()+1;
        if(nextSlide<layout.length)
        {
            mPager.setCurrentItem(nextSlide);
        }
        else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
    private void loadHome()
    {
        startActivity(new Intent(this,SplashActivity.class));
        finish();
    }
}
