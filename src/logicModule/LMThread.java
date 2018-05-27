package logicModule;

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

        for(int j = 0; j < _lstArgs.size(); j++){
            System.out.println(_lstArgs.get(j));
        }
    }


    private void parseArgs(JSONArray Args) {
        /*String[] _strArgs = Args.split(",");
        int _intArgsLength = _strArgs.length;
        System.out.println(_intArgsLength);

        for ( int i = 0; i < _intArgsLength; i++){
            System.out.println(i);
            System.out.println( _strArgs[i]);
            _lstArgs.add(_strArgs[i]);*/

        for (int i = 0; i < Args.length(); i++){
            try {
                JSONObject _jsnArgs = Args.getJSONObject(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
