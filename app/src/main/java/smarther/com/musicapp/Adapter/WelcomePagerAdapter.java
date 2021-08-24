package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;
import smarther.com.musicapp.Activity.WelcomeActivity;
import smarther.com.musicapp.R;

public class WelcomePagerAdapter extends PagerAdapter {
    Context context;
//    String title[],description[];
    LayoutInflater layoutInflater;
    WelcomeActivity welcomeActivity;


    public WelcomePagerAdapter(Context context, WelcomeActivity welcomeActivity) {
        this.context = context;
        this.welcomeActivity = welcomeActivity;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.welcome_item, container, false);
        LinearLayout welcome_1=(LinearLayout)itemView.findViewById(R.id.welcome_1);
        LinearLayout welcome_2=(LinearLayout)itemView.findViewById(R.id.welcome_2);
        LinearLayout welcome_3=(LinearLayout)itemView.findViewById(R.id.welcome_3);
        container.addView(itemView);
         if(position==0)
         {
             welcome_1.setVisibility(View.VISIBLE);
             welcome_2.setVisibility(View.GONE);
             welcome_3.setVisibility(View.GONE);
//             welcomeActivity.move_to_login.setBackgroundColor(context.getResources().getColor(R.color.red));
         }
        else if(position==1)
        {
            welcome_1.setVisibility(View.GONE);
            welcome_2.setVisibility(View.VISIBLE);
            welcome_3.setVisibility(View.GONE);
//            welcomeActivity.move_to_login.setBackgroundColor(context.getResources().getColor(R.color.blue));
        }
        else if(position==2)
        {
            welcome_1.setVisibility(View.GONE);
            welcome_2.setVisibility(View.GONE);
            welcome_3.setVisibility(View.VISIBLE);
//            welcomeActivity.move_to_login.setBackgroundColor(context.getResources().getColor(R.color.WelcomeText));
        }
        //listening to image click
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_SHORT).show();
//            }
//        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
