package serverPackage;

public class Main {
    public static String serverAddress = "tcp://10.209.106.201:9001/";
    private static Controller controller = new Controller();
    public static void main(String[] args) {
        controller.run();
    }

}
