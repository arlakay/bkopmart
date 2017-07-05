package id.co.octolink.ilm.bkopmart.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.ui.BaseActivity;
import id.co.octolink.ilm.bkopmart.ui.cart.CartActivity;
import id.co.octolink.ilm.bkopmart.ui.favorite.BottomTabFavoriteFragment;
import id.co.octolink.ilm.bkopmart.ui.home.BottomTabHomeFragment;
import id.co.octolink.ilm.bkopmart.ui.profile.BottomTabProfileFragment;
import id.co.octolink.ilm.bkopmart.ui.transaction.BottomTabTransaksiFragment;
import id.co.octolink.ilm.bkopmart.utils.SessionManager;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    @BindView(R.id.toolbar)Toolbar toolbar;

    public static final String CURRENT_TAB = "CURRENT_TAB";
    private TabHost mTabHost;
    private String mCurrentTab;

    public static final String FIRST_TAB = "FIRST_TAB";
    public static final String SECOND_TAB = "SECOND_TAB";
    public static final String THIRD_TAB = "THIRD_TAB";
    public static final String FOURTH_TAB = "FOURTH_TAB";

    private SessionManager sessionManager;
    private ArrayAdapter<CharSequence> adapterOutlet;
    private String valueOutlet;
    private Spinner spinOutlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        if (!sessionManager.isLoggedIn()) {
            sessionManager.setLogin(false);
            sessionManager.logoutUser();
            finish();
        }

        HashMap<String, String> user = sessionManager.getLoginDetails();
        String name = user.get(SessionManager.KEY_CUSTOMER_NAME);
        String email = user.get(SessionManager.KEY_EMAIL);
        String pic_url = user.get(SessionManager.KEY_AVATAR);

        setupToolbar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        ImageView imgHeader =  (ImageView)header.findViewById(R.id.img_header);
        TextView txtName = (TextView)header.findViewById(R.id.txt_header_name);
        spinOutlet = (Spinner) header.findViewById(R.id.spin_header_outlet_location);

        setupSpinner();

        mTabHost = (TabHost) findViewById(R.id.content_main);

        mTabHost.setup();

        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getString(CURRENT_TAB);
            initializeTabs();
            mTabHost.setCurrentTabByTag(mCurrentTab);
            mTabHost.setOnTabChangedListener(listener);
        } else {
            mTabHost.setOnTabChangedListener(listener);
            initializeTabs();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getSupportActionBar() == null) {
            throw new IllegalStateException("Activity must implement toolbar");
        }
    }

    private void setupSpinner() {
        adapterOutlet = ArrayAdapter.createFromResource(this, R.array.outlet_location,
                android.R.layout.simple_spinner_item);
        adapterOutlet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinOutlet.setAdapter(adapterOutlet);
        spinOutlet.setOnItemSelectedListener(this);
    }

    private View createTabView(final int id, final String text) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_icon, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, id));
        TextView textView = (TextView) view.findViewById(R.id.tab_text);
        textView.setText(text);
        return view;
    }

    /*
    create 3 tabs with name and image
    and add it to TabHost
     */
    public void initializeTabs() {

        TabHost.TabSpec spec;

        spec = mTabHost.newTabSpec(SECOND_TAB);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.activity_main_real_tab_content);
            }
        });
        spec.setIndicator(createTabView(R.drawable.tab_home_selector, "Home"));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(THIRD_TAB);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.activity_main_real_tab_content);
            }
        });
        spec.setIndicator(createTabView(R.drawable.tab_favorite_selector, "Favourite"));
        mTabHost.addTab(spec);


        spec = mTabHost.newTabSpec(FIRST_TAB);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.activity_main_real_tab_content);
            }
        });
        spec.setIndicator(createTabView(R.drawable.tab_transaksi_selector, "Transaksi"));
        mTabHost.addTab(spec);


        spec = mTabHost.newTabSpec(FOURTH_TAB);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.activity_main_real_tab_content);
            }
        });
        spec.setIndicator(createTabView(R.drawable.tab_profile_selector, "Profile"));
        mTabHost.addTab(spec);



    }

    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        public void onTabChanged(String tabId) {

            mCurrentTab = tabId;

            if (tabId.equals(FIRST_TAB)) {
                pushFragments(BottomTabTransaksiFragment.newInstance(), false,
                        false, null);
            } else if (tabId.equals(SECOND_TAB)) {
                pushFragments(BottomTabHomeFragment.newInstance(), false,
                        false, null);
            }else if (tabId.equals(THIRD_TAB)) {
                pushFragments(BottomTabFavoriteFragment.newInstance(), false,
                        false, null);
            }else if (tabId.equals(FOURTH_TAB)) {
                pushFragments(BottomTabProfileFragment.newInstance(), false,
                        false, null);
            }

        }
    };

    /*
    Example of starting nested fragment from another fragment:

    Fragment newFragment = ManagerTagFragment.newInstance(tag.getMac());
                    TagsActivity tAct = (TagsActivity)getActivity();
                    tAct.pushFragments(newFragment, true, true, null);
     */
    public void pushFragments(Fragment fragment,
                              boolean shouldAnimate, boolean shouldAdd, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
//        if (shouldAnimate) {
//            ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
//                    R.animator.fragment_slide_left_exit,
//                    R.animator.fragment_slide_right_enter,
//                    R.animator.fragment_slide_right_exit);
//        }
        ft.replace(R.id.activity_main_real_tab_content, fragment, tag);

        if (shouldAdd) {
            /*
            here you can create named backstack for realize another logic.
            ft.addToBackStack("name of your backstack");
             */
            ft.addToBackStack(null);
        } else {
            /*
            and remove named backstack:
            manager.popBackStack("name of your backstack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            or remove whole:
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
             */
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.commit();
    }

    //If you want to start this activity from another
    public static void startUrself(Activity context) {
        Intent newActivity = new Intent(context, MainActivity.class);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newActivity);
        context.finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_TAB, mCurrentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent i = new Intent(this, CartActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spin_outlet_location:
                valueOutlet = parent.getSelectedItem().toString();
                Log.e("TAG", "IN : " + valueOutlet);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
