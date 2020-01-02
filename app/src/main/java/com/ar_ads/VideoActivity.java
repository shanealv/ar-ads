/*
 * Copyright 2019 Google LLC. All Rights Reserved.
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
package com.ar_ads;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = VideoActivity.class.getSimpleName();

    private static final double MIN_OPENGL_VERSION = 3.0;

    private FaceArFragment arFragment;
    private VideoView videoView;
    private Telemetry telemetry;


    private final HashMap<AugmentedFace, AugmentedFaceNode> faceNodeMap = new HashMap<>();

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        setContentView(R.layout.activity_video);
        telemetry = new Telemetry(getApplicationContext().getFilesDir().toString());
        arFragment = (FaceArFragment) getSupportFragmentManager().findFragmentById(R.id.face_fragment_empty);

        ArSceneView sceneView = arFragment.getArSceneView();

        // This is important to make sure that the camera stream renders first so that
        // the face mesh occlusion works correctly.
        sceneView.setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        // Setup Face Tracking
        Scene scene = sceneView.getScene();
        scene.addOnUpdateListener((FrameTime frameTime) -> {
            Collection<AugmentedFace> faceList =
                    sceneView.getSession().getAllTrackables(AugmentedFace.class);

            // Make new AugmentedFaceNodes for any new faces.
            for (AugmentedFace face : faceList) {
                if (!faceNodeMap.containsKey(face))
                    faceNodeMap.put(face, new AugmentedFaceNode(face));
            }

            // Remove any AugmentedFaceNodes associated with an AugmentedFace that stopped tracking.
            Iterator<Map.Entry<AugmentedFace, AugmentedFaceNode>> iter =
                    faceNodeMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<AugmentedFace, AugmentedFaceNode> entry = iter.next();
                AugmentedFace face = entry.getKey();
                if (face.getTrackingState() == TrackingState.PAUSED) {
                    telemetry.logFaceTracking("video");
                    iter.remove();
                }
            }
        });

        // setup video
        videoView = findViewById(R.id.video_fragment);
        videoView.setOnPreparedListener((MediaPlayer mp) -> mp.setLooping(true));
        Uri uri = Uri.parse("android.resource://com.ar_ads/" + R.raw.byu_store_video_landscape);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (ArCoreApk.getInstance().checkAvailability(activity)
                == ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE) {
            Log.e(TAG, "Augmented Faces requires ARCore.");
            Toast.makeText(activity, "Augmented Faces requires ARCore", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
