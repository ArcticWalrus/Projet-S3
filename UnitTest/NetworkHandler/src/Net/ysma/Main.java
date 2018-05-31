package Net.ysma;

public class Main
{
    private static final Integer UNKNOWN = 0;
    private static final Integer INPUTVALUE = 1;
    private static final Integer OUTPUTCHANGE = 2;
    private static final Integer DATAPROCESSING = 3;
    private static final Integer UI = 4;
    private static final Integer LOGICSETUP = 5;
    private static final Integer ERRORPROCESS = 6;

	public static void main(String[] args)
	{
        CommServer cseListener = new CommServer();

        CommClient cclWriter = new CommClient();
        cclWriter._seoToSend.setSourceIp("La mère à Josh");
        cclWriter._seoToSend.setTargetIp("La mère à Max");
        cclWriter._seoToSend.setRequestType(INPUTVALUE);
        cclWriter._seoToSend.setDataFrameTime();

        cseListener.start();
        cclWriter.start();

        try
        {
            Thread.sleep(1000);
            System.out.println(cseListener._seoLastValid.getSourceIp());
            System.out.println(cseListener._seoLastValid.getTargetIp());
            System.out.println(cseListener._seoLastValid.getRequestType().toString());
            System.out.println(cseListener._seoLastValid.getDataFrameTime().toString());
            cseListener.commServerStop();
            cseListener.join();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        System.out.println("Fin du programme");
	}
}
