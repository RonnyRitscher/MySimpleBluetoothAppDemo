package de.proneucon.mysimplebluetoothappdemo;
/**
 * Bluetooth
 * -> verbinden 2er Geräte über die Bluetooth-Schnittstelle
 *
 *
 * - BT ein und ausschalten
 * - Gerät sichtbar (für andere) machen
 * - Koppeln von geräten
 * -  Finden der vorhandenen Geräte in der Nähe, die nicht gekoppelt sind
 * - Verbindung zu einemausgewählten Gerät herstellen -> auswahle welches Server u/o Client ist
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Methode für den Button btn_turn_on
    public void turnOn(View view) {
    }
    //Methode für den Button btn_turn_off
    public void turnOff(View view) {
    }
    //Methode für den Button btn_get_visible
    public void getVisible(View view) {
    }
    //Methode für den Button btn_list_divices
    public void listDivices(View view) {
    }
    //Methode für den Button btn_search_devices
    public void searchDevices(View view) {
    }
}
