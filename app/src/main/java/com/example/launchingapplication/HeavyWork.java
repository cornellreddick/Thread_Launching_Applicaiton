package com.example.launchingapplication;

import android.util.Log;

import java.util.Random;


public class HeavyWork implements Runnable {

	public static final long DELAY_MILLI_SECS = 2000;

	public static double getNumber(){
		//Log.d("Demo", "Starting .....");
		addSomeDelay(DELAY_MILLI_SECS);
		Random rand = new Random();
		//Log.d("Demo", "Stopping .....");
		return rand.nextDouble();
	}

	private static void addSomeDelay(long millis){
		//Log.d("Demo", "Progress .....");
		try {
			//Log.d("Demo", "Progress .....");
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		getNumber();
		addSomeDelay(DELAY_MILLI_SECS);
	}
}