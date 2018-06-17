import Net.ysma.*;
import static Net.ysma.SerialObjInterface.*;

public class Main
{
    public static void main(String[] args)
	{
	    int i = 0;

	    //Code to implement where a Listener is required #1
        //Le constructeur mainServerAddon(int iport) peut être utilisé pour être sur autre port que 45000
        mainServerAddon msaServerObserver = new mainServerAddon();
        //end of code #1

        //Code to implement where a Writer is required
        boolean booSuccess = false;
        CommClient cclWriter = new CommClient();
        cclWriter.setSourceIp("La mère à Josh");
        cclWriter.setTargetIp("La mère à Max");
        while(!booSuccess)
            booSuccess = cclWriter.setRequestType(INPUTVALUE);
        cclWriter.setDataFrameTime();
        cclWriter.start();
        //end of code

        //Code to implement where a Listener is required #2
        while(i < 1) {
            if (msaServerObserver._booNewData) {
                SerialObj serTemp = msaServerObserver.getSerialObject();
                i++;
                //Add code to do with new serial object (Start thread or other)
            }
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        msaServerObserver.stopServer();
        //end of code #2

        System.out.println("Fin du programme");
	}
}
