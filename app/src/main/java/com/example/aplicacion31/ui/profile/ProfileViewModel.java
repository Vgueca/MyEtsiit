package com.example.aplicacion31.ui.profile;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<String> userName;
    private final MutableLiveData<Integer> profileImageResource;

    public ProfileViewModel() {
        userName = new MutableLiveData<>();
        profileImageResource = new MutableLiveData<>();
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<Integer> getProfileImageResource() {
        return profileImageResource;
    }
}
