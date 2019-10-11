package alien.com.products;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import alien.com.api.ApiService;
import alien.com.model.Product;
import alien.com.pishongroceries.R;
import alien.com.volley.AppController;

public class ViewProducts extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    List<Product> productList;
    ProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("View Products");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), AddProducts.class));
            }
        });
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.productInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        productList = new ArrayList<>();
        getData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

    }
    public void refreshPage()
    {
        adapter = new ProductAdapter(getApplicationContext(),productList);
        adapter.clearAdapter();
        getData();
    }
    public void getData()
    {
        swipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiService.VIEW_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray ja=new JSONArray(response);
                    JSONObject jo=null;
                    productList.clear();
                    for(int i=0;i<ja.length();i++) {
                        jo=ja.getJSONObject(i);

                        String id=jo.getString("product_id");
                        String name=jo.getString("product_name");
                        String imageurl=jo.getString("product_image");
                        String price=jo.getString("product_price");
                        String quantity=jo.getString("product_quantity");
                        String catid=jo.getString("category_id");

                        Product products = new Product
                                (catid,id,name,price,imageurl,quantity);

                        productList.add(products);

                        swipeRefreshLayout.setRefreshing(false);
                        adapter = new ProductAdapter(ViewProducts.this, productList);
                        recyclerView.setAdapter(adapter);
                    }
                }


                catch (JSONException e) {
                    Toast.makeText(ViewProducts.this, "Nothing Found "+e, Toast.LENGTH_SHORT).show();
                    Log.e("Nothing",e.toString());
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ViewProducts.this, ""+error, Toast.LENGTH_SHORT).show();
                Log.e("RES", String.valueOf(error));
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
