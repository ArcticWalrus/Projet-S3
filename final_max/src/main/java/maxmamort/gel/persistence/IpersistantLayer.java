package maxmamort.gel.persistence;

import org.json.JSONArray;

public interface IpersistantLayer {
    int addInput(String inputName, double defaultValue, int sensorType);

    boolean updateOutputValue(int outputId, double value);

    boolean updateValueOutputGroup(JSONArray jsonUpdate);

    int createInputGroupCondition(int[] inputIds, int operation);

    JSONArray getConditionsAndInputs(int inputId);
}
