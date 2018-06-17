package Net.ysma;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.Date;

public class SerialObj implements SerialObjInterface, Serializable {
    /**
     * ID pour permettre la vérification de la classe lors de la dé-Serialization
     */
    private static final long serialVersionUID = -8973178568329153795L;

    private String _strIPsrc;
    private Integer _strPortsrc;
    private String _strIPtarget;
    private Integer _strPorttarget;
    private Date _dateTimeSent;
    private JSONArray _jsnData;
    private Integer _reqTargetType;
    private String _reqType;
    private boolean _booNeedFeedback;

    public SerialObj() {
        this._reqTargetType = 10;
        _jsnData = new JSONArray();
        _booNeedFeedback = false;
    }

    public void setSourceIp(String str) {
        _strIPsrc = str;
    }

    public String getSourceIp() {
        return _strIPsrc;
    }

    public void setSourcePort(Integer str) {
        _strPortsrc = str;
    }

    public Integer getSourcePort() {
        return _strPortsrc;
    }

    public void setTargetIp(String str) {
        _strIPtarget = str;
    }

    public String getTargetIp() {
        return _strIPtarget;
    }

    public void setTargetPort(Integer str) {
        _strPorttarget = str;
    }

    public Integer getTargetPort() {
        return _strPorttarget;
    }

    public void setDataFrameTime() {
        _dateTimeSent = new Date();
    }

    public Date getDataFrameTime() {
        return _dateTimeSent;
    }

    public void setDataFrame(JSONArray arr) {
        _jsnData = arr;
    }

    public JSONArray getDataFrame() {
        return _jsnData;
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

    public void setIfFeedbackNeeded(boolean boo)
    {
        _booNeedFeedback = boo;
    }

    public boolean getIfFeedbackNeeded()
    {
        return _booNeedFeedback;
    }

}
