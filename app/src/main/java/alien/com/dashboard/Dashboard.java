package alien.com.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alien.com.category.ViewCategory;
import alien.com.pishongroceries.R;
import alien.com.pishongroceries.RegisterCustomer;
import alien.com.category.AddCategory;
import alien.com.products.AddProducts;
import alien.com.viewcustomers.AllCustomersFragment;
import alien.com.viewcustomers.ViewCustomers;
import alien.com.vieworders.ViewDeliveredOrders;
import alien.com.vieworders.ViewPendingOrders;

public class Dashboard extends AppCompatActivity
{
    RelativeLayout addcustomer,showcustomer,viewpending,viewdelivered,addcategory,addproducts;
    public static final int MULTIPLE_PERMISSIONS = 10;
    int count;
    private String[] permissions= new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        checkPermissions();
        //setSupportActionBar(toolbar);
       // ActionBar actionBar = getSupportActionBar();
        addcustomer = findViewById(R.id.addcustomer);
        showcustomer = findViewById(R.id.showcustomers);
        viewpending = findViewById(R.id.pending);
        viewdelivered = findViewById(R.id.delivered);
        addcategory = findViewById(R.id.category);
        addproducts = findViewById(R.id.products);
        addcustomer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addCustomer();
            }
        });
        showcustomer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                viewCustomers();
            }
        });
        viewpending.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                viewPendingOrders();
            }
        });
        viewdelivered.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                viewDeliveredOrders();
            }
        });
        addcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });
        addproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProducts();
            }
        });
    }
    public void addCustomer()
    {
        startActivity(new Intent(Dashboard.this, RegisterCustomer.class));
    }
    public void viewCustomers() {
        /*android.support.v4.app.FragmentManager fragmentManager = Dashboard.this.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AllCustomersFragment fragment = new AllCustomersFragment();

        fragmentTransaction.add(R.id.reportDrugs, fragment);
        fragmentTransaction.commit();*/
        startActivity(new Intent(Dashboard.this, ViewCustomers.class));
    }

//    public void updateCustomers()
//    {
//        startActivity(new Intent(Dashboard.this, UpdateUserInfo.class));
//    }
    public void viewPendingOrders()
    {
        startActivity(new Intent(Dashboard.this, ViewPendingOrders.class));
    }
    public void viewDeliveredOrders()
    {
        startActivity(new Intent(Dashboard.this, ViewDeliveredOrders.class));
    }
    public void addCategory()
    {
        startActivity(new Intent(Dashboard.this, AddCategory.class));
    }
    public void addProducts()
    {
        startActivity(new Intent(Dashboard.this, AddProducts.class));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (count == 0) {
                Toast.makeText(Dashboard.this, "Click again to exit application ", Toast.LENGTH_SHORT).show();
                count++;
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        return true;
    }
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0 ) {

                    for (String permission : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            closeNow();
                            Toast.makeText(Dashboard.this,"There is a rogue permission"+permission,Toast.LENGTH_LONG).show();
                        }else {

                        }
                    }
                }
                return;
            }
        }
    }
    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

}
