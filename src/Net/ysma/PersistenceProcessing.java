package Net.ysma;

import maxmamort.gel.persistence.errorManager;
import maxmamort.gel.persistence.persistantLayer;
import org.json.JSONArray;
import org.json.JSONObject;

import static Net.ysma.SerialObjInterface.ERRORPROCESS;
import static Net.ysma.SerialObjInterface.PERSISTANCE;

public class PersistenceProcessing extends InfoProcess {

	public PersistenceProcessing(SerialObj obj) {
		super(obj);
	}

	public SerialObj Aiguilleur(SerialObj temp) {
		System.out.println("Aiguilleur de persistance");

		SerialObj seoOutbound = new SerialObj();

		Integer tempInt = temp.getTargetType();
		if (tempInt.equals(PERSISTANCE)) {
			seoOutbound.setDataFrame(to_persistance(temp));
		} else if (tempInt.equals(ERRORPROCESS)) {
			to_error_logger(temp);
		} else {
			System.out.println("2. Ya done fucked up " + tempInt + "   " + ERRORPROCESS);
		}

		return seoOutbound;
	}

	protected JSONArray to_persistance(SerialObj sro_temp) {

		JSONArray json = new JSONArray();
		String tempValue = sro_temp.getRequestType();
		persistantLayer pl = new persistantLayer();
		JSONObject jo = sro_temp.getDataFrame().getJSONObject(0);
		if ("addInput".equalsIgnoreCase(tempValue)) {
			json.put(new JSONObject().put("serintinput", pl.addInput(jo.getString("inputName"), jo.getDouble("defaultValue"), jo.getInt("sensorType"))));
		} /*else if ("getInputIDForIO".equalsIgnoreCase(tempValue)) {
			json = pl.getInputIDForIO(jo.getString("cip"));
		}*/ else if ("getIOByDevice".equalsIgnoreCase(tempValue)) {
			json = pl.getIOByDevice(jo.getString("deviceid"));
		} else if ("createDevice".equalsIgnoreCase(tempValue)) {
			pl.createDevice(jo.getString("MAC"), jo.getString("cip"), jo.getString("name"));
		} else if ("createUser".equalsIgnoreCase(tempValue)) {
			pl.createUser(jo.getString("cip"));
		} else if ("getDevicesByUser".equalsIgnoreCase(tempValue)) {
			json = pl.getDevicesByUser(jo.getString("cip"));
		} else if ("getIOForUser".equalsIgnoreCase(tempValue)) {
			json = pl.getIOForUser(jo.getString("cip"));
		} else if ("getIo".equalsIgnoreCase(tempValue)) {
			pl.getIo();
		} else if ("getDevices".equalsIgnoreCase(tempValue)) {
			pl.getDevices();
		} else if ("getUsers".equalsIgnoreCase(tempValue)) {
			pl.getUsers();
		} else if ("cleardb".equalsIgnoreCase(tempValue)) {
			pl.cleardb();
		} else if ("createCondition".equalsIgnoreCase(tempValue)) {
			int operation = jo.getInt("operation");
            String mac = jo.getString("mac");

            pl.createCondition(mac, jo.getInt("pin1"), jo.getInt("pin2"), jo.getInt("pin3"), operation);
		} else if ("renameDevice".equalsIgnoreCase(tempValue)) {
			pl.renameDevice(jo.getString("MAC"), jo.getString("name"));
		} else if ("renameIO".equalsIgnoreCase(tempValue)) {
			pl.renameIO(jo.getInt("IOID"), jo.getString("name"));
		} else if ("updateConfigurationBit".equalsIgnoreCase(tempValue)) {
			pl.updateConfigurationBit(jo.getInt("IOID"), jo.getInt("ConfigurationBit"));
		} else if ("updatePhysicalMapping".equalsIgnoreCase(tempValue)) {
			pl.updatePhysicalMapping(jo.getInt("IOID"), jo.getInt("physicalPin"));
		} else if ("updateDeviceIP".equalsIgnoreCase(tempValue)) {
			pl.updateDeviceIP(jo.getString("MAC"), jo.getString("IP"));
		} else if ("createIO".equalsIgnoreCase(tempValue)) {
			json = new JSONArray().put(new JSONObject().put("IOID",
					pl.createIO(jo.getString("IOName"), jo.getString("DeviceId"), jo.getInt("physicalPin"), jo.getInt("ConfigurationBit"))));
		} else if ("updateOutputValue".equalsIgnoreCase(tempValue)) {
			pl.updateOutputValue(jo.getInt("outputId"), jo.getDouble("value"));
		} else if ("createInputGroupCondition".equalsIgnoreCase(tempValue)) {
			String[] str = jo.getString("inputs").replace("[", "").replace("]", "").replace(" ", "").split(",");
			int[] intb = new int[str.length];
			for (int i = 0; i < str.length; i++) {
				intb[i] = Integer.parseInt(str[i]);
			}
			pl.createInputGroupCondition(intb, jo.getInt("operation"));
		} else if ("updateValueOutputGroup".equalsIgnoreCase(tempValue)) {
			pl.updateValueOutputGroup(sro_temp.getDataFrame());
		} else if ("getConditionsandInputs".equalsIgnoreCase(tempValue)) {
			json = pl.getConditionsAndInputs(jo.getInt("inputId"));
		}
		return json;
	}

	protected void to_error_logger(SerialObj sro_temp) {
		errorManager er = new errorManager();
		er.logError("target:" + sro_temp.getTargetIp() + "   source:" + sro_temp.getSourceIp() + "   " + sro_temp.getDataFrame().toString(), "stjm2505");
	}
}
