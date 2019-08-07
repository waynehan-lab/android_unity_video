package com.pvr.player;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends UnityPlayerActivity {

    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private MediaPlayer mMediaPlayer;
    private boolean mIsFrameAvailable = false;
    private final String TAG = "TestPlayer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSurface != null){
            mSurface.release();
            mSurface = null;
        }
        if(mSurfaceTexture != null){
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initSurface() {
        Log.i(TAG,"initSurface");
        int textures[] = new int[1];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textures, 0);
        int textureId = textures[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        mSurfaceTexture = new SurfaceTexture(textureId);
        mSurfaceTexture.setDefaultBufferSize(640, 360);
        mSurface = new Surface(mSurfaceTexture);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mIsFrameAvailable = true;
                Log.i(TAG,"onFrameAvailable");
                UnityPlayer.UnitySendMessage("Quad", "UpdateTexImage", "");
            }
        });
        initMediaPlayer();
    }

    private void initMediaPlayer(){
        Log.i(TAG,"initMediaPlayer");
        if(mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
        }
        if(mSurface != null){
            mMediaPlayer.setSurface(mSurface);
        }
        try {
            File file = new File("/sdcard/1.mp4");
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(Uri.fromFile(file).toString());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG,"MediaPlayer onPrepared");
                mMediaPlayer.start();
            }
        });
    }

    public void updateTexImage() {
        if (mIsFrameAvailable) {
            Log.i(TAG,"updateTexImage");
            mSurfaceTexture.updateTexImage();
        }
        mIsFrameAvailable = false;
    }

}