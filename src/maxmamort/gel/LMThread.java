//TEST

package maxmamort.gel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class LMThread implements Runnable {

    private static final double TOLERANCE = 0.01;

    private int _intInstruction;
    private ArrayList<String> _lstArgs = new ArrayList<String>();
    private boolean _booThreadStop = false;
    ArrayList<JSONObject> _lstJSONOutput = new ArrayList<JSONObject>();
    ArrayList<JSONObject> _lstJSONInput = new ArrayList<JSONObject>();
    ArrayList<JSONObject> _lstJSONInputValues = new ArrayList<JSONObject>();
    ArrayList<JSONObject> _jsnTasks = new ArrayList<>();



    LMThread(JSONArray _jsnArgs) {
        parseArgs(_jsnArgs);
        processAllTasks();
    }

    public void stop() {
        _booThreadStop = true;
    }

    public void run() {
        //while (_booThreadStop == false) {}

        for (int j = 0; j < _lstArgs.size(); j++) {
            System.out.println(_lstArgs.get(j));
        }
    }

    private void parseArgs(JSONArray Args) {
        System.out.println("--------------------------------------");
        JSONArray commands = new JSONArray();

        seperateIOC(Args);
        matchIOC();
        System.out.println("--------------------------------------");
    }

    private void matchIOC(){
        /* get condition number for all outputs */
        for(int i = 0; i < _lstJSONOutput.size(); i++){
            JSONObject _jsnCurrentOutput = _lstJSONOutput.get(i);
            JSONObject _jsnCurrentTask = new JSONObject();

            _jsnCurrentTask.put("output", _jsnCurrentOutput.getInt("outputgroup"));
            _jsnCurrentTask.put("conditionNumber", _jsnCurrentOutput.getInt("inputgroup"));
            _jsnCurrentTask.put("operation", _jsnCurrentOutput.getInt("operation"));

            _jsnTasks.add(_jsnCurrentTask);
        }

        /* for all conditions */
        for(int j = 0; j < _jsnTasks.size(); j++){
            JSONObject _jsnCurrentTask = _jsnTasks.get(j);
            int currentOutputConditionGroup = _jsnCurrentTask.getInt("conditionNumber");
            ArrayList<Integer> inputNumbers = new ArrayList<>();
            ArrayList<Double> inputValues = new ArrayList<>();

            /* find all associated inputs */
            for (int k = 0; k < _lstJSONInput.size(); k++){
                JSONObject _jsnCurrentInputItem = _lstJSONInput.get(k);
                int _intCurrentItemCondGroup = _jsnCurrentInputItem.getInt("conditiongroup");
                if( _intCurrentItemCondGroup == currentOutputConditionGroup){
                    inputNumbers.add(_jsnCurrentInputItem.getInt("inputid"));
                }
            }

            /* find values of all inputs */
            for (int l = 0; l < inputNumbers.size(); l++){
                int _intCurrentInputNumber = inputNumbers.get(l);
                for(int i = 0; i < _lstJSONInputValues.size(); i++){
                    JSONObject _jsnCurrentItem = _lstJSONInputValues.get(i);
                    if(_jsnCurrentItem.getInt("serintinput") == _intCurrentInputNumber){
                        inputValues.add(_jsnCurrentItem.getDouble("value"));
                        break;
                    }
                }
            }
            System.out.println("for output: " + _jsnCurrentTask.getInt("output") + " input ids are: " + inputNumbers);
            System.out.println("for output: " + _jsnCurrentTask.getInt("output") + " input values are: " + inputValues);
            _jsnCurrentTask.put("input", inputValues);
        }
    }

    private void seperateIOC(JSONArray Args){
        System.out.println("seperating inputs/outputs/conds");
        for (int i = 0; i < Args.length(); i++) {

            JSONObject _jsnItem = Args.getJSONObject(i);

            if(_jsnItem.has("outputgroup")){
                _lstJSONOutput.add(Args.getJSONObject(i));
            }

            else if(_jsnItem.has("inputid")){
                _lstJSONInput.add(Args.getJSONObject(i));
            }

            else {
                _lstJSONInputValues.add(Args.getJSONObject(i));
            }
        }
    }

    private void processAllTasks(){
        for (int i = 0; i < _jsnTasks.size(); i++){
            processTask(_jsnTasks.get(i));
        }
    }

    private void processTask(JSONObject _jsnTask){
        int _intTaskID = _jsnTask.getInt("operation");
        switch(_intTaskID){
            case 0:
                System.out.println("task ID is 0. Executing task greaterThan");
                greaterThan(_jsnTask);
                break;
            case 1:
                System.out.println("task ID is 1. Executing task lessThan");
                lessThan(_jsnTask);
                break;
            case 2:
                System.out.println("task ID is 2. Executing task equalTo");
                equalTo(_jsnTask);
            default:
                System.out.println("task ID is not mapped to any behavior. Task ID received is : " + _intTaskID);
                break;
        }
    }

    private void greaterThan(JSONObject _jsnTask){

        double input1, input2;

        JSONArray _jsnInputs = _jsnTask.getJSONArray("input");
        input1 = _jsnInputs.getDouble(0);
        input2 = _jsnInputs.getDouble(1);

        System.out.println("GT test for values: " + input1 + " and " + input2);
        if(input1 > input2){
            System.out.println("input1 is greater than input2. PASSED");

        }
        else{
            System.out.println("input1 is less than input2. FAILED");
        }
    }

    private void lessThan(JSONObject _jsnTask){
        double input1, input2;

        JSONArray _jsnInputs = _jsnTask.getJSONArray("input");
        input1 = _jsnInputs.getDouble(0);
        input2 = _jsnInputs.getDouble(1);
        System.out.println("LT test for values: " + input1 + " and " + input2);
        if(input1 < input2){
            System.out.println("input1 is less than input2. PASSED");

        }
        else{
            System.out.println("input1 is greater than input2. FAILED");
        }
    }

    private void equalTo(JSONObject _jsnTask){
        double input1, input2;

        JSONArray _jsnInputs = _jsnTask.getJSONArray("input");
        input1 = _jsnInputs.getFloat(0);
        input2 = _jsnInputs.getFloat(1);
        System.out.println("ET test for values: " + input1 + " and " + input2);

        double _dblUpperLimit = input1 * (1.0 + TOLERANCE);
        double _dblLowerLimit = input1 * (1.0 - TOLERANCE);

        if(input2 < _dblUpperLimit && input2 > _dblLowerLimit){
            System.out.println("input1 is equal to input2. PASSED");
        }

        else{
            System.out.println("input1 is not equal to input2. FAILED");
        }
    }
}
