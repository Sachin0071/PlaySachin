package com.example.playsachin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView searching;
    TextView selectsong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        searching = findViewById(R.id.Searching);
        selectsong = findViewById(R.id.SelectSong);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)== PackageManager.PERMISSION_GRANTED){
            ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
            String [] items = new String[mySongs.size()];
            for(int i=0;i<mySongs.size();i++){
                items[i] = mySongs.get(i).getName().replace(".mp3", "");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, PlaySong.class);
                    String currentSong = listView.getItemAtPosition(position).toString();
                    intent.putExtra("songList", mySongs);
                    intent.putExtra("currentSong", currentSong);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }
    }

    public ArrayList<File> fetchSongs(File file){
        ArrayList arrayList = new ArrayList();
        File [] songs = file.listFiles();
        if(songs !=null){
            for(File myFile: songs){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchSongs(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        searching.setVisibility(View.INVISIBLE);
        selectsong.setVisibility(View.VISIBLE);
        return arrayList;
    }
}
