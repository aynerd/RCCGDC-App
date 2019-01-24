package com.lollykrown.rccgdc.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.lollykrown.rccgdc.utils.Config;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.adapters.YoutubeAdapter;
import com.lollykrown.rccgdc.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;
import static com.facebook.FacebookSdk.getCacheDir;

public class VideosFragment extends Fragment {

    public static final String TAG = "MyTag";
    ProgressBar progressDialog;
    ListView lvVideo;
    ArrayList<Video> videoDetailsArrayList;
    YoutubeAdapter customListAdapter;
    //String TAG="VideosFragment";
    String API_KEY = Config.YOUTUBE_API_KEY;
    String URL="https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCMlSaeKHZnjLcXP56B9neGg&maxResults=25&order=date&key=API_KEY";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    View emptyView;
    public VideosFragment() {
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
        final View rv = inflater.inflate(R.layout.fragment_videos, container, false);

        emptyView = rv.findViewById(R.id.empty_view);

        progressDialog=rv.findViewById(R.id.vid_progress);
        lvVideo=rv.findViewById(R.id.videoList);
        videoDetailsArrayList=new ArrayList<>();
        customListAdapter=new YoutubeAdapter(getActivity(),videoDetailsArrayList);

        lvVideo.setEmptyView(emptyView);
        showVideo();
		return rv;
    }

    private void showVideo() {
        //instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        //mRequestQueue = Volley.newRequestQueue(getContext());
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();
        //RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        stringRequest=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("items");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId=jsonObject1.getJSONObject("id");
                        JSONObject jsonsnippet= jsonObject1.getJSONObject("snippet");
                        JSONObject jsonObjectdefault = jsonsnippet.getJSONObject("thumbnails").getJSONObject("medium");
                        Video videoDetails=new Video();

                        videoDetails.setURL(jsonObjectdefault.getString("url"));
                        videoDetails.setVideoName(jsonsnippet.getString("title"));
                        videoDetails.setVideoDesc(jsonsnippet.getString("description"));
                        videoDetails.setChannelTitle(jsonsnippet.getString("channelTitle"));
                        videoDetails.setVideoId(jsonVideoId.getString("videoId"));

                        videoDetailsArrayList.add(videoDetails);
                        if (videoDetailsArrayList!=null){
                            progressDialog.setVisibility(View.GONE);
                            emptyView.setVisibility(View.GONE);
                        }
                    }
                    lvVideo.setAdapter(customListAdapter);
                    customListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String err = null;
                if (error instanceof com.android.volley.NoConnectionError){
                    err = "No internet Connection!";
                }
                try {
                    if(!isEmpty(err)) {
                        Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                        progressDialog.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        stringRequest.setTag(TAG);
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
        emptyView.setVisibility(View.GONE);
        progressDialog.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
