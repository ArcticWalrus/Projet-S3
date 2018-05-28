package maxmamort.gel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LMThread implements Runnable {
    private int _intInstruction;
    private ArrayList<String> _lstArgs = new ArrayList<String>();
    private boolean _booThreadStop = false;
    ArrayList<Integer> _lstOutputSerials = new ArrayList<Integer>();
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


        for (int i = 0; i < Args.length(); i++) {
            try {
                JSONObject _jsnArgs = Args.getJSONObject(i);
                if (_jsnArgs.has("outputgroup")) {
                    //tests.add(_jsnArgs);
                    _lstOutputSerials.add(_jsnArgs.getInt("outputgroup"));
                }
                //System.out.print("line number" + i + ": ");
                //System.out.println(_jsnArgs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /******** Will replace current function to seperate and match data
        for (int i = 0; i < Args.length(); i++) {
            //Extract outputs
            //Extract inputs
            //Match things
        }
        */

        for (int j = 0; j < _lstOutputSerials.size();j++){
            JSONObject _jsnItemOutput = Args.getJSONObject(j);
            JSONObject _jsnNewCondition = new JSONObject();
            ArrayList<Float> _strInputValues = new ArrayList<Float>();

            //looking for outputgroup
            if(_jsnItemOutput.has("outputgroup")){
                if(_jsnItemOutput.getInt("outputgroup") == _lstOutputSerials.get(j)){
                    _jsnNewCondition.put("outputgroup",_lstOutputSerials.get(j));
                    _jsnNewCondition.put("operation", _jsnItemOutput.getInt("operation"));
                    int _intInputGroupForCurrentOutput = _jsnItemOutput.getInt("inputgroup");
                    System.out.println("Condition group for output " + _lstOutputSerials.get(j) + " is " + _intInputGroupForCurrentOutput);
                    //find all inputs related
                    for(int k = 0; k < Args.length(); k++){
                        JSONObject _jsnItemInput = Args.getJSONObject(k);
                        if(_jsnItemInput.has("conditiongroup")){
                            //Find inputs with right conditiongroup
                            if(_intInputGroupForCurrentOutput == _jsnItemInput.getInt("conditiongroup")){
                                int _intWantedInputID = _jsnItemInput.getInt("inputid");
                                System.out.println("Currently wanted inputid is: " + _intWantedInputID);
                                for (int l = 0; l < Args.length(); l++){
                                    JSONObject _jsnItemInputValues = Args.getJSONObject(l);
                                    if(_jsnItemInputValues.has("serintinput")){
                                        if(_jsnItemInputValues.getInt("serintinput") == _intWantedInputID){
                                            //_jsnNewCondition.put("input", _jsnItemInputValues.getFloat("value"));
                                            float _fltInputValue = _jsnItemInputValues.getFloat("value");
                                            _strInputValues.add(_fltInputValue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            _jsnNewCondition.put("input",_strInputValues);
            System.out.println(_jsnNewCondition);
            _jsnTasks.add(_jsnNewCondition);
        }
        
        System.out.println("--------------------------------------");
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
                System.out.println("task ID is 0. Executing task division");
                break;
            case 1:
                System.out.println("task ID is 1. Executing task greaterThan");
                break;
            case 2:
                System.out.println("task ID is 2. Executing task lessThan");
            default:
                System.out.println("task ID is not mapped to any behavior. Task ID received is : " + _intTaskID);
                break;
        }
    }

    private void updateOutput(int outputID, int value){
        //send update to server
    }
}
