package com.example.facedetector;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facedetector.Helper.GraphicOverlay;
import com.example.facedetector.Helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListenerAdapter;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

	CameraView cameraView;
	GraphicOverlay graphicOverlay;
	Button button;
	AlertDialog dialog;

	@Override
	protected void onResume() {
		super.onResume();
		cameraView.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		cameraView.stop();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cameraView = findViewById(R.id.camera_view);
		graphicOverlay = findViewById(R.id.graphic_overlay);
		button = findViewById(R.id.btn_detect);

		dialog = new SpotsDialog.Builder().setContext(this)
				.setMessage("Please Wait")
				.setCancelable(false)
				.build();

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				cameraView.start();
				cameraView.captureImage();
				graphicOverlay.clear();
			}
		});

		cameraView.addCameraKitListener(new CameraKitEventListenerAdapter() {
			@Override
			public void onEvent(CameraKitEvent event) {
				super.onEvent(event);
			}

			@Override
			public void onError(CameraKitError error) {
				super.onError(error);
			}

			@Override
			public void onImage(CameraKitImage image) {
				super.onImage(image);
				dialog.show();
				Bitmap bitmap = image.getBitmap();
				bitmap = Bitmap.createScaledBitmap(bitmap , cameraView.getWidth() , cameraView.getHeight() , false);
				cameraView.stop();

				startDetector(bitmap);
			}
		});
	}

	private void startDetector(Bitmap bitmap) {
		InputImage image = InputImage.fromBitmap(bitmap , 0);
		FaceDetectorOptions options = new FaceDetectorOptions.Builder().build();
		FaceDetector detector = FaceDetection.getClient(options);
		Task<List<Face>> result = detector.process(image)
				.addOnSuccessListener(new OnSuccessListener<List<Face>>() {
					@Override
					public void onSuccess(List<Face> faces) {
						processResult(faces);
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_SHORT).show();
					}
				});

	}

	private void processResult(List<Face> inputImages) {
		int count = 0;
		for (Face face : inputImages){
			Rect bounds = face.getBoundingBox();
			RectOverlay rect = new RectOverlay(graphicOverlay , bounds);
			graphicOverlay.add(rect);
			count++;
		}
		dialog.dismiss();
		Toast.makeText(this, String.format("Detected %d faces in image" , count), Toast.LENGTH_SHORT).show();
	}
}