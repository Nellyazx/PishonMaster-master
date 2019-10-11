package alien.com.auth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class AdminLogin extends AppCompatActivity
{
    Button Login;
    EditText email,password;
    String gottenEmail,gottenPassword;
    AlertDialog.Builder alertDialog;
    ProgressDialog progressDialog;
    StringRequest stringRequest;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21)
        {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.activity_admin_login);
        Login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        alertDialog = new AlertDialog.Builder(AdminLogin.this);


        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


            }
        });

        gottenEmail = email.getText().toString().trim();
        gottenPassword = password.getText().toString().trim();
        getLogin();
    }
    public void getLogin()
    {
       // Toast.makeText(this, "We are Here", Toast.LENGTH_SHORT).show();
        progressDialog = ProgressDialog.show(AdminLogin.this, "", "Logging in...", false, false);
        stringRequest = new StringRequest(Request.Method.POST, ApiService.LOGIN_URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                //Toast.makeText(AdminLogin.this, "Success", Toast.LENGTH_SHORT).show();
                try
                {
                    JSONArray array = new JSONArray(response);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    if(code.equals("1"))
                    {
                        alertDialog.setTitle("Failed");
                        // alertDialog.setMessage(message);
                        displayAlert(jsonObject.getString("message"));
                    }
                    else
                    {
                        Intent nextScreen = new Intent(AdminLogin.this,Dashboard.class);
                        startActivity(nextScreen);
                    }
                }
                catch(JSONException e)
                {

                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("email",gottenEmail);
                params.put("password",gottenPassword);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(AdminLogin.this).addToRequestQueue();
    }
    public void displayAlert(final String message) {
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                email.setText("");
                password.setText("");
            }
        });
        AlertDialog alertDiag = alertDialog.create();
        alertDiag.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (count == 0) {
                Toast.makeText(AdminLogin.this, "Click again to exit application ", Toast.LENGTH_SHORT).show();
                count++;
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        return true;
    }
}
