package alien.com.category;

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
import alien.com.model.Category;
import alien.com.pishongroceries.R;
import alien.com.volley.AppController;

public class ViewCategory extends AppCompatActivity
{
     SwipeRefreshLayout swipeRefreshLayout;
     RecyclerView recyclerView;
     List<Category> categoryList;
     CategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("View Categories");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.customerInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        categoryList = new ArrayList<>();
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
        adapter = new CategoryAdapter(getApplicationContext(),categoryList);
        adapter.clearAdapter();
        getData();
    }
    public void getData()
    {
        swipeRefreshLayout.setRefreshing(true);
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

                        swipeRefreshLayout.setRefreshing(false);
                        adapter = new CategoryAdapter(ViewCategory.this, categoryList);
                        recyclerView.setAdapter(adapter);
                    }

                }


                catch (JSONException e) {
                    Toast.makeText(ViewCategory.this, "Nothing Found "+e, Toast.LENGTH_SHORT).show();
                    Log.e("Nothing",e.toString());
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                swipeRefreshLayout.setRefreshing(false);
                   Toast.makeText(ViewCategory.this, ""+error, Toast.LENGTH_SHORT).show();
                Log.e("RES", String.valueOf(error));
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
        //MySingleTone.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
