package IOController;

import java.io.FileWriter;

public class main {
    public static void main(String[] args){

        IOController io;

        if(args.length > 0){
            io = new IOController(args[0]);
        }
        else{
            io = new IOController();
        }

        io.gpio_mainProgram();
    }
}