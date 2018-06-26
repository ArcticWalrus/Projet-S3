package Net2.ysma;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WriterAddon implements CustomClassListener<LThread>
{
	private static final int BUFF_SIZE = 50;
    public class structObjThread
    {
        public SerialObj _serObj;
        public Integer _intIndex;
        public structObjThread(SerialObj ser, Integer index)
        {
            _serObj = ser;
            _intIndex = index;
        }
    }

    private CommServer _cseListener;
    private BlockingQueue<SerialObj> _fileObjectToProcess = new ArrayBlockingQueue(BUFF_SIZE);
    private List<structObjThread> _lstObjThread = new ArrayList<>();
    private SerialObj _serData;

    public WriterAddon()
    {
        _cseListener  = new CommServer();
        _cseListener.addListener(this);
        _cseListener.start();
    }

    public WriterAddon(int iport)
    {
        _cseListener  = new CommServer(iport);
        _cseListener.addListener(this);
        _cseListener.start();
    }

    public void stopServer()
    {
        _cseListener.commServerStop();
        try
        {
            _cseListener.join();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public Integer getIndexOfObject(SerialObj serTemp)
	{
		int i = 0, indexOf = 0;
		for(structObjThread serObj: _lstObjThread)
		{
			if(serObj._serObj.equals(serTemp))
				indexOf = i;
			i++;
		}
		return _lstObjThread.get(_lstObjThread.indexOf(indexOf))._intIndex;
	}

	public void setAnswer(SerialObj serOb, Integer index)
	{
		_cseListener.setOutboundSerialObj(serOb, index);
	}

    public SerialObj getSerialObject()
    {
        return _fileObjectToProcess.poll();
    }

    @Override
    public void receivedNewThread(LThread temp){
        temp.addListener(this);
    }

    @Override
    public void receivedNewDataFrame(Integer iIndex) {
        System.out.println("Got to save and use that new data");
        _serData = _cseListener.getPayload(iIndex);
        _fileObjectToProcess.add(_serData);
        if(_serData.getIfFeedbackNeeded())
        {
			_lstObjThread.add(new structObjThread(_serData, iIndex));
        }
    }
}
