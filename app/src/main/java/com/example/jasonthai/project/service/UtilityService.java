/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jasonthai.project.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jasonthai.project.R;
import com.example.jasonthai.project.shared.Attraction;
import com.example.jasonthai.project.shared.Constants;
import com.example.jasonthai.project.shared.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;
import static com.google.android.gms.location.LocationServices.GeofencingApi;

/**
 * A utility IntentService, used for a variety of asynchronous background
 * operations that do not necessarily need to be tied to a UI.
 */
public class UtilityService extends IntentService {
    private static final String TAG = UtilityService.class.getSimpleName();

    public static final String ACTION_GEOFENCE_TRIGGERED = "geofence_triggered";
    private static final String ACTION_LOCATION_UPDATED = "location_updated";
    private static final String ACTION_REQUEST_LOCATION = "request_location";
    private static final String ACTION_ADD_GEOFENCES = "add_geofences";
    private static final String ACTION_CLEAR_NOTIFICATION = "clear_notification";
    private static final String ACTION_CLEAR_REMOTE_NOTIFICATIONS = "clear_remote_notifications";
    private static final String ACTION_FAKE_UPDATE = "fake_update";
    private static final String EXTRA_TEST_MICROAPP = "test_microapp";

    public static IntentFilter getLocationUpdatedIntentFilter() {
        return new IntentFilter(UtilityService.ACTION_LOCATION_UPDATED);
    }

