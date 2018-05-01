package com.mike.moviles.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.mike.moviles.Activities.LoginActivity;
import com.mike.moviles.Activities.SignupActivity;
import com.mike.moviles.Fragments.User.Admin.AddInventoryAdminFragment;
import com.mike.moviles.Fragments.User.Admin.AddProductAdminFragment;
import com.mike.moviles.Fragments.User.All.ShoppingCartFragment;
import com.mike.moviles.Objects.ShoppingCart;
import com.mike.moviles.R;

public class CommonClass {

    public static boolean HandleMenu(Context c, int MenuEntry) {
        Intent actividad;
        Fragment newFragment;
        FragmentTransaction transaction;

        switch (MenuEntry) {
            case R.id.action_cart:
                newFragment = new ShoppingCartFragment();
                transaction = ((AppCompatActivity)c).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.logoutMenuBtn:
                ShoppingCart.getInstance().shoppingCartArray.clear();
                ShoppingCart.getInstance().setShoppingCartTotal(0.0);
                actividad = new Intent(c, LoginActivity.class);
                actividad.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(actividad);
                break;
            case R.id.addProductMenuBtn:
                newFragment = new AddProductAdminFragment();
                transaction = ((AppCompatActivity)c).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout_admin, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.addInventoryMenuBtn:
                newFragment = new AddInventoryAdminFragment();
                transaction = ((AppCompatActivity)c).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout_admin, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;


        }
        return true;
    }
}
