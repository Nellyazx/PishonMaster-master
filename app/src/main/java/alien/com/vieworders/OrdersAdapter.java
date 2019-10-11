package alien.com.vieworders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alien.com.api.ApiService;
import alien.com.dashboard.Dashboard;
import alien.com.model.OrderInfo;
import alien.com.pishongroceries.R;
import alien.com.viewcustomers.UpdateUserInfo;
import alien.com.volley.AppController;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>
{
    public Context context;
    public List<OrderInfo> orderInfos;
    public OrdersAdapter(Context context,List<OrderInfo> orderInfos)
    {
        this.context = context;
        this.orderInfos = orderInfos;
    }
    public String orderId;
    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.order,viewGroup,false);
        OrdersViewHolder ordersViewHolder =  new OrdersViewHolder(view);
                return ordersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder ordersViewHolder, int position)
    {

        final Context context = ordersViewHolder.itemView.getContext();
        ordersViewHolder.ordercustname.setText(orderInfos.get(position).getOrdercustname());
        ordersViewHolder.orderitem.setText(orderInfos.get(position).getOrderitem());
        ordersViewHolder.orderquantity.setText(orderInfos.get(position).getOrderquantity());
        ordersViewHolder.orderprice.setText(orderInfos.get(position).getOrderprice());
        ordersViewHolder.customerlocation.setText(orderInfos.get(position).getCustomerlocation());
        ordersViewHolder.customerphone.setText(orderInfos.get(position).getCustomerphonenumber());

        ordersViewHolder.deliver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context, " Order Id" +ViewPendingOrders.orderId, Toast.LENGTH_SHORT).show();
                StringRequest deliverOrder = new StringRequest(Request.Method.POST, ApiService.MOVE_TO_DELIVER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, Dashboard.class));
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "SomeErr"+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("orderid",orderId);
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(deliverOrder);
                //MySingleTone.getInstance(getApplicationContext()).addToRequestQueue(updateRequest);

            }
        });


    }
    public String  getId()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiService.VIEW_ALL_PENDING_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray ja=new JSONArray(response);
                    JSONObject jo=null;
                    for(int i=0;i<ja.length();i++)
                    {
                        jo=ja.getJSONObject(i);
                        orderId = jo.getString("username");
                        //Toast.makeText(context, "Order Id "+orderId, Toast.LENGTH_SHORT).show();
                    }
                }

                catch (JSONException e) {
                    Toast.makeText(context, "Nothing Found "+e, Toast.LENGTH_SHORT).show();
                    Log.e("Nothing",e.toString());
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                Log.e("RES", String.valueOf(error));
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    return orderId;
    }
    @Override
    public int getItemCount()
    {
        if (orderInfos != null)
        {
            return orderInfos.size();
        }
        return 0;

    }
    public void clearAdapter()
    {
        orderInfos.clear();
        notifyDataSetChanged();
    }
    public static class OrdersViewHolder extends RecyclerView.ViewHolder
    {
        public CardView ordercardview;
        public TextView ordercustname;
        public TextView orderitem;
        public TextView orderquantity;
        public TextView orderprice;
        public TextView customerlocation;
        public TextView customerphone;
        public Button deliver;
        public OrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ordercardview = itemView.findViewById(R.id.ordercardView);
            ordercustname = itemView.findViewById(R.id.ordercustName);
            orderitem = itemView.findViewById(R.id.orderitem);
            orderquantity = itemView.findViewById(R.id.orderquantity);
            orderprice = itemView.findViewById(R.id.orderprice);
            customerlocation = itemView.findViewById(R.id.Location);
            customerphone = itemView.findViewById(R.id.phonenumber);

            deliver = itemView.findViewById(R.id.deliver);

        }
    }

}
