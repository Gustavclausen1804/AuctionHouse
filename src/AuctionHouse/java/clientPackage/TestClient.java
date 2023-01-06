package clientPackage;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import serverPackage.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class TestClient implements Runnable {
    RemoteSpace lobbySpace;
    RemoteSpace auctionSpace;
    Object[] lobbyChoice;
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    String uri;
    String name;
    public void run() {
        lobbySelection();
        bidding();
        submitting();
    }
    public void lobbySelection() {
        try {
            // Set the URI of the chat space
            // Default value
            uri = "tcp://127.0.0.1:9001/";

            lobbySpace = new RemoteSpace(uri + "lobby?keep");
            System.out.print("Enter your name: ");
            name = input.readLine();
            Object[] choices = lobbySpace.query(new FormalField(String.class),new FormalField(String.class));
            for (Object choice : choices) {
                System.out.println((String) choice);
            }
            String choiceInput = input.readLine();
            lobbySpace.put(parseInt(choiceInput),name);
            lobbyChoice = lobbySpace.get(new FormalField(String.class),new ActualField(name));
            auctionSpace = new RemoteSpace(uri + (String) lobbyChoice[0] + "?keep");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void bidding() {
        if (Objects.equals((String) lobbyChoice[0], "auctionSpace")) {
            try {
                List<Object[]> auctionList = auctionSpace.queryAll(new FormalField(Integer.class), new FormalField(Item.class));
                for (Object[] listing : auctionList) {
                    Item item1 = (Item) listing[1];
                    int counter = (int) listing[0];
                    System.out.println(counter + ": " + item1.getName());
                }
                String auctionChoice = input.readLine();
                RemoteSpace auction = new RemoteSpace(uri + "auction" + auctionChoice + "?keep");
                String startingPrice = (String) auction.query(new FormalField(String.class))[0];
                System.out.println(startingPrice);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void submitting() {
        if (Objects.equals((String) lobbyChoice[0], "newAuctions")) {
            try {
                String item = input.readLine();
                String[] itemArray = item.split("\\s+");
                auctionSpace.put(itemArray[0], name, parseInt(itemArray[1]));
            } catch (IOException | InterruptedException | NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
