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


    LMThread(JSONArray _jsnArgs) {
        parseArgs(_jsnArgs);
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
        ArrayList<JSONObject> _jsnTasks = new ArrayList<>();

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

        for (int j = 0; j < _lstOutputSerials.size();j++){
            JSONObject _jsnItemOutput = Args.getJSONObject(j);
            JSONObject _jsnNewCondition = new JSONObject();
            ArrayList<String> _strInputValues = new ArrayList<String>();

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
                                            _strInputValues.add(String.valueOf(_fltInputValue));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            _jsnNewCondition.put("input",_strInputValues);
            _jsnTasks.add(_jsnNewCondition);
        }





        System.out.println(_lstOutputSerials.size() + " conditions found are : ");
        for (int i = 0; i < _jsnTasks.size(); i++) {
            processTask(_jsnTasks.get(i));
        }



        System.out.println("--------------------------------------");
    }

    private void processTask(JSONObject _jsnTask){
        //process task by ID and shit
    }

    private void updateOutput(int outputID, int value){
        //send update to server
    }
}
