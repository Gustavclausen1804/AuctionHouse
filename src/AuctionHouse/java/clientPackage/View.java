package clientPackage;

import org.jspace.FormalField;
import org.jspace.QueueSpace;
import org.jspace.Space;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;

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

    public static String[] displayUsercreation() throws IOException {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your userName");
            String name = input.readLine();
            System.out.println("Enter your personal address");
            String address = input.readLine();

            return new String[]{name, address};

    }
}
