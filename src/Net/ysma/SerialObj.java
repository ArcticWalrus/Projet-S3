package Net.ysma;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.Date;

public class SerialObj implements SerialObjInterface, Serializable
{
	/**
	 *  ID pour permettre la vérification de la classe lors de la dé-Serialization
	 */
	private static final long serialVersionUID = -8973178568329153795L;
	
	String _strIPsrc;
	Integer _strPortsrc;
	String _strIPtarget;
	Integer _strPorttarget;
	Date _dateTimeSent;
	JSONArray _jsnData;
	Integer _reqType;
	
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

	public void setSourcePort(Integer str)
	{
		_strPortsrc = str;
	}

	public Integer getSourcePort()
	{
		return _strPortsrc;
	}
	
	public void setTargetIp(String str)
	{
		_strIPtarget = str;
	}
	
	public String getTargetIp()
	{
		return _strIPtarget;
	}

	public void setTargetPort(Integer str)
	{
		_strPorttarget = str;
	}

	public Integer getTargetPort()
	{
		return _strPorttarget;
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
	
	public boolean setRequestType(Integer rt)
	{
		if((rt <= ERRORPROCESS) && (rt >= APPSERVER))
			_reqType = rt;
		else
			return false;
		return true;
	}
	
	public Integer getRequestType()
	{
		return _reqType;
	}
}
