package alien.com.vieworders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import alien.com.model.OrderInfo;
import alien.com.pishongroceries.R;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>
{
    public Context context;
    public List<OrderInfo> orderInfos;
    public OrdersAdapter(Context context,List<OrderInfo> orderInfos)
    {
        this.context = context;
        this.orderInfos = orderInfos;
    }

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

            }
        });


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
