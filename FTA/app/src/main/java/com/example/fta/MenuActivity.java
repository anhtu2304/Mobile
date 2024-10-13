package com.example.fta;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fta.Fragment.HomeFragment;
import com.example.fta.Fragment.NotificationFragment;
import com.example.fta.Fragment.ProfileFragment;
import com.example.fta.Fragment.ShoppingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNav;
    private ImageView img_avatar;
    private TextView tv_name, tv_email;
    final private ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        onControl();
        addEvent();

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        loadFragment(new HomeFragment());
        showUserInfomation();
    }

    private void onControl() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        bottomNav = findViewById(R.id.bottom_nav);
        img_avatar = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        tv_name = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        tv_email = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private void loadFragment(Fragment fmNew){
        FragmentTransaction fmTran = getSupportFragmentManager().beginTransaction();
        fmTran.replace(R.id.main_frame,fmNew);
        fmTran.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fmNew;
        if (menuItem.getItemId() == R.id.nav_login){
            Intent i = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(i);
            return true;
        }

        if (menuItem.getItemId() == R.id.nav_user){
            fmNew= profileFragment;
            loadFragment(fmNew);
            toolbar.setTitle("Thông tin người dùng");
            return true;
        }

        if (menuItem.getItemId() == R.id.nav_cart){
            Intent i = new Intent(this, CartActivity.class);
            startActivity(i);
            return true;
        }

        if (menuItem.getItemId() == R.id.nav_logout){
            new AlertDialog.Builder(this).setTitle("Đăng xuất ứng dụng").setMessage("Bạn chắc chắn muốn thoát?").setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(MenuActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Hủy", null).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addEvent() {
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fmNew;
                if(item.getItemId()==R.id.nav_home){
                    fmNew= new HomeFragment();
                    loadFragment(fmNew);
                    toolbar.setTitle("Trang chủ");
                    return true;
                }

                if(item.getItemId()==R.id.nav_shop){
                    fmNew= new ShoppingFragment();
                    loadFragment(fmNew);
                    toolbar.setTitle("Sản phẩm");
                    return true;
                }

                if(item.getItemId()==R.id.nav_bell){
                    fmNew= new NotificationFragment();
                    loadFragment(fmNew);
                    toolbar.setTitle("Thông báo");
                    return true;
                }

                return true;
            }
        });
    }
    public void showUserInfomation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Menu menu = navigationView.getMenu();

        if (user == null){
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_user).setVisible(false);
            menu.findItem(R.id.nav_cart).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(false);
            return;
        }
        menu.findItem(R.id.nav_login).setVisible(false);
        menu.findItem(R.id.nav_user).setVisible(true);
        menu.findItem(R.id.nav_cart).setVisible(true);
        menu.findItem(R.id.nav_logout).setVisible(true);
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if (name == null){
            tv_name.setVisibility(View.GONE);
        }
        else {
            tv_name.setVisibility(View.VISIBLE);
            tv_name.setText(name);
        }
        tv_email.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(img_avatar);
    }
}