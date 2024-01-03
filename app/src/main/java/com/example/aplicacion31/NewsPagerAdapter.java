package com.example.aplicacion31;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.aplicacion31.NewsFragment;

import java.util.List;

public class NewsPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> newsTitles;
    private List<Integer> newsImageResources;

    public NewsPagerAdapter(FragmentManager fm, List<String> newsTitles, List<Integer> newsImageResources) {
        super(fm);
        this.newsTitles = newsTitles;
        this.newsImageResources = newsImageResources;
    }

    @Override
    public Fragment getItem(int position) {
        return new NewsFragment(newsTitles.get(position), newsImageResources.get(position));
    }

    @Override
    public int getCount() {
        return newsTitles.size();
    }
}
