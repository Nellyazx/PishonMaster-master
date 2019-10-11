package alien.com.products;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alien.com.api.ApiService;
import alien.com.dashboard.Dashboard;
import alien.com.model.Category;
import alien.com.pishongroceries.R;
import alien.com.volley.AppController;

public class AddProducts extends AppCompatActivity {

    EditText productNameTxt,productpriceTxt,productQuantityTxt;
    Spinner spinnercategories;
    List<Category> categoryList;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;
    ImageView imgproduct, imgcamproduct,placeholder;
    String image="";
    static final int CAMERA_REQUEST = 1888;
    static final int MY_CAMERA_PERMISSION_CODE = 100;
    static final int PICK_IMAGE_REQUEST = 1;
    Button addproduct;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Products");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(AddProducts.this);

        productNameTxt = findViewById(R.id.productName);
        productpriceTxt = findViewById(R.id.productPrice);
        productQuantityTxt = findViewById(R.id.productQuantity);

        categoryList = new ArrayList<>();

        spinnercategories = findViewById(R.id.spinnercategories);

        getData();

        spinnercategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Select something first!",Toast.LENGTH_SHORT).show();
            }
        });

        imgproduct = findViewById(R.id.productImage);
        placeholder = findViewById(R.id.placeholder);
        imgproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        imgcamproduct = findViewById(R.id.productcamImage);
        imgcamproduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

        addproduct = findViewById(R.id.addprod);
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String catid = SpinnerCategoriesAdapter.categories.get(spinnercategories.getSelectedItemPosition()).getId();
                String proname = productNameTxt.getText().toString();
                String proprice = productpriceTxt.getText().toString();
                String proqty = productQuantityTxt.getText().toString();

                if(catid.isEmpty() || proname.isEmpty() || proprice.isEmpty() || proqty.isEmpty() || image.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill all credentials and choose image",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog = ProgressDialog.show(AddProducts.this, "", "Adding Product...", false, false);
                    addProducts(proname,proprice,proqty,image,catid);
                }
            }
        });


    }
    public void addProducts(final String productname,final  String productprice, final String productquantity,
                           final String img, final String catid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.ADD_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                params.put("product_name", productname);
                params.put("product_price", productprice);
                params.put("product_quantity", productquantity);
                params.put("product_image", img);
                params.put("category_id", catid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(AddProducts.this).addToRequestQueue(stringRequest);
    }

    public void displayAlert(final String code) {
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which)
            {
                if (code.equals("input_error")) {
                    productNameTxt.setText("");
                    productpriceTxt.setText("");
                    productQuantityTxt.setText("");

                }
                else if(code.equals("ins_failed")) {
                    alertDialog.setTitle("Upload Failed");
                    alertDialog.setMessage("Data Upload Failed");
                    productNameTxt.setText("");
                    productpriceTxt.setText("");
                    productQuantityTxt.setText("");

                }
                else {
                    Intent intent = new Intent(AddProducts.this,Dashboard.class);
                    startActivity(intent);
                }

            }

        });
        AlertDialog theDialog = alertDialog.create();
        theDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                image = getStringImage(bitmap);
                placeholder.setImageBitmap(bitmap);
                placeholder.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image = getStringImage(photo);

            placeholder.setVisibility(View.VISIBLE);
            placeholder.setImageBitmap(photo);
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiService.VIEW_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray ja=new JSONArray(response);
                    JSONObject jo=null;
                    categoryList.clear();
                    for(int i=0;i<ja.length();i++) {
                        jo=ja.getJSONObject(i);

                        Category categories = new Category
                                (jo.getString("category_name"),jo.getString("id"),jo.getString("category_image"));

                        categoryList.add(categories);

                        SpinnerCategoriesAdapter adapterclasses=new SpinnerCategoriesAdapter(AddProducts.this, categoryList);
                        spinnercategories.setAdapter(adapterclasses);
                    }

                }

                catch (JSONException e) {
                    //Toast.makeText(AddProducts.this, "No category Found "+e, Toast.LENGTH_SHORT).show();
                    Log.e("Nothing",e.toString());
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(AddProducts.this, ""+error, Toast.LENGTH_SHORT).show();
                Log.e("RES", String.valueOf(error));
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.productmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.viewproducts:
                //  navigate to home stock list page
                Intent viewProducts = new Intent(getApplicationContext(), ViewProducts.class);
                startActivity(viewProducts);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
}
