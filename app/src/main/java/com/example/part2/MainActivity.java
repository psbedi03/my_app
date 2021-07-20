package com.example.part2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String fish_name;
    String fish_desc;
    private final static String JSON_URL = "https://raw.githubusercontent.com/max-vericatch/fisheries-data/main/data/species.json";
    ProgressBar progressBar;

    ArrayList<String> fish_name_list = new ArrayList<>();
    ArrayList<String> fish_desc_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);
        listView.setClickable(true);
        progressBar = (ProgressBar)findViewById(R.id.progressView);

        GetFishesData getFishesData = new GetFishesData();
        getFishesData.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "clicked on "+i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
                intent.putExtra("TITLE_TEXT", fish_name_list.get(i));
                intent.putExtra("DESC_TEXT", fish_desc_list.get(i));
                startActivity(intent);

            }
        });
    }


    public  class GetFishesData extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try{
                URL url;
                HttpURLConnection urlConnection = null;
                try{
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection)url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuffer buffer = new StringBuffer();
                    String line="";

                    while((line = reader.readLine()) != null){
                        buffer.append(line+"\n");
                    }

                    return  buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(urlConnection!=null){
                        urlConnection.disconnect();
                    }
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONArray jsonArray = new JSONArray(s);


                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    fish_name = object.getString("name");
                    fish_desc = object.getString("desc");

                    fish_name_list.add(fish_name);
                    fish_desc_list.add(fish_desc);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Display the results in the list
            //ListAdapter listAdapter = new SimpleAdapter(MainActivity.this, fish_name_list, R.layout.)
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    MainActivity.this, android.R.layout.simple_list_item_1, fish_name_list);
            listView.setAdapter(arrayAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }

}