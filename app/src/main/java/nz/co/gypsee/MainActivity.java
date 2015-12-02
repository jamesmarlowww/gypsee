package nz.co.gypsee;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Fragment mainFragment;
    private CallbackManager callbackManager;


    private Button fbbutton;
    private boolean is18;

    // Creating Facebook CallbackManager Value
    public static CallbackManager callbackmanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        if (checkLogin()) {
           Toast.makeText(getApplicationContext(), "asdlfas", Toast.LENGTH_LONG).show();
        }

        fbbutton = (LoginButton) findViewById(R.id.login_button);
        fbbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call private method
                onFblogin();
            }
        });


    }



    // Private method to handle Facebook login and callback
    private void onFblogin() {


        // Set permissions

        List<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("user_friends");


        LoginManager.getInstance().logInWithReadPermissions(this, permissions);


        callbackmanager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(getApplicationContext(), "asdlfas", Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onCancel() {
                        Log.d("on Cancel", "On cancel");
                        Toast.makeText(getApplicationContext(), "hmm something went wrong. Check your internet", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("on error ", error.toString());

                        Toast.makeText(getApplicationContext(), "ERRRRROR: "+error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }


    public boolean checkLogin() {
        boolean loggedIn = false;
        AccessToken t = AccessToken.getCurrentAccessToken();
        if (AccessToken.getCurrentAccessToken() != null)
            loggedIn = true;

        return AccessToken.getCurrentAccessToken() != null;
    }


    public void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("name not found", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        }
    }

}
