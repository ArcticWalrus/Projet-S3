package maxmamort.gel;

import java.util.ArrayList;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LMThread implements Runnable {
    private int _intInstruction;
    private ArrayList<String> _lstArgs = new ArrayList<String>();
    private boolean _booThreadStop = false;


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
        JSONArray commands = new JSONArray();
        ArrayList<JSONObject> tests = new ArrayList<JSONObject>();
        for (int i = 0; i < Args.length(); i++) {
            try {
                JSONObject _jsnArgs = Args.getJSONObject(i);
                if (_jsnArgs.has("outputgroup")) {
                    tests.add(_jsnArgs);
                }
                //System.out.print("line number" + i + ": ");
                //System.out.println(_jsnArgs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        System.out.println("conditions found are : ");
        for (int i = 0; i < tests.size(); i++) {
            System.out.println(tests.get(i));
        }
    }
}
