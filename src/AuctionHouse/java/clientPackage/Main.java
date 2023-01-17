package clientPackage;

import org.jspace.RemoteSpace;

import java.io.IOException;

public class Main {
    public static String serverAddress = "tcp://127.0.0.1:52644/";
    private static TestClient client = new TestClient();
    public static void main(String[] args) {
            client.run();
            //String lobbyUri = serverAddress + "lobby" + "?keep";
            //Controller.connectToLobby(lobbyUri); // connect user to lobby.





    }

}
