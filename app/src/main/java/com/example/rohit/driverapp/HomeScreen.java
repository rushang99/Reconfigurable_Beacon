package com.example.rohit.driverapp;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@TargetApi(23)
public class HomeScreen extends AppCompatActivity {

    public static DatabaseHelper device_db;
    public static DatabaseHelper2 gps_locations;
    public Criteria criteria;
    public String bestProvider;

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Handler mHandler;
    private int REQUEST_ENABLE_BT = 1;
    private boolean mScanning = false;
    private ListView listView;
    private Button scanButton;
    private Button gps_button;
    private BeaconAdapter adapter;
    private ArrayList<String> BT_devices;
    ArrayList<ibeaconDevice>devices_list;
    private HashMap<String, BluetoothDevice> map;
    private LocationManager locationManager;
    private LocationListener listener;
    private static int SPLASH_TIME_OUT=4000;

    private BluetoothLeScanner BLEScanner = mBluetoothAdapter.getBluetoothLeScanner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        device_db = new DatabaseHelper(this);
        gps_locations = new DatabaseHelper2(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        devices_list=new ArrayList<>();

//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                Intent homeIntent=new Intent(HomeScreen.this,HomeActivity.class);
//                startActivity(homeIntent);
//                finish();
//            }
//        },SPLASH_TIME_OUT);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        // Prompt for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("BleActivity", "Location access not granted!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 42);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("BleActivity", "Location access not granted!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 42);
        }

        BT_devices = new ArrayList<String>();
        map = new HashMap<String, BluetoothDevice>();
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mHandler = new Handler();

        scanButton = (Button) findViewById(R.id.gps_button123);
        listView = (ListView) findViewById(R.id.list);
        gps_button=(Button) findViewById(R.id.scan);

         adapter = new BeaconAdapter(this,R.layout.list_item,devices_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BLEScanner.stopScan(mLeScanCallback);

                final BluetoothDevice device = map.get(devices_list.get(position).name);
                BT_devices.clear();
                devices_list.clear();
                adapter.clear();
                adapter.notifyDataSetChanged();

                final Dialog dialog = new Dialog(HomeScreen.this);
                dialog.setContentView(R.layout.radio_button2);
                dialog.setTitle("Select an option");
                dialog.setCancelable(true);


                final RadioButton info = (RadioButton) dialog.findViewById(R.id.rd_101);
                final RadioButton conf = (RadioButton) dialog.findViewById(R.id.rd_102);
                //final RadioButton gps_locker = (RadioButton) dialog.findViewById(R.id.rd_103);

                Button b;
                b = (Button) dialog.findViewById(R.id.button3);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (info.isChecked()) {
                            Intent i = new Intent(HomeScreen.this, Information.class);
                            i.putExtra("DEVICE", device);
                            dialog.dismiss();
                            startActivity(i);

                        } else if (conf.isChecked()) {
                            Intent i = new Intent(HomeScreen.this, Configuration.class);
                            i.putExtra("DEVICE", device);
                            dialog.dismiss();
                            startActivity(i);
                        }

//                        } else if (gps_locker.isChecked()) {
//                            Intent i = new Intent(HomeScreen.this, Gps_locker.class);
//                            i.putExtra("DEVICE", device);
//                            dialog.dismiss();
//                            startActivity(i);
//                        }

                    }
                });
                dialog.show();

