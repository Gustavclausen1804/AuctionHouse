package serverPackage;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class TestClient {
    public static void main(String[] args) {
        try {

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            // Set the URI of the chat space
            // Default value
            String uri = "tcp://127.0.0.1:9001/";

            RemoteSpace lobbySpace = new RemoteSpace(uri + "lobby?keep");
            System.out.print("Enter your name: ");
            String name = input.readLine();
            Object[] choices = lobbySpace.query(new FormalField(String.class),new FormalField(String.class));
            for (int i = 0; i < choices.length; i++) {
                System.out.println((String) choices[i]);
            }
            String choiceInput = input.readLine();
            lobbySpace.put(parseInt(choiceInput),name);
            Object[] lobbyChoice = lobbySpace.get(new FormalField(String.class),new ActualField(name));
            RemoteSpace auctionSpace = new RemoteSpace(uri + (String) lobbyChoice[0] + "?keep");
            if (Objects.equals((String) lobbyChoice[0], "auctionSpace")) {
                try {
                    Object[] auctionListing = auctionSpace.get(new FormalField(Item.class));
                    Item item1 = (Item) auctionListing[0];
                    System.out.println(item1.getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (Objects.equals((String) lobbyChoice[0], "newAuctions")) {
                try {
                    String item = input.readLine();
                    String[] itemArray = item.split("\\s+");
                    auctionSpace.put(itemArray[0], name, parseInt(itemArray[1]));
                } catch (IOException | InterruptedException | NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
            /*new Thread(() -> {
                try {
                    while (true) {
                        String item = input.readLine();
                        String[] itemArray = item.split("\\s+");
                        lobbySpace.put(itemArray[0], name, parseInt(itemArray[1]));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            new Thread(() -> {
                try {
                    while(true) {
                        String newUri = "tcp://127.0.0.1:9001/auctionSpace?keep";
                        RemoteSpace auction1 = new RemoteSpace(newUri);
                        Object[] auctionListing = auction1.get(new FormalField(Item.class));
                        Item item1 = (Item) auctionListing[0];
                        System.out.println(item1.getName());
                    }
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }).start();*/
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
