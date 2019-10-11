package alien.com.vieworders;

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
import alien.com.dashboard.Dashboard;
import alien.com.model.DeliveredInfo;
import alien.com.model.OrderInfo;
import alien.com.pishongroceries.R;
import alien.com.volley.AppController;

public class ViewDeliveredOrders extends AppCompatActivity {
    SwipeRefreshLayout deliveredswiperefreshlayout;
    RecyclerView deliveredrecyclerView;
    List<DeliveredInfo>deliveredInfoList;
    DeliveredOrdersAdapter deliveredAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_delivered_orders);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Delivered Orders");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        deliveredrecyclerView = findViewById(R.id.deliveredrecyclerview);
        deliveredswiperefreshlayout = findViewById(R.id.deliveredswipeRefreshLayout);
        deliveredrecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(deliveredrecyclerView.getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.divider));
        deliveredrecyclerView.addItemDecoration(dividerItemDecoration);
        deliveredrecyclerView.setHasFixedSize(true);
        deliveredInfoList = new ArrayList<>();
        getData();
        deliveredswiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                refreshPage();
            }
        });

    }
    public void refreshPage()
    {
        deliveredAdapter = new DeliveredOrdersAdapter(getApplicationContext(),deliveredInfoList);
        deliveredAdapter.clearAdapter();
        getData();
    }
    public void getData()
    {
        deliveredswiperefreshlayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiService.VIEW_ALL_DELIVERED_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray ja=new JSONArray(response);
                    JSONObject jo=null;
                    deliveredInfoList.clear();
                    for(int i=0;i<ja.length();i++) {
                        jo=ja.getJSONObject(i);
                        DeliveredInfo deliveredInfo = new DeliveredInfo
                                (jo.getString("username"),
                                        jo.getString("product_name"),
                                        jo.getString("variance"),
                                        jo.getString("price"),
                                        jo.getString("location"),
                                        jo.getString("phonenumber"));

                        deliveredInfoList.add(deliveredInfo);
                        deliveredswiperefreshlayout.setRefreshing(false);
                        deliveredAdapter = new DeliveredOrdersAdapter(ViewDeliveredOrders.this, deliveredInfoList);
                        deliveredrecyclerView.setAdapter(deliveredAdapter);
                    }
                }

                catch (JSONException e) {
                    Toast.makeText(ViewDeliveredOrders.this, "Nothing Found "+e, Toast.LENGTH_SHORT).show();
                    Log.e("Nothing",e.toString());
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                deliveredswiperefreshlayout.setRefreshing(false);
                Toast.makeText(ViewDeliveredOrders.this, ""+error, Toast.LENGTH_SHORT).show();
                Log.e("RES", String.valueOf(error));
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);

//  AppController.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
    
    
}
