package com.example.aplicacion31.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aplicacion31.R;
import com.example.aplicacion31.ui.profile.ProfileViewModel;

public class ProfileFragment extends Fragment {
    private ProfileViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        TextView userNameTextView = view.findViewById(R.id.profileName);
        ImageView profileImageView = view.findViewById(R.id.profileImage);

        // Aquí puedes observar los datos del ViewModel y actualizar las vistas cuando cambien
        viewModel.getUserName().observe(getViewLifecycleOwner(), userName -> userNameTextView.setText(userName));
        viewModel.getProfileImageResource().observe(getViewLifecycleOwner(), profileImageResource -> profileImageView.setImageResource(profileImageResource));

        return view;
    }
}
