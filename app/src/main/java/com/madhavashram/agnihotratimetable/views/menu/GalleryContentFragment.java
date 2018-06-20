package com.madhavashram.agnihotratimetable.views.menu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.madhavashram.agnihotratimetable.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryContentFragment extends Fragment {

    public static final String IMAGE_RESID = "image_resid";
    public static final  String IMAGE_NAME = "image_name";

    private int imageResId;
    private String imageName;


    public GalleryContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        imageResId = bundle.getInt(IMAGE_RESID);
        imageName = bundle.getString(IMAGE_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery_content, container, false);

        ImageView imageView = rootView.findViewById(R.id.galleryImageView);
        TextView txtImageName = rootView.findViewById(R.id.lblGalleryContent);

        imageView.setImageResource(imageResId);
        txtImageName.setText(imageName);

        return rootView;
    }

}
