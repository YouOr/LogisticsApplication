package com.yifeng.logistics.activity.media;

import android.content.ContentValues;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.yifeng.logistics.Base.BaseActivity;
import com.yifeng.logistics.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/6.
 */
public class MyAudioRecord extends BaseActivity implements View.OnClickListener {
    private Button btn_start, btn_stop, btn_play;

    private MediaRecorder recorder;

    private MediaPlayer player;

    private File audioFile;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_media);
        init();
    }

    private void init() {
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_play = (Button) findViewById(R.id.btn_play);

        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_play.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //开始录制
            case R.id.btn_start:
                //我们需要实例化一个MediaRecorder对象，然后进行相应的设置
                recorder = new MediaRecorder();
                //指定AudioSource 为MIC(Microphone audio source ),这是最长用的
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //指定OutputFormat,我们选择3gp格式
                //其他格式，MPEG-4:这将指定录制的文件为mpeg-4格式，可以保护Audio和Video
                //RAW_AMR:录制原始文件，这只支持音频录制，同时要求音频编码为AMR_NB
                //THREE_GPP:录制后文件是一个3gp文件，支持音频和视频录制
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                //指定Audio编码方式，目前只有AMR_NB格式
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                //接下来我们需要指定录制后文件的存储路径
                File fpath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/files/");
                fpath.mkdirs();//创建文件夹
                try {
                    //创建临时文件
                    audioFile = File.createTempFile("recording", ".3gp", fpath);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                recorder.setOutputFile(audioFile.getAbsolutePath());
                //下面就开始录制了
                try {
                    recorder.prepare();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                recorder.start();
                btn_start.setText("正在录制");
                break;
            case R.id.btn_stop:
                recorder.stop();
                recorder.release();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.TITLE, "this is my first record-audio");
                values.put(MediaStore.Audio.Media.DATE_ADDED, System.currentTimeMillis());
                values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
                fileUri = this.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);

                //录制结束后，我们实例化一个MediaPlayer对象，然后准备播放
                player = new MediaPlayer();
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer arg0) {
                        //更新状态
                        btn_stop.setText("准备录制");
                    }
                });

                //准备播放
                try {
                    player.setDataSource(audioFile.getAbsolutePath());
                    player.prepare();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //更新状态
                btn_stop.setText("准备播放");
                break;
            case R.id.btn_play:
                //播放录音
                //注意，我们在录音结束的时候，已经实例化了MediaPlayer，做好了播放的准备
                player.start();
                //更新状态
                btn_play.setText("正在播放");
                break;
        }
    }
}
