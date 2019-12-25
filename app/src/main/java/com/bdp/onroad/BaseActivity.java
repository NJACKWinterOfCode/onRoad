package com.bdp.onroad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdp.onroad.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    List<AuthUI.IdpConfig> providers;
    private final static int MyReqCode=2;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.base_layout);
        setContentView(R.layout.activity_base);
        dl = (DrawerLayout) findViewById(R.id.activity_base);
        t = new ActionBarDrawerToggle(this, dl, R.string.app_name, R.string.about_us);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        Intent I = new Intent(BaseActivity.this, UserTypeActivity.class);
                        startActivity(I);
                        finish();
                        break;
                    case R.id.nav_aboutUs:
                        startActivity(new Intent(BaseActivity.this, AboutUs.class));
                        finish();
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(BaseActivity.this, profileActivity.class));                            break;
                    case R.id.nav_rides:
                        startActivity(new Intent(BaseActivity.this, activity_HikeSearch.class));
                        finish();
                        break;
                    case R.id.nav_ridereqs:
                        startActivity(new Intent(BaseActivity.this, UserDriverActivity.class));
                        finish();
                        break;
                    case R.id.nav_logout:
                        AuthUI.getInstance()
                                .signOut(BaseActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        showSignInOptions();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BaseActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(BaseActivity.this, "Logged You Out", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );
      //  showSignInOptions();

    }
    public void showSignInOptions(){

        startActivityForResult(

                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(),MyReqCode
        );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MyReqCode){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(BaseActivity.this, "Hi " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(BaseActivity.this, ""+response.getError(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
