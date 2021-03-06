package com.prer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static java.lang.Integer.parseInt;

/**
 * Created by Ryan on 2/11/2015.
 */
public class DoctorAdapter extends BaseAdapter{
    Context context;
    JSONArray content;
    String email;
    SharedPreferences logPrefs;
    SharedPreferences formPrefs;
    int status;

    public DoctorAdapter(Context context, JSONArray content, String email, SharedPreferences logPrefs) {
        this.context = context;
        this.content = content;
        this.email = email;
        this.logPrefs = logPrefs;

        formPrefs = context.getSharedPreferences("formDetails", 0);
        status = formPrefs.getInt("status", -1);
    }

    @Override
    public int getCount() {
        return content.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return content.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Object();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int id = -1;
        String str_id = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_doctor_item, parent, false);
        }
        JSONObject jsonObject = null;
        TextView docName = (TextView) convertView.findViewById(R.id.name);
        TextView docDistance = (TextView) convertView.findViewById(R.id.distance);
        ImageButton form = (ImageButton) convertView.findViewById(R.id.form);
        ImageButton bio = (ImageButton) convertView.findViewById(R.id.profile);

        try {
            jsonObject = (JSONObject) content.get(position);
            docName.setText(jsonObject.getString("FirstName") + " " + jsonObject.getString("LastName"));
            docDistance.setText(jsonObject.getString("Distance"));
            str_id = jsonObject.getString("DoctorId")  ;
            id = parseInt(str_id);
            convertView.setId(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalStr_id = str_id;
        form.setOnClickListener(new View.OnClickListener() {
            Intent myIntent;

            public void onClick(View view) {
                if (email != null && status != 1) {
                    myIntent = new Intent(context, Form.class);
                } else if (status == 1) {
                    Toast.makeText(context, "You Have Already Requested A Call. Check the Waitroom.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    myIntent = new Intent(context, Login.class);
                }
                myIntent.putExtra("docId", finalStr_id);
                context.startActivity(myIntent);
            }
        });

        bio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(context, Bio.class);
                myIntent.putExtra("docId", finalStr_id);
                context.startActivity(myIntent);
            }
        });

        return convertView;
    }
}
