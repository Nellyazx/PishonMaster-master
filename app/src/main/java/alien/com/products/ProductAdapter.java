package alien.com.products;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alien.com.api.ApiService;
import alien.com.model.Product;
import alien.com.pishongroceries.R;
import alien.com.utils.GlideClient;
import alien.com.volley.AppController;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CategoryViewHolder>
{
    ProgressDialog progressDialog;

    //public Context context;
    public String id;
    public List<Product> productInfo;
    Context context;
    public ProductAdapter(Context context, List<Product> productInfo)
    {
        this.context = context;
        this.productInfo = productInfo;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product,viewGroup,false);
        ProductAdapter.CategoryViewHolder categoryViewHolder = new ProductAdapter.CategoryViewHolder(view);
        return categoryViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder productViewHolder, final int position)
    {
        //final Context context = productViewHolder.itemView.getContext();
        productViewHolder.productName.setText(productInfo.get(position).getName());
        //getImageUsingGlide
        GlideClient.downloadImage(context,productInfo.get(position).getImageurl(),productViewHolder.productImage);

        productViewHolder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context,EditProducts.class);

                intent.putExtra("product_id", productInfo.get(position).getId());
                intent.putExtra("product_name", productInfo.get(position).getName());
                intent.putExtra("product_image", productInfo.get(position).getImageurl());
                intent.putExtra("product_price", productInfo.get(position).getPrice());
                intent.putExtra("product_quantity", productInfo.get(position).getQuantity());
                intent.putExtra("category_id", productInfo.get(position).getCategory());
                context.startActivity(intent);
            }
        });
        productViewHolder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               id = productInfo.get(position).getId();
                progressDialog = ProgressDialog.show(context, "Delete", "Deleting Product..", false, false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.DELETE_PRODUCT, new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                        productInfo.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, productInfo.size());
                        Toast.makeText(context, " Success "+response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, " Error "+error, Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("productid", id);
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
                //MySingleTone.getInstance(context).addToRequestQueue(stringRequest);

            }
        });
        productViewHolder.freezeproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                id = productInfo.get(position).getId();
                progressDialog = ProgressDialog.show(context, "Freeze", "Freezing Product..", false, false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.FREEZE_PRODUCT, new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(context, " Success "+response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, " Error "+error, Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("productid", id);
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
                //MySingleTone.getInstance(context).addToRequestQueue(stringRequest);

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return productInfo.size();
    }
    public void clearAdapter()
    {
        productInfo.clear();
        notifyDataSetChanged();
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView productImage;
        public TextView productName;
        public Button editProduct;
        public Button deleteProduct;
        public Button freezeproduct;
        public CategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            deleteProduct = itemView.findViewById(R.id.deleteproduct);
            editProduct = itemView.findViewById(R.id.editproduct);
            freezeproduct = itemView.findViewById(R.id.freezeproduct);
        }
    }

}
