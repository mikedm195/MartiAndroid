package com.mike.moviles.Fragments.User.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mike.moviles.Fragments.User.User.ProductsFragment;
import com.mike.moviles.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mike.moviles.Services.Services.PRODUCTS_API;

public class AddProductAdminFragment extends Fragment {

    private EditText nameTxt, idTxt, photoTxt, videoTxt, categoryTxt, colorTxt, priceText, ageText;
    private Button addBtn;
    ProgressDialog progress_bar;

    public static AddProductAdminFragment newInstance() {
        AddProductAdminFragment fragment = new AddProductAdminFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product_admin, container, false);
        view.setBackgroundResource(R.color.white);

        progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);


        nameTxt = (EditText)view.findViewById(R.id.nameProdAdminAddField);
        photoTxt = (EditText)view.findViewById(R.id.PhotoTextField);
        videoTxt = (EditText)view.findViewById(R.id.VideoTextField);
        categoryTxt = (EditText)view.findViewById(R.id.CategoryTextField);
        colorTxt = (EditText)view.findViewById(R.id.ColoroTextField);
        priceText = (EditText)view.findViewById(R.id.priceProdAdminAddField);
        ageText = (EditText)view.findViewById(R.id.AgeTextField);

        addBtn = (Button) view.findViewById(R.id.addProdAdminAddButton);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInsertProduct();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void confirmInsertProduct() {
        new AlertDialog.Builder(getContext())
                .setTitle("Insert Product?")
                .setMessage("Are you sure you want to create this product?")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        doProductInsert();
                    }
                })
                .create()
                .show();
    }

    void doProductInsert() {

        progress_bar.show();

        StringRequest addProduct = new StringRequest(Request.Method.POST, PRODUCTS_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if(res.getString("product_id").equals("-1"))
                            {
                                Toast.makeText(getContext(), R.string.queryErrorText , Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), "Product Inserted" , Toast.LENGTH_SHORT).show();

                                Fragment newFragment = new ProductsFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout_admin, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(getContext(), "Error: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",nameTxt.getText().toString());
                params.put("photo",photoTxt.getText().toString());
                params.put("video",videoTxt.getText().toString());
                params.put("category_id",categoryTxt.getText().toString());
                params.put("color",colorTxt.getText().toString());
                params.put("price",priceText.getText().toString());
                params.put("age",ageText.getText().toString());

                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(addProduct);
    }
}
