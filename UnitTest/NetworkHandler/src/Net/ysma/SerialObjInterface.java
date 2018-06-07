package Net.ysma;

import org.json.JSONArray;

import java.util.Date;

public interface SerialObjInterface
{	
	public void setSourceIp(String str);
	public String getSourceIp();
	public void setTargetIp(String str);
	public String getTargetIp();
	public void setDataFrameTime();
	public Date getDataFrameTime();
	public boolean setDataFrame(JSONArray arr);
	public JSONArray getDataFrame();
	public void setRequestType(Integer rt);
	public Integer getRequestType();
}
