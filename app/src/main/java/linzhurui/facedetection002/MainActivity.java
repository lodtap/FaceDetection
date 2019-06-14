package linzhurui.facedetection002;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
//        }
    }
}
