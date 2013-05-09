package org.gnuton.droidxperiment;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class SplashScreen extends Activity {
	private static final int SPLASH_DURATION = 2; // 2 seconds
	private static final String TAG = "SplashScreen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		// Start timer and launch main activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}

	private class IntentLauncher extends Thread {
		@Override
		/**
		 * Sleep for some time and than start new activity.
		 */
		public void run() {
			try {
				// Sleeping
				Thread.sleep(SPLASH_DURATION * 1000);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			// Start main activity
			SplashScreen ss = SplashScreen.this;
			Intent intent = new Intent(ss, MainActivity.class);
			ss.startActivity(intent);
			ss.finish();
		}
	}
}
