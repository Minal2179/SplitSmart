package com.example.minalshettigar.splashscreen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.Manifest;

import com.example.minalshettigar.splashscreen.helper.PermissionUtils;

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    // Constants
    private static final int GALLERY_FRAGMENT_NUM = 0;
    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int MANUAL_FRAGMENT_NUM = 2;
    private static final int CAMERA_REQUEST_CODE = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        Button btnLaunchCamera = (Button) view.findViewById(R.id.btnLaunchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((AddExpenses)getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                    // TODO: CHECK CAMERA PERMISSIONS

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
                else {
                    // need to ask for camera permission again
                }
            }
        });

        return view;
    }

    private boolean isRootTask(){
        if(((AddExpenses)getActivity()).getTask() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            // TODO navigate to next screen for adding expense
        }
    }

    private void requestCameraPermission() {
        Log.d(TAG, "Camera permission not granted, request permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(getActivity(), permissions, CAMERA_REQUEST_CODE);
            return;
        }
    }
}
