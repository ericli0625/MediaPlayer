package com.example.erichcli.mediaplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.content.Intent;


public class MyActivity extends Activity {

    public static final int REQUEST_VIDEO_CAPTURE = 200;
    private static final int CHOOSE_VIDEO_FILE = 100;
    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";

    private VideoView mVideoView;
    private MediaController mMediaController;
    private Button mButton,mButtonRecorder;
    private Uri mVideoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mVideoView = (VideoView)findViewById(R.id.videoView);
        mMediaController = (MediaController)findViewById(R.id.mediaController);
        mButton = (Button)findViewById(R.id.button);
        mButtonRecorder = (Button)findViewById(R.id.button2);
        mVideoUri = null;

        mMediaController = new MediaController(this);
        mMediaController.setAnchorView(mVideoView);

        mVideoView.setMediaController(mMediaController);

        mButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // 建立 "選擇檔案 Action" 的 Intent
                Intent intent = new Intent( Intent.ACTION_PICK );

                // 過濾檔案格式
                intent.setType( "video/*" );

                // 建立 "檔案選擇器" 的 Intent  (第二個參數: 選擇器的標題)
                Intent destIntent = Intent.createChooser( intent, "Choose Video File" );

                // 切換到檔案選擇器 (它的處理結果, 會觸發 onActivityResult 事件)
                startActivityForResult( destIntent, CHOOSE_VIDEO_FILE );

            }
        });

        mButtonRecorder.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener(){
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int arg1, int arg2){
                if(arg1 == mediaPlayer.MEDIA_ERROR_UNKNOWN){
                    Toast.makeText(MyActivity.this, "ERROR_UNKNOWN.", Toast.LENGTH_LONG).show();
                }else if(arg1 == mediaPlayer.MEDIA_ERROR_SERVER_DIED){
                    Toast.makeText(MyActivity.this, "ERROR_SERVER_DIED.", Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mediaPlayer){
                mediaPlayer.seekTo(0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_VIDEO_FILE){
            if ( resultCode == RESULT_OK ){
                mVideoUri = data.getData();
                Toast.makeText(this, "Video location:\n" + data.getData(), Toast.LENGTH_LONG).show();
                if( mVideoUri != null ){
                    mVideoView.setVideoURI(mVideoUri);
                }else{
                    setTitle("Error Address");
                }
            }else{
                setTitle("Cancel");
            }
        }

        if(requestCode == REQUEST_VIDEO_CAPTURE){
            if ( resultCode == RESULT_OK ){
                mVideoUri = data.getData();
                Toast.makeText(this, "Video location:\n" + data.getData(), Toast.LENGTH_LONG).show();
                if( mVideoUri != null ){
                    //mVideoView.setVideoURI(mVideoUri);
                }else{
                    setTitle("Error Address");
                }
            }else{
                setTitle("Cancel");
            }
        }

        if(!mVideoView.isPlaying()) {
            mVideoView.start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mVideoView.setVideoURI(mVideoUri);

    }

}
