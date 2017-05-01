package com.example.adie.chattingappassignmnt;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText et;
    Button b;

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList arrayList;
    String name;
    public static  final int REQ_CD1=1;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et= (EditText) findViewById(R.id.edittext);
        b= (Button) findViewById(R.id.button);
        arrayList=new ArrayList();
        listView= (ListView) findViewById(R.id.listview);
       // tv= (TextView) findViewById(R.id.textview);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);
        requestUsername();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,Object> map=new HashMap<String, Object>();
                map.put(et.getText().toString(),"");
                databaseReference.updateChildren(map);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set=new HashSet<String>();
                Iterator i=dataSnapshot.getChildren().iterator();
                while(i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                arrayList.clear();
                arrayList.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                i.putExtra("roomname",((TextView)view).getText().toString());
                i.putExtra("username",name);
                startActivity(i);
            }
        });

    }

    public  void  requestUsername()
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("enter usernam");
        final EditText editText=new EditText(this);
        alertDialog.setView(editText);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                name = editText.getText().toString();
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                requestUsername();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        super.onActivityResult(requestCode, resultCode, data);
    }
}
