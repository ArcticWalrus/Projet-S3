package maxmamort.gel.persistence;

import org.json.JSONArray;

public interface IpersistantLayer {
    int addInput(String inputName, double defaultValue, int sensorType);

    boolean updateOutputValue(int outputId, double value);

    boolean updateValueOutputGroup(JSONArray jsonUpdate);

    int createInputGroup(int[] inputIds);

    int createCondition(int inputGroup, int outputGroup, int conditionType);

    JSONArray getConditionsAndInputs(int inputId);
}
