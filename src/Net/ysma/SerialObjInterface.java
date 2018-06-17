package Net.ysma;

import org.json.JSONArray;

import java.util.Date;

public interface SerialObjInterface
{
	Integer APPSERVER = 0;
	Integer PERSISTANCE = 1;
	Integer LOGIC = 2;
	Integer DEVICE = 3;
	Integer UI = 4;
	Integer LOGICSETUP = 5;
	Integer ERRORPROCESS = 6;
	public void setSourceIp(String str);
	public String getSourceIp();
	public void setSourcePort(Integer str);
	public Integer getSourcePort();
	public void setTargetIp(String str);
	public String getTargetIp();
	public void setTargetPort(Integer str);
	public Integer getTargetPort();
	public void setDataFrameTime();
	public Date getDataFrameTime();
	public boolean setDataFrame(JSONArray arr);
	public JSONArray getDataFrame();
	public boolean setRequestType(Integer rt);
	public Integer getRequestType();
}
