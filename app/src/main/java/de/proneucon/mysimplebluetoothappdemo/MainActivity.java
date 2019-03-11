package de.proneucon.mysimplebluetoothappdemo;
/**
 * Bluetooth
 * -> verbinden 2er Geräte über die Bluetooth-Schnittstelle
 * <p>
 * - Setzen der Permissions
 * <p>
 * - BT ein und ausschalten
 * - Gerät sichtbar (für andere) machen
 * - Koppeln von geräten
 * -  Finden der vorhandenen Geräte in der Nähe, die nicht gekoppelt sind
 * - Verbindung zu einemausgewählten Gerät herstellen -> auswahle welches Server u/o Client ist
 */

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    //MEMBER
    private TextView tv_bt_activated;
    private ListView lv_devices;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> list;
    private Set<BluetoothDevice> bondetDevices;
    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_PERMISSION_LOCATION = 123;

    //benötigter BroadcastReciver
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){ //Fragt die vorhandenen/verwendbaren BT-geräte ab
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); //aus dem intent werden alle Devices abgefragt die parcelable sind -> übergibt das BT-Device als EXTRA

                //Prüfen ob das Device vorhanden ist:
                String address = device.getAddress() +" : "+ device.getName(); //übergibt die Adresse und den Namendes gefundenen Gerätes
                arrayAdapter.add(address); //übergibt die Adresse/Name dem ArrayAdapter
            }
        }
    };
    


    //*******************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Member initialisieren:
        tv_bt_activated = findViewById(R.id.tv_bt_activated);
        lv_devices = findViewById(R.id.lv_devices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //verwenden des DefaultAdapters
        list = new ArrayList<>(); //leere ArrayList
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list); //(Context , Layout, ArrayAdapter)

        //Listview den Adapter anhängen:
        lv_devices.setAdapter(arrayAdapter);

        //PERMISSION-CHECK: *******************************
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            }
        }

        //Reciver muss registriert/unregistriert werden:
        //Registrieren in der onCreate - Unregistrieren in der onDestroy
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, foundFilter);
        Toast.makeText(this, "Reciver Registriert", Toast.LENGTH_SHORT).show();

    }

    //PERMISSION-RESULT: *******************************
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_PERMISSION_LOCATION  && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED);
    }
    
  

    //Methode für die Buttons: *******************************
    public void turnOn(View view) {
        //
        if(!bluetoothAdapter.isEnabled()){
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
            startActivity(turnOn);
            Toast.makeText(this, "Turned On", Toast.LENGTH_SHORT).show();
            tv_bt_activated.setText("Bluetooth AKTIVIERT");
        }else{
            Toast.makeText(this, "Already On", Toast.LENGTH_SHORT).show();
        }
    }

    public void turnOff(View view) {
        bluetoothAdapter.disable();
        Toast.makeText(this, "Turned Off", Toast.LENGTH_SHORT).show();
        tv_bt_activated.setText("Bluetooth DEAKTIVIERT");
    }

    public void setVisible(View view) {
        //Sichtbarmachen unseres Gerätes für andere
        Intent setVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE); //über den Adapter den Discover einstellen
        setVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION , 180); //als Extra übergeben und festlegen der Zeit der sichtbarkeit
        startActivity(setVisible); //Starten der Activity
    }

    public void listDevices(View view) {
        // Liste aller bereits befundenen Devices anzeigen:
        bondetDevices = bluetoothAdapter.getBondedDevices(); //liefert ein Set von BluetoothDevices zurück
        arrayAdapter.clear();
        for (BluetoothDevice device : bondetDevices){
            arrayAdapter.add(device.getAddress()+" : "+ device.getName()); //speichert die gekoppelten Geräte in der List
        }
    }

    public void searchDevices(View view) {
        //Suchen aller verfügbaren Devices in der Nähe:
        arrayAdapter.clear();   //Löscht die vorhandenen Daten
        bluetoothAdapter.startDiscovery();  //sucht die Geräte die auswertbar sind... Dafür brauchen wir einen BroatCastReciver
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver); //Register UnRegistrieren
        Toast.makeText(this, "Reciver Unregistriert", Toast.LENGTH_SHORT).show();
    }
}
