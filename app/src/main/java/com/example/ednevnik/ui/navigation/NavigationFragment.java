package com.example.ednevnik.ui.navigation;


import android.Manifest;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ednevnik.POJO.Lesson;
import com.example.ednevnik.R;
import com.example.ednevnik.databinding.FragmentNavigationBinding;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.common.permission.AndroidPermissionChecker;
import com.tomtom.online.sdk.common.permission.PermissionChecker;
import com.tomtom.online.sdk.common.util.DateFormatter;
import com.tomtom.online.sdk.routing.OnlineRoutingApi;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.routing.RoutingException;
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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class NavigationFragment extends Fragment {
    private final Handler searchTimerHandler = new Handler();
    private Runnable searchRunnable;
    private FragmentNavigationBinding binding;
    private List<String> searchAutocompleteList = new ArrayList<>();
    private LatLng destination;
    private HashMap<String, Object> address;
    private HashMap<String, LatLng> searchResultsMap = new HashMap<>();
    private SearchApi searchApi;
    private ArrayAdapter<String> searchAdapter;
    private ArrayList<HashMap<String, Object>> addresses = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NavigationViewModel navigationViewModel = new ViewModelProvider(this).get(NavigationViewModel.class);

        binding = FragmentNavigationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView txt = binding.textView24;
        txt.setVisibility(View.INVISIBLE);

        navigationViewModel.getText().observe(getViewLifecycleOwner(), lessons -> {
            for (Lesson le :
            lessons) {
                addresses.add((HashMap<String, Object>) le.address);
            }

            Button button1 = binding.button7;
            Button button2 = binding.button8;
            RadioGroup radioGroup = binding.radioGroup;

            button1.setOnClickListener(v -> {
                CharSequence[] names = new CharSequence[addresses.size()];
                int i = 0;
                for (HashMap<String, Object> addr:
                     addresses) {
                    names[i] = (CharSequence) addr.get("name");
                    i += 1;
                }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle(R.string.choose_address).setSingleChoiceItems(names, -1, (dialogInterface, i1) -> address = addresses.get(i1)).setPositiveButton(R.string.choose, (dialogInterface, i12) -> {
                    TextView textView = getView().findViewById(R.id.textView22);
                    textView.setText((CharSequence) address.get("name"));
                }).create().show();

            });
            searchApi = OnlineSearchApi.create(getContext(), getString(R.string.apikey));
            searchAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, searchAutocompleteList);

            PermissionChecker permissionChecker = AndroidPermissionChecker.createLocationChecker(getContext());
            if(permissionChecker.ifNotAllPermissionGranted()) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
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
            button2.setOnClickListener(v -> {
                if (!binding.atvMainDepartureLocation.getText().toString().equals("") & !(destination == null)) {
                    RoutingApi routingApi = OnlineRoutingApi.create(getContext(), getString(R.string.apikey));
                    RouteDescriptor routeDescriptor;
                    RouteCalculationDescriptor routeCalculationDescriptor;
                    RouteSpecification routeSpecification;
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.radioButton3:
                            routeDescriptor = new RouteDescriptor.Builder().routeType(RouteType.FASTEST).considerTraffic(true).travelMode(TravelMode.CAR).departAt(new Date()).build();
                            routeCalculationDescriptor = new RouteCalculationDescriptor.Builder().routeDescription(routeDescriptor).reportType(ReportType.EFFECTIVE_SETTINGS).instructionType(InstructionsType.TEXT).build();
                            routeSpecification = new RouteSpecification.Builder(destination, new LatLng((Double) address.get("lat"), (Double) address.get("lng"))).routeCalculationDescriptor(routeCalculationDescriptor).build();


                            routingApi.planRoute(routeSpecification, new RouteCallback() {
                                @Override
                                public void onSuccess(@NonNull RoutePlan routePlan) {
                                    FullRoute fullRoute = routePlan.getRoutes().get(0);
                                    DateFormatter dateFormatter = new DateFormatter();
                                    DateTime dateTime = dateFormatter.formatWithTimeZone(fullRoute.getSummary().getArrivalTime());
                                    txt.setVisibility(View.VISIBLE);
                                    TextView textView1 = getView().findViewById(R.id.textView25);
                                    textView1.setText(dateTime.plusHours(3).toString("HH:mm", Locale.getDefault()));

                                }

                                @Override
                                public void onError(@NonNull RoutingException e) {

                                }
                            });
                            break;
                        case R.id.radioButton1:
                            routeDescriptor = new RouteDescriptor.Builder().routeType(RouteType.FASTEST).considerTraffic(true).travelMode(TravelMode.BUS).departAt(new Date()).build();
                            routeCalculationDescriptor = new RouteCalculationDescriptor.Builder().routeDescription(routeDescriptor).reportType(ReportType.EFFECTIVE_SETTINGS).instructionType(InstructionsType.TEXT).build();
                            routeSpecification = new RouteSpecification.Builder(destination, new LatLng((Double) address.get("lat"), (Double) address.get("lng"))).routeCalculationDescriptor(routeCalculationDescriptor).build();


                            routingApi.planRoute(routeSpecification, new RouteCallback() {
                                @Override
                                public void onSuccess(@NonNull RoutePlan routePlan) {
                                    FullRoute fullRoute = routePlan.getRoutes().get(0);
                                    DateFormatter dateFormatter = new DateFormatter();
                                    DateTime dateTime = dateFormatter.formatWithTimeZone(fullRoute.getSummary().getArrivalTime());
                                    txt.setVisibility(View.VISIBLE);
                                    TextView textView1 = getView().findViewById(R.id.textView25);
                                    textView1.setText(dateTime.plusHours(3).toString("HH:mm", Locale.getDefault()));

                                }

                                @Override
                                public void onError(@NonNull RoutingException e) {

                                }
                            });
                            break;
                        case R.id.radioButton2:
                            routeDescriptor = new RouteDescriptor.Builder().routeType(RouteType.FASTEST).considerTraffic(true).travelMode(TravelMode.PEDESTRIAN).departAt(new Date()).build();
                            routeCalculationDescriptor = new RouteCalculationDescriptor.Builder().routeDescription(routeDescriptor).reportType(ReportType.EFFECTIVE_SETTINGS).instructionType(InstructionsType.TEXT).build();
                            routeSpecification = new RouteSpecification.Builder(destination, new LatLng((Double) address.get("lat"), (Double) address.get("lng"))).routeCalculationDescriptor(routeCalculationDescriptor).build();

                            routingApi.planRoute(routeSpecification, new RouteCallback() {
                                @Override
                                public void onSuccess(@NonNull RoutePlan routePlan) {
                                    FullRoute fullRoute = routePlan.getRoutes().get(0);
                                    DateFormatter dateFormatter = new DateFormatter();
                                    DateTime dateTime = dateFormatter.formatWithTimeZone(fullRoute.getSummary().getArrivalTime());
                                    txt.setVisibility(View.VISIBLE);
                                    TextView textView1 = getView().findViewById(R.id.textView25);
                                    textView1.setText(dateTime.plusHours(3).toString("HH:mm", Locale.getDefault()));

                                }

                                @Override
                                public void onError(@NonNull RoutingException e) {

                                }
                            });
                    }
                }
            });

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
}