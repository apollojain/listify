package com.example.apollo.listify;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snappydb.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    // Array of strings...
    DB snap;
    ArrayList mA;

    String AL = "ALNAME";

    String[] mobileArray;
    HashMap h;
    String HM = "HMNAME";
    int currentItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Context context = this;
            snap = DBFactory.open(context);
        }catch(SnappydbException e){

        }
        mA = getArrayListFromDB();
        h = getHashMapFromDB();
        if(mA == null){
            mA = new ArrayList();
        }
        if(h == null){
            h = new HashMap();
        }

        super.onCreate(savedInstanceState);
        goHome();

    }
    protected void goHome() {
        mobileArray = new String[mA.size()];
        mA.toArray(mobileArray);
        for(int i = 0; i < mobileArray.length; i++){
            if(!h.containsKey(mobileArray[i])){
                h.put(mobileArray[i], "item" + Integer.toString(i));
            }

        }
        setContentView(R.layout.activity_main);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                currentItem = position;
                showItem();
            }
        });
        Button add_item = (Button) findViewById(R.id.add_item_button);
        add_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItem();

            }
        });
    }

    protected void updateDB(ArrayList al, HashMap hm){
        try{
            snap.put(AL, al);
            snap.put(HM, hm);
        }catch(SnappydbException e){

        }

    }

    protected HashMap getHashMapFromDB() {
        try {
            return snap.getObject(HM, HashMap.class);
        }catch(SnappydbException s){
            return null;
        }
    }

    protected ArrayList getArrayListFromDB() {
        try {
            return snap.getObject(AL, ArrayList.class);
        }catch(SnappydbException s){
            return null;
        }
    }

    protected String getName(int key){
        return mobileArray[key];
    }

    protected String getDescription(int key){
        return (String) h.get(mobileArray[key]);
    }

    protected void addItem(){
        setContentView(R.layout.add_item_main);
        final EditText name = (EditText) findViewById(R.id.newName);
        final EditText description = (EditText) findViewById(R.id.newDescription);

        ImageButton show_home = (ImageButton) findViewById(R.id.show_homepage_button);
        show_home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goHome();

            }
        });
        Button new_item = (Button) findViewById(R.id.submit_new);
        new_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nametext = name.getText().toString();
                String descriptiontext = description.getText().toString();
                addNewItem(nametext, descriptiontext);
            }
        });

    }

    protected void editItem(){
        setContentView(R.layout.edit_item_main);
        final EditText name = (EditText) findViewById(R.id.editName);
        name.setText(getName(currentItem), TextView.BufferType.EDITABLE);
        final EditText description = (EditText) findViewById(R.id.editDescription);
        description.setText(getDescription(currentItem), TextView.BufferType.EDITABLE);
        ImageButton show_home = (ImageButton) findViewById(R.id.show_homepage_button);
        show_home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goHome();

            }
        });
        Button new_item = (Button) findViewById(R.id.submit_edit);
        new_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nametext = name.getText().toString();
                String descriptiontext = description.getText().toString();
                editOldItem(nametext, descriptiontext);
            }
        });

    }

    protected void editOldItem(String name, String description){
        h.remove(mA.get(currentItem));
        mA.set(currentItem, name);
        h.put(name, description);
        updateDB(mA, h);
        goHome();
    }


    protected void addNewItem(String name, String description){
        mA.add(name);
        h.put(name, description);
        updateDB(mA, h);
        goHome();
    }

    protected void showItem(){
        int key = currentItem;
        String name = getName(key);
        String description = getDescription(key);
        setContentView(R.layout.show_main);
        TextView mShowName = (TextView) findViewById(R.id.showname);
        mShowName.setText(name);
        TextView mShowDescription = (TextView) findViewById(R.id.showdescription);
        mShowDescription.setText(description);
        ImageButton show_home = (ImageButton) findViewById(R.id.show_homepage_button);
        show_home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goHome();

            }
        });
        Button destroy_item = (Button) findViewById(R.id.destroy_item_button);
        destroy_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                destroyItem();
            }
        });

        Button edit_item = (Button) findViewById(R.id.edit_item_button);
        edit_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    protected void destroyItem(){
        int key = currentItem;
        h.remove(getName(key));
        mA.remove(getName(key));
        updateDB(mA, h);
        goHome();
    }
}