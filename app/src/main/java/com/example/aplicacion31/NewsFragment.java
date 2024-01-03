package com.example.aplicacion31;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aplicacion31.R;
public class NewsFragment extends Fragment {
    private String newsTitle;
    private int newsImageResource;

    public NewsFragment(String newsTitle, int newsImageResource) {
        this.newsTitle = newsTitle;
        this.newsImageResource = newsImageResource;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        TextView titleTextView = view.findViewById(R.id.newsTitle);
        ImageView newsImageView = view.findViewById(R.id.newsImage);

        titleTextView.setText(newsTitle);
        newsImageView.setImageResource(newsImageResource);

        return view;
    }
}
