package com.yifeng.logistics.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	private static Toast myToast;
	/*private static Handler mhandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				myToast.setText(str);
				myToast.show();
				break;
			case 2:
				myToast = Toast.makeText(mContext,str,Toast.LENGTH_SHORT);
				myToast.show();
				break;

			default:
				break;
			}
		}
	};*/

	public static void DisplayToast(Context mContext,String str){
//		Message msg = new Message();
		if(myToast != null){
			myToast.setText(str);
		}else{
			myToast = Toast.makeText(mContext,str,Toast.LENGTH_SHORT);
		}
		myToast.show();
	}

	public static void DisplayToast(Context mContext,String str,int showtime){
//		Message msg = new Message();
		if(myToast != null){
			myToast.setText(str);
		}else{
			myToast = Toast.makeText(mContext,str,showtime);
		}
		myToast.show();
	}
	
}
