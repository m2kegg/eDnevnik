package com.example.ednevnik.ui.navigation;


import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ednevnik.R;
import com.example.ednevnik.databinding.FragmentNavigationBinding;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DataSnapshot;
import com.tomtom.online.sdk.common.Result;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.common.permission.AndroidPermissionChecker;
import com.tomtom.online.sdk.common.permission.PermissionChecker;
import com.tomtom.online.sdk.data.reachablerange.ReachableRangeQuery;
import com.tomtom.online.sdk.data.reachablerange.ReachableRangeResponse;
import com.tomtom.online.sdk.location.FusedLocationSource;
import com.tomtom.online.sdk.location.LocationSource;
import com.tomtom.online.sdk.location.LocationUpdateListener;
import com.tomtom.online.sdk.routing.BatchableRoutingResultListener;
import com.tomtom.online.sdk.routing.MatrixRoutingResultListener;
import com.tomtom.online.sdk.routing.OnlineRoutingApi;
import com.tomtom.online.sdk.routing.ReachableRangeResultListener;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.routing.RoutingException;
import com.tomtom.online.sdk.routing.batch.BatchRoutesCallback;
import com.tomtom.online.sdk.routing.batch.BatchRoutesPlan;
import com.tomtom.online.sdk.routing.batch.BatchRoutesSpecification;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteResponse;
import com.tomtom.online.sdk.routing.data.batch.BatchRoutingQuery;
import com.tomtom.online.sdk.routing.data.batch.BatchRoutingResponse;
import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingQuery;
import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingResponse;
import com.tomtom.online.sdk.routing.ev.EvRouteCallback;
import com.tomtom.online.sdk.routing.ev.EvRoutePlan;
import com.tomtom.online.sdk.routing.ev.EvRouteSpecification;
import com.tomtom.online.sdk.routing.matrix.MatrixRoutesCallback;
import com.tomtom.online.sdk.routing.matrix.MatrixRoutesPlan;
import com.tomtom.online.sdk.routing.matrix.MatrixRoutesSpecification;
import com.tomtom.online.sdk.routing.reachablerange.ReachableAreaCallback;
import com.tomtom.online.sdk.routing.reachablerange.ReachableRangeArea;
import com.tomtom.online.sdk.routing.reachablerange.ReachableRangeSpecification;
import com.tomtom.online.sdk.routing.route.RouteCalculationDescriptor;
import com.tomtom.online.sdk.routing.route.RouteCallback;
import com.tomtom.online.sdk.routing.route.RouteDescriptor;
import com.tomtom.online.sdk.routing.route.RoutePlan;
import com.tomtom.online.sdk.routing.route.RouteSpecification;
import com.tomtom.online.sdk.routing.route.calculation.InstructionsType;
import com.tomtom.online.sdk.routing.route.description.RouteType;
import com.tomtom.online.sdk.routing.route.description.TravelMode;
import com.tomtom.online.sdk.routing.route.diagnostic.ReportType;
import com.tomtom.online.sdk.routing.route.information.FullRoute;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResponse;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class NavigationFragment extends Fragment implements LocationUpdateListener {
    private final Handler searchTimerHandler = new Handler();
    private Runnable searchRunnable;
    private FragmentNavigationBinding binding;
    private List<String> searchAutocompleteList = new ArrayList<>();
    private String search;
    private LatLng curloc;
    private LatLng destination;
    private HashMap<String, LatLng> searchResultsMap = new HashMap<>();
    private SearchApi searchApi;
    private ArrayAdapter<String> searchAdapter;
    private LocationSource locationSource;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NavigationViewModel navigationViewModel = new ViewModelProvider(this).get(NavigationViewModel.class);

        binding = FragmentNavigationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchApi = OnlineSearchApi.create(getContext(), "xPypePUiAnfK3EenqbBxGqeG4sLuF8Jm");
        searchAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, searchAutocompleteList);

        PermissionChecker permissionChecker = AndroidPermissionChecker.createLocationChecker(getContext());
        if(permissionChecker.ifNotAllPermissionGranted()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        locationSource = new FusedLocationSource(getContext(), LocationRequest.create());
        locationSource.addLocationUpdateListener(this);

        AutoCompleteTextView textView = binding.atvMainDepartureLocation;
        textView.setAdapter(searchAdapter);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTimerHandler.removeCallbacks(searchRunnable);
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>3)
                searchRunnable = () -> searchAddress(s.toString(), textView);
                searchAdapter.clear();
                searchTimerHandler.postDelayed(searchRunnable, 600);
            }
        });






        RoutingApi routingApi = OnlineRoutingApi.create(getContext(), "xPypePUiAnfK3EenqbBxGqeG4sLuF8Jm");
        RouteDescriptor routeDescriptor = new RouteDescriptor.Builder().routeType(RouteType.FASTEST).considerTraffic(true).travelMode(TravelMode.CAR).departAt(new Date()).build();
        RouteCalculationDescriptor routeCalculationDescriptor = new RouteCalculationDescriptor.Builder().routeDescription(routeDescriptor).reportType(ReportType.EFFECTIVE_SETTINGS).instructionType(InstructionsType.TEXT).build();
        RouteSpecification routeSpecification = new RouteSpecification.Builder(new LatLng(55.753544, 37.621211), new LatLng(55.743689, 37.681721)).routeCalculationDescriptor(routeCalculationDescriptor).build();

        routingApi.planRoute(routeSpecification, new RouteCallback() {
            @Override
            public void onSuccess(@NonNull RoutePlan routePlan) {
                FullRoute fullRoute = routePlan.getRoutes().get(0);
                Log.i("ROUTEDEP", fullRoute.getSummary().getDepartureTime());
                Log.i("ROUTEARR", fullRoute.getSummary().getArrivalTime());

            }

            @Override
            public void onError(@NonNull RoutingException e) {

            }
        });
        navigationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void searchAddress(final String address, final AutoCompleteTextView autoCompleteTextView){
        searchApi.search(new FuzzySearchQueryBuilder(address)
                .withLanguage(Locale.getDefault().toLanguageTag())
                .withTypeAhead(true)
                .withMinFuzzyLevel(2).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<FuzzySearchResponse>() {
                    @Override
                    public void onSuccess(@NonNull FuzzySearchResponse fuzzySearchResponse) {
                        searchAutocompleteList.clear();
                        for (FuzzySearchResult result : fuzzySearchResponse.getResults()) {
                            String addressString = result.getAddress().getFreeformAddress();
                            searchAutocompleteList.add(addressString);
                            searchResultsMap.put(addressString, result.getPosition());
                        }
                        searchAdapter.clear();
                        searchAdapter.addAll(searchAutocompleteList);
                        searchAdapter.getFilter().filter("");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
                autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                    String item = (String) parent.getItemAtPosition(position);
                    destination = searchResultsMap.get(item);});

    }

    @Override
    public void onLocationChanged(Location location) {
        if (curloc == null) {
            curloc = new LatLng(location);
            locationSource.deactivate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PermissionChecker checker = AndroidPermissionChecker.createLocationChecker(getContext());
        if(!checker.ifNotAllPermissionGranted()) {
            locationSource.activate();
        }
    }
}