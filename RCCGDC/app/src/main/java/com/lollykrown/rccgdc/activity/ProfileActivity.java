package com.lollykrown.rccgdc.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.model.User;
import com.lollykrown.rccgdc.utils.ChangePhotoDialog;
import com.lollykrown.rccgdc.utils.FilePaths;
import com.lollykrown.rccgdc.utils.GlideCircleTransformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.text.TextUtils.isEmpty;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    //firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;

    private static final int REQUEST_CODE = 1234;
    private static final double MB_THRESHHOLD = 5.0;
    private static final double MB = 1000000.0;

    ImageView userProfileImageView;
    TextView user_email, userNameTextView, c_no;
    String userEmail, uid, userName, no, pp_url;

    private boolean mStoragePermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        userProfileImageView = (ImageView) findViewById(R.id.profile_p);
        userNameTextView = (TextView) findViewById(R.id.username);
        Button edit = (Button) findViewById(R.id.share_button);
        user_email = (TextView) findViewById(R.id.emailAdd);
        c_no = (TextView) findViewById(R.id.contact_no);

        if (savedInstanceState != null){
            userNameTextView.setText(savedInstanceState.getString("username"));
            user_email.setText(savedInstanceState.getString("useremail"));
            c_no.setText(savedInstanceState.getString("userno"));
            Glide.with(ProfileActivity.this)
                    .load(Uri.parse(savedInstanceState.getString("ppurl"))) // add your image url
                    .crossFade()
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.loading_img)
                    .transform(new GlideCircleTransformation(ProfileActivity.this)) // applying the image transformer
                    .into(userProfileImageView);
        }

        setupFirebaseAuth();
        getUserAccountData();

        Toolbar toolbar = findViewById(R.id.ev_toolbar);
        toolbar.setTitle("  Profile");
        toolbar.setLogo(R.drawable.rccg);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer " + "@" + userName +
                        ", " + uid);
                Intent chooser = Intent.createChooser(shareIntent, "Share via");
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        });
    }

    private void getUserAccountData () {

        FirebaseAuth mFirebaseAuth;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.getCurrentUser().getUid();

        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference mDbr = fdb.getReference();

        Query query1 = mDbr.child(getString(R.string.dbnode_users))
                .orderByKey()
                .equalTo(mFirebaseAuth.getCurrentUser().getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: (QUERY METHOD 1) found user: "
                            + singleSnapshot.getValue(User.class).toString());
                    User user = singleSnapshot.getValue(User.class);
                    userName = user.getUsername();
                    userEmail = user.getUserEmail();
                    no = user.getMobileNo();
                    pp_url = user.getPpUurl();


                    if (!isEmpty(userEmail)){
                        user_email.setText(userEmail);
                    }

                    if (!isEmpty(no)){
                        c_no.setText(no);
                    }

                    if (!isEmpty(userName)){
                        userNameTextView.setText(userName);
                    }

                    if (!isEmpty(pp_url)) {
                        Glide.with(ProfileActivity.this)
                                .load(Uri.parse(pp_url)) // add your image url
                                .crossFade()
                                .placeholder(R.drawable.loading_img)
                                .error(R.drawable.loading_img)
                                .transform(new GlideCircleTransformation(ProfileActivity.this)) // applying the image transformer
                                .into(userProfileImageView);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.e(TAG, "onSaveInstanceState()" + userName + "," + userEmail + "," + pp_url);

        outState.putString("username", userName);
        outState.putString("useremail", userEmail);
        outState.putString("userno", no);
        outState.putString("ppurl", pp_url);
    }
    
    /**
     * Generalized method for asking permission. Can pass any array of permissions
     */
    public void verifyStoragePermissions () {
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED) {
            mStoragePermissions = true;
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this,
                    permissions,
                    REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){

        Log.d(TAG, "onRequestPermissionsResult: requestCode: " + requestCode);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: User has allowed permission to access: " + permissions[0]);

                }
                break;
        }
    }


    @Override
    public void onStart () {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty (String string){
        return string.equals("");
    }

    @Override
    protected void onResume () {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }
    
    private void setupFirebaseAuth () {
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(ProfileActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                // ...
            }
        };


    }

}