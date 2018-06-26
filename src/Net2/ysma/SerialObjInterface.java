package Net2.ysma;

import org.json.JSONArray;

import java.util.Date;

public interface SerialObjInterface {
	public Integer UNKNOWN = 0;
	public Integer PERSISTANCE = 1;
	public Integer LOGIC = 2;
	public Integer DEVICE = 3;
	public Integer UI = 4;
	public Integer APPSERVER = 5;
	public Integer ERRORPROCESS = 6;

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

	public void setDataFrame(JSONArray arr);

	public JSONArray getDataFrame();

	public void setTargetType(Integer rt);

	public Integer getTargetType();

	public void setRequestType(String rt);

	public String getRequestType();

	public void setIfFeedbackNeeded(boolean boo);

	public boolean getIfFeedbackNeeded();
}
