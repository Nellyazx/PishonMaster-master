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
import alien.com.model.OrderInfo;
import alien.com.pishongroceries.R;
import alien.com.volley.AppController;

public class ViewPendingOrders extends AppCompatActivity
{
    SwipeRefreshLayout ordersswiperefreshlayout;
    RecyclerView ordersrecyclerView;
    List<OrderInfo> orderInfoList;
    OrdersAdapter ordersAdapter;
    public static String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pending_orders);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pending Orders");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        ordersrecyclerView = findViewById(R.id.ordersrecyclerview);
        ordersswiperefreshlayout = findViewById(R.id.ordersswipeRefreshLayout);
        ordersrecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ordersrecyclerView.getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.divider));
        ordersrecyclerView.addItemDecoration(dividerItemDecoration);
        ordersrecyclerView.setHasFixedSize(true);
        orderInfoList = new ArrayList<>();
        getData();
        ordersswiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                refreshPage();
            }
        });

    }
    public void refreshPage()
    {
        ordersAdapter = new OrdersAdapter(getApplicationContext(),orderInfoList);
        ordersAdapter.clearAdapter();
        getData();
    }
    public void getData()
    {
        ordersswiperefreshlayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiService.VIEW_ALL_PENDING_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray ja=new JSONArray(response);
                    JSONObject jo=null;
                    orderInfoList.clear();
                    for(int i=0;i<ja.length();i++) {
                        jo=ja.getJSONObject(i);
                        OrderInfo orderInfo = new OrderInfo
                                (jo.getString("username"),
                                        jo.getString("product_name"),
                                        jo.getString("variance"),
                                        jo.getString("price"),
                                        jo.getString("location"),
                                        jo.getString("phonenumber"));
                        orderId = jo.getString("id");
                        orderInfoList.add(orderInfo);
                        ordersswiperefreshlayout.setRefreshing(false);
                        ordersAdapter = new OrdersAdapter(ViewPendingOrders.this, orderInfoList);
                        ordersrecyclerView.setAdapter(ordersAdapter);
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(ViewPendingOrders.this, "Nothing Found "+e, Toast.LENGTH_SHORT).show();
                    Log.e("Nothing",e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                ordersswiperefreshlayout.setRefreshing(false);
                Toast.makeText(ViewPendingOrders.this, ""+error, Toast.LENGTH_SHORT).show();
                Log.e("RES", String.valueOf(error));
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);

//  AppController.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
