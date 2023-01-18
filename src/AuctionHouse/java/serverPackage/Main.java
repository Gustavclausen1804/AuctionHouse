package serverPackage;

public class Main {
    public static String serverAddress = "tcp://192.168.0.143:9001/";
    private static Controller controller = new Controller();
    public static void main(String[] args) {
        controller.run();
    }

}
