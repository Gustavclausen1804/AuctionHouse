package clientPackage;

import org.jspace.QueueSpace;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;
import java.util.LinkedList;

// controls what should happen when user interacts with UI, startup ect.
public class Controller {


    public static void connectToLobby(String lobbyUri) {
        try {
            RemoteSpace lobby = new RemoteSpace(lobbyUri);
            View.displayLobby();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static LinkedList<Object[]> getAllItems(Space space) {


        // TODO: get all items.
        return null;


    }

    public static void chooseOption (int option) {

        // TODO: if 1 or 2. -> display screen.

    }


}
