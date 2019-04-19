package com.example.rohit.driverapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Configuration extends AppCompatActivity {

    public ArrayList<String> config_options=new ArrayList<>();
    BluetoothDevice device;
    private String beacon_name;
    private BluetoothGatt mGatt;

    @Override
    protected void onDestroy() {
        disconnectDevice();
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent i= getIntent();
        device=i.getParcelableExtra("DEVICE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_beacon);
        config_options.add("Beacon Name");
        config_options.add("Buzzer on");
        config_options.add("Buzzer off");
        config_options.add("Major");
        config_options.add("Minor");
        config_options.add("Power Level");
        config_options.add("Advertising Interval");
        //config_options.add("UUID last 4");

        //config_options.add("Buzzer Off");
        config_options.add("Send Beacon to sleep");
        config_options.add("Wake up Beacon");

        final ListView configurations=(ListView) findViewById(R.id.config_options);

        ArrayAdapter<String> configs=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,config_options);
        configurations.setAdapter(configs);

        configurations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String action=(String) configurations.getItemAtPosition(i);
                if(action.equals("Beacon Name"))
                {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(Configuration.this);
                final EditText edittext = new EditText(Configuration.this);
                alert.setMessage("Enter new name");
                alert.setTitle("Rename Device");
                alert.setView(edittext);
                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        beacon_name= edittext.getText().toString();
                        beacon_name = "AT+NAME"+beacon_name;
                            connectToDevice(device);
                    }
                });
                alert.show();

                }
                else if(action.equals("Send Beacon to sleep"))
                {
                    beacon_name = "AT+PWRM0";
                    connectToDevice(device);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            disconnectDevice();
//                        }
//                    }, Constants.connectionTime);

                }
                else if(action.equals("Wake up Beacon"))
                {
                    beacon_name = "AT+PWRM1";
                    connectToDevice(device);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            disconnectDevice();
//                        }
//                    }, Constants.connectionTime);

                }
                else if(action.equals("Buzzer off"))
                {
                    beacon_name = "AT+PIO20";
                    connectToDevice(device);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            disconnectDevice();
//                        }
//                    }, Constants.connectionTime);

                }
                else if(action.equals("Buzzer on"))
                {

                    beacon_name = "AT+PIO21";
                    Log.d("VAL", beacon_name);
                    connectToDevice(device);

//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            // this code will be executed after 2 seconds
//                            beacon_name = "AT+PIO20";
//                            Log.d("VAL", beacon_name);
//                            connectToDevice(device);
////                            new Timer().schedule(new TimerTask() {
////                                @Override
////                                public void run() {
////                                    // this code will be executed after 2 seconds
////                                    beacon_name = "AT+PIO21";
////                                    Log.d("VAL", beacon_name);
////                                    connectToDevice(device);
////                                    new Timer().schedule(new TimerTask() {
////                                        @Override
////                                        public void run() {
////                                            // this code will be executed after 2 seconds
////                                            beacon_name = "AT+PIO20";
////                                            Log.d("VAL", beacon_name);
////                                            connectToDevice(device);
////                                            new Timer().schedule(new TimerTask() {
////                                                @Override
////                                                public void run() {
////                                                    // this code will be executed after 2 seconds
////                                                    beacon_name = "AT+PIO21";
////                                                    Log.d("VAL", beacon_name);
////                                                    connectToDevice(device);
////                                                    new Timer().schedule(new TimerTask() {
////                                                        @Override
////                                                        public void run() {
////                                                            // this code will be executed after 2 seconds
////                                                            beacon_name = "AT+PIO20";
////                                                            Log.d("VAL", beacon_name);
////                                                            connectToDevice(device);
////                                                        }
////                                                    }, 1000);
////                                                }
////                                            }, 1000);
////                                        }
////                                    }, 1000);
////                                }
////                            }, 1000);
//                        }
//                    }, 1500);






                }
