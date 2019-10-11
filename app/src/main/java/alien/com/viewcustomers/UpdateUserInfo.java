package alien.com.viewcustomers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import alien.com.api.ApiService;
import alien.com.dashboard.Dashboard;
import alien.com.pishongroceries.R;
import alien.com.volley.AppController;

public class UpdateUserInfo extends AppCompatActivity {
EditText name,email,location,phone,famsize,delfee;
Button update;
StringRequest stringRequest;
String getname,getmail,getloc,getphone,getfamsize,getdelfee,gottenCustPhone;
String getCustName,getCustPhone,getCustEmail,getCustLoc,getCustFamilySize,getCustDelFee;
StringRequest updateRequest;
    AlertDialog.Builder alertDialog;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile Settings");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        alertDialog = new AlertDialog.Builder(UpdateUserInfo.this);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        phone = findViewById(R.id.phone);
        famsize = findViewById(R.id.famsize);
        delfee = findViewById(R.id.delfee);
        update = findViewById(R.id.btnupdate);
        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                updateUsers();
            }
        });
        Intent getCustPhone = getIntent();

        gottenCustPhone = getCustPhone.getStringExtra("personphone");

        getData();

    }
    public void updateUsers()
    {
        getCustName = name.getText().toString().trim();
        getCustEmail = email.getText().toString().trim();
        getCustLoc = location.getText().toString().trim();
        getCustPhone = phone.getText().toString().trim();
        getCustFamilySize = famsize.getText().toString().trim();
        getCustDelFee = delfee.getText().toString().trim();
        progressDialog = ProgressDialog.show(UpdateUserInfo.this, "", "Registering..", false, false);
        updateRequest = new StringRequest(Request.Method.POST, ApiService.UPDATE_CUSTOMER_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.e("Update",response);
                try {
                    progressDialog.dismiss();
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    alertDialog.setTitle("Update Customer");
                    alertDialog.setMessage(message);
                    displayAlert(code);
                    startActivity(new Intent(UpdateUserInfo.this, Dashboard.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",getCustName);
                params.put("email",getCustEmail);
                params.put("phone",getCustPhone);
                params.put("location",getCustLoc);
                params.put("familysize",getCustFamilySize);
                params.put("fee",getCustDelFee);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(getApplicationContext()).addToRequestQueue(updateRequest);

    }
    public void getData()
    {
       // Toast.makeText(this, "We reached Here", Toast.LENGTH_SHORT).show();
        stringRequest = new StringRequest(Request.Method.POST, ApiService.GET_CUSTOMER_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONArray jsonArray = new JSONArray(response);
                    //Log.e("Data",response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    getname = jsonObject.getString("name");
                    getmail = jsonObject.getString("email");
                    getloc = jsonObject.getString("location");
                    getphone = jsonObject.getString("phone");
                    getfamsize = jsonObject.getString("famsize");
                    getdelfee = jsonObject.getString("deliveryfee");

                    name.setText(getname);
                    email.setText(getmail);
                    phone.setText(getphone);
                    location.setText(getloc);
                    famsize.setText(getfamsize);
                    delfee.setText(getdelfee);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(UpdateUserInfo.this, "No data Found", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> params = new HashMap<>();
                params.put("phone",gottenCustPhone);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(getApplication()).addToRequestQueue(stringRequest);
    }
    public void displayAlert(final String code) {
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which)
            {

            }
        });
        AlertDialog theDialog = alertDialog.create();
        theDialog.show();
    }
}
