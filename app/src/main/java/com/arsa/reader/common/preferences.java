package com.arsa.reader.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

public class preferences {
	static SharedPreferences pref;
    public preferences(Context con)
    {
    	super();
		pref = con.getSharedPreferences("arsaa", 0); // 0 - for private mode
    }
    public  void setstring(String name,String value)
    {
    	
    	Editor editor = pref.edit();
    	editor.putString(name, value);
    	editor.commit();
    	
    }
	public  void setstringset(String name, Set<String> value)
	{

		Editor editor = pref.edit();
		editor.putStringSet(name, value);
		editor.commit();

	}
	public void delete_key(String key)
   {
	   Editor editor = pref.edit();
	   editor.remove(key);
	   editor.apply();
   }
   public  String getstring(String name)
	{
		try
		{
			return pref.getString(name, null);
		}
		catch(Exception exception)
		{
			return null;
		}
	}
	public  Set<String> getstringset(String name)
	{
		try
		{
			return pref.getStringSet(name, null);
		}
		catch(Exception exception)
		{
			return null;
		}
	}
}
