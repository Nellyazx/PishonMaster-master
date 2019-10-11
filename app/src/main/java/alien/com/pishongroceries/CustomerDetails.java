package alien.com.pishongroceries;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import alien.com.api.ApiService;
import alien.com.dashboard.Dashboard;
import alien.com.volley.AppController;

public class CustomerDetails extends AppCompatActivity
{
    AlertDialog.Builder alertDialog;
    ProgressDialog progressDialog;

    EditText location,famsize,password,confpass,charge;
    Button register;
    String getname,getemail,getphone,getlocation,getfamsize,getpassword,getconfpass,getcharge;
    Intent getData ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        alertDialog = new AlertDialog.Builder(CustomerDetails.this);
        register = findViewById(R.id.register);
        location = findViewById(R.id.location);
        famsize = findViewById(R.id.famsize);
        password = findViewById(R.id.password);
        confpass = findViewById(R.id.confpass);
        charge = findViewById(R.id.charge);
        //get Intent


        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getData = getIntent();
                getname=getData.getStringExtra("name");
                getemail=getData.getStringExtra("email");
                getphone=getData.getStringExtra("phone");
                getlocation=location.getText().toString().trim();
                getfamsize=famsize.getText().toString().trim();
                getpassword=password.getText().toString().trim();
                getconfpass=confpass.getText().toString().trim();
                getcharge=charge.getText().toString().trim();

                Log.e("RES",getname+getemail+getphone+getlocation+getfamsize+getpassword+getconfpass+getcharge);
                if(getlocation.equals("")||getfamsize.equals("")||getpassword.equals("")||getconfpass.equals("")||getcharge.equals(""))
                {
                    alertDialog.setTitle("Empty Fields");
                    // alertDialog.setMessage(message);
                    displayAlert("Please fill all the fields");
                }
                else {
                    if (!(getpassword.equals(getconfpass))) {
                        alertDialog.setTitle("Password...");
                        alertDialog.setMessage("Passwords Mismatch...");
                        displayAlert("Please ensure that password matches confirm password");
                    } else {
                        getData();
                    }
                }

            }
        });
    }
    public void getData()
    {
       // Toast.makeText(this, "We are here too", Toast.LENGTH_SHORT).show();
        //Log.e("RES",getname+getemail+getphone+getlocation+getfamsize+getpassword+getconfpass+getcharge);

        progressDialog = ProgressDialog.show(CustomerDetails.this, "", "Registering..", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    //Toast.makeText(CustomerDetails.this, "Check this", Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    alertDialog.setTitle("Register Customer");
                    alertDialog.setMessage(message);
                    displayAlert(code);
                    startActivity(new Intent(CustomerDetails.this, Dashboard.class));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", getname);
                params.put("email", getemail);
                params.put("phone", getphone);
                params.put("location", getlocation);
                params.put("familysize", getfamsize);
                params.put("password", getpassword);
                params.put("confpass", getconfpass);
                params.put("fee", getcharge);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(CustomerDetails.this).addToRequestQueue(stringRequest);
    }
    public void displayAlert(final String code) {
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) 
            {
                password.setText("");
                confpass.setText("");
            }
        });
        AlertDialog theDialog = alertDialog.create();
        theDialog.show();
    }
}
