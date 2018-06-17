package Net.ysma;

public class mainServerAddon implements NewDataFrameListener
{
    private CommServer _cseListener;
    private SerialObj _serData;
    public boolean _booNewData;

    public mainServerAddon()
    {
        _cseListener  = new CommServer();
        _cseListener.addListener(this);
        _cseListener.start();
    }

    public mainServerAddon(int iport)
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

    /*@Override
    protected void finalize()
    {
        this.stopServer();
    }*/

    public SerialObj getSerialObject()
    {
        return _serData;
    }

    @Override
    public void receivedNewDataFrame() {
        System.out.println("Got to save and use that new data");
        _serData = _cseListener.getNewPayload();
        if(_serData.getIfFeedbackNeeded())
        {

        }
        _booNewData = true;
    }
}
