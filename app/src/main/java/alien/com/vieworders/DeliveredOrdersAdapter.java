package alien.com.vieworders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alien.com.model.DeliveredInfo;
import alien.com.model.OrderInfo;
import alien.com.pishongroceries.R;

public class DeliveredOrdersAdapter extends RecyclerView.Adapter<DeliveredOrdersAdapter.DeliveredOrdersViewHolder>
{

        public Context context;
        public List<DeliveredInfo> deliveredInfo;
        public DeliveredOrdersAdapter(Context context, List< DeliveredInfo > deliveredInfo)
        {
            this.context = context;
            this.deliveredInfo = deliveredInfo;
        }

        @NonNull
        @Override
        public DeliveredOrdersAdapter.DeliveredOrdersViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup,int i)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.deliveredorders, viewGroup, false);
            DeliveredOrdersAdapter.DeliveredOrdersViewHolder deliveredOrdersViewHolder = new DeliveredOrdersAdapter.DeliveredOrdersViewHolder(view);
            return deliveredOrdersViewHolder;
        }
        @Override
        public void onBindViewHolder (@NonNull DeliveredOrdersAdapter.DeliveredOrdersViewHolder deliveredOrdersViewHolder,
        int position)
        {
            final Context context = deliveredOrdersViewHolder.itemView.getContext();
            deliveredOrdersViewHolder.deliveredcustname.setText(deliveredInfo.get(position).getOrdercustname());
            deliveredOrdersViewHolder.delivereditem.setText(deliveredInfo.get(position).getOrderitem());
            deliveredOrdersViewHolder.deliveredquantity.setText(deliveredInfo.get(position).getOrderquantity());
            deliveredOrdersViewHolder.deliveredprice.setText(deliveredInfo.get(position).getOrderprice());
            deliveredOrdersViewHolder.deliveredcustomerlocation.setText(deliveredInfo.get(position).getCustomerlocation());
            deliveredOrdersViewHolder.deliveredcustomerphone.setText(deliveredInfo.get(position).getCustomerphonenumber());
        }

        @Override
        public int getItemCount ()
        {
            if (deliveredInfo != null) {
                return deliveredInfo.size();
            }
            return 0;

        }
        public void clearAdapter ()
        {
            deliveredInfo.clear();
            notifyDataSetChanged();
        }
        public static class DeliveredOrdersViewHolder extends RecyclerView.ViewHolder {
            public CardView deliveredcardview;
            public TextView deliveredcustname;
            public TextView delivereditem;
            public TextView deliveredquantity;
            public TextView deliveredprice;
            public TextView deliveredcustomerlocation;
            public TextView deliveredcustomerphone;

            public DeliveredOrdersViewHolder(@NonNull View itemView) {
                super(itemView);
                deliveredcardview = itemView.findViewById(R.id.deliveredcardView);
                deliveredcustname = itemView.findViewById(R.id.deliveredcustName);
                delivereditem = itemView.findViewById(R.id.delivereditem);
                deliveredquantity = itemView.findViewById(R.id.deliveredquantity);
                deliveredprice = itemView.findViewById(R.id.deliveredprice);
                deliveredcustomerlocation = itemView.findViewById(R.id.deliveredLocation);
                deliveredcustomerphone = itemView.findViewById(R.id.deliveredphonenumber);
            }
        }

}