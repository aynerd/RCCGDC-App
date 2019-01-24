package com.lollykrown.rccgdc.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.adapters.NotificationsAdapter;
import com.lollykrown.rccgdc.model.Notifications;
import com.lollykrown.rccgdc.model.NotificationsDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.lollykrown.rccgdc.activity.MainActivity.navItemIndex;

public class NotificationsFragment extends Fragment {
    private static final String TAG = NotificationsFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private NotificationsAdapter mAdapter;
    private NotificationsDatabase db;
    final ArrayList<Notifications> noti = new ArrayList<>();

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rv = inflater.inflate(R.layout.fragment_notifications, container, false);


        recyclerView = rv.findViewById(R.id.rview);

        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

        fetchData();

        return rv;
    }

    public void deleteAllData(){
        NotificationsDatabase db = NotificationsDatabase.getInMemoryDatabase(getContext());
        Notifications n = new Notifications();
        db.notificationsDao().deleteNotifications(n);
        NotificationsDatabase.destroyInstance();
        mAdapter.notifyDataSetChanged();
        Log.e(TAG, "data deleted");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.sign_in).setVisible(false);
        menu.findItem(R.id.sign_out).setVisible(false);
        menu.findItem(R.id.profile).setVisible(false);
        menu.findItem(R.id.delete_account).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notifications, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mark_all_read:
                Toast.makeText(getActivity(), "mark read", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_clear_notifications:
                deleteAllData();
                Toast.makeText(getActivity(), "Notifications Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    int isRead;
    public void fetchData(){
        db = NotificationsDatabase.getInMemoryDatabase(getContext());
        List<Notifications> e = db.notificationsDao().getAll();

        for(Notifications n  : e){
            String title = n.getTitlee();
            String date = n.getDatee();
            String time = n.getTimee();
            String url = n.getUrl();
            isRead = n.getIsRead();

            Notifications even = new Notifications(title, date, time, url, isRead);
            Log.e(TAG, title + " "+ date + time + url+ " "+ isRead);
            noti.add(even);

        }

        if(isRead == 1){
            recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        }
        mAdapter = new NotificationsAdapter(getContext(), noti);
        recyclerView.setAdapter(mAdapter);
    }
}
