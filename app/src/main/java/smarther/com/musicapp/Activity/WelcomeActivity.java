package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import smarther.com.musicapp.Adapter.WelcomePagerAdapter;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.MusicIntentReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    ViewPager welcome_viewPager;
    WelcomePagerAdapter welcomePagerAdapter;
    LinearLayout skip_background;
    TextView dot_1;
    TextView dot_2;
    TextView dot_3;
    ImageView move_to_theme;
    public MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcome_viewPager = (ViewPager)findViewById(R.id.welcome_viewPager);
        welcomePagerAdapter = new WelcomePagerAdapter(WelcomeActivity.this,WelcomeActivity.this);
        welcome_viewPager.setAdapter(welcomePagerAdapter);
        skip_background=(LinearLayout)findViewById(R.id.skip_background);
        dot_1=(TextView)findViewById(R.id.dot_1);
        dot_2=(TextView)findViewById(R.id.dot_2);
        dot_3=(TextView)findViewById(R.id.dot_3);
        move_to_theme = (ImageView) findViewById(R.id.move_to_theme);
        myReceiver = new MusicIntentReceiver();


        move_to_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WelcomeActivity.this, ThemeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        skip_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WelcomeActivity.this, ThemeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        welcome_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {

                    skip_background.setBackgroundColor(getResources().getColor(R.color.gradient_blue));
                    dot_1.setTextColor(getResources().getColor(R.color.white));
                    dot_2.setTextColor(getResources().getColor(R.color.light_blue));
                    dot_3.setTextColor(getResources().getColor(R.color.light_blue));
                }
                else if(position==1)
                {

                    skip_background.setBackgroundColor(getResources().getColor(R.color.gradient_lightred));
                    dot_1.setTextColor(getResources().getColor(R.color.light_rose));
                    dot_2.setTextColor(getResources().getColor(R.color.white));
                    dot_3.setTextColor(getResources().getColor(R.color.light_rose));
                }
                else if(position==2)
                {

                    skip_background.setBackgroundColor(getResources().getColor(R.color.gradient_lightrose));
                    dot_1.setTextColor(getResources().getColor(R.color.light_rose));
                    dot_2.setTextColor(getResources().getColor(R.color.light_rose));
                    dot_3.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onResume();
    }
    @Override public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }
}
