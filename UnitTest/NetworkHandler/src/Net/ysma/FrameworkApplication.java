package Net.ysma;

import java.util.List;

import static Net.ysma.SerialObjInterface.*;

public class FrameworkApplication
{
    public List<InfoProcess> _lisThread;
    public WriterAddon _wraObserver;
    private boolean _booAppOn = true;

    public FrameworkApplication()
    {
        //Code to implement where a Listener is required #1
        //Le constructeur WriterAddon(int iport) peut être utilisé pour être sur autre port que 45000
        _wraObserver = new WriterAddon();
        //end of code #1

        //Code to implement where a Writer is required
        CommClient cclWriter = new CommClient();
        cclWriter.setSourceIp("La mère à Josh");
        cclWriter.setTargetIp("La mère à Max");
        cclWriter.setRequestType(APPSERVER);
        cclWriter.setDataFrameTime();
        cclWriter.start();
        //end of code
    }

    public void startApp()
    {
        //Code to implement where a Listener is required #2
        while(_booAppOn) {
            SerialObj serTemp = _wraObserver.getSerialObject();
            if (serTemp != null)
            {
            	InfoProcess ifpTemp = new InfoProcess(serTemp);
            	if (serTemp.getIfFeedbackNeeded()) {
					SerialObj serOutbound = new SerialObj();
					serOutbound = ifpTemp.Aiguilleur(serTemp);
					//Doit relayer l'information au LThread
					_wraObserver.setAnswer(serOutbound, _wraObserver.getIndexOfObject(serTemp));
				}
            	else
				{
					ifpTemp.Aiguilleur(serTemp);
				}

                //Add code to do with new serial object (Start thread or other)

            }
            //TODO mettre sleep a max
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        _wraObserver.stopServer();

        //end of code #2
        System.out.println("L'application ferme...");
    }

    public void stopApp()
    {
        _booAppOn = false;
    }
}
