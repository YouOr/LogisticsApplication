package com.yifeng.logistics.activity.media;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.logistics.Base.BaseActivity;
import com.yifeng.logistics.R;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.util.ActionSheetDialog;
import com.yifeng.logistics.util.AudioFileUtils;
import com.yifeng.logistics.util.MediaRecordFunc;
import com.yifeng.logistics.util.PlayerUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/16.
 */
public class AddMessage extends BaseActivity implements View.OnClickListener {
    private TextView txt_center, txt_righe;
    private LinearLayout rl_back;
    private Button btn_luyin, btn_bofang;
    LinearLayout img_top2, img_top3;
    TextView submit_report;
    private int num = 999;
    EditText detail_Testimonials;
    private String[] imgSrc = {"", "", "", "", "", "", "", "", ""};
    private String picSrc = "";
    String ss = "";
    String pathUrl = Environment.getExternalStorageDirectory().getPath() + "/sfaImg/";
    ImageView photoImgBtn1, photoImgBtn2, photoImgBtn3, photoImgBtn4, photoImgBtn5, photoImgBtn6, photoImgBtn7, photoImgBtn8, photoImgBtn9;
    private String imagePath;
    private int onNum = 0,seconds;
    private long time = 0;
    private  Date timeStart = null,timeEnd = null;
    PlayerUtils mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        setTranslucentStatus(R.color.blue);
        txt_center = (TextView) findViewById(R.id.txt_center);
        txt_righe = (TextView) findViewById(R.id.txt_righe);
        rl_back = (LinearLayout) findViewById(R.id.rl_back);
        btn_luyin = (Button) findViewById(R.id.btn_luyin);
        detail_Testimonials = (EditText) findViewById(R.id.detail_Testimonials);
        photoImgBtn1 = (ImageView) findViewById(R.id.photoImgBtn1);
        photoImgBtn2 = (ImageView) findViewById(R.id.photoImgBtn2);
        photoImgBtn3 = (ImageView) findViewById(R.id.photoImgBtn3);
        photoImgBtn4 = (ImageView) findViewById(R.id.photoImgBtn4);
        photoImgBtn5 = (ImageView) findViewById(R.id.photoImgBtn5);
        photoImgBtn6 = (ImageView) findViewById(R.id.photoImgBtn6);
        photoImgBtn7 = (ImageView) findViewById(R.id.photoImgBtn7);
        photoImgBtn8 = (ImageView) findViewById(R.id.photoImgBtn8);
        photoImgBtn9 = (ImageView) findViewById(R.id.photoImgBtn9);
        img_top2 = (LinearLayout) findViewById(R.id.img_top2);
        img_top3 = (LinearLayout) findViewById(R.id.img_top3);
        btn_bofang = (Button) findViewById(R.id.btn_bofang);
        mPlayer = new PlayerUtils();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        init();
    }

    private void init() {
        txt_center.setText("信息");
        txt_righe.setText("发布");
        rl_back.setOnClickListener(this);
        txt_righe.setOnClickListener(this);
        photoImgBtn1.setOnClickListener(this);
        photoImgBtn2.setOnClickListener(this);
        photoImgBtn3.setOnClickListener(this);
        photoImgBtn4.setOnClickListener(this);
        photoImgBtn5.setOnClickListener(this);
        photoImgBtn6.setOnClickListener(this);
        photoImgBtn7.setOnClickListener(this);
        photoImgBtn8.setOnClickListener(this);
        photoImgBtn9.setOnClickListener(this);
        btn_bofang.setOnClickListener(this);
        btn_luyin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MediaRecordFunc utils = MediaRecordFunc.getInstance();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            utils.startRecordAndFile();
                            timeStart = new Date(System.currentTimeMillis());
                            btn_luyin.setText("正在录音");
                            btn_luyin.setBackgroundResource(R.color.blues);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        try {
                            utils.stopRecordAndFile();
                            timeEnd = new Date(System.currentTimeMillis());
                            btn_luyin.setText("按住录音");
                            btn_luyin.setBackgroundResource(R.color.blue);
                            time = timeEnd.getTime() - timeStart.getTime();
                            seconds = (int)(time / 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            try {
                Bitmap bitmap = null;
                if (requestCode == 1) {
                    bitmap = ImageCrop(upImage(getPhotopath()));
                    saveScalePhoto(upImage(getPhotopath()));
                    setBitmap(bitmap);
                } else if (requestCode == 2) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                        bitmap = ImageCrop(upImage(imagePath));
                        saveScalePhoto(upImage(imagePath));
                        setBitmap(bitmap);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                        bitmap = ImageCrop(upImage(imagePath));
                        saveScalePhoto(upImage(imagePath));
                        setBitmap(bitmap);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
    private void setSubmitAllMessage()
    {
        for (int i = 0; i < imgSrc.length; i++) {
            if (imgSrc[i] != "") {
                Bitmap bm = BitmapFactory.decodeFile(pathUrl + imgSrc[i]);
                ss = convertIconToString(bm);
                upImgAPI(imgSrc[i], ss);
                picSrc += imgSrc[i] + ",";
            }
        }
            if (picSrc.equals("") && detail_Testimonials.getText().toString().equals("")&&seconds==0) {
               Toast.makeText(AddMessage.this,"信息不能为空",Toast.LENGTH_SHORT).show();
            } else {

                if (seconds == 0) {
                    AddSpeechAPI(Constant.openid,detail_Testimonials.getText().toString(),picSrc.equals("")?picSrc:picSrc.substring(0,picSrc.length()-1),"",String.valueOf(seconds),"Android","");
                    Log.i("fdsfds",Constant.openid);
                } else
                {
                    try {
                        Log.i("fdsfds",Constant.openid);
                        AddSpeechAPI(Constant.openid,detail_Testimonials.getText().toString(),picSrc.equals("")?picSrc:picSrc.substring(0,picSrc.length()-1),createFileNameSpx(),String.valueOf(seconds),"Android",File2byte(AudioFileUtils.getFileBasePath() + "/FinalAudio.amr"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                new AlertDialog.Builder(AddMessage.this).setTitle("提示").setMessage("提交成功").setIcon(android.R.drawable.ic_dialog_info).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constant.CURRENT_Order = 1;
                        finish();
                    }
                }).show();
            }
        }
    private void setBitmap(Bitmap bitmap) {
        switch (num) {
            case 0:
                photoImgBtn2.setVisibility(View.VISIBLE);
                photoImgBtn1.setImageBitmap(bitmap);
                break;
            case 1:
                photoImgBtn3.setVisibility(View.VISIBLE);
                photoImgBtn2.setImageBitmap(bitmap);
                break;
            case 2:
                photoImgBtn4.setVisibility(View.VISIBLE);
                img_top2.setVisibility(view.VISIBLE);
                photoImgBtn3.setImageBitmap(bitmap);
                break;
            case 3:
                photoImgBtn5.setVisibility(View.VISIBLE);
                photoImgBtn4.setImageBitmap(bitmap);
                break;
            case 4:
                photoImgBtn6.setVisibility(View.VISIBLE);
                photoImgBtn5.setImageBitmap(bitmap);
                break;
            case 5:
                photoImgBtn7.setVisibility(View.VISIBLE);
                img_top3.setVisibility(View.VISIBLE);
                photoImgBtn6.setImageBitmap(bitmap);
                break;
            case 6:
                photoImgBtn8.setVisibility(View.VISIBLE);
                photoImgBtn7.setImageBitmap(bitmap);
                break;
            case 7:
                photoImgBtn9.setVisibility(View.VISIBLE);
                photoImgBtn8.setImageBitmap(bitmap);
                break;
            case 8:
                photoImgBtn9.setImageBitmap(bitmap);
                break;

        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(AddMessage.this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = AddMessage.this.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.txt_righe:
                setSubmitAllMessage();
                break;
            case R.id.photoImgBtn1:
                selectorModel();
                num = 0;
                break;
            case R.id.photoImgBtn2:
                selectorModel();
                num = 1;
                break;
            case R.id.photoImgBtn3:
                selectorModel();
                num = 2;
                break;
            case R.id.photoImgBtn4:
                selectorModel();
                num = 3;
                break;
            case R.id.photoImgBtn5:
                selectorModel();
                num = 4;
                break;
            case R.id.photoImgBtn6:
                selectorModel();
                num = 5;
                break;
            case R.id.photoImgBtn7:
                selectorModel();
                num = 6;
                break;
            case R.id.photoImgBtn8:
                selectorModel();
                num = 7;
                break;
            case R.id.photoImgBtn9:
                selectorModel();
                num = 8;
                break;
            case R.id.btn_bofang:
                try {
                    if (btn_bofang.getText().toString().equals("试听")) {
                        mPlayer = new PlayerUtils();
                        mPlayer.setUrlPrepare(AudioFileUtils.getAMRFilePath());
                        mPlayer.play();
                        btn_bofang.setText("停止");
                    } else {
                        mPlayer.stop();
                        btn_bofang.setText("试听");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 2); // 打开相册
    }
    private void selectorModel() {
        new ActionSheetDialog(AddMessage.this).builder().setCancelable(false).setCanceledOnTouchOutside(false).addSheetItem("从相册获取图片", ActionSheetDialog.SheetItemColor.Blue,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        if (ContextCompat.checkSelfPermission(AddMessage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AddMessage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            openAlbum();
                        }
                    }
                }).addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        getImageFromCamera();
                    }
                }).show();
    }

    private String saveScalePhoto(Bitmap bitmap) {
        // 照片全路径
        String fileName = "";
        // 文件夹路径
        String pathUrl = Environment.getExternalStorageDirectory().getPath() + "/sfaImg/";
        String imageName = createFileName();
        FileOutputStream fos = null;
        File file = new File(pathUrl);
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;
        imgSrc[num] = imageName;
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathUrl + imageName;
    }

    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 跳转至拍照界面
            Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            File out = new File(getPhotopath());
            Uri uri = Uri.fromFile(out);
            // 获取拍照后未压缩的原图片，并保存在uri路径中
            intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                intentPhote.putExtra(MediaStore.Images.Media.ORIENTATION, 180);
            startActivityForResult(intentPhote, 1);
        } else {
            Toast.makeText(AddMessage.this, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    private String getPhotopath() {
        // 照片全路径
        String fileName = "";
        // 文件夹路径
        String pathUrl = Environment.getExternalStorageDirectory() + "/mymy/";
        String imageName = "imageTest.jpg";
        File file = new File(pathUrl);
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;
        return fileName;
    }

    public static String createFileName() {
        String fileName = "";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMdd_HHmmss");
        fileName = Constant.openid + "_" + dateFormat.format(date) + ".jpg";
        return fileName;
    }
    public static String createFileNameSpx() {
        String fileName = "";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMdd_HHmmss");
        fileName = Constant.openid + "_" + dateFormat.format(date) + ".amr";
        return fileName;
    }

    @Override
    public void getResult(String result, int type) {
        super.getResult(result, type);

    }
}
