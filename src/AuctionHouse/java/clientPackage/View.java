package clientPackage;

import org.jspace.FormalField;
import org.jspace.QueueSpace;
import org.jspace.Space;

import java.util.LinkedList;

public class View {
    public static void displayItems(QueueSpace space) {
        // for each display item.
       LinkedList<Object[]> items = Controller.getAllItems(space);

       // TODO: display items.

    }

    public static void displayLobby() {

        // TODO: print the two options

        // TODO: ReadInput
        //Controller.chooseOption(optionNumber);



    }
}
