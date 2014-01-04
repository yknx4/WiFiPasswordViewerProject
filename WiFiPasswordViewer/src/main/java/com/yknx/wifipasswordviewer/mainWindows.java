package com.yknx.wifipasswordviewer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class mainWindows extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private wifiNetworkAdapter mWifiAdapter;

    private ArrayList<wifiNetwork> networks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();



        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (!ExecuteAsRootBase.canRunRootCommands()){
            Toast.makeText(this.getApplicationContext(),"You need root access to use this application.",Toast.LENGTH_LONG).show();
            moveTaskToBack(true);

        }else{
            java.lang.String[] cmds = {"cat /data/misc/wifi/wpa_supplicant.conf"};
            networks = new ArrayList<wifiNetwork>();
            try {
                runAsRoot(cmds);
                parseOutput();
                mWifiAdapter = new wifiNetworkAdapter(this,networks);
                /*FIX HERE*/
                setContentView(R.layout.fragment_wifikeys);
                ListView lvNets = (ListView) findViewById(R.id.listView_wifikeys);
                if(null != lvNets)
                lvNets.setAdapter(mWifiAdapter);
                /*!FIX HERE*/

            } catch (Exception e) {
                Log.w("ROOT", "Can't get wifi networks", e);
                Toast.makeText(this.getBaseContext(),"Something failed loading wifi networks.\nSend Logcat to developer.",Toast.LENGTH_SHORT).show();
                moveTaskToBack(true);
            }
        }
    }

    private void parseOutput() {
        networks.add(new wifiNetwork("Almenos1","test",wifiNetwork.security.wep));
        result = result.replace('$',' ');
        result = result.replace("network=","$");
        String[] nets = result.split("$");
        for(String cNet : nets){
            networks.add(new wifiNetwork(cNet,"test",wifiNetwork.security.wep));
        }
    }


    private int currentFragment=0;
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = position;
        switch (position){
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, WifiKeysFragment.newInstance())
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AboutFragment.newInstance())
                        .commit();
                break;

            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;

        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            MenuInflater mInflater;
            mInflater = getMenuInflater();
            mInflater.inflate(R.menu.main_windows, menu);

            restoreActionBar();
            if(currentFragment!=0){
                menu.findItem(R.id.action_copytoclipboard).setVisible(false);
            }else {menu.findItem(R.id.action_copytoclipboard).setVisible(true);}


            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((mainWindows) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    /**
     * A fragment containing Wifi Keys.
     */
    public static class WifiKeysFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
       // private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static WifiKeysFragment newInstance() {
            WifiKeysFragment fragment = new WifiKeysFragment();
            Bundle args = new Bundle();
            //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public WifiKeysFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_wifikeys, container, false);
           // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
          //  textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((mainWindows) activity).onSectionAttached(1);
        }
    }
    public static class AboutFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        // private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AboutFragment newInstance() {
            AboutFragment fragment = new AboutFragment();
            Bundle args = new Bundle();
            //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public AboutFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_about, container, false);
            // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //  textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((mainWindows) activity).onSectionAttached(3);
        }
    }

    private String result;

    public void runAsRoot(String[] cmds) throws Exception {
        Process p = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        InputStream is = p.getInputStream();
        for (String tmpCmd : cmds) {
            os.writeBytes(tmpCmd+"\n");
            int readed = 0;
            byte[] buff = new byte[4096];
            boolean cmdRequiresAnOutput = true;
            if (cmdRequiresAnOutput) {
                while( is.available() <= 0) {
                    try { Thread.sleep(5000); } catch(Exception ex) {}
                }

                while( is.available() > 0) {
                    readed = is.read(buff);
                    if ( readed <= 0 ) break;
                    String seg = new String(buff,0,readed);
                    result=seg; //result is a string to show in textview
                }
            }
        }
        os.writeBytes("exit\n");
        os.flush();
}}
