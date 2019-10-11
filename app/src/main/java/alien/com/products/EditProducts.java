package alien.com.products;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

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
import alien.com.category.EditCategory;
import alien.com.dashboard.Dashboard;
import alien.com.model.Category;
import alien.com.pishongroceries.R;
import alien.com.utils.GlideClient;
import alien.com.volley.AppController;

public class EditProducts extends AppCompatActivity {

    EditText productNameTxt,productpriceTxt,productQuantityTxt;
    Spinner spinnercategories;
    List<Category> categoryList;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;
    ImageView imgproduct,imgcamproduct,placeholder;
    String image="",catid;
    static final int CAMERA_REQUEST = 1888;
    static final int MY_CAMERA_PERMISSION_CODE = 100;
    static final int PICK_IMAGE_REQUEST = 1;
    Button addproduct;
    Bitmap bitmap;
    Intent getData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Products");
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

        getData = getIntent();

        final String getid=getData.getStringExtra("product_id");
        String getname=getData.getStringExtra("product_name");
        final String getimage=getData.getStringExtra("product_image");
        String getprice=getData.getStringExtra("product_price");
        String getqty=getData.getStringExtra("product_quantity");
        catid=getData.getStringExtra("category_id");

        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(EditProducts.this);

        productNameTxt = findViewById(R.id.productName);
        productNameTxt.setText(getname);
        productpriceTxt = findViewById(R.id.productPrice);
        productpriceTxt.setText(getprice);
        productQuantityTxt = findViewById(R.id.productQuantity);
        productQuantityTxt.setText(getqty);

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

                if(catid.isEmpty() && proname.isEmpty() && proprice.isEmpty() && proqty.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill all credentials",Toast.LENGTH_SHORT).show();
                }else{
                    if(image==""){
                        Glide.with(EditProducts.this)
                                .asBitmap()
                                .load(getimage)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        image=imageToString(resource);
                                    }
                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                    }
                        progressDialog = ProgressDialog.show(EditProducts.this, "", "Editing Product...", false, false);
                        updateProduct(getid,proname,proprice,proqty,image,catid);
                }
            }
        });

        GlideClient.downloadImage(getApplicationContext(),getimage,placeholder);
        placeholder.setVisibility(View.VISIBLE);
    }
    public void updateProduct(final String productid,final String productname, final  String productprice, final String productquantity,
                              final String img, final String catid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.EDIT_PRODUCTS, new Response.Listener<String>() {
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
                params.put("product_id", productid);
                params.put("product_name", productname);
                params.put("product_price", productprice);
                params.put("product_quantity", productquantity);
                params.put("product_image", img);
                params.put("category_id", catid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(EditProducts.this).addToRequestQueue(stringRequest);
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
                    Intent intent = new Intent(EditProducts.this,Dashboard.class);
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
                image = imageToString(bitmap);
                placeholder.setImageBitmap(bitmap);
                placeholder.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            image = imageToString(bitmap);
            placeholder.setImageBitmap(bitmap);
            placeholder.setVisibility(View.VISIBLE);
        }
    }

    public String imageToString(Bitmap bmp) {
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

                        SpinnerCategoriesAdapter adapterclasses=new SpinnerCategoriesAdapter(EditProducts.this, categoryList);
                        spinnercategories.setAdapter(adapterclasses);
                        spinnercategories.setSelection(Integer.parseInt(catid)-1);
                    }
                }

                catch (JSONException e) {
                    Toast.makeText(EditProducts.this, "No category Found "+e, Toast.LENGTH_SHORT).show();
                    Log.e("Nothing",e.toString());
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(EditProducts.this, ""+error, Toast.LENGTH_SHORT).show();
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
