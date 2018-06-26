package Net.ysma;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.Date;

public class SerialObj implements SerialObjInterface, Serializable {
	/**
	 * ID pour permettre la vérification de la classe lors de la dé-Serialization
	 */
	private static final long serialVersionUID = -8973178568329153795L;

	private String _strIPSrc;
	private Integer _strPortSrc;
	private String _strIPTarget;
	private Integer _strPortTarget;
	private Date _dateTimeSent;
	private String _jsnData;
	private Integer _reqTargetType;
	private String _reqType;
	private boolean _booNeedFeedback;

	public SerialObj() {
		this._reqTargetType = 0;
		_jsnData = new String();
		_booNeedFeedback = false;
	}

	public void setSourceIp(String str) {
		_strIPSrc = str;
	}

	public String getSourceIp() {
		return _strIPSrc;
	}

	public void setSourcePort(Integer str)
	{
		_strPortSrc = str;
	}

	public Integer getSourcePort()
	{
		return _strPortSrc;
	}

	public void setTargetIp(String str) {
		_strIPTarget = str;
	}

	public String getTargetIp() {
		return _strIPTarget;
	}

	public void setTargetPort(Integer str)
	{
		_strPortTarget = str;
	}

	public Integer getTargetPort()
	{
		return _strPortTarget;
	}

	public void setDataFrameTime() {
		_dateTimeSent = new Date();
	}

	public Date getDataFrameTime() {
		return _dateTimeSent;
	}

	public void setDataFrame(JSONArray arr) {
		_jsnData = arr.toString();
	}

	public JSONArray getDataFrame() {
		try{
			JSONArray jarTemp = new JSONArray(_jsnData);
			return jarTemp;
		}
		catch (Exception e)
		{
			return new JSONArray();
		}
	}

	public void setTargetType(Integer rt) {
		_reqTargetType = rt;
	}

	public Integer getTargetType() {
		return _reqTargetType;
	}

	public void setRequestType(String rt) {
		_reqType = rt;
	}

	public String getRequestType() {
		return _reqType;
	}

	public void setIfFeedbackNeeded(boolean boo) {
		_booNeedFeedback = boo;
	}

	public boolean getIfFeedbackNeeded() {
		return _booNeedFeedback;
	}
}
