package Net2.ysma;

import maxmamort.gel.Utils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class CommServer extends Thread {
    private int COMM_PORT; // socket port for client comms

    private ServerSocket _ssoServerSocket;
    private InetSocketAddress _isaInboundAddr;
    private List<CustomClassListener> listeners = new ArrayList<>();
    private List<LThread> ServerThreads = new ArrayList<>();

    boolean _bServerActive = false;

    public CommServer() {
        this.COMM_PORT = 45000; // socket port for client comms
        _bServerActive = true;
    }

    public CommServer(int iPort) {
        COMM_PORT = iPort; // socket port for client comms
        _bServerActive = true;
    }

    private void init_ssoServerSocket() {
        try {
            this._isaInboundAddr = new InetSocketAddress(COMM_PORT);
            this._ssoServerSocket = new ServerSocket(COMM_PORT);
            assert this._ssoServerSocket.isBound();
            if (this._ssoServerSocket.isBound()) {
                System.out.println("SERVER inbound data port " + this._ssoServerSocket.getLocalPort()
                        + " is ready and waiting for client to connect...");
                _ssoServerSocket.setSoTimeout(1000);
            }
        } catch (SocketException se) {
            System.err.println("Unable to create socket.");
            System.err.println(se.toString());
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println("Unable to read data from an open socket.");
            System.err.println(ioe.toString());
            System.exit(1);
        }
    }

    public void run() {
        int CommID = 1;
        System.out.println("STARTING Listener Thread...");
        init_ssoServerSocket();

        while (_bServerActive) {
            try {
                // listen for and accept a client connection to _ssoServerSocket
                Socket sock = this._ssoServerSocket.accept();
                LThread lTemp = new LThread(ServerThreads.size(), sock);
				callListeners(lTemp);
                ServerThreads.add(lTemp);
                ServerThreads.get(ServerThreads.size()-1).start();
                CommID++;
                if (CommID == 65535)
                    CommID = 1;

                Utils.sleep(5);
            } catch (SecurityException se) {
                System.err.println("Unable to get host address due to security.");
                System.err.println(se.toString());
                System.exit(1);
            } catch (SocketTimeoutException ste) {

            } catch (IOException ioe) {
                System.err.println("Unable to read data from an open socket.");
                System.err.println(ioe.toString());
                System.exit(1);
            }
        }
        System.out.println("STOPPING Listener Thread...");
        try {
            this._ssoServerSocket.close();
        } catch (IOException ioe) {
            System.err.println("Unable to close an open socket.");
            System.err.println(ioe.toString());
            System.exit(1);
        }
    }

    public SerialObj getPayload(Integer iIndex)
    {
        return ServerThreads.get(iIndex).getNewPayload();
    }

    public void addListener(CustomClassListener toAdd) {
        listeners.add(toAdd);
    }

    public void callListeners(LThread lm) {
        System.out.println("New LThread created!");
        // Notify everybody that may be interested.
        for (CustomClassListener hl : listeners)
            hl.receivedNewThread(lm);
    }

    public void commServerStop() {
        _bServerActive = false;
    }

	public void setOutboundSerialObj(SerialObj ser, Integer iIndex)
	{
		ServerThreads.get(iIndex).setOutboundPayload(ser);
		ServerThreads.get(iIndex).setOutboundFlagToSend();
		ServerThreads.remove(iIndex);
	}
}
