package com.example.myetsiit.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class NewsPagerAdapter extends FragmentPagerAdapter {

    private List<NewsItem> newsList;

    public NewsPagerAdapter(FragmentManager fm, List<NewsItem> newsList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Devuelve un fragmento específico para cada noticia
        // Puedes pasar la información de la noticia al fragmento
        return NewsItemFragment.newInstance(newsList.get(position));
    }

    @Override
    public int getCount() {
        // Devuelve la cantidad total de noticias
        return newsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Devuelve el título de cada página (noticia)
        //return newsList.get(position).getTitle();
        return "";
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // Forzar la recarga del fragmento al llamar a notifyDataSetChanged()
        return POSITION_NONE;
    }
}
