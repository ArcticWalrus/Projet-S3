package maxmamort.gel;

import maxmamort.gel.persistence.persistantLayer;
import org.json.JSONArray;

public class Main {
    private static final int OUTPUT = 1;
    private static final int INPUT = 0;
    private static final int GREATERTHAN = 0;
    private static final int LESSTHAN = 1;
    private static final int EQUALTO = 2;



    public static void main(String[] args) {
        persistantLayer pl = new persistantLayer();
        int temp = pl.addInput("Test input", 22.5, INPUT);
        int temp2 = pl.addInput("Input2", 21.2, INPUT);
        int inputGroup = pl.createInputGroup(new int[]{temp, temp2});
        int temp3 = pl.addInput("output1", 1.3, OUTPUT);
        int outputGroup = pl.createInputGroup(new int[]{temp3});
        int conditionId = pl.createCondition(inputGroup, outputGroup, 1);



        /* JOSH VALUES FOR LMTHREAD TESTS */
        /* greaterThan test 1. Should pass */
        int gtTest1_value1 = pl.addInput("gtTest1_value1", 23.5, INPUT);
        int gtTest1_value2 = pl.addInput("gtTest1_value2", 21.5, INPUT);
        int gtTest1_InputGroup = pl.createInputGroup(new int[]{temp, gtTest1_value2});
        int gtTest1_output = pl.addInput("gtTest1_output", 1.3, OUTPUT);
        int gtTest1_OutputGroup = pl.createInputGroup(new int[]{gtTest1_output});
        int gtTest1_condID = pl.createCondition(gtTest1_InputGroup, gtTest1_OutputGroup, GREATERTHAN);

        /* lessthan test 1. Should pass */
        int ltTest1_value1 = pl.addInput("ltTest1_value1", 23.5, INPUT);
        int ltTest1_value2 = pl.addInput("gtTest1_value2", 23.5, INPUT);
        int ltTest1_InputGroup = pl.createInputGroup(new int[]{temp, ltTest1_value2});
        int ltTest1_output = pl.addInput("ltTest1_output", 1.3, OUTPUT);
        int ltTest1_OutputGroup = pl.createInputGroup(new int[]{ltTest1_output});
        int ltTest1_condID = pl.createCondition(ltTest1_InputGroup, ltTest1_OutputGroup, LESSTHAN);

        /* equal to test1. Should pass */
        int etTest1_value1 = pl.addInput("etTest1_value1", 22.5, INPUT);
        int etTest1_value2 = pl.addInput("etTest1_value2", 22.5, INPUT);
        int etTest1_InputGroup = pl.createInputGroup(new int[]{temp, etTest1_value2});
        int etTest1_output = pl.addInput("etTest1_output", 1.3, OUTPUT);
        int etTest1_OutputGroup = pl.createInputGroup(new int[]{etTest1_output});
        int etTest1_condID = pl.createCondition(etTest1_InputGroup, etTest1_OutputGroup, EQUALTO);


        JSONArray jsonFinal = pl.getConditionsAndInputs(temp);
        LMThread _lmtLogic = new LMThread(jsonFinal);
        for (int i = 0; i < jsonFinal.length(); i++) {
            System.out.println(jsonFinal.get(i).toString());
        }
        System.out.println("\n\n" + jsonFinal.toString());

        //TEST methods for error manager
     /*   System.out.println("\n Division by zero exception handling in database");
        try {
            int test = 0;
            double value = test / 0;
        } catch (Exception ex) {
            errorManager em = new errorManager();
            em.logError(ex);
        }

        //TEST select 50 logs
        System.out.println("\n\n select last 50 log");
        dbAccess db = new dbAccess();
        JSONArray json = db.selectQuery("SELECT * FROM log LIMIT 50");

        JSONObject objType = new JSONObject();
        objType.put("datatype", "Double");
        json.put(objType);
        for (int i = 0; i < json.length(); i++) {
            System.out.println(json.get(i).toString());
        }
        System.out.print("Full json string: ");
        System.out.println(json.toString());


        //TEST update a log
        System.out.println("\n\n Update a log");
        int logId = 1;
        Date date = new Date();
        try {
            PreparedStatement ps = db.getConnection().prepareStatement("UPDATE log SET valerrorcode = ?, valcip = ? , datdate = ?, details = ? WHERE serserial = ?;");
            ps.setString(1, "ERROR_CODE");
            ps.setString(2, "BOLM2210");
            ps.setTimestamp(3, new Timestamp(date.getTime()));
            ps.setString(4, "NEW DETAILS");
            ps.setInt(5, 5);

            db.updateQuery(ps);
        } catch (SQLException ex) {
            errorManager em = new errorManager();
            em.logError(ex);
        }*/


        //TEST Delete
   /*     System.out.println("\n\n Delete log with given ID");
        try {
            PreparedStatement ps = db.getConnection().
                    prepareStatement("DELETE FROM log WHERE serserial = ?");
            ps.setInt(1, 4);
            db.deleteQuery(ps.toString());
        } catch (SQLException ex) {
            errorManager em = new errorManager();
            em.logError(ex);
        }*/


        //Close dB connection
        //db.closeConnection();
    }
}
