package com.hokagelab.www.tugaspcd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

//import diatas untuk memanggil library android yang digunakan seperti toolbar, button, notif toast, lalu library openCV

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private CameraBridgeViewBase mCameraView;
    private Button mButton;
    //Aktifkan terdahulu fungsi button dengan nama variabel nya yang di inginkan

    Mat mEdge;
    //pada variabel mat adalah fungsi untuk memfilter citra.

    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    mCameraView.enableView();
                    break;
                    //ini untuk membuka kamera view
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        //pada menu toolbar ini terlebih dahulu diberi variabel untuk mengaktifkan Toolbar pada android

        if (OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(), "Sukses OpenCV", Toast.LENGTH_SHORT).show();
            //pada menu toast ini akan memberitahukan jika mcameraview nya dapat membuka kamera bersama library opencv maka muncul alert toast sukses
        } else {
            Toast.makeText(getApplicationContext(), "Gagal OpenCV", Toast.LENGTH_SHORT).show();
            //pada menu toast ini akan memberitahukan jika mcameraview nya tidak dapat membuka kamera bersama library opencv maka muncul alert toast tidak sukses
        }

        mButton = (Button) findViewById(R.id.ambil);
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv);
        //fungsi ini mencari id opencv yang kegunaan nya menampilkan tampilan camera tersebut pada id open cv ditampilan
        mCameraView.setVisibility(SurfaceView.VISIBLE);
        //menampilkan camera
        mCameraView.setCvCameraViewListener(this);
        setSupportActionBar(myToolbar);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraView != null)
            mCameraView.disableView();
        mCameraView.disableFpsMeter();
    }
    //fungsi pada class onpause jika terjadi pada activity android membuka aplikasi lain nya maka aplikasi ini yang dibuka akan disable kan camera namun tetap berjalan pada cache android

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraView != null)
            mCameraView.disableView();
        mCameraView.disableFpsMeter();
    }
    //pada kelas jika aplikasi ini diclose makan camera di matikan dan activity pada os dimatikan

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(), "Sukses OpenCV", Toast.LENGTH_SHORT).show();
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Toast.makeText(getApplicationContext(), "Gagal Sukses OpenCV", Toast.LENGTH_SHORT).show();
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallBack);
        }
    }
    //ketika pengguna mengklik pada about maka activity pada activity selanjut nya di pause lalu ketika ingin kembali pada kamera yang sebelum nya di menu about maka secara otomatis muncul alert ketika kembali pada menu camera


    @Override
    public void onCameraViewStarted(int width, int height) {
        mEdge = new Mat(height, width, CvType.CV_8UC3);
    }
    //pada kelas ini ketika on kamera maka variabel mat tadi akan mengenerate atau memfilter citra yang ada

    @Override
    public void onCameraViewStopped() {
        mEdge.release();
    }
    //pada kelas ini ketika kamera sedang on lalu view baru maka akan memfilter edge yang baru

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame cvCameraViewFrame) {
        Imgproc.Canny(cvCameraViewFrame.gray(), mEdge, 80, 100);
        return mEdge;
    }
    //kelas ini jika on camera maka otomatis memfilter citra nya menjadi gray

    //pemanggilan menu res pada pojok atas yang terdiri about dan insert gambar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //pada kelas ini dibawah ini ketika user mengklik maka muncul item menu tersebut misal menu about
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, About.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
