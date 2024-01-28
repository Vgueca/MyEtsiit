package com.example.myetsiit.view.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myetsiit.R;

import java.io.Serializable;

public class NewsItemFragment extends Fragment {

    private static final String ARG_NEWS_ITEM = "news_item";

    private NewsItem newsItem;

    public NewsItemFragment() {
        // Constructor vacío requerido por Fragment.
    }

    public static NewsItemFragment newInstance(NewsItem newsItem) {
        NewsItemFragment fragment = new NewsItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NEWS_ITEM, newsItem);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsItem = getArguments().getParcelable(ARG_NEWS_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.news_item_fragment, container, false);

        if (newsItem != null) {
            // Configurar los elementos de la noticia en el layout
            ImageView imageView = view.findViewById(R.id.imageViewNews);
            TextView textViewTitle = view.findViewById(R.id.textViewTitle);
            TextView textViewCaption = view.findViewById(R.id.textViewCaption);

            imageView.setImageResource(newsItem.getImageResId());
            textViewTitle.setText(newsItem.getTitle());
            textViewCaption.setText(newsItem.getPieDeFoto());
        }

        return view;
    }
}
