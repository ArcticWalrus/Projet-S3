package Net2.ysma;

// An interface to be implemented by everyone
// interested in "NewDataFrame" events
public interface CustomClassListener <E> {
    void receivedNewThread(E temp);
    void receivedNewDataFrame(Integer iIndex);
}
