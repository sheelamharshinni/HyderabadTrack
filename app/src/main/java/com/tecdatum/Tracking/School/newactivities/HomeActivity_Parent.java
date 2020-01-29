//package com.tecdatum.Tracking.IVTS_Dashboard.newactivities;
//
//import android.animation.ValueAnimator;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.LinearInterpolator;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.Polyline;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.android.gms.maps.model.SquareCap;
//import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import com.tecdatum.Tracking.IVTS_Dashboard.Constants.remote.IGoogleApi;
//import com.tecdatum.Tracking.IVTS_Dashboard.R;
//import com.tecdatum.Tracking.IVTS_Dashboard.models.Result;
//import com.tecdatum.Tracking.IVTS_Dashboard.models.Route;
//import com.tecdatum.Tracking.IVTS_Dashboard.models.events.BeginJourneyEvent;
//import com.tecdatum.Tracking.IVTS_Dashboard.models.events.CurrentJourneyEvent;
//import com.tecdatum.Tracking.IVTS_Dashboard.models.events.EndJourneyEvent;
//import com.tecdatum.Tracking.IVTS_Dashboard.utils.JourneyEventBus;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//import io.reactivex.SingleObserver;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//import static android.content.Context.MODE_PRIVATE;
//import static com.google.android.gms.maps.model.JointType.ROUND;
//import static com.tecdatum.Tracking.IVTS_Dashboard.newactivities.SplashScreen.MY_PREFS_NAME;
//
//public class HomeActivity_Parent extends Fragment implements OnMapReadyCallback {
//
//    private SupportMapFragment mapFragment;
//    private GoogleMap mMap;
//    private List<LatLng> polyLineList;
//    private Marker marker;
//    private float v;
//    private double lat, lng;
//    private Handler handler;
//    private LatLng startPosition, endPosition;
//    private int index, next;
//    private LatLng sydney;
//    private PolylineOptions polylineOptions, blackPolylineOptions;
//    private Polyline blackPolyline, greyPolyLine;
//    private IGoogleApi apiInterface;
//    private Disposable disposable;
//    String RouteID;
//    TextView textView;
//    String UserName;
//
//    public HomeActivity_Parent() {
//        // Required empty public constructor
//    }
//
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    View view;
//
//    public static HomeActivity_Parent newInstance(String param1, String param2) {
//        HomeActivity_Parent fragment = new HomeActivity_Parent();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.activity_home__parent2, container, false);
//        SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        UserName = bb.getString("UserName", "");
//        mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        polyLineList = new ArrayList<>();
//        service();
//        mapFragment.getMapAsync(this);
//
//
//        return view;
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setTrafficEnabled(false);
//        mMap.setIndoorEnabled(false);
//        mMap.setBuildingsEnabled(false);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setAllGesturesEnabled(true);
//        mMap.getUiSettings().setZoomGesturesEnabled(true);
//        sydney = new LatLng(17.4371852, 78.4349906);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Home"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
//                .target(googleMap.getCameraPosition().target)
//                .zoom(17)
//                .bearing(30)
//                .tilt(45)
//                .build()));
//
//
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        disposable = JourneyEventBus.getInstance().getOnJourneyEvent()
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        if (o instanceof BeginJourneyEvent) {
//                            Toast.makeText(getContext(), "Journey has started", Toast.LENGTH_LONG).show();
//
//                        } else if (o instanceof EndJourneyEvent) {
//                            Toast.makeText(getContext(), "Journey has ended", Toast.LENGTH_LONG).show();
//
//                        } else if (o instanceof CurrentJourneyEvent) {
//
//                        }
//                    }
//                });
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (!disposable.isDisposed()) {
//            disposable.dispose();
//        }
//    }
//
//    public void service() {
//        SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//
//        RouteID = bb.getString("RouteID", "");
//
//        try {
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("ActionName", "GridAllocationVehicles");
//            jsonBody.put("VehicleRouteID", RouteID);
//            Log.e("RequestBody", "Request Body:- ${reqobject}");
//            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody requestBody = RequestBody.create(mediaType, jsonBody.toString());
//            Retrofit retrofit2 = new Retrofit.Builder()
//                    .baseUrl("http://tecdatum.net/IVTSSchools/api/")
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .build();
//            apiInterface = retrofit2.create(IGoogleApi.class);
//            apiInterface.Places(requestBody)
//                    .enqueue(new Callback<String>() {
//                        @Override
//                        public void onResponse(Call<String> call, Response<String> response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response.body());
//                                Log.e("response", "" + jsonObject);
//                                String code = jsonObject.getString("Code");
//                                if (marker != null) {
//                                    marker.remove();
//                                }
//                                if (code.equalsIgnoreCase("0")) {
//                                    JSONArray jsonArray = jsonObject.getJSONArray("VehicleRouteData");
//                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        JSONObject route = jsonArray.getJSONObject(i);
//                                        String DropingLastStop = route.getString("DropingLastStop");
//                                        Log.e("locationstring", "" + DropingLastStop);
//                                        placesdata(DropingLastStop);
//
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<String> call, Throwable t) {
//                            Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
//
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }
//
//    public void placesdata(String dropingLastStop) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .baseUrl("https://maps.googleapis.com/")
//                .build();
//
//        apiInterface = retrofit.create(IGoogleApi.class);
//        apiInterface.getDirections("driving", "less_driving",
//                17.4371852 + "," + 78.4349906, dropingLastStop,
//                getResources().getString(R.string.google_direction_key))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<Result>() {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(Result result) {
//                        List<Route> routeList = result.getRoutes();
//                        for (Route route : routeList) {
//                            String polyLine = route.getOverviewPolyline().getPoints();
//                            polyLineList = decodePoly(polyLine);
//                            drawPolyLineAndAnimateCar();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//                });
//    }
//
//    private void drawPolyLineAndAnimateCar() {
//        //Adjusting bounds
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (LatLng latLng : polyLineList) {
//            builder.include(latLng);
//        }
//        LatLngBounds bounds = builder.build();
//        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
//        mMap.animateCamera(mCameraUpdate);
//
//        polylineOptions = new PolylineOptions();
//        polylineOptions.color(Color.GRAY);
//        polylineOptions.width(5);
//        polylineOptions.startCap(new SquareCap());
//        polylineOptions.endCap(new SquareCap());
//        polylineOptions.jointType(ROUND);
//        polylineOptions.addAll(polyLineList);
//        greyPolyLine = mMap.addPolyline(polylineOptions);
//
//        blackPolylineOptions = new PolylineOptions();
//        blackPolylineOptions.width(5);
//        blackPolylineOptions.color(Color.BLACK);
//        blackPolylineOptions.startCap(new SquareCap());
//        blackPolylineOptions.endCap(new SquareCap());
//        blackPolylineOptions.jointType(ROUND);
//        blackPolyline = mMap.addPolyline(blackPolylineOptions);
//
//        mMap.addMarker(new MarkerOptions()
//                .position(polyLineList.get(polyLineList.size() - 1)));
//
//        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
//        polylineAnimator.setDuration(2000);
//        polylineAnimator.setInterpolator(new LinearInterpolator());
//        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                List<LatLng> points = greyPolyLine.getPoints();
//                int percentValue = (int) valueAnimator.getAnimatedValue();
//                int size = points.size();
//                int newPoints = (int) (size * (percentValue / 100.0f));
//                List<LatLng> p = points.subList(0, newPoints);
//                blackPolyline.setPoints(p);
//            }
//        });
//        polylineAnimator.start();
//        marker = mMap.addMarker(new MarkerOptions().position(sydney)
//                .flat(true)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_run_e)));
//        handler = new Handler();
//        index = -1;
//        next = 1;
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (index < polyLineList.size() - 1) {
//                    index++;
//                    next = index + 1;
//                }
//                if (index < polyLineList.size() - 1) {
//                    startPosition = polyLineList.get(index);
//                    endPosition = polyLineList.get(next);
//                }
//                if (index == 0) {
//                    BeginJourneyEvent beginJourneyEvent = new BeginJourneyEvent();
//                    beginJourneyEvent.setBeginLatLng(startPosition);
//                    JourneyEventBus.getInstance().setOnJourneyBegin(beginJourneyEvent);
//                }
//                if (index == polyLineList.size() - 1) {
//                    EndJourneyEvent endJourneyEvent = new EndJourneyEvent();
//                    endJourneyEvent.setEndJourneyLatLng(new LatLng(polyLineList.get(index).latitude,
//                            polyLineList.get(index).longitude));
//                    JourneyEventBus.getInstance().setOnJourneyEnd(endJourneyEvent);
//                }
//                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
//                valueAnimator.setDuration(3000);
//                valueAnimator.setInterpolator(new LinearInterpolator());
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                        v = valueAnimator.getAnimatedFraction();
//                        lng = v * endPosition.longitude + (1 - v)
//                                * startPosition.longitude;
//                        lat = v * endPosition.latitude + (1 - v)
//                                * startPosition.latitude;
//                        LatLng newPos = new LatLng(lat, lng);
//                        CurrentJourneyEvent currentJourneyEvent = new CurrentJourneyEvent();
//                        currentJourneyEvent.setCurrentLatLng(newPos);
//                        JourneyEventBus.getInstance().setOnJourneyUpdate(currentJourneyEvent);
//                        marker.setPosition(newPos);
//                        marker.setAnchor(0.5f, 0.5f);
//                        marker.setRotation(getBearing(startPosition, newPos));
//                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition
//                                (new CameraPosition.Builder().target(newPos)
//                                        .zoom(15.5f).build()));
//                    }
//                });
//                valueAnimator.start();
//                if (index != polyLineList.size() - 1) {
//                    handler.postDelayed(this, 3000);
//                }
//            }
//        }, 3000);
//    }
//
//
//    private List<LatLng> decodePoly(String encoded) {
//        List<LatLng> poly = new ArrayList<>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((((double) lat / 1E5)),
//                    (((double) lng / 1E5)));
//            poly.add(p);
//        }
//
//        return poly;
//    }
//
//    private float getBearing(LatLng begin, LatLng end) {
//        double lat = Math.abs(begin.latitude - end.latitude);
//        double lng = Math.abs(begin.longitude - end.longitude);
//
//        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
//            return (float) (Math.toDegrees(Math.atan(lng / lat)));
//        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
//            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
//        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
//            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
//        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
//            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
//        return -1;
//    }
//
//
//}
//
