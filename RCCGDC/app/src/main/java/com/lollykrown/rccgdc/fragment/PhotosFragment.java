package com.lollykrown.rccgdc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.activity.MainActivity;
import com.lollykrown.rccgdc.activity.ProfileActivity;
import com.lollykrown.rccgdc.adapters.AlbumGridAdapter;
import com.lollykrown.rccgdc.model.Album;
public class PhotosFragment extends Fragment {

    public PhotosFragment() {
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
        final View rv =  inflater.inflate(R.layout.fragment_photos, container, false);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerView = rv.findViewById(R.id.albums_grid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        AlbumGridAdapter adapter = new AlbumGridAdapter(getContext(), Album.getAlbums());
        recyclerView.setAdapter(adapter);

        return rv;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
