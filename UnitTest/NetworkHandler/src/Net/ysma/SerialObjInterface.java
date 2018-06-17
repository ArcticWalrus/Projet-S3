package Net.ysma;

import org.json.JSONArray;

import java.util.Date;

public interface SerialObjInterface
{
	public Integer UNKNOWN = 0;
	public Integer INPUTVALUE = 1;
	public Integer OUTPUTCHANGE = 2;
	public Integer DATAPROCESSING = 3;
	public Integer UI = 4;
	public Integer LOGICSETUP = 5;
	public Integer ERRORPROCESS = 6;
	public void setSourceIp(String str);
	public String getSourceIp();
	public void setTargetIp(String str);
	public String getTargetIp();
	public void setDataFrameTime();
	public Date getDataFrameTime();
	public boolean setDataFrame(JSONArray arr);
	public JSONArray getDataFrame();
	public boolean setRequestType(Integer rt);
	public Integer getRequestType();
}
