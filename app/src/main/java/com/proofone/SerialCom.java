package com.proofone;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import java.io.IOException;
import java.util.List;

public class SerialCom extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "1lnew";
    TextView textView;
    EditText editText;
    UsbManager usbManager;
    UsbDevice device;
    UsbDeviceConnection connection;

    public void connect(View view){
        try {
            List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
            if (availableDrivers.isEmpty()) {
                return;
            }
            // Open a connection to the first available driver.
            UsbSerialDriver driver = availableDrivers.get(availableDrivers.size()-1);
            UsbDeviceConnection connection = usbManager.openDevice(driver.getDevice());
            if (connection == null) {
                requestPermission(driver.getDevice());
                // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
                return;
            }
            UsbSerialPort yeni_port = driver.ge
            UsbSerialPort port = driver.getPorts().get(driver.getPorts().size()-1);
            textView.setText(textView.getText()+" con"+port.getSerial());
            try {
                port.open(connection);
                port.setParameters(9600, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

                byte buffer[] = new byte[16];
                int numBytesRead = port.read(buffer, 1000);
                //port.write(editText.getText().toString().getBytes("UTF-8"),500);
                textView.setText(textView.getText()+" byte"+numBytesRead);
            } catch (IOException e){
                textView.setText(textView.getText()+" fhjfj"+e.getMessage());
            } finally {
                try {
                    port.close();
                } catch (IOException e) {
                    textView.setText(textView.getText()+"\n"+e.getMessage());
                }
            }
        }
        catch (Exception e){
            textView.setText(textView.getText()+"\n"+e.getMessage());
        }
        finally {
        }
    }
    public void requestPermission(UsbDevice device){
        PendingIntent permissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        usbManager.requestPermission(device, permissionIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_com);
        textView = findViewById(R.id.textViewser);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        device = null;
        connection = null;
    }
}