/*
 * Copyright (C) 2014 3IN Consulting Group 
 * Created by: 3IN Consulting - Mehmet Saylica
 * Date: September 2014
 */
package com.trin.nilmobile;

import android.content.Context;
import android.os.PowerManager;

//Wake device to receive GCM message
public abstract class WakeLocker {
	private static PowerManager.WakeLock wakeLock;

	public static void acquire(Context context) {
		if (wakeLock != null)
			wakeLock.release();

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "WakeLock");
		wakeLock.acquire();
	}

	public static void release() {
		if (wakeLock != null)
			wakeLock.release();
		wakeLock = null;
	}
}
