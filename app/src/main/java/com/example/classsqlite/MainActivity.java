package com.example.classsqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton button;
    private DataBaseHelper dataBaseHelper;
    private List<Model> playerList;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHelper= new DataBaseHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase= dataBaseHelper.getWritableDatabase();
        adapter= new MyAdapter();

        recyclerView= findViewById(R.id.recycleViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        button= findViewById(R.id.insertButtonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,InsertActivity.class);
                startActivity(intent);
            }
        });

        loadData();

        adapter.setOnItemListener(new MyAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                String id= "Index: "+playerList.get(position).getId();
                String name= "Name: "+playerList.get(position).getName();
                String code= "Code: "+playerList.get(position).getCode();
                String type= "Type: "+playerList.get(position).getType();
                String details= id+"\n"+name+"\n"+code+"\n"+type;
                showDetails(details);
            }

            @Override
            public void onLongItemClick(final int position) {
                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                String[] option={"Delete","Update"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which==0)
                        {
                            deleteData(position);
                        }
                        if(which==1){

                        }
                    }
                }).create().show();

            }
        });
    }

    private void deleteData(int position) {
        String id= playerList.get(position).getId();
        int value= dataBaseHelper.delete(id);
        if(value>0){
            Toast.makeText(this, "Delete Successfully", Toast.LENGTH_SHORT).show();
            playerList.remove(position);
            adapter.notifyItemRemoved(position);
        }
        else {
            Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show();
        }


    }

    private void showDetails(String details) {
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Details").setMessage(details).setCancelable(true)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }


    private void loadData() {
        playerList= new ArrayList<>();
        Cursor cursor= dataBaseHelper.show();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
        else {

            while (cursor.moveToNext()){

                String id= String.valueOf(cursor.getString(0));
                String name= cursor.getString(1);
                String type= cursor.getString(2);
                String code= String.valueOf(cursor.getString(3));

                Model model= new Model(id,name,code,type);
                playerList.add(model);
            }
        }
        adapter.getPlayerList(playerList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
}