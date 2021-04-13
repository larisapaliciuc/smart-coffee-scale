package com.example.scaleapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PourOverS1Fragment extends Fragment {

    public PourOverS1Fragment() {
        super(R.layout.fragment_pour_over_s1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_pour_over_s1, container, false);
        TextView title = rootView.findViewById(R.id.titleTextView);
        TextView content = rootView.findViewById(R.id.contentTextView);
        TextView subtitle = rootView.findViewById(R.id.subtitleTextView);
        PourOverActivity pourOver = (PourOverActivity) getActivity();
        title.setText(R.string.title1);
        subtitle.setText(R.string.subtitle1);
        assert pourOver != null;
        content.setText(getString(R.string.content1,pourOver.water));
        return rootView;
    }

}