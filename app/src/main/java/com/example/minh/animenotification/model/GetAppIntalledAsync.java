package com.example.minh.animenotification.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.minh.animenotification.adapter.AppAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetAppIntalledAsync extends AsyncTask<Void, Void, ArrayList<App>> {
    private ArrayList<App> mApps;
    private AppAdapter mAdapter;
    private Context mContext;

    public GetAppIntalledAsync(Context context,ArrayList<App> apps, AppAdapter adapter) {
        mAdapter = adapter;
        mApps = apps;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<App> doInBackground(Void... voids) {
        return getAllApp();
    }

    @Override
    protected void onPostExecute(ArrayList<App> apps) {
        super.onPostExecute(apps);
        mApps.addAll(apps);
        mAdapter.notifyDataSetChanged();    }

    private ArrayList<App> getAllApp() {
        ArrayList<App> apps = new ArrayList<>();
        SharedPreferences sharedPreferences = null;
        sharedPreferences = mContext.getSharedPreferences("ANIME_NOTIFY", Context.MODE_PRIVATE);
        PackageManager packageManager = mContext.getPackageManager();
        List<ApplicationInfo> applist = packageManager.getInstalledApplications(PackageManager.PERMISSION_GRANTED);
        Iterator<ApplicationInfo> it = applist.iterator();
        while (it.hasNext()) {

            ApplicationInfo pk = (ApplicationInfo) it.next();

            if ((pk.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                // updated system apps

            } else if ((pk.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                // system apps

            } else {
                // user installed apps
                String appname = packageManager.getApplicationLabel(pk).toString();
                String pkg = pk.packageName;//your package name
                Drawable icon = null;
                try {
                    icon = mContext.getPackageManager().getApplicationIcon(pkg);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                boolean isChoose = sharedPreferences.getBoolean(pkg, false);
                App app = new App(appname, icon, isChoose, pkg);
                apps.add(app);
            }
        }
        return apps;
    }
}
