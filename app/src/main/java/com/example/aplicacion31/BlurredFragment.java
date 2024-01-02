package com.example.aplicacion31;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.Button;
import android.graphics.Color;
import android.graphics.PorterDuff;



public class BlurredFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_blurred, container, false);

        ImageButton buttonX = (ImageButton) view.findViewById(R.id.buttonX);
        buttonX.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        buttonX.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onFragmentInteraction();
            }
        });

        return view;
    }
}

