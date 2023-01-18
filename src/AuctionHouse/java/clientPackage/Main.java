package clientPackage;

import org.jspace.RemoteSpace;

import java.io.IOException;

public class Main {
    public static String serverAddress = "10.209.79.136/?keep";
    private static TestClient client = new TestClient();
    public static void main(String[] args) {
            client.run();
            //String lobbyUri = serverAddress + "lobby" + "?keep";
            //Controller.connectToLobby(lobbyUri); // connect user to lobby.





    }

}
