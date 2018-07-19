import Net.ysma.*;

public class Main_Application_Layer {

    private static FrameworkApplication _fapMainApp;

    public static void main(String[] args) {
        _fapMainApp = new FrameworkApplication(1);
        _fapMainApp.startApp();
        System.out.println("fin du programme");

    }
}
