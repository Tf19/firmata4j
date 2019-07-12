package org.firmata4j.firmata.parser;

import java.io.IOException;

import org.firmata4j.IODevice;
import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

public class MyTest {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		IODevice device = new FirmataDevice("COM3"); // using the name of a port
		// IODevice device = new FirmataDevice(new NetworkTransport("192.168.1.18:4334")); // using a network address
		// subscribe to events using device.addEventListener(...);
		// and/or device.getPin(n).addEventListener(...);
		device.start(); // initiate communication to the device
		device.ensureInitializationIsDone(); // wait for initialization is done
		// sending commands to the board
		
		Pin pin = device.getPin(13);
		pin.setMode(Pin.Mode.OUTPUT); // our listeners will get event about this change
		
		device.addEventListener(new IODeviceEventListener() {
		    @Override
		    public void onStart(IOEvent event) {
		        // since this moment we are sure that the device is initialized
		        // so we can hide initialization spinners and begin doing cool stuff
		        System.out.println("Device is ready");
		    }

		    @Override
		    public void onStop(IOEvent event) {
		        // since this moment we are sure that the device is properly shut down
		        System.out.println("Device has been stopped");
		    }

		    @Override
		    public void onPinChange(IOEvent event) {
		        // here we react to changes of pins' state
		        Pin pin = event.getPin();
		        System.out.println(
		                String.format(
		                    "Pin %d got a value of %d",
		                    pin.getIndex(),
		                    pin.getValue())
		            );
		    }

		    @Override
		    public void onMessageReceive(IOEvent event, String message) {
		        // here we react to receiving a text message from the device
		        System.out.println(message);
		    }
		});
		
		while(true) {
			pin.setValue(1); // and then about this change
			Thread.sleep(500);
			pin.setValue(0);
			Thread.sleep(500);
		}
	}
	
}
