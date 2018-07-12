package IOController;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import maxmamort.gel.persistence.*;

public class IOController {
    private ArrayList<Input> activeInputs;
    private ArrayList<Integer> activeOutputs;
    private String macAdress = "b8:27:eb:ee:e5:13";
    persistantLayer pl = new persistantLayer();

    public void gpio_mainProgram(){
        gpio_createNewIO(2, 1);
        gpio_createNewIO(3, 0);
        gpio_createNewIO(4, 0);

        try{
            gpio_setNewValue(2,1);
            Thread.sleep(500);
            gpio_setNewValue(2,0);
            Thread.sleep(500);
        }
        catch(Exception ae){
            System.out.print("mainProgram exception: ");
            System.out.println(ae.toString());
        }
    }

    public IOController(String macAddress){
        System.out.println(macAddress);

        activeInputs = new ArrayList<Input>();
        activeOutputs = new ArrayList<Integer>();
        //set reference to persistence
    }

    public IOController(){
        activeInputs = new ArrayList<Input>();
        activeOutputs = new ArrayList<Integer>();
        //set reference to persistence
        getMacAdress();
    }

    public void gpio_createNewIO(int pinNumber, int direction){
        if(checkIfExists(pinNumber) == false){
            gpio_export(pinNumber);
            gpio_setDirection(pinNumber, direction);
            if(direction == 0){ //is input
                Input newInput = new Input(pinNumber, this);
                newInput.begin();
                activeInputs.add(newInput);
            }
            else{
                activeOutputs.add(pinNumber);
            }
        }
    }

    public void gpio_setNewValue(int pinNumber, int newValue){
        if(activeOutputs.contains(pinNumber)){
            gpio_digitalWrite(pinNumber, newValue);
        }
    }

    public int gpio_requestRead(int pinNumber){
        int inputIndex = checkIfInputExists(pinNumber);

        if(inputIndex != -1){
            Input tempInput = activeInputs.get(inputIndex);
            return tempInput.digital_readPin();
        }

        //does not exist
        return -2;
    }

    public synchronized void inputHasChanged(int pinNumber, int value){
        //make pl and push data
        System.out.println("would call PL form here");
        System.out.println(String.format("pin %1$d is now: %2$d", pinNumber, value));
    }

    private void getMacAdress(){}

    private boolean gpio_digitalWrite(int pinNumber, int value){
        try{
            String _strIO = String.format("/sys/class/gpio/gpio%1$d/value", pinNumber);
            FileWriter gpioWrite = new FileWriter(_strIO);
            String _strValue = String.format("%1$d", value);
            gpioWrite.write(_strValue);
            gpioWrite.close();
            return true;
        }
        catch(Exception ae){
            System.out.print("gpio_digitalWrite exception: ");
            System.out.println(ae.toString());
            return false;
        }
    }

    private void gpio_unexport(int pinNumber){
        try{
            FileWriter gpioExport = new FileWriter("/sys/class/gpio/unexport");
            String _strOut = String.format("%1$d", pinNumber);
            gpioExport.write(_strOut);
            gpioExport.close();
        }
        catch(IOException ae){
            System.out.print("gpio_unexport exception: ");
            System.out.println(ae.toString());
        }
    }

    private void gpio_export(int pinNumber){
        try{
            FileWriter gpioExport = new FileWriter("/sys/class/gpio/export");
            String _strOut = String.format("%1$d", pinNumber);
            gpioExport.write(_strOut);
            gpioExport.close();
        }
        catch(IOException ae){
            System.out.print("gpio_export exception: ");
            System.out.println(ae.toString());
        }

    }

    private void gpio_setDirection(int pinNumber, int direction){
        try{
            String _strIO = String.format("/sys/class/gpio/gpio%1$d/direction", pinNumber);
            FileWriter gpioSetDir = new FileWriter(_strIO);

            if(direction == 0){
                gpioSetDir.write("in");
                //check if pin already exists in array
                activeInputs.add(new Input(pinNumber, this));
            }
            else{
                gpioSetDir.write("out");
            }

            gpioSetDir.close();
        }
        catch(IOException ae){
            System.out.print("gpio_setDir exception: ");
            System.out.println(ae.toString());
        }
    }

    private boolean checkIfExists(int pinNumber){
        if((checkIfInputExists(pinNumber) != -1) || checkIfOutputExists(pinNumber)){
            return true;
        }
        return false;
    }

    private int checkIfInputExists(int pinNumber){
        Input tempInput;
        //if exists in input
        for(int i = 0; i < activeInputs.size(); i++){
            tempInput = activeInputs.get(i);
            if(tempInput.getPinNumber() == pinNumber){
                return i;
            }
        }

        //if not found
        return -1;
    }

    private boolean checkIfOutputExists(int pinNumber){
        //if exists in output
        if(activeOutputs.contains(pinNumber)){
            return true;
        }

        //if not found
        return false;
    }
}
