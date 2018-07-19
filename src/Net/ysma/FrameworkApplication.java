package Net.ysma;

import maxmamort.gel.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import maxmamort.gel.persistence.*;
//For ajax and receiving data
import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static Net.ysma.SerialObjInterface.*;

public class FrameworkApplication {
    public List<InfoProcess> _lisThread;
    public WriterAddon _wraObserver;
    private boolean _booAppOn = true;
    private int _intExeType = 0;

    private static final int OUTPUT = 1;
    private static final int INPUT = 0;

	private static final int MAINAPP = 1;
	private static final int PERSISTENCE = 2;

    public FrameworkApplication() {
        //Code to implement where a Listener is required #1
        //Le constructeur WriterAddon(int iport) peut être utilisé pour être sur autre port que 45000
        _wraObserver = new WriterAddon(45000);
        //end of code #1
		input _input = new input();
		device _device = new device();
		user _user = new user();

    }
	
	@WebServlet("/input")
	public class input extends HttpServlet {
	String inputname, defaultvalue, sensortype;
	
		protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			// read form html fields
			inputname = request.getParameter("inputname");
			defaultvalue = request.getParameter("defaultvalue");
			sensortype = request.getParameter("sensortype");
			System.out.println("Input name: " + inputname);
			System.out.println("Default value: " + defaultvalue);
			System.out.println("Sensor type: " + sensortype);  

