package com.example.ednevnik.ui.addLesson;

import static com.example.ednevnik.R.string.сhooseGroup;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ednevnik.POJO.Group;
import com.example.ednevnik.POJO.Lesson;
import com.example.ednevnik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.common.permission.AndroidPermissionChecker;
import com.tomtom.online.sdk.common.permission.PermissionChecker;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResponse;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class AddLessonFragment extends Fragment {
    private final Handler searchTimerHandler = new Handler();
    private Runnable searchRunnable;
    EditText theme, start, finish, date;
    AutoCompleteTextView address;
    Button choose, add;
    ArrayList<Group> groups = new ArrayList<>();
    Group group1;
    private String addressname;
    private LatLng destination;
    private HashMap<String, LatLng> searchResultsMap = new HashMap<>();
    private SearchApi searchApi;
    private ArrayAdapter<String> searchAdapter;
    private List<String> searchAutocompleteList = new ArrayList<>();

    public AddLessonFragment() {
        // Required empty public constructor
    }

    public static AddLessonFragment newInstance(String param1, String param2) {
        AddLessonFragment fragment = new AddLessonFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_lesson, container, false);
        init(view);
        searchApi = OnlineSearchApi.create(getContext(), getString(R.string.apikey));
        searchAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, searchAutocompleteList);

        PermissionChecker permissionChecker = AndroidPermissionChecker.createLocationChecker(getContext());
        if(permissionChecker.ifNotAllPermissionGranted()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        address.setAdapter(searchAdapter);
        address.addTextChangedListener(new TextWatcher() {
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
                    searchRunnable = () -> searchAddress(s.toString(), address);
                searchAdapter.clear();
                searchTimerHandler.postDelayed(searchRunnable, 600);
            }
        });
        choose.setOnClickListener(view1 -> {
            CharSequence[] names = new CharSequence[groups.size()];
            int n = 0;
            for (Group group: groups){
                names[n] = group.name;
                n += 1;
            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setTitle(сhooseGroup).setSingleChoiceItems(names, -1, (dialogInterface, i) -> group1 = groups.get(i)).setPositiveButton(R.string.choose, (dialogInterface, i) -> {
                TextView textView = getView().findViewById(R.id.textView11);
                textView.setText(group1.name);
            }).create().show();
        });
        add.setOnClickListener(view12 -> {
            if (check()){
                String date1 = date.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
                try {
                    Date date = simpleDateFormat.parse(date1);
                    Date start1 = simpleDateFormat1.parse(start.getText().toString());
                    start1.setYear(date.getYear());
                    start1.setMonth(date.getMonth());
                    start1.setDate(date.getDate());

                    Date finish1 = simpleDateFormat1.parse(finish.getText().toString());
                    finish1.setYear(date.getYear());
                    finish1.setMonth(date.getMonth());
                    finish1.setDate(date.getDate());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name", addressname);
                    hashMap.put("lat", destination.getLatitude());
                    hashMap.put("lng", destination.getLongitude());
                    Lesson lesson = new Lesson(date, hashMap, start1, finish1, FirebaseFirestore.getInstance().collection("Groups").document(group1.name), theme.getText().toString());
                    FirebaseFirestore.getInstance().collection("Lessons").document(lesson.theme).set(lesson).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()){
                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(), R.string.lesson_success, Toast.LENGTH_LONG).show();
                            getActivity().recreate();
                        }
                    });
                } catch (ParseException e) {
                    Log.i("ERR", e.toString());
                }

            }
        });
        return view;
    }
    private void init(View view){
        theme = view.findViewById(R.id.editTextTextPersonName4);
        start = view.findViewById(R.id.editTextTime3);
        finish = view.findViewById(R.id.editTextTime5);
        address = view.findViewById(R.id.autoCompleteTextView);
        date = view.findViewById(R.id.editTextDate2);
        choose = view.findViewById(R.id.button4);
        add = view.findViewById(R.id.button6);
        FirebaseFirestore.getInstance().collection("Groups").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot s:
                        task.getResult()) {
                    Group group = s.toObject(Group.class);
                    groups.add(group);
                }
            }
        });
    }
    private boolean check(){
        if (theme.getText().toString().equals("")){
            theme.setError(getString(R.string.enter_theme));
            return false;
        }
        if (start.getText().toString().equals("")){
            theme.setError(getString(R.string.enter_start));
            return false;
        }
        if (finish.getText().toString().equals("")){
            theme.setError(getString(R.string.enter_finish));
            return false;
        }
        if (address.getText().toString().equals("")){
            theme.setError(getString(R.string.enter_place));
            return false;
        }
        if (date.getText().toString().equals("")){
            theme.setError(getString(R.string.enter_date));
            return false;
        }
        return true;
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
            addressname  = (String) parent.getItemAtPosition(position);
            destination = searchResultsMap.get(addressname);});

    }
}