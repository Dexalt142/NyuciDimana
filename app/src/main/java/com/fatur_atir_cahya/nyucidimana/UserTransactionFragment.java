package com.fatur_atir_cahya.nyucidimana;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserTransactionFragment extends Fragment {

    public UserTransactionFragment() {

    }

    public static UserTransactionFragment newInstance(String param1, String param2) {
        UserTransactionFragment fragment = new UserTransactionFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_transaction, container, false);
    }
}