			persistantLayer p1 = new persistantLayer();
			p1.addInput(inputname, Double.parseDouble(defaultvalue), Integer.parseInt(sensortype));
		}

//		public values[] returninput(){
//			values[] returnvalues = new String[3];
//			returnvalues[0] = inputname;
//			returnvalues[1] = defaultvalue;
//			returnvalues[2] = sensortype;
//
//		return returnvalues;
//		}

	
	}
	
	@WebServlet("/device")
	public class device extends HttpServlet {
	String macadd, cip, devicename;
	
		protected void doPost(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {
			//read form html fields
			macadd = request.getParameter("macaddress");
			cip = request.getParameter("cip");
			devicename = request.getParameter("devicename");
			System.out.println("MAC address : " + macadd);
			System.out.println("CIP : " + cip);
			System.out.println("Device name : " + devicename); 

			persistantLayer p2 = new persistantLayer();
			p2.createDevice(macadd, cip, devicename);
		}

//		public values[] returndevice(){
//			values[] returnvalues = new String[3];
//			returnvalues[0] = macadd;
//			returnvalues[1] = cip;
//			returnvalues[2] = devicename;
//
//		return returnvalues;
//		}
	
	}
	
	@WebServlet("/user")
	public class user extends HttpServlet {
	String cip;
		protected void doPost(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {
			//read form html fields
			cip = request.getParameter("cip");
			System.out.println("CIP : " + cip);   
			
			persistantLayer p3 = new persistantLayer();
			p3.createUser(cip);
		}
			
		public String returndevice(){return cip;}
	}


    public FrameworkApplication(int type) {
        //Code to implement where a Listener is required #1
        //Le constructeur WriterAddon(int iport) peut être utilisé pour être sur autre port que 45000
        if(type == 1)
            _wraObserver = new WriterAddon(45000);
        else if(type == 2)
            _wraObserver = new WriterAddon(45010);
        _intExeType = type;
        //end of code #1

    }

    public void startApp() {
        //System.out.println("Populating test db");
        //populate();
        //System.out.println("Done populating");


        //Code to implement where a Listener is required #2
        while (_booAppOn) {
            SerialObj serTemp = _wraObserver.getSerialObject();
            if (serTemp != null) {
                System.out.println("FrameworkApp 1 new object received");
                InfoProcess ifpTemp;
                if(_intExeType == MAINAPP)
					ifpTemp = new AppProcessing(serTemp);
                else if(_intExeType == PERSISTENCE)
					ifpTemp = new PersistenceProcessing(serTemp);
                else
				{
					System.out.println("Bad Framework builder used!! Need parameters...");
					break;
				}
				if (serTemp.getIfFeedbackNeeded()) {
                    SerialObj serOutbound = new SerialObj();
                    serOutbound = ifpTemp.Aiguilleur(serTemp);
                    //Doit relayer l'information au LThread
                    _wraObserver.setAnswer(serOutbound, _wraObserver.getIndexOfObject(serTemp));
                } else {
                    ifpTemp.Aiguilleur(serTemp);
                }
            }
            Utils.sleep(5);
        }
        _wraObserver.stopServer();

        //end of code #2
        System.out.println("L'application ferme...");
    }

    public void stopApp() {
        _booAppOn = false;
    }

    private SerialObj sendPersistance(SerialObj obj, boolean blocking) {
        CommClient cc = new CommClient("127.0.0.1", 45010);
        cc.setSerialObject(obj);
		cc.setIfFeedbackNeeded(blocking);
        cc.start();

        while (!cc.getIfDataReceived()) {
            Utils.sleep(10);
        }
        cc.setReceivedDataRead();
        return cc.getInputBuffer();
    }

    private SerialObj sendPersistanceSetupObj(SerialObj obj, boolean blocking, String requestType) {
        obj.setRequestType(requestType);
        obj.setTargetType(PERSISTANCE);
        return sendPersistance(obj, blocking);
    }

    private void populate() {
        SerialObj temp_obj = new SerialObj();

        temp_obj.setDataFrame(new JSONArray().put(new JSONObject().put("inputName", "Test input").put("defaultValue", 22.5).put("sensorType", INPUT)));
        temp_obj = sendPersistanceSetupObj(temp_obj, true, "addInput");
        int temp = temp_obj.getDataFrame().getJSONObject(0).getInt("serintinput");

        temp_obj.setDataFrame(new JSONArray().put(new JSONObject().put("inputName", "Inpu 2").put("defaultValue", 21.5).put("sensorType", INPUT)));
        temp_obj = sendPersistanceSetupObj(temp_obj, true, "addInput");
        int temp2 = temp_obj.getDataFrame().getJSONObject(0).getInt("serintinput");

        temp_obj.setDataFrame(new JSONArray().put(new JSONObject().put("inputName", "Input 3").put("defaultValue", 19.5).put("sensorType", INPUT)));
        temp_obj = sendPersistanceSetupObj(temp_obj, true, "addInput");
        int temp3 = temp_obj.getDataFrame().getJSONObject(0).getInt("serintinput");

        //temp_obj =
        //sendPersistance(temp_obj,false, "createInputGroupCondition");




        /*

        //JOSH VALUES FOR LMTHREAD TESTS
        //greaterThan test 1. Should pass
        //int gtTest1_value1 = pl.addInput("gtTest1_value1", 23.5, INPUT);
        int gtTest1_value2 = pl.addInput("gtTest1_value2", 21.5, INPUT);
        int gtTest1_output = pl.addInput("gtTest1_output", 1.3, OUTPUT);
        pl.createInputGroupCondition(new int[]{temp, gtTest1_value2, gtTest1_output}, 0);

        // lessthan test 1. Should pass
        //int ltTest1_value1 = pl.addInput("ltTest1_value1", 23.5, INPUT);
        int ltTest1_value2 = pl.addInput("gtTest1_value2", 23.5, INPUT);
        int ltTest1_output = pl.addInput("ltTest1_output", 1.3, OUTPUT);
        pl.createInputGroupCondition(new int[]{temp, ltTest1_value2, ltTest1_output}, 2);

        // equal to test1. Should pass
        //int etTest1_value1 = pl.addInput("etTest1_value1", 22.5, INPUT);
        int etTest1_value2 = pl.addInput("etTest1_value2", 22.5, INPUT);
        int etTest1_output = pl.addInput("etTest1_output", 1.3, OUTPUT);
        pl.createInputGroupCondition(new int[]{temp, etTest1_value2, etTest1_output}, 1);

        pl.createUser("bolm2210");
        pl.createUser("denj1605");
        pl.createUser("demo1622");

        System.out.println("\n\n Creating Devices");
        pl.createDevice("THIS IS A MAC", "bolm2210", "name of device");
        pl.createDevice("MAC", "bolm2210", "name of device 0");
        pl.createDevice("THIS IS A MAC 2", "bolm2210", "name of device 2");
        pl.createDevice("THIS IS A MAC 3", "denj1605", "name of device 3");

        pl.renameDevice("MAC", "new name");
        pl.updateDeviceIP("MAC", "1.2.3.4");


        // Not working cause of DB uniquenesss constraint
        pl.createIO("name of IO 1", "MAC", 10, 1);
        pl.createIO("name of IO 2", "MAC", 11, 0);
        pl.createIO("name of IO 3", "MAC", 12, 1);

        JSONArray deviceID = pl.getIOByDevice("MAC");
        System.out.println("IO for device MAC: " + deviceID.toString());
         */
    }
}
