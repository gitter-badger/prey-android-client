package com.prey.geofences;


import com.google.android.gms.location.Geofence;

import java.io.Serializable;

public class PreyGeofence implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int ONE_MINUTE = 60000;

    private int id;
    private double latitude;
    private double longitude;
    private float radius;
    private int transitionType;

    public String toString(){
    	StringBuffer sb=new StringBuffer("");
    	sb.append("  id            :").append(id);
    	sb.append("\nlatitude      :").append(latitude);
    	sb.append("\nlongitude     :").append(longitude);
    	sb.append("\nradius        :").append(radius);
    	sb.append("\ntransitionType:").append(transitionType);
    	return sb.toString();
    }
    
    public PreyGeofence(int id, double latitude, double longitude, float radius, int transitionType) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.transitionType = transitionType;
    }

    public Geofence toGeofence() {
        return new Geofence.Builder()
                .setRequestId(String.valueOf(id))
                .setTransitionTypes(transitionType)
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(ONE_MINUTE)
                .build();
    }
}
