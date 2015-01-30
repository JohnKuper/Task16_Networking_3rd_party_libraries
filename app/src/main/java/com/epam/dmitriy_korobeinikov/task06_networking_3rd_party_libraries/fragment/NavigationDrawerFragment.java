package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.DrawerItem;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountGeneral;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountHelper;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.PreferencesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.IssueContent;

/**
 * Created by Dmitriy Korobeynikov on 12/12/2014.
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 */
public class NavigationDrawerFragment extends BaseFragment {

    public static final String LOG_TAG = NavigationDrawerFragment.class.getSimpleName();
    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "STATE_SELECTED_POSITION";
    private static final String PREF_USER_LEARNED_DRAWER = "PREF_USER_LEARNED_DRAWER";

    private NavigationDrawerCallbacks mCallbacks;

    private ActionBarDrawerToggle mDrawerToggle;
    private AccountHelper mAccountHelper;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private FrameLayout mFragmentContainerView;
    private List<String> mAvailableAccounts;
    private ArrayAdapter<String> mSpinnerAdapter;
    private AccountManager mAccountManager;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        mAccountHelper = new AccountHelper(getActivity());
        mAccountManager = AccountManager.get(getActivity());

        if (!mAccountHelper.isAtLeastOneAccount(AccountGeneral.ACCOUNT_TYPE)) {
            PreferencesUtils.eraseCurrentAccountName(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) v.findViewById(R.id.list_navigation_drawer_items);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mDrawerListView.setAdapter(new DrawerAdapter(getActivity(), R.layout.row_drawer, fillDrawerItems()));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        Spinner accountNamePicker = (Spinner) v.findViewById(R.id.account_username);

        mAvailableAccounts = mAccountHelper.getAvailableAccounts(AccountGeneral.ACCOUNT_TYPE);
        mSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mAvailableAccounts);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountNamePicker.setAdapter(mSpinnerAdapter);

        accountNamePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String accountName = mSpinnerAdapter.getItem(position);
                PreferencesUtils.setCurrentAccountName(getActivity(), accountName);
                getActivity().getContentResolver().notifyChange(IssueContent.ISSUES_URI, null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView addNewAccount = (ImageView) v.findViewById(R.id.account_add_new);
        addNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void addAccount() {
        mAccountManager.addAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS,
                null, null, getActivity(), new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle result;
                            result = future.getResult();
                            String accName = result.getString(AccountManager.KEY_ACCOUNT_NAME);
                            mAvailableAccounts.add(0, accName);
                            mSpinnerAdapter.notifyDataSetChanged();
                            PreferencesUtils.setCurrentAccountName(getActivity(), accName);
                            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Account " + accName + " was successfully created");
                        } catch (OperationCanceledException e) {
                            Log.e(LOG_TAG, "Account creation canceled");
                        } catch (AuthenticatorException e) {
                            Log.e(RepositoriesApplication.APP_NAME, LOG_TAG + "> Authentication error during creating account");
                        } catch (IOException e) {
                            Log.e(RepositoriesApplication.APP_NAME, LOG_TAG + "> Error during creating account");
                        }
                    }
                }, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private ArrayList<DrawerItem> fillDrawerItems() {
        Resources resources = getActivity().getResources();
        String[] drawerTitles = resources.getStringArray(R.array.nav_drawer_titles);
        TypedArray drawerIcons = resources.obtainTypedArray(R.array.nav_drawer_icons);

        ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        for (int i = 0; i < drawerTitles.length; i++) {
            drawerItems.add(new DrawerItem(drawerTitles[i], drawerIcons.getResourceId(i, -1)));
        }

        drawerIcons.recycle();
        return drawerItems;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = (FrameLayout) getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.drawable.ic_drawer
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
            }
        };


        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> onOptionsItemSelected");
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }

    private class DrawerAdapter extends ArrayAdapter<DrawerItem> {

        private ArrayList<DrawerItem> mDrawerItems;
        private int mLayoutResId;


        public DrawerAdapter(Context context, int resource, ArrayList<DrawerItem> objects) {
            super(context, resource, objects);
            this.mDrawerItems = objects;
            this.mLayoutResId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(mLayoutResId, parent, false);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.drawer_item_icon);
                viewHolder.title = (TextView) convertView.findViewById(R.id.drawer_item_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            DrawerItem drawerItem = mDrawerItems.get(position);
            viewHolder.icon.setImageDrawable(convertView.getResources().getDrawable(drawerItem.getIcon()));
            viewHolder.title.setText(drawerItem.getTitle());
            return convertView;
        }

        private class ViewHolder {
            public ImageView icon;
            public TextView title;
        }
    }
}
