package com.example.minalshettigar.splashscreen.helper;




import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.minalshettigar.splashscreen.*;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        saveRegistrationToDevice(refreshedToken);
        String email = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).getString("email", null);
        if(email!=null){
            UtilityMethods.sendRegistrationToServer(MyFirebaseInstanceIDService.this, email, refreshedToken);
        }
    }

    private void saveRegistrationToDevice(String token) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.GCM_TOKEN), token);
        editor.commit();
    }
    // [END refresh_token]
}
