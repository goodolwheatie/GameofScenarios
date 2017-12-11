package com.example.memeinnovations.gameofscenarios.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memeinnovations.gameofscenarios.data.FirebaseDB;
import com.example.memeinnovations.gameofscenarios.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {

    private boolean anonymousUser = false;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // get if anonymous has logged in or not
        Bundle bundles = getIntent().getExtras();
        if (bundles != null) {
            anonymousUser = bundles.getBoolean("anonymousUser");
        }

        // initialize sidebar list
        mDrawerLayout = (DrawerLayout)findViewById(R.id.mainMenu);
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.mainMenu));
    }

    private void addDrawerItems() {
        String[] osArray = {"",""};
        if(anonymousUser){
            osArray[0] = getString(R.string.register_now);
            osArray[1] = getString(R.string.delete_temp_profile);
        }else{
            osArray[0] = getString(R.string.profile);
            osArray[1] = getString(R.string.sign_out);
        }
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch((int) id){
                    case 0:
                    {
                        profile(findViewById(R.id.mainMenu));
                        break;
                    }
                    case 1:
                    {
                        signOut(findViewById(R.id.mainMenu));
                        break;
                    }
                }
            }
        });

    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void play(View view) {
        Intent openLoading = new Intent(MainMenuActivity.this, LoadingActivity.class);
        startActivity(openLoading);
    }

    public void rules(View view) {
        Intent openRules = new Intent(MainMenuActivity.this, RulesActivity.class);
        startActivity(openRules);
    }

    public void profile(View view) {
        Intent openActivity;
        if (anonymousUser) {
            // open register for anonymous users
            openActivity = new Intent(MainMenuActivity.this, RegisterActivity.class);
        } else {
            // open profile for already logged in users
            openActivity = new Intent(MainMenuActivity.this, ProfileActivity.class);
        }
        startActivity(openActivity);
    }

    public void signOut(View view) {
        if (anonymousUser) {
            final FirebaseUser anonUser = FirebaseDB.mAuth.getCurrentUser();
            final String uid = anonUser.getUid();
            FirebaseDB.mDatabase.child("users").child(uid).
                    removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    anonUser.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("ANONYMOUS", "User account deleted.");
                                        finish();
                                    }
                                }
                            });
                }
            });
        }
        else {
            FirebaseAuth.getInstance().signOut();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing on back pressed
    }
}
