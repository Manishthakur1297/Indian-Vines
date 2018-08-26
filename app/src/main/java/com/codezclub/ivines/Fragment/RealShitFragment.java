package com.codezclub.ivines.Fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.codezclub.ivines.Adapter.RecyclerAdapter;
import com.codezclub.ivines.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mj 2 on 18-Jun-17.
 */

public class RealShitFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    SQLiteDatabase articlesDb;
    RecyclerAdapter customAdapterRecycler;
    Button loadMore;

    ArrayList<String> VideoID = new ArrayList<String>();
    ArrayList<String> TextID = new ArrayList<String>();

    ProgressDialog progressDialog;

    String nextPageToken="";

    static int n = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.realshit_fragment,container,false);

        loadMore = (Button) view.findViewById(R.id.loadMore);
        //loadMore.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        articlesDb = getActivity().getApplicationContext().openOrCreateDatabase("Vines", MODE_PRIVATE, null);

        articlesDb.execSQL("CREATE TABLE IF NOT EXISTS realShit (id INTEGER PRIMARY KEY, videoId VARCHAR, titleId VARCHAR)");

        //updateListView();

        customAdapterRecycler = new RecyclerAdapter(getActivity(),VideoID,TextID);
        recyclerView.setAdapter(customAdapterRecycler);

        updateListView();

        progressDialog = new ProgressDialog(getActivity().getApplicationContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data Please wait...");

        StringBuffer str = new StringBuffer("https://www.googleapis.com/youtube/v3/search?key=AIzaSyCuNPXBzrHf0l9MF-jkY8d_xoHRekzputw&channelId=UCsSZyyGKf9FdpqDmynjVBcA&part=snippet,id&order=date&maxResults=20");

        final AQuery aQuery=new AQuery(getActivity().getApplicationContext());

        if(nextPageToken==null)
        {
            loadMore.setVisibility(View.GONE);

        }
        else
        {
            str.append("&pageToken=" + nextPageToken);
            loadMore.setVisibility(View.VISIBLE);
        }

        Log.i("hello2----", String.valueOf(str));

        aQuery.progress(progressDialog).ajax(String.valueOf(str), JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String str, JSONObject json, AjaxStatus status) {

                if (json != null) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(String.valueOf(json));

                        if (jsonObject.has("nextPageToken")) {
                            nextPageToken =  jsonObject.getString("nextPageToken");
                        } else {
                            nextPageToken = null;
                        }

                        Log.i("NextPageToken-----", nextPageToken+"");

                        JSONArray items = jsonObject.getJSONArray("items");

                        Log.i("i befre", String.valueOf(n));

                        if(n==1)
                        {
                            articlesDb.execSQL("DELETE FROM realShit");
                            n++;
                            Log.i("i inside", String.valueOf(n));
                        }

                        Log.i("i outside", String.valueOf(n));

                        //articlesDb.execSQL("DELETE FROM bbKiVines");

                        for (int i = 0; i < items.length(); i++) {

                            JSONObject c = items.getJSONObject(i);
                            JSONObject id = c.getJSONObject("id");
                            JSONObject snippet = c.getJSONObject("snippet");

                            if (!id.isNull("videoId") && !snippet.isNull("title")) {

                                String videoId = id.getString("videoId");
                                String titleId = snippet.getString("title");

                                String sql = "INSERT INTO realShit (videoId, titleId) VALUES (?, ?)";

                                SQLiteStatement statement = articlesDb.compileStatement(sql);

                                statement.bindString(1, videoId);
                                statement.bindString(2, titleId);

                                statement.execute();
                            }
                        }

                        //updateListView();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                //updateListView();
            }

        });

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nextPageToken==null)
                {

                    loadMore.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Loading Videos Completed....",Toast.LENGTH_LONG).show();
                }
                else
                {
                    loadMore.setVisibility(View.VISIBLE);
                    Count cnt = new Count();
                    /*try {
                        cnt.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
                //updateListView();

            }
        });

        updateListView();

        return view;
    }

    public void updateListView() {
        Cursor c = articlesDb.rawQuery("SELECT * FROM realShit", null);

        int videoIndex = c.getColumnIndex("videoId");
        int titleIndex = c.getColumnIndex("titleId");

        if (c.moveToFirst()) {
            VideoID.clear();
            TextID.clear();

            do {
                VideoID.add(c.getString(videoIndex));
                TextID.add(c.getString(titleIndex));
            } while (c.moveToNext());

            customAdapterRecycler.notifyDataSetChanged();
        }
    }



    class Count extends Thread
    {
        //static String str = "";
        Count()
        {
            start();
        }
        @Override
        public void run()
        {
            try
            {

                StringBuffer str = new StringBuffer("https://www.googleapis.com/youtube/v3/search?key=AIzaSyCuNPXBzrHf0l9MF-jkY8d_xoHRekzputw&channelId=UCsSZyyGKf9FdpqDmynjVBcA&part=snippet,id&order=date&maxResults=20");

                final AQuery aQuery=new AQuery(getActivity().getApplicationContext());

                if(nextPageToken==null)
                {
                    loadMore.setVisibility(View.GONE);

                }
                else
                {
                    str.append("&pageToken=" + nextPageToken);
                    loadMore.setVisibility(View.VISIBLE);
                }

                Log.i("hello2----", String.valueOf(str));

                aQuery.progress(progressDialog).ajax(String.valueOf(str), JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String str, JSONObject json, AjaxStatus status) {

                        if (json != null) {

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(String.valueOf(json));

                                if (jsonObject.has("nextPageToken")) {
                                    nextPageToken =  jsonObject.getString("nextPageToken");
                                } else {
                                    nextPageToken = null;
                                }

                                Log.i("NextPageToken-----", nextPageToken+"");

                                JSONArray items = jsonObject.getJSONArray("items");

                                Log.i("i befre", String.valueOf(n));

                                if(n==1)
                                {
                                    articlesDb.execSQL("DELETE FROM realShit");
                                    n++;
                                    Log.i("i inside", String.valueOf(n));
                                }

                                Log.i("i outside", String.valueOf(n));

                                //articlesDb.execSQL("DELETE FROM bbKiVines");

                                for (int i = 0; i < items.length(); i++) {

                                    JSONObject c = items.getJSONObject(i);
                                    JSONObject id = c.getJSONObject("id");
                                    JSONObject snippet = c.getJSONObject("snippet");

                                    if (!id.isNull("videoId") && !snippet.isNull("title")) {

                                        String videoId = id.getString("videoId");
                                        String titleId = snippet.getString("title");

                                        String sql = "INSERT INTO realShit (videoId, titleId) VALUES (?, ?)";

                                        SQLiteStatement statement = articlesDb.compileStatement(sql);

                                        statement.bindString(1, videoId);
                                        statement.bindString(2, titleId);

                                        statement.execute();
                                    }
                                }

                                //updateListView();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        updateListView();
                    }

                });

                //updateListView();

            }


            catch(Exception e)
            {
                System.out.println("my thread interrupted");
            }
        }
    }

}