package alien.com.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager
{
    private Context context;
    private SharedPreferences sharedPreferences;
    public PreferenceManager(Context context)
    {
        this.context = context;
        getSharedPreference();
    }
    private void getSharedPreference()
    {
        sharedPreferences = context.getSharedPreferences("Slide",Context.MODE_PRIVATE);
    }
    public void writePreference()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LoadSlide","INIT_OK");
        editor.commit();
    }
    public boolean checkPreference()
    {
        boolean status  = false;
        if(sharedPreferences.getString("LoadSlide","null").equals("null"))
        {
            status=false;
        }
        else
        {
            status = true;
        }
        return status;
    }
    public void clearPreference()
    {
        sharedPreferences.edit().clear().commit();
    }
}
