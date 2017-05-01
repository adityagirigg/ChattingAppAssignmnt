package com.example.adie.chattingappassignmnt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity {

    TextView tv;
    EditText et1;
    Button b1;
    String s,s1;
    DatabaseReference databaseReference;
    private String tempkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tv= (TextView) findViewById(R.id.textView);
        et1= (EditText) findViewById(R.id.editText);
        b1= (Button) findViewById(R.id.button2);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT);

        Intent i=getIntent();
        Bundle b=i.getExtras();
        s=b.getString("username");
        s1=b.getString("roomname");

         et1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                 imm.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT);
             }
         });
        setTitle("Username"+s +"  " + s1);
        databaseReference= FirebaseDatabase.getInstance().getReference().child(s1.replace("[","").replace("]","").toString());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,Object> map=new HashMap<String, Object>();
                tempkey=databaseReference.push().getKey();
                databaseReference.updateChildren(map);
                DatabaseReference mrf=databaseReference.child(tempkey);

                HashMap<String,Object> map1=new HashMap<String, Object>();
                map1.put("username",s);
                map1.put("message",et1.getText().toString());
                mrf.updateChildren(map1);


            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendchatconveration(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendchatconveration(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    String chatmsg,username1;
    private void appendchatconveration(DataSnapshot dataSnapshot)
    {

        Iterator i=dataSnapshot.getChildren().iterator();
        while (i.hasNext())

        {
            chatmsg=(String)((DataSnapshot)i.next()).getValue();
            username1=(String)((DataSnapshot)i.next()).getValue();

            tv.append(username1+" : "+ chatmsg +"\n");

        }
    }
}