//                else if(action.equals("Buzzer Off"))
//                {
//                    beacon_name = "AT+PIO20";
//                    Log.d("VAL",beacon_name);
//                    connectToDevice(device);
//
////                    final Handler handler = new Handler();
////                    handler.postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            disconnectDevice();
////                        }
////                    }, Constants.connectionTime);
//
//                }
                else if(action.equals("Minor"))
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Configuration.this);
                    final EditText edittext = new EditText(Configuration.this);
                    edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alert.setMessage("Enter new Minor value (From 0 to 65535)");
                    alert.setTitle("Change Minor Value");
                    alert.setView(edittext);
                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            int x;
                            x= Integer.parseInt(edittext.getText().toString());
                            String Hex="";
                            if(0<=x && x<=65536)
                            {
                                Hex=Integer.toHexString(x);
                                if(Hex.length()==1)
                                    Hex="000"+Hex;
                                else if (Hex.length()==2)
                                {
                                    Hex="00"+Hex;
                                }
                                else if(Hex.length()==3)
                                {
                                    Hex="0"+Hex;
                                }
                            }
                            beacon_name = "AT+MINO0x"+Hex.toUpperCase();
                            Log.d("VAL",beacon_name);
                            connectToDevice(device);
                        }
                    });
                    alert.show();
                }
                else if(action.equals("Major"))
                {

                    AlertDialog.Builder alert = new AlertDialog.Builder(Configuration.this);
                    final EditText edittext = new EditText(Configuration.this);
                    edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alert.setMessage("Enter new Major value (From 0 to 65535)");
                    alert.setTitle("Change Major Value");
                    alert.setView(edittext);
                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            int x;
                            x= Integer.parseInt(edittext.getText().toString());
                            String Hex="";
                            if(0<=x && x<=65536)
                            {
                                Hex=Integer.toHexString(x);
                                if(Hex.length()==1)
                                    Hex="000"+Hex;
                                else if (Hex.length()==2)
                                {
                                    Hex="00"+Hex;
                                }
                                else if(Hex.length()==3)
                                {
                                    Hex="0"+Hex;
                                }
                            }
                            beacon_name = "AT+MARJ0x"+Hex.toUpperCase();
                            Log.d("VAL",beacon_name);
                            connectToDevice(device);
//                            final Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    disconnectDevice();
//                                }
//                            }, Constants.connectionTime);
                            ProgressDialog progress = new ProgressDialog(Configuration.this);
                            progress.setMessage("Changing Major Value");
                            progress.setCancelable(true);
                            progress.show();
                        }
                    });
                    alert.show();
                }
                else if(action.equals("Advertising Interval"))
                {
                    final Dialog dialog = new Dialog(Configuration.this);
                    dialog.setContentView(R.layout.radio_button);
                    dialog.setTitle("Advertising Interval");
                    dialog.setCancelable(true);
                    // there are a lot of settings, for dialog, check them all out!
                    // set up radiobutton
                    final RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.rd_1);
                    final RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.rd_2);
                    final RadioButton rd3 = (RadioButton) dialog.findViewById(R.id.rd_3);
                    final RadioButton rd4 = (RadioButton) dialog.findViewById(R.id.rd_4);
                    final RadioButton rd5 = (RadioButton) dialog.findViewById(R.id.rd_5);
                    final RadioButton rd6 = (RadioButton) dialog.findViewById(R.id.rd_6);
                    final RadioButton rd7 = (RadioButton) dialog.findViewById(R.id.rd_7);
                    final RadioButton rd8 = (RadioButton) dialog.findViewById(R.id.rd_8);
                    final RadioButton rd9 = (RadioButton) dialog.findViewById(R.id.rd_9);
                    final RadioButton rd10 = (RadioButton) dialog.findViewById(R.id.rd_10);
                    final RadioButton rd11 = (RadioButton) dialog.findViewById(R.id.rd_11);
                    final RadioButton rd12 = (RadioButton) dialog.findViewById(R.id.rd_12);
                    final RadioButton rd13 = (RadioButton) dialog.findViewById(R.id.rd_13);
                    final RadioButton rd14 = (RadioButton) dialog.findViewById(R.id.rd_14);
                    final RadioButton rd15 = (RadioButton) dialog.findViewById(R.id.rd_15);
                    final RadioButton rd16 = (RadioButton) dialog.findViewById(R.id.rd_16);


                    Button b;
                    b = (Button) dialog.findViewById(R.id.button1);

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            beacon_name="AT+ADVI";
                            if(rd1.isChecked())
                            {
                                beacon_name=beacon_name+"0";
                            }
                            else if(rd2.isChecked())
                            {
                                beacon_name=beacon_name+"1";
                            }
                            else if(rd3.isChecked())
                            {
                                beacon_name=beacon_name+"2";
                            }
                            else if(rd4.isChecked())
                            {
                                beacon_name=beacon_name+"3";
                            }
                            else if(rd5.isChecked())
                            {
                                beacon_name=beacon_name+"4";
                            }
                            else if(rd6.isChecked())
                            {
                                beacon_name=beacon_name+"5";
                            }
                            else if(rd7.isChecked())
                            {
                                beacon_name=beacon_name+"6";
                            }
                            else if(rd8.isChecked())
                            {
                                beacon_name=beacon_name+"7";
                            }
                            else if(rd9.isChecked())
                            {
                                beacon_name=beacon_name+"8";
                            }
                            else if(rd10.isChecked())
                            {
                               beacon_name=beacon_name+"9";
                            }
                            else  if(rd11.isChecked())
                            {
                                beacon_name=beacon_name+"A";
                            }
                            else  if(rd12.isChecked())
                            {
                                beacon_name=beacon_name+"B";
                            }
                            else  if(rd13.isChecked())
                            {
                                beacon_name=beacon_name+"C";
                            }
                            else  if(rd14.isChecked())
                            {
                                beacon_name=beacon_name+"D";
                            }
                            else  if(rd15.isChecked())
                            {
                                beacon_name=beacon_name+"E";
                            }
                            else  if(rd16.isChecked())
                            {
                                beacon_name=beacon_name+"F";
                            }
                            connectToDevice(device);
                            dialog.dismiss();


                        }
                    });





                    // now that the dialog is set up, it's time to show it
                    dialog.show();







                }
                else if(action.equals("Power Level"))
                {
                    final Dialog dialog = new Dialog(Configuration.this);
                    dialog.setContentView(R.layout.radio_button1);
                    dialog.setTitle("Advertising Interval");
                    dialog.setCancelable(true);
                    // there are a lot of settings, for dialog, check them all out!
                    // set up radiobutton
                    final RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.rd_41);
                    final RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.rd_42);
                    final RadioButton rd3 = (RadioButton) dialog.findViewById(R.id.rd_43);
                    final RadioButton rd4 = (RadioButton) dialog.findViewById(R.id.rd_44);



                    Button b;
                    b = (Button) dialog.findViewById(R.id.button2);

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            beacon_name="AT+POWE";
                            if(rd1.isChecked())
                            {
                                beacon_name=beacon_name+"0";
                            }
                            else if(rd2.isChecked())
                            {
                                beacon_name=beacon_name+"1";
                            }
                            else if(rd3.isChecked())
                            {
                                beacon_name=beacon_name+"2";
                            }
                            else if(rd4.isChecked())
                            {
                                beacon_name=beacon_name+"3";
                            }
                            connectToDevice(device);
                            dialog.dismiss();;


                        }
                    });





                    // now that the dialog is set up, it's time to show it
                    dialog.show();

                }

            }
        });
        }




    public void connectToDevice(BluetoothDevice device) {

        if (mGatt == null) {
            mGatt = device.connectGatt(this, false, gattCallback);
            Log.v("rishi","connected"+mGatt);
            // scanLeDevice(false);
        }
        else{
            writeCharacteristic(beacon_name);
        }
    }

    public void disconnectDevice(){
        if(mGatt==null){
            return;
        }
        mGatt.disconnect();
        Log.v("rishi","disconnected"+mGatt);
        mGatt.close();
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.get(0).getUuid().toString()+services.get(1).getUuid().toString()+services.get(2).getUuid().toString());
            gatt.readCharacteristic(services.get(1).getCharacteristics().get(0));
            writeCharacteristic(beacon_name);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.getUuid().toString());
            //gatt.disconnect();
        }
    };

    public boolean writeCharacteristic(String str) {

        //check mBluetoothGatt is available
        if (mGatt == null) {
            return false;
        }

        BluetoothGattService Service = mGatt.getService(mGatt.getServices().get(Constants.writeToServiceIndex).getUuid());


        if (Service == null) {
            return false;
        }

        BluetoothGattCharacteristic charac = Service.getCharacteristic(mGatt.getServices().get(Constants.writeToServiceIndex).getCharacteristics().get(Constants.writeToCharacterisiticIndex).getUuid());
        if (charac == null) {
            return false;
        }
        Log.i("UUID",mGatt.getServices().get(Constants.writeToServiceIndex).getCharacteristics().get(Constants.writeToCharacterisiticIndex).getUuid().toString());

        byte[] value = new byte[20];
        value = str.getBytes();
        charac.setValue(value);
        boolean status = mGatt.writeCharacteristic(charac);
//        if(status){
//            Looper.prepare();
//            Toast.makeText(this,"Changed Successfully",Toast.LENGTH_SHORT).show();
//            Looper.loop();
//        }
//        else {
//            Looper.prepare();
//            Toast.makeText(this,"Failed ",Toast.LENGTH_SHORT).show();
//            Looper.loop();
//        }
        return status;

    }
}
