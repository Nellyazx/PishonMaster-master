package alien.com.viewcustomers;

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

import java.util.List;

import alien.com.model.UserInfo;
import alien.com.pishongroceries.R;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomersViewHolder>
{
    public Context context;
    public List<UserInfo> userinfo;

    public CustomersAdapter(Context context,List<UserInfo> userinfo)
    {
        this.context = context;
        this.userinfo = userinfo;
    }


    @NonNull
    @Override
    public CustomersAdapter.CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customuser,viewGroup,false);
        CustomersViewHolder customersViewHolder = new CustomersViewHolder(view);
        return customersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersAdapter.CustomersViewHolder customersViewHolder, final int position)
    {
        final Context context = customersViewHolder.itemView.getContext();
        customersViewHolder.custname.setText(userinfo.get(position).getName());
        customersViewHolder.custemail.setText(userinfo.get(position).getEmail());
        customersViewHolder.custphone.setText(userinfo.get(position).getPhone());
        customersViewHolder.custlocation.setText(userinfo.get(position).getLocation());
        customersViewHolder.edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Log.e("Phone",userinfo.get(position).getPhone());
                Intent intent = new Intent(context,UpdateUserInfo.class);
                intent.putExtra("personname",userinfo.get(position).getName());
                intent.putExtra("personemail",userinfo.get(position).getEmail());
                intent.putExtra("personphone",userinfo.get(position).getPhone());
                intent.putExtra("personlocation",userinfo.get(position).getLocation());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount()
    {

        if (userinfo != null)
        {
            return userinfo.size();
        }
        return 0;
    }
    public void clearAdapter()
    {
        userinfo.clear();
        notifyDataSetChanged();
    }
    public static class CustomersViewHolder extends RecyclerView.ViewHolder
    {
        public CardView cardview;
        public TextView custname;
        public TextView custemail;
        public TextView custphone;
        public TextView custlocation;
        public Button edit;
        public CustomersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            cardview =  itemView.findViewById(R.id.cardView);
            custname = itemView.findViewById(R.id.custName);
            custemail = itemView.findViewById(R.id.custEmail);
            custphone = itemView.findViewById(R.id.phonenumber);
            custlocation = itemView.findViewById(R.id.location);
            edit = itemView.findViewById(R.id.edit);

        }
    }
}
