package alien.com.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alien.com.model.Category;
import alien.com.pishongroceries.R;

public class SpinnerCategoriesAdapter extends BaseAdapter {
    Context context;
    public static List<Category> categories;
    LayoutInflater inflater;

    public SpinnerCategoriesAdapter(Context applicationContext, List<Category> categories) {
        this.context = applicationContext;
        this.categories = categories;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Category category = categories.get(position);
        view = inflater.inflate(R.layout.custom_spinner_items, null);
        TextView names = view.findViewById(R.id.textView);
        names.setText(category.getCategoryName());
        return view;
    }

}
