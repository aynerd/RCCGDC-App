package com.lollykrown.rccgdc.fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.activity.BullDetailsActivity;
import com.lollykrown.rccgdc.activity.MeetJesusActivity;
import com.lollykrown.rccgdc.adapters.AnnouncementsAdapter;
import com.lollykrown.rccgdc.adapters.BulletinAdapter;
import com.lollykrown.rccgdc.adapters.EventsAdapter;
import com.lollykrown.rccgdc.adapters.WorkforceAdapter;
import com.lollykrown.rccgdc.model.Announcements;
import com.lollykrown.rccgdc.model.Bulletin;
import com.lollykrown.rccgdc.model.Events;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.text.TextUtils.isEmpty;
import static com.facebook.FacebookSdk.getCacheDir;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements WorkforceAdapter.WfListItemClickListener,
        BulletinAdapter.BullListItemClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private ImageView facebook, twitter, instagram, youtube, whatsapp;
    private TextView churchAddress, phoneNo, emailAdd, website;
    private Button vision, meetJesus;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    private LinearLayout linearLayout;

    private Button verse;
    private static final int NUM_LIST_ITEMS = 100;


    private RecyclerView mWorkforceList;
    private RecyclerView upComingRv;
    private RecyclerView announcements_rv;
    private RecyclerView bulletin_rv;

    private EventsAdapter mAdapter;
    private AnnouncementsAdapter mAnnAdapter;
    private BulletinAdapter mBullAdapter;
    private WorkforceAdapter mWorkforceAdapter;


    final ArrayList<Announcements> ann = new ArrayList<>();
    final ArrayList<Bulletin> bull = new ArrayList<>();
    final ArrayList<Events> events = new ArrayList<>();

    String[] wf = {"Editorial", "Social", "Ushering", "Follow up", "Greeters", "Technical",
            "Sanitation", "Drama", "Choir", "Prayer", "Book Club", "Sunday School",
            "Children/Teenagers", "Welfare" };

    FirebaseDatabase fdb;
    String res;

    RequestQueue mRequestQueue;
    public static final String VTAG = "MyTag";
    StringRequest stringRequest;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rv = inflater.inflate(R.layout.fragment_home, container, false);

        fdb = FirebaseDatabase.getInstance();

        linearLayout = rv.findViewById(R.id.linear_layout);
        verse = rv.findViewById(R.id.verse);
        facebook = rv.findViewById(R.id.facebook);
        twitter = rv.findViewById(R.id.twitter);
        instagram = rv.findViewById(R.id.instagram);
        youtube = rv.findViewById(R.id.youtube);
        whatsapp = rv.findViewById(R.id.whatsapp);
        upComingRv = rv.findViewById(R.id.events_rv);
        mWorkforceList = rv.findViewById(R.id.workforce_rv);
        announcements_rv = rv.findViewById(R.id.announcements_rv);
        bulletin_rv = rv.findViewById(R.id.bulletin_rv);
        churchAddress = rv.findViewById(R.id.address);
        phoneNo = rv.findViewById(R.id.phone_no);
        emailAdd = rv.findViewById(R.id.email);
        website = rv.findViewById(R.id.website);
        vision = rv.findViewById(R.id.vision);
        meetJesus = rv.findViewById(R.id.meet);

        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mLayoutManager.setItemPrefetchEnabled(false);
        upComingRv.setLayoutManager(mLayoutManager);
        upComingRv.setItemAnimator(new DefaultItemAnimator());
        upComingRv.setHasFixedSize(true);

        LinearLayoutManager mAnnLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        announcements_rv.setLayoutManager(mAnnLayoutManager);

        announcements_rv.setHasFixedSize(true);

        //Method calls
        loadBibleVerse();
        initViews();
        getEventsDataFromFirebase();
        getAnnDataFromFirebase();
        generateBullData();

        //Load saved bible verse shared prefs
        SharedPreferences sp = getActivity().getSharedPreferences("verse_sp", Context.MODE_PRIVATE);
        if (sp == null) {
            verse.setText(res);
        }else {
            String vas = sp.getString("verse", "Verse for Today");
            verse.setText(vas);
        }

        return rv;
    }

    //Various widgets onclick methods and recyclerviews set up
    private void initViews(){

        churchAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String text;
                text = getString(R.string.church_address);

                myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getActivity().getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        phoneNo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String text;
                text = getString(R.string.church_phone);

                myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getActivity().getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        emailAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String text;
                text = getString(R.string.church_email);

                myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getActivity().getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        website.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String text;
                text = getString(R.string.church_website);

                myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getActivity().getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        phoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = getString(R.string.church_phone);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +number));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        emailAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getString(R.string.church_email);
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));
                //intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                //intent.putExtra(Intent.EXTRA_EMAIL, email);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, "Send email..."));
                }
            }
        });

        vision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewVision();
            }
        });

        meetJesus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MeetJesusActivity.class);
                startActivity(i);
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String website = "https://www.rccgdiscoverycentre.com";
                openWebPage(website);
            }
        });

        verse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFullVerse();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = "https://www.facebook.com/rccgdiscoverycentreyouthchurch";
                openWebPage(urlString);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = "https://www.twitter.com/rccgdc";
                openWebPage(urlString);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = "https://www.instagram.com/rccgdc";
                openWebPage(urlString);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = "https://www.youtube.com/channel/UCMlSaeKHZnjLcXP56B9neGg";
                openWebPage(urlString);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString;
                //whatsapp group invite
                //openWebPage(urlString);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mWorkforceList.setLayoutManager(layoutManager);
        mWorkforceList.setHasFixedSize(true);
        mWorkforceAdapter = new WorkforceAdapter(NUM_LIST_ITEMS, wf, this);
        mWorkforceList.setAdapter(mWorkforceAdapter);

        LinearLayoutManager mBullLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        bulletin_rv.setLayoutManager(mBullLayoutManager);

        bulletin_rv.setHasFixedSize(true);

        mBullAdapter = new BulletinAdapter(getContext(), bull, this);
        bulletin_rv.setAdapter(mBullAdapter);
    }

    //Bulletin array list
    private ArrayList<Bulletin> generateBullData(){

        bull.add(new Bulletin("https://imgur.com/tU5VbPs.jpg", "July 2018"));
        bull.add(new Bulletin("https://imgur.com/tU5VbPs.jpg", "July 2018"));
        bull.add(new Bulletin("https://imgur.com/tU5VbPs.jpg", "July 2018"));
        bull.add(new Bulletin("https://imgur.com/tU5VbPs.jpg", "July 2018"));
        bull.add(new Bulletin("https://imgur.com/tU5VbPs.jpg", "July 2018"));
        bull.add(new Bulletin("https://imgur.com/tU5VbPs.jpg", "July 2018"));
        bull.add(new Bulletin("https://imgur.com/tU5VbPs.jpg", "July 2018"));


        return bull;
    }

    //Second snackbar onclicklistener for eorkforce list item click
    View.OnClickListener myOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(linearLayout, "Enroll in the Next round of Believer class",
                    Snackbar.LENGTH_LONG).show();
        }
    };

    @Override
    public void onWfListItemClick(int clickedItemIndex) {
        Snackbar.make(linearLayout, "Are you born again? ", Snackbar.LENGTH_LONG)
                .setAction("Yes/No", myOnclickListener)
                .setActionTextColor(Color.RED).show();
    }

    //Bulletin list item click method
    @Override
    public void onBullListItemClick(int clickedItemIndex) {
        final Bulletin bulletin = bull.get(clickedItemIndex);

        Intent i = new Intent(getContext(), BullDetailsActivity.class);
        i.putExtra("bullMonth", bulletin.getBullMonth());
        i.putExtra("bullImgUrl", bulletin.getBullImgUrl());
        startActivity(i);

    }

    //Dialog to view full verse of the day
    private void viewFullVerse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.word_for_today);
        SharedPreferences sp = getActivity().getSharedPreferences("verse_sp", Context.MODE_PRIVATE);
        if (sp != null) {
            String vas = sp.getString("verse", "bible verse");
            builder.setMessage(vas);
        }else {
            builder.setMessage(res);
        }
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                dialog.dismiss();
            }
        });


        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Dialog for vision ans mission statement of RCCG
    private void viewVision() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Our Vision and Mission: ");
        builder.setMessage(R.string.vision_statement);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                dialog.dismiss();
            }
        });


        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Method to open any webpage
    private void openWebPage(String webUrl) {
        Uri webpage = Uri.parse(webUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
       }
    }

    //Load event or programmes details from firebase
    private ArrayList<Events> getEventsDataFromFirebase () {

        DatabaseReference mDbr = fdb.getReference();

        Query q = mDbr.child("events")
                .orderByValue();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Events ev = singleSnapshot.getValue(Events.class);
                    String ur = ev.getUrl();
                    String dat = ev.getEventDate();
                    String tit = ev.getEventTitle();
                    String tim = ev.getEventTime();

                    Events upev = new Events(dat, tim, tit, ur);
                    events.add(upev);
                }
                mAdapter = new EventsAdapter(getContext(), events);
                //mAdapter.notifyDataSetChanged();
                upComingRv.setAdapter(mAdapter);
                upComingRv.scrollToPosition(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return events;
    }

    //Load announcement images from Firebase
    private ArrayList<Announcements> getAnnDataFromFirebase () {

        DatabaseReference mDbr = fdb.getReference();


        Query q = mDbr.child("announcements")
                .orderByValue();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){

                    Announcements an = singleSnapshot.getValue(Announcements.class);
                    String url = an.getUrl();

                    Announcements upev = new Announcements(url);
                    ann.add(upev);
                    }
                mAnnAdapter = new AnnouncementsAdapter(getContext(), ann);
                announcements_rv.setAdapter(mAnnAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return ann;
    }

    //Load verse for the day using daily manna Api
    public void loadBibleVerse(){
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = Volley.newRequestQueue(getContext());
        // Start the queue
        mRequestQueue.start();
        String url ="https://beta.ourmanna.com/api/v1/get/?format=text";
        //Formulate the request and handle the response.
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        try {
                            //String result = response;
                            res = response;
                            SharedPreferences sp = getActivity().getSharedPreferences("verse_sp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("verse", response);
                            editor.apply();

                            // Commit the transaction
                        } catch (StringIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        String err = null;
                        if (error instanceof com.android.volley.NoConnectionError){
                            err = "No internet Connection!";
                        }
                        try {
                            if(!isEmpty(err)) {
                                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }

}