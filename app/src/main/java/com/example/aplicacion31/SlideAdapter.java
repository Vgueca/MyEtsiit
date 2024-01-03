package com.example.aplicacion31;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SlideAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    public SlideAdapter(Context context){
        this.context = context;
    }

    // Array of your slide views.
    // Replace with your own views.
    private int[] layouts = {
            R.layout.slide1,
            R.layout.slide2,
            R.layout.slide3
    };

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layouts[position], container, false);

        // Si es la última diapositiva, configura el botón
        if (position == layouts.length - 1) {
            Button button = view.findViewById(R.id.startButton);
            button.setOnClickListener(v -> {
                Intent mainIntent = new Intent(context, MainActivity.class);
                context.startActivity(mainIntent);
                ((Activity)context).finish();
            });
        }

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
