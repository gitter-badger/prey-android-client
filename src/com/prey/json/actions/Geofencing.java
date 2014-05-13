/*******************************************************************************
 * Created by Orlando Aliaga
 * Copyright 2013 Fork Ltd. All rights reserved.
 * License: GPLv3
 * Full license at "/LICENSE"
 ******************************************************************************/
package com.prey.json.actions;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

 
 
 



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.location.Geofence;
import com.prey.PreyConfig;
import com.prey.PreyLogger;
import com.prey.actions.HttpDataService;
import com.prey.actions.geo.ProxAlertActivity;
import com.prey.actions.observer.ActionResult;
 
 
import com.prey.geofences.GeofencingService;
import com.prey.geofences.PreyGeofence;
import com.prey.json.JsonAction;
import com.prey.json.UtilJson;
import com.prey.net.PreyWebServices;

public class Geofencing extends JsonAction{

 
	public void start(Context ctx, List<ActionResult> lista, JSONObject parameters) {	
		double latitude = Double.parseDouble("-32.7521");
		double longitude = Double.parseDouble("-70.7193");
		float radius = Float.parseFloat("100");

		int transitionType = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT;
		int mId = 0;

		PreyGeofence myGeofence = new PreyGeofence(mId, latitude, longitude, radius, transitionType);

		Intent geofencingService = new Intent(ctx, GeofencingService.class);

		geofencingService.putExtra(GeofencingService.EXTRA_ACTION, GeofencingService.Action.ADD);
		geofencingService.putExtra(GeofencingService.EXTRA_GEOFENCE, myGeofence);

		ctx.startService(geofencingService);
	}

	public void start2(Context ctx, List<ActionResult> lista, JSONObject parameters) {
		
		try {
			PreyConfig preyConfig = PreyConfig.getPreyConfig(ctx);
			preyConfig.setMissing(true);
			
			 
			 
			
			String origin = parameters.getString("origin");
			String[] centralPoints = origin.split(",");
			String longitude =  centralPoints[0] ;
			String latitude =  centralPoints[1] ;
			String radius = parameters.getString("radius");


			Bundle bundle = new Bundle();
			bundle.putDouble("longitude",Double.parseDouble( longitude));
			bundle.putDouble("latitude",Double.parseDouble( latitude));
			bundle.putFloat ("radius",  Float.parseFloat( radius));
			bundle.putInt("type",ProxAlertActivity.START);
 
			
		
			Intent popup = new Intent(ctx, ProxAlertActivity.class);
			popup.putExtras(bundle);
			popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startActivity(popup);
			
 

		 PreyLogger.i("Finish Geofencing start");
			
		} catch (JSONException e) {
			PreyLogger.e("Error en json:" + e.getMessage(), e);
			PreyWebServices.getInstance().sendNotifyActionResultPreyHttp(ctx,UtilJson.makeMapParam("start","geofence","failed",e.getMessage()));
		}

	}

	public void stop(Context ctx, List<ActionResult> lista, JSONObject parameters) {
	 
 

			Bundle bundle = new Bundle();
 
			bundle.putInt("type",ProxAlertActivity.STOP);
		
			Intent popup = new Intent(ctx, ProxAlertActivity.class);
			popup.putExtras(bundle);
			popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startActivity(popup);

		 
			 PreyLogger.i("Finish Geofencing stop");
		 
	}

	@Override
	public HttpDataService run(Context ctx, List<ActionResult> list, JSONObject parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
