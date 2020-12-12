package com.fatur_atir_cahya.nyucidimana;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fatur_atir_cahya.nyucidimana.database.LaundromatManager;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;

public class OwnerHomeFragment extends Fragment {

    SessionManager sessionManager;
    LaundromatManager laundromatManager;
    TextView laundromatName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getActivity());
        laundromatManager = new LaundromatManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_owner_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        laundromatName = view.findViewById(R.id.home_laundromat_name);
        laundromatName.setText(laundromatManager.getName());
    }
}