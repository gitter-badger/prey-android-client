package com.prey.geofences;


import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.prey.PreyLogger;

import java.util.ArrayList;
import java.util.List;


public class ReceiveTransitionsIntentService extends IntentService {

    public static final String TRANSITION_INTENT_SERVICE = "ReceiveTransitionsIntentService";

    public ReceiveTransitionsIntentService() {
        super(TRANSITION_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (LocationClient.hasError(intent)) {
            PreyLogger.d( "Location Services error: " + LocationClient.getErrorCode(intent));
            return;
        }

        int transitionType = LocationClient.getGeofenceTransition(intent);

        List<Geofence> triggeredGeofences = LocationClient.getTriggeringGeofences(intent);
        List<String> triggeredIds = new ArrayList<String>();

        for (Geofence geofence : triggeredGeofences) {
            PreyLogger.d("onHandle:" + geofence.getRequestId());
            processGeofence(geofence, transitionType);
            triggeredIds.add(geofence.getRequestId());
        }

        if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)
            removeGeofences(triggeredIds);
    }

    private void processGeofence(Geofence geofence, int transitionType) {
    	PreyLogger.i("processGeofence "+geofence+" transitionType"+transitionType);
/*
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());

        PendingIntent openActivityIntetnt = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        int id = Integer.parseInt(geofence.getRequestId());

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Geofence id: " + id)
                .setContentText("Transition type: " + getTransitionTypeString(transitionType))
                .setVibrate(new long[]{500, 500})
                .setContentIntent(openActivityIntetnt)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(transitionType * 100 + id, notificationBuilder.build());

        PreyLogger.d("notification built:" + id);*/
    }

    private String getTransitionTypeString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "enter";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "exit";
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return "dwell";
            default:
                return "unknown";
        }
    }

    private void removeGeofences(List<String> requestIds) {
        Intent intent = new Intent(getApplicationContext(), GeofencingService.class);

        String[] ids = new String[0];
        intent.putExtra(GeofencingService.EXTRA_REQUEST_IDS, requestIds.toArray(ids));
        intent.putExtra(GeofencingService.EXTRA_ACTION, GeofencingService.Action.REMOVE);

        startService(intent);
    }
}

