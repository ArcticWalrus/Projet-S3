package maxmamort.gel;

import Net.ysma.*;

import maxmamort.gel.persistence.persistantLayer;
import org.json.JSONArray;


public class ApplicationLayer {
    persistantLayer pl;
    LMThread _lmtLogic;
    int temp_test;

    private static final int OUTPUT = 1;
    private static final int INPUT = 0;
    private static final int GREATERTHAN = 0;
    private static final int LESSTHAN = 1;
    private static final int EQUALTO = 2;

    public ApplicationLayer() {

    }

    public void Populate() {
        int temp = pl.addInput("Test input", 22.5, INPUT);
        temp_test = temp; //for creation of LM thread in main
        int temp2 = pl.addInput("Input2", 21.2, INPUT);
        int temp3 = pl.addInput("output1", 1.3, OUTPUT);
        pl.createInputGroupCondition(new int[]{temp, temp2, temp3}, 1);

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


        pl.updateIoValue("MAC", 10, 66.66);

        JSONArray deviceID = pl.getIOByDevice("MAC");
        System.out.println("IO for device MAC: " + deviceID.toString());

        System.out.println(pl.getIOForUser("bolm2210").toString());
    }

    public void Create_Persistance_Layer() {
        pl = new persistantLayer();
    }

    public void Logic_Module_Parse(JSONArray _jsnArgstest) {
        _lmtLogic = new LMThread(_jsnArgstest);
    }

    void to_persistance(SerialObj sro_temp) {
        CommClient pers_client = new CommClient("127.0.0.1", 45010);
    }

    void to_logic(SerialObj sro_temp) {
        CommClient lm_client = new CommClient("127.0.0.1", 45020);
    }

    void to_device(SerialObj sro_temp) {
        CommClient device_client = new CommClient(sro_temp.getTargetIp(), sro_temp.getTargetPort());
    }

}