//                Intent i=new Intent(HomeScreen.this,Configuration.class);
//                i.putExtra("DEVICE",device);
//                startActivity(i);

            }
        });


        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                device_db.onUpgrade(device_db.getWritableDatabase(), 1, 2);
                BT_devices.clear();
                adapter.notifyDataSetChanged();
                map.clear();
                adapter.clear();
                BLEScanner.startScan(mLeScanCallback);

            }
        });

        configure_button();

        gps_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, Gps_locker.class);
                startActivity(i);

            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            BLEScanner.stopScan(mLeScanCallback);
        }
    }

    @Override
    protected void onDestroy() {
        BLEScanner.stopScan(mLeScanCallback);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void scanLeDevice(final boolean enable) {
        if (enable && !mScanning) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BLEScanner.stopScan(mLeScanCallback);
                    mScanning = false;
                }
            }, Constants.SCAN_PERIOD);
            mScanning = true;
            BLEScanner.startScan(mLeScanCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
    }

    private android.bluetooth.le.ScanCallback mLeScanCallback =
            new ScanCallback() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    List<ADStructure> structures =
                            ADPayloadParser.getInstance().parse(result.getScanRecord().getBytes());

                    if (!BT_devices.contains(result.getDevice().getName()) && result.getDevice().getName() != null) {
                        final BluetoothDevice device = result.getDevice();

//                        BT_devices.add(device.getName());
//                        map.put(device.getName(), device);
//                        adapter.notifyDataSetChanged();

                        for (ADStructure structure : structures) {
                            if (structure instanceof IBeacon) {
                                // An iBeacon was found.
                                IBeacon iBeacon = (IBeacon) structure;
                                UUID uuid = iBeacon.getUUID();

                                String Uuid = uuid.toString();

// (2) Major number
                                int major = iBeacon.getMajor();

// (3) Minor number
                                int minor = iBeacon.getMinor();

// (4) Tx Power
                                int power = iBeacon.getPower();
                                Log.d("uuidd",Uuid.substring(Uuid.length()-6));

                                if(Uuid.substring(Uuid.length()-6).equals("059935"))
                                {
                                    BT_devices.add(device.getName());
                                    devices_list.add(new ibeaconDevice(iBeacon,device.getName(),result.getRssi()));
                                    devices_list.sort(new Comparator<ibeaconDevice>() {
                                        @Override
                                        public int compare(ibeaconDevice ibeaconDevice, ibeaconDevice t1) {
                                            return -(ibeaconDevice.power-t1.power);
                                        }
                                    });
                                    map.put(device.getName(), device);
                                    adapter.notifyDataSetChanged();
                                }



                                boolean temp = device_db.insertData(device.getName(), major, minor, Uuid);
                                Log.d("TAG", "onScanResult: Ibeacon " + temp + uuid + " " + major + " " + minor + " " + power);
                                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                                if (ActivityCompat.checkSelfPermission(HomeScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }

//                                Location l=null;
//                                LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
//                                List<String> providers = mLocationManager.getProviders(true);
//                                Log.d("LOC", "onScanResult: "+providers);
//
//                                Location bestLocation = null;
//                                for (String provider : providers) {
//                                    Log.d("Providers", "onScanResult: "+provider+bestLocation+l);
//                                    if(ContextCompat.checkSelfPermission(HomeScreen.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
//                                        l = mLocationManager.getLastKnownLocation(provider);
//                                        Log.d("IF1", "onScanResult: ");
//                                    }
//                                    if (l == null) {
//                                        Log.d("IF2", "onScanResult: ");
//                                        continue;
//
//                                    }
//                                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                                        bestLocation = l;
//                                        Log.d("IF3", "onScanResult: "+bestLocation);
//                                    }
//                                    Log.d("Providers", "onScanResult: "+provider+bestLocation);
//                                }
//                                String loc=bestLocation.getLatitude()+","+bestLocation.getLongitude();
//                                Log.d(loc, "onLocationChanged: hello");
//                                gps_locations.update_loc(device.getName(),loc);

                                LocationListener locationListener = new MyLocationListener(HomeScreen.this,device.getName());
                                locationManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

//
//                                Location location=locationManager.getLastKnownLocation("gps");
//
//                                String loc=location.getLatitude()+", "+location.getLongitude();
//                                Log.d(loc, "onLocationChanged: hello");
//                                gps_locations.update_loc(device.getName(),loc);





//                                listener = new LocationListener() {
//                                    @Override
//                                    public void onLocationChanged(Location location) {
//                                        String loc=location.getLongitude()+", "+location.getLatitude();
//                                        Log.d(loc, "onLocationChanged: hello");
//                                        gps_locations.update_loc(device.getName(),loc);
//
//                                    }
//
//                                    @Override
//                                    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//                                    }
//
//                                    @Override
//                                    public void onProviderEnabled(String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onProviderDisabled(String s) {
//
//                                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                        startActivity(i);
//                                    }
//                                };
//
//                                locationManager.requestLocationUpdates("gps", 5000, 0, listener);


                            }
                        }
                    }
                }

            };





}
