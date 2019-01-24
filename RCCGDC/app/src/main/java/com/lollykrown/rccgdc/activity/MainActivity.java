package com.lollykrown.rccgdc.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lollykrown.rccgdc.NewEventNotification;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.fragment.AudiosFragment;
import com.lollykrown.rccgdc.fragment.BibleFragment;
import com.lollykrown.rccgdc.fragment.HomeFragment;
import com.lollykrown.rccgdc.fragment.VideosFragment;
import com.lollykrown.rccgdc.fragment.NotificationsFragment;
import com.lollykrown.rccgdc.fragment.PhotosFragment;
import com.lollykrown.rccgdc.model.Notifications;
import com.lollykrown.rccgdc.model.NotificationsDatabase;
import com.lollykrown.rccgdc.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 101;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView txtName, txtDesc;
    private Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_AUDIOS = "audios";
    private static final String TAG_VIDEOS = "videos";
    private static final String TAG_BIBLE = "bible";
    private static final String TAG_NOTIFICATIONS = "notifications";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles for selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private Handler handler;

    String title, date, url;

    private FragmentManager man;
    String tit, dat, ur;

    NotificationsDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get intent from the notification tray when app is in the background
        Intent i = getIntent();
        if (i.getExtras() == null) {
            String title = getIntent().getStringExtra("title");
            String date = getIntent().getStringExtra("date");
            String time = getIntent().getStringExtra("time");
            String url = getIntent().getStringExtra("url");

            if (!title.isEmpty()  || date != null || time != null || url != null) {
                Notifications n = new Notifications(title, date, time, url, 0);

                db = NotificationsDatabase.getInMemoryDatabase(getApplicationContext());
                db.notificationsDao().addNotifications(n);
            }
        }

        man = getSupportFragmentManager();

        mFirebaseAuth = FirebaseAuth.getInstance();

        coordinatorLayout = findViewById(R.id.cordinator);

        txtName = findViewById(R.id.rccgdc);
        txtDesc = findViewById(R.id.descrip);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setLogo(R.drawable.rccg);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        mHandler = new Handler();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // initializing navigation menu
        setUpNavigationView();

        //loadBibleVerse();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // photos
                PhotosFragment photosFragment = new PhotosFragment();
                return photosFragment;
            case 2:
                // audios
                AudiosFragment audiosFragment = new AudiosFragment();
                return audiosFragment;
            case 3:
                // movies fragment
                VideosFragment videosFragment = new VideosFragment();
                return videosFragment;
            case 4:
                // movies fragment
                BibleFragment bibleFragment = new BibleFragment();
                return bibleFragment;
            case 5:
                // notifications fragment
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;

            default:
                return new HomeFragment();
        }
    }


    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    //Set up the nav view here
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        if (navItemIndex != 0 || !CURRENT_TAG.equals(TAG_HOME)) {
                            loadHomeFragment();
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_HOME;
                        }
                        break;
                    case R.id.nav_pictures:
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();

                        if (user != null) {
                            Snackbar.make(coordinatorLayout, "Signed in as " + user.getDisplayName(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_PHOTOS;
                        } else {
                            buildDialog();
                        }
                        break;
                    case R.id.nav_audios:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_AUDIOS;
                        break;
                    case R.id.nav_videos:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_VIDEOS;
                        break;
                    case R.id.nav_bible:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_BIBLE;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_about_us:
                        // launch about us intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch privacy policy intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // this will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0 || !CURRENT_TAG.equals(TAG_HOME)) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
            FirebaseUser user = mFirebaseAuth.getCurrentUser();
            if (user != null) {
                menu.findItem(R.id.sign_in).setVisible(false);
                menu.findItem(R.id.sign_out).setVisible(true);
            } else {
                menu.findItem(R.id.sign_in).setVisible(true);
                menu.findItem(R.id.sign_out).setVisible(false);
            }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sign_in) {
            createSignInIntent();
            return true;
        }

        if (id == R.id.sign_out) {
            signOut();
            return true;
        }

        if (id == R.id.profile) {
            FirebaseUser user = mFirebaseAuth.getCurrentUser();
            if (user != null) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            } else {
                buildDialog();
            }

        }
        if (id == R.id.delete_account) {
            deleteAccount();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Auth UI sign in
    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

        // [END auth_fui_create_intent]
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                readAuthData();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Snackbar.make(coordinatorLayout, "You are now signed out!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
        // [END auth_fui_signout]
    }


    public void deleteAccount() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Snackbar.make(coordinatorLayout, "Your account was deketed successfully!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
        // [END auth_fui_delete]
    }

    //buil dialog for unauthenticated user
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.fui_sign_in_default);
        builder.setMessage("You are currently signed out, you have to sign in to be able to view this");
        builder.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                createSignInIntent();
            }
        });

        builder.setNegativeButton("exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showNotification() {
        NewEventNotification.notify(this, title, date, url, 0);
    }


    String userEmail, uid, username, no, pp_url;

    //get auth details after logging in
    public void readAuthData() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            uid = mFirebaseAuth.getCurrentUser().getUid();
            if (mFirebaseAuth.getCurrentUser().getEmail() == null) {
                userEmail = "";
            } else {
                userEmail = mFirebaseAuth.getCurrentUser().getEmail();
            }

            if (mFirebaseAuth.getCurrentUser().getDisplayName() == null) {
                username = "";
            } else {
                username = mFirebaseAuth.getCurrentUser().getDisplayName();
            }

            if (mFirebaseAuth.getCurrentUser().getPhoneNumber() == null) {
                no = "07034750495";
            } else {
                no = mFirebaseAuth.getCurrentUser().getPhoneNumber();
            }

            if (mFirebaseAuth.getCurrentUser().getPhotoUrl() == null) {
                pp_url = "";
            } else {
                pp_url = mFirebaseAuth.getCurrentUser().getPhotoUrl().toString();
            }

            User user = new User();
            user.setUserEmail(userEmail);
            user.setPpUurl(pp_url);
            user.setUsername(username);
            user.setMobileNo(no);

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference mDbr = db.getReference();

            mDbr.child(getString(R.string.dbnode_users))
                    .child(uid)
//        if(mDbr.child(getString(R.string.dbnode_users))
//                    .child(uid) != null)
                    .setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Profile data updated.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "something went wrong.", Toast.LENGTH_SHORT).show();
                    //FirebaseAuth.getInstance().signOut();
                }
            });

        }

    }

}