    public static void triggerWearTest(Context context, boolean microApp) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_FAKE_UPDATE);
        intent.putExtra(EXTRA_TEST_MICROAPP, microApp);
        context.startService(intent);
    }

    public static void addGeofences(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_ADD_GEOFENCES);
        context.startService(intent);
    }

    public static void requestLocation(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_REQUEST_LOCATION);
        context.startService(intent);
    }

    public static void clearNotification(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_CLEAR_NOTIFICATION);
        context.startService(intent);
    }

    public static Intent getClearRemoteNotificationsIntent(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_CLEAR_REMOTE_NOTIFICATIONS);
        return intent;
    }

    public UtilityService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (ACTION_ADD_GEOFENCES.equals(action)) {
            addGeofencesInternal();
        } else if (ACTION_REQUEST_LOCATION.equals(action)) {
            requestLocationInternal();
        } else if (ACTION_LOCATION_UPDATED.equals(action)) {
            locationUpdated(intent);
        } else if (ACTION_CLEAR_NOTIFICATION.equals(action)) {
            clearNotificationInternal();
        } else if (ACTION_CLEAR_REMOTE_NOTIFICATIONS.equals(action)) {
            clearRemoteNotifications();
        } else if (ACTION_FAKE_UPDATE.equals(action)) {
            LatLng currentLocation = Utils.getLocation(this);

            // If location unknown use test city, otherwise use closest city
            //String city = currentLocation == null ? TouristAttractions.TEST_CITY :
            //        TouristAttractions.getClosestCity(currentLocation);

            //showNotification(city,
            //        intent.getBooleanExtra(EXTRA_TEST_MICROAPP, Constants.USE_MICRO_APP));
        }
    }

    /**
     * Add geofences using Play Services
     */
    private void addGeofencesInternal() {
        Log.v(TAG, ACTION_ADD_GEOFENCES);

        if (!Utils.checkFineLocationPermission(this)) {
            return;
        }

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        //if (connectionResult.isSuccess() && googleApiClient.isConnected()) {
        //    PendingIntent pendingIntent = PendingIntent.getBroadcast(
        //            this, 0, new Intent(this, UtilityReceiver.class), 0);
        //    GeofencingApi.addGeofences(googleApiClient,
        //            TouristAttractions.getGeofenceList(), pendingIntent);
        //    googleApiClient.disconnect();
        //} else {
        //    Log.e(TAG, String.format(Constants.GOOGLE_API_CLIENT_ERROR_MSG,
        //            connectionResult.getErrorCode()));
        //}
    }


    /**
     * Called when a location update is requested
     */
    private void requestLocationInternal() {
        Log.v(TAG, ACTION_REQUEST_LOCATION);

        if (!Utils.checkFineLocationPermission(this)) {
            return;
        }

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {

            Intent locationUpdatedIntent = new Intent(this, UtilityService.class);
            locationUpdatedIntent.setAction(ACTION_LOCATION_UPDATED);

            // Send last known location out first if available
            Location location = FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                Intent lastLocationIntent = new Intent(locationUpdatedIntent);
                lastLocationIntent.putExtra(
                        FusedLocationProviderApi.KEY_LOCATION_CHANGED, location);
                startService(lastLocationIntent);
            }

            // Request new location
            LocationRequest mLocationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest,
                    PendingIntent.getService(this, 0, locationUpdatedIntent, 0));

            googleApiClient.disconnect();
        } else {
            Log.e(TAG, String.format(Constants.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
    }

    /**
     * Called when the location has been updated
     */
    private void locationUpdated(Intent intent) {
        Log.v(TAG, ACTION_LOCATION_UPDATED);

        // Extra new location
        Location location =
                intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);

        if (location != null) {
            LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());

            // Store in a local preference as well
            Utils.storeLocation(this, latLngLocation);

            // Send a local broadcast so if an Activity is open it can respond
            // to the updated location
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    /**
     * Clears the local device notification
     */
    private void clearNotificationInternal() {
        Log.v(TAG, ACTION_CLEAR_NOTIFICATION);
        NotificationManagerCompat.from(this).cancel(Constants.MOBILE_NOTIFICATION_ID);
    }

    /**
     * Clears remote device notifications using the Wearable message API
     */
    private void clearRemoteNotifications() {
        Log.v(TAG, ACTION_CLEAR_REMOTE_NOTIFICATIONS);
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {

            // Loop through all nodes and send a clear notification message
            Iterator<String> itr = Utils.getNodes(googleApiClient).iterator();
            while (itr.hasNext()) {
                Wearable.MessageApi.sendMessage(
                        googleApiClient, itr.next(), Constants.CLEAR_NOTIFICATIONS_PATH, null);
            }
            googleApiClient.disconnect();
        }
    }

    /**
     * Transfer the required data over to the wearable
     * @param attractions list of attraction data to transfer over
     */
    private void sendDataToWearable(List<Attraction> attractions) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        // Limit attractions to send
        int count = attractions.size() > Constants.MAX_ATTRACTIONS ?
                Constants.MAX_ATTRACTIONS : attractions.size();

        ArrayList<DataMap> attractionsData = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Attraction attraction = attractions.get(i);

            Bitmap image = null;
            Bitmap secondaryImage = null;

            try {
                // Fetch and resize attraction image bitmap
                image = Glide.with(this)
                        .load(attraction.imageUrl)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(Constants.WEAR_IMAGE_SIZE_PARALLAX_WIDTH, Constants.WEAR_IMAGE_SIZE)
                        .get();

                secondaryImage = Glide.with(this)
                        .load(attraction.secondaryImageUrl)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(Constants.WEAR_IMAGE_SIZE_PARALLAX_WIDTH, Constants.WEAR_IMAGE_SIZE)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(TAG, "Exception loading bitmap from network");
            }

            if (image != null && secondaryImage != null) {

                DataMap attractionData = new DataMap();

                String distance = Utils.formatDistanceBetween(
                        Utils.getLocation(this), attraction.location);

                attractionData.putString(Constants.EXTRA_TITLE, attraction.name);
                attractionData.putString(Constants.EXTRA_DESCRIPTION, attraction.description);
                attractionData.putDouble(
                        Constants.EXTRA_LOCATION_LAT, attraction.location.latitude);
                attractionData.putDouble(
                        Constants.EXTRA_LOCATION_LNG, attraction.location.longitude);
                attractionData.putString(Constants.EXTRA_DISTANCE, distance);
                attractionData.putString(Constants.EXTRA_CITY, attraction.city);
                attractionData.putAsset(Constants.EXTRA_IMAGE,
                        Utils.createAssetFromBitmap(image));
                attractionData.putAsset(Constants.EXTRA_IMAGE_SECONDARY,
                        Utils.createAssetFromBitmap(secondaryImage));

                attractionsData.add(attractionData);
            }
        }

        if (connectionResult.isSuccess() && googleApiClient.isConnected()
                && attractionsData.size() > 0) {

            PutDataMapRequest dataMap = PutDataMapRequest.create(Constants.ATTRACTION_PATH);
            dataMap.getDataMap().putDataMapArrayList(Constants.EXTRA_ATTRACTIONS, attractionsData);
            dataMap.getDataMap().putLong(Constants.EXTRA_TIMESTAMP, new Date().getTime());
            PutDataRequest request = dataMap.asPutDataRequest();
            request.setUrgent();

            // Send the data over
            DataApi.DataItemResult result =
                    Wearable.DataApi.putDataItem(googleApiClient, request).await();

            if (!result.getStatus().isSuccess()) {
                Log.e(TAG, String.format("Error sending data using DataApi (error code = %d)",
                        result.getStatus().getStatusCode()));
            }

        } else {
            Log.e(TAG, String.format(Constants.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
        googleApiClient.disconnect();
    }
}
