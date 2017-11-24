package com.example.hp.chatlive;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.sinch.android.rtc.SinchError;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import io.fabric.sdk.android.Fabric;

public class ResideV extends BaseActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private Context mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemAddF;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemCom;
    private ResideMenuItem itemLog;
    private ResideMenuItem itemAbout;
    private ProgressDialog mSpinner;
    TextView ActT;
    Dialog dialogg;
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.zx);
        Fabric.with(this, new Crashlytics());
        ActT=(TextView)findViewById(R.id.actiontitle);
        mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(new KnownUsers2());

    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);

        resideMenu.setBackground(R.drawable.aaa);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "Home");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "Profile");
        itemAddF  = new ResideMenuItem(this, R.drawable.icaddf,  "Add Friend");
        itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "What's next!");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");
        itemCom = new ResideMenuItem(this, R.drawable.commicon, "Frost's Voice");
        itemLog = new ResideMenuItem(this, R.drawable.logouticon, "Logout");
        itemAbout = new ResideMenuItem(this, R.drawable.abouticon, "About");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemAddF.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemCom.setOnClickListener(this);
        itemLog.setOnClickListener(this);
        itemAbout.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAddF, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCom, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLog, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
          resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }
//
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
    @Override
    public void onClick(View view) {

        if (view == itemHome){
            ActT.setText("Home");
            //changeFragment(new Users());
            changeFragment(new KnownUsers2());
        }else if (view == itemProfile){
            ActT.setText("Profile");
            changeFragment(new ProfileFrag());
           // changeFragment(new ProfileFragment());
        }else if (view == itemCalendar){
            ActT.setText("Know Users");
            changeFragment(new KnowUsers());
           // changeFragment(new CalendarFragment());
        }else if (view == itemSettings){
            //ActT.setText("Test");
            //changeFragment(new KnownUsers2());
            //changeFragment(new Users());
          //  changeFragment(new SettingsFragment());
            Intent itt= new Intent (ResideV.this,Settingg.class);
            startActivity(itt);
        }else if (view == itemCom){
            //Intent itt= new Intent (ResideV.this,Frostvoice.class);
            Intent itt= new Intent (ResideV.this,FrostVoice3.class);
            startActivity(itt);
            //  changeFragment(new SettingsFragment());
        }else if (view == itemLog){
            String yes="yes";
            Intent ittt= new Intent (ResideV.this,Login.class);
            ittt.putExtra("Logout",yes);
            startActivity(ittt);
            finish();
            //  changeFragment(new SettingsFragment());
        }else if (view == itemAbout){
            ActT.setText("About");
            //  changeFragment(new SettingsFragment());
            changeFragment(new BlankFragment());
        }
        else if (view == itemAddF){
            Intent intt=new Intent(ResideV.this,ScanF.class);
            startActivity(intt);
            // changeFragment(new ProfileFragment());
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            /*Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
            View decorView = getWindow().getDecorView();
// Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);*/

            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(R.color.pc));
            }
        }

        @Override
        public void closeMenu() {
          /*  Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
            View decorView = getWindow().getDecorView();
// Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);*/
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }

    @Override
    public void onBackPressed() {
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
    }
}

