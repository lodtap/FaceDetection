package linzhurui.facedetection002.activity;

import linzhurui.facedetection002.camera.CameraInterface;
import linzhurui.facedetection002.camera.CameraInterface.CamOpenOverCallback;
import linzhurui.facedetection002.camera.preview.CameraSurfaceView;
import linzhurui.facedetection002.mode.GoogleFaceDetect;
import linzhurui.facedetection002.R;
import linzhurui.facedetection002.ui.FaceView;
import linzhurui.facedetection002.util.DisplayUtil;
import linzhurui.facedetection002.util.EventUtil;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

public class CameraActivity extends Activity{
	private static final String TAG = "yanzi";

	//读写权限
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE};
	//请求状态码
	private static int REQUEST_PERMISSION_CODE = 1;

	CameraSurfaceView surfaceView = null;
	ImageButton shutterBtn;
	ImageButton switchBtn;
	FaceView faceView;
	float previewRate = -1f;
	private MainHandler mMainHandler = null;
	GoogleFaceDetect googleFaceDetect = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_camera);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
			}
		}
		if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
		}
		initUI();
		initViewParams();
		mMainHandler = new MainHandler();
		googleFaceDetect = new GoogleFaceDetect(getApplicationContext(), mMainHandler);


		shutterBtn.setOnClickListener(new BtnListeners());
		switchBtn.setOnClickListener(new BtnListeners());
		mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	private void initUI(){
		surfaceView = (CameraSurfaceView)findViewById(R.id.camera_surfaceview);
		shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
		switchBtn = (ImageButton)findViewById(R.id.btn_switch);
		faceView = (FaceView)findViewById(R.id.face_view);
	}
	private void initViewParams(){
		LayoutParams params = surfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
		surfaceView.setLayoutParams(params);


	}

	private class BtnListeners implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_shutter:
				takePicture();
				break;
			case R.id.btn_switch:
				switchCamera();
				break;
			default:break;
			}
		}

	}
	private  class MainHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what){
			case EventUtil.UPDATE_FACE_RECT:
				Face[] faces = (Face[]) msg.obj;
				faceView.setFaces(faces);
				break;
			case EventUtil.CAMERA_HAS_STARTED_PREVIEW:
				startGoogleFaceDetect();
				break;
			}
			super.handleMessage(msg);
		}

	}

	private void takePicture(){
		CameraInterface.getInstance().doTakePicture();
		mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
	}
	private void switchCamera(){
		stopGoogleFaceDetect();
		int newId = (CameraInterface.getInstance().getCameraId() + 1)%2;
		CameraInterface.getInstance().doStopCamera();
		CameraInterface.getInstance().doOpenCamera(null, newId);
		CameraInterface.getInstance().doStartPreview(surfaceView.getSurfaceHolder(), previewRate);
		mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);

//		startGoogleFaceDetect();

	}
	private void startGoogleFaceDetect(){
		Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
		if(params.getMaxNumDetectedFaces() > 0){
			if(faceView != null){
				faceView.clearFaces();
				faceView.setVisibility(View.VISIBLE);
			}
			CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(googleFaceDetect);
			CameraInterface.getInstance().getCameraDevice().startFaceDetection();
		}
	}
	private void stopGoogleFaceDetect(){
		Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
		if(params.getMaxNumDetectedFaces() > 0){
			CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(null);
			CameraInterface.getInstance().getCameraDevice().stopFaceDetection();
			faceView.clearFaces();
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_CODE) {
			for (int i = 0; i < permissions.length; i++) {
				Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
			}
		}
	}


}
