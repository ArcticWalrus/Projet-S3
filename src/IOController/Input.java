package IOController;

import java.io.FileReader;

public class Input implements Runnable{
    private int lastState;
    private int pinNumber;
    private IOController parent;

    public boolean killThread = false;

    public Input(int newPinNumber, IOController newParent){
        pinNumber = newPinNumber;
        lastState = 0;
        parent = newParent;
    }

    public boolean hasChanged(){
        return true;
    }

    public int digital_readPin(){
        String _strIO = String.format("/sys/class/gpio/gpio%1$d/value", pinNumber);
        try{
            FileReader fileReader = new FileReader(_strIO);
            int state = fileReader.read();

            if(state == '1'){
                return 1;
            }
            else if(state == '0'){
                return 0;
            }
        }
        catch(Exception ae){
            System.out.println(ae.toString());
            return -1;
        }
        return -1;
    }

    public int getPinNumber(){
        return pinNumber;
    }

    public void stopThread(){
        killThread = true;
    }

    public void begin(){
        Thread thrd = new Thread(this);
        thrd.start();
    }

    public void run(){
        while(!killThread){
            int status = digital_readPin();
            if(lastState != status){
                lastState = status;
                parent.inputHasChanged(pinNumber, lastState);
            }
            try{
                Thread.sleep(500);
            }catch(Exception ae){
                System.out.println(ae.toString());
            }

        }
    }
}
