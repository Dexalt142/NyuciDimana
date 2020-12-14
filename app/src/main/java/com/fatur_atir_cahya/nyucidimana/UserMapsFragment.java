package com.fatur_atir_cahya.nyucidimana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.Laundromat;
import com.fatur_atir_cahya.nyucidimana.api.service.LaundromatInterface;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMapsFragment extends Fragment {

    private static final int REQUEST_LOCATION_PERMISSION = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng currentLoc;

    SessionManager sessionManager;
    LaundromatInterface laundromatInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        sessionManager = new SessionManager(getActivity());
        laundromatInterface = ApiClient.getApiClient().create(LaundromatInterface.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.user_map);
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 16));

                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                googleMap.setMyLocationEnabled(true);
                            }

                            Call<JsonObject> callLaundromat = laundromatInterface.getNearbyLaundromat("Bearer " + sessionManager.getToken(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                            callLaundromat.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if(response.code() == 200) {
                                        JsonArray laundromats = response.body().getAsJsonArray("data");
                                        drawLaundromatMarker(googleMap, laundromats);
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_LOCATION_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    private ArrayList<Laundromat> getLaundromatFromJsonArray(JsonArray laundromatArray) {
        ArrayList<Laundromat> laundromatList = new ArrayList<>();
        for(int i = 0; i < laundromatArray.size(); i++) {
            JsonObject el = laundromatArray.get(i).getAsJsonObject();
            Laundromat laundromat = new Laundromat(el);
            laundromatList.add(laundromat);
        }

        return laundromatList;
    }

    private void drawLaundromatMarker(GoogleMap map, JsonArray laundromatArray) {
        ArrayList<Laundromat> laundromatList = getLaundromatFromJsonArray(laundromatArray);

        for(Laundromat laundromat : laundromatList) {
            LatLng latLng = new LatLng(Double.valueOf(laundromat.getLatitude()), Double.valueOf(laundromat.getLongitude()));
            MarkerOptions options = new MarkerOptions().position(latLng).title(laundromat.getName()).snippet(laundromat.getAddress());
            map.addMarker(options);
        }
    }
}