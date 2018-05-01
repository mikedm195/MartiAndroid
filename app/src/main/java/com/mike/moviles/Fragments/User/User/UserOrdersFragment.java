package com.mike.moviles.Fragments.User.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mike.moviles.Adapters.OrdersAdapter;
import com.mike.moviles.Objects.Order;
import com.mike.moviles.Objects.User;
import com.mike.moviles.R;
import com.mike.moviles.Utilities.ParserJSONOrders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mike.moviles.Services.Services.ORDERS_API;


public class UserOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Order> orderList;
    private OrdersAdapter adapter;

    public static UserOrdersFragment newInstance() {
        UserOrdersFragment fragment = new UserOrdersFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_orders, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.orders_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        adapter = new OrdersAdapter(getContext(),orderList);
        recyclerView.setAdapter(adapter);

        final ProgressDialog progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        StringRequest productsReq = new StringRequest(Request.Method.GET, ORDERS_API + "?customer_id=" + User.getInstance().getUserID(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01"))
                            {
                                JSONArray orders = res.getJSONArray("order_data");
                                orderList = ParserJSONOrders.parseaArreglo(orders);

                                adapter = new OrdersAdapter(getContext(), orderList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            } else if (res.getString("code").equals("04"))
                            {
                                Toast.makeText(getContext(), R.string.queryErrorText , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), R.string.unknownResponseText , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(getContext(), R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        Volley.newRequestQueue(getContext()).add(productsReq);

        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.myOrdersMenuText);
    }

    public void onStop () {
        super.onStop();
        getActivity().setTitle(R.string.app_name);
        orderList.clear();
    }

    public void onDestroy () {
        super.onDestroy();
        getActivity().setTitle(R.string.app_name);
        orderList.clear();
    }
}
