package Net.ysma;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONArray;

public class SerialObj implements SerialObjInterface, Serializable
{
	/**
	 *  ID pour permettre la v�rification de la classe lors de la d�-Serialization
	 */
	private static final long serialVersionUID = -8973178568329153795L;
	
	String _strIPsrc;
	String _strIPtarget;
	Date _dateTimeSent;
	JSONArray _jsnData;
	int _reqType;
	
	public SerialObj()
	{
		this._reqType = 0;
	}
	
	public void setSourceIp(String str)
	{
		_strIPsrc = str;
	}
	
	public String getSourceIp()
	{
		return _strIPsrc;
	}
	
	public void setTargetIp(String str)
	{
		_strIPtarget = str;
	}
	
	public String getTargetIp()
	{
		return _strIPtarget;
	}
	
	public void setDataFrameTime()
	{
		_dateTimeSent = new Date();
	}
	
	public Date getDataFrameTime()
	{
		return _dateTimeSent;
	}
	
	public boolean setDataFrame(JSONArray arr)
	{
		try
		{
			_jsnData = new JSONArray(arr.toString());
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public JSONArray getDataFrame()
	{
		return _jsnData;
	}
	
	public void setRequestType(int rt)
	{
			_reqType = rt;
	}
	
	public int getRequestType()
	{
		return _reqType;
	}
}
