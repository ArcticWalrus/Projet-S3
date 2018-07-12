import Net.ysma.*;

public class Main_Persistance_Layer
{
    private static FrameworkApplication _fapMainApp;

    public static void main(String[] args)
    {
        _fapMainApp = new FrameworkApplication(2);
        _fapMainApp.startApp();
        System.out.println("fin du programme");
    }
}
