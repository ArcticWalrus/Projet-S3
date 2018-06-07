package maxmamort.gel;

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
        pl = new persistantLayer();
        //LMThread _lmtLogic = new LMThread(jsonFinal); //Dont have JSON during construction.
    }

    public void Populate() {
        int temp = pl.addInput("Test input", 22.5, INPUT);
        temp_test = temp; //for creation of LM thread in main
        int temp2 = pl.addInput("Input2", 21.2, INPUT);
        int inputGroup = pl.createInputGroup(new int[]{temp, temp2});
        int temp3 = pl.addInput("output1", 1.3, OUTPUT);
        int outputGroup = pl.createInputGroup(new int[]{temp3});
        int conditionId = pl.createCondition(inputGroup, outputGroup, 1);

        //JOSH VALUES FOR LMTHREAD TESTS
        //greaterThan test 1. Should pass
        //int gtTest1_value1 = pl.addInput("gtTest1_value1", 23.5, INPUT);
        int gtTest1_value2 = pl.addInput("gtTest1_value2", 21.5, INPUT);
        int gtTest1_InputGroup = pl.createInputGroup(new int[]{temp, gtTest1_value2});
        int gtTest1_output = pl.addInput("gtTest1_output", 1.3, OUTPUT);
        int gtTest1_OutputGroup = pl.createInputGroup(new int[]{gtTest1_output});
        int gtTest1_condID = pl.createCondition(gtTest1_InputGroup, gtTest1_OutputGroup, GREATERTHAN);

        // lessthan test 1. Should pass
        //int ltTest1_value1 = pl.addInput("ltTest1_value1", 23.5, INPUT);
        int ltTest1_value2 = pl.addInput("gtTest1_value2", 23.5, INPUT);
        int ltTest1_InputGroup = pl.createInputGroup(new int[]{temp, ltTest1_value2});
        int ltTest1_output = pl.addInput("ltTest1_output", 1.3, OUTPUT);
        int ltTest1_OutputGroup = pl.createInputGroup(new int[]{ltTest1_output});
        int ltTest1_condID = pl.createCondition(ltTest1_InputGroup, ltTest1_OutputGroup, LESSTHAN);

        // equal to test1. Should pass
        //int etTest1_value1 = pl.addInput("etTest1_value1", 22.5, INPUT);
        int etTest1_value2 = pl.addInput("etTest1_value2", 22.5, INPUT);
        int etTest1_InputGroup = pl.createInputGroup(new int[]{temp, etTest1_value2});
        int etTest1_output = pl.addInput("etTest1_output", 1.3, OUTPUT);
        int etTest1_OutputGroup = pl.createInputGroup(new int[]{etTest1_output});
        int etTest1_condID = pl.createCondition(etTest1_InputGroup, etTest1_OutputGroup, EQUALTO);
    }

    public void Logic_Module_Parse(JSONArray _jsnArgstest) {
        _lmtLogic = new LMThread(_jsnArgstest);
    }
}
