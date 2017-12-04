package com.qinqi.debugtoolbox;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class LocalDataStorage {

	private SharedPreferences mSharedPreferences = null;
	private SharedPreferences.Editor mEditor = null;

	public LocalDataStorage(String name, Context context) {
		mSharedPreferences = context.getSharedPreferences(
				name,
				Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public SharedPreferences.Editor getmEditor() {
		return mEditor;
	}

	public void setmEditor(SharedPreferences.Editor mEditor) {
		this.mEditor = mEditor;
	}

	public void resetStorage() {
		mEditor.clear();
		mEditor.commit();
	}
	
	public Map<String, ?> getAll(){
		return mSharedPreferences.getAll();
	}
	
	public void setIntProperty(String key, int param){
		mEditor.putInt(key, param);
		mEditor.commit();
	}
	
	public int getIntProperty(String key, int defValue){
		return mSharedPreferences.getInt(key, defValue);
	}
	
	public void setBooleanProperty(String key, boolean param){
		mEditor.putBoolean(key, param);
		mEditor.commit();
	}
	
	public boolean getBooleanProperty(String key, boolean defValue){
		return mSharedPreferences.getBoolean(key, defValue);
	}
	
	public void setStringProperty(String key, String value){
		mEditor.putString(key, value);
		mEditor.commit();
	}
	
	public String getStringProperty(String key, String value){
		return mSharedPreferences.getString(key, value);
	}
	
	public void setLongProperty(String key, Long value){
		mEditor.putLong(key, value);
		mEditor.commit();
	}
	
	public long getLongProperty(String key, Long value){
		return mSharedPreferences.getLong(key, value);
	}
	
	public boolean hasProperty(String key){
		return mSharedPreferences.contains(key);
	}
}