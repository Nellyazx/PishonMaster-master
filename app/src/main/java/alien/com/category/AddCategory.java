package alien.com.category;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import alien.com.api.ApiService;
import alien.com.dashboard.Dashboard;
import alien.com.pishongroceries.R;
import alien.com.volley.AppController;

public class AddCategory extends AppCompatActivity
{
    private final int IMG_REQUEST=1;
    ImageView catImage;
    Bitmap bitmap;
    int count = 0;
    RelativeLayout addImage;
    TextView selectImage;
    String categoryname,image="";
    EditText categoryName;
    Button addCat;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Categories");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(AddCategory.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        addImage = findViewById(R.id.addImage);
        selectImage = findViewById(R.id.selectImage);
        categoryName = findViewById(R.id.categoryName);
        catImage = findViewById(R.id.catImage);
        addCat = findViewById(R.id.addCat);
        addImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectImage();
            }
        });
        addCat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                categoryname = categoryName.getText().toString();
                if(categoryname.isEmpty() || image.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill all credentials and choose image",Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog = ProgressDialog.show(AddCategory.this, "", "Adding Category...", false, false);
                    addCategory();
                }
            }
        });

    }
    public void addCategory()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.ADD_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    alertDialog.setTitle("Server Response");
                    alertDialog.setMessage(message);
                    displayAlert(code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), " something went wrong ", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category_name", categoryname);
                params.put("category_image", image);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(AddCategory.this).addToRequestQueue(stringRequest);
    }
    //convert imagebitmap to strng
    public String getStringImage(Bitmap mbitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        mbitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imagebytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagebytes,Base64.DEFAULT);

    }
    public void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try
            {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                image = getStringImage(bitmap);
                catImage.setImageBitmap(bitmap);
                selectImage.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void displayAlert(final String code) {
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which)
            {
                if (code.equals("input_error")) {
                    categoryName.setText("");
                }
                else if(code.equals("ins_failed"))
                {
                    alertDialog.setTitle("Upload Failed");
                    alertDialog.setMessage("Data Upload Failed");
                    categoryName.setText("");

                }
                else
                {
                    Intent intent = new Intent(AddCategory.this,Dashboard.class);
                    startActivity(intent);
                }

            }

        });
        AlertDialog theDialog = alertDialog.create();
        theDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categorymenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.viewcategory:
                //  navigate to home stock list page
                Intent viewCategory = new Intent(getApplicationContext(), ViewCategory.class);
                startActivity(viewCategory);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

}
