package serverPackage;

import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import static java.lang.Integer.parseInt;

public class TestClient {
    public static void main(String[] args) {
        try {

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            // Set the URI of the chat space
            // Default value
            System.out.print("Enter URI of the chat server or press enter for default: ");
            String uri = input.readLine();
            // Default value
            if (uri.isEmpty()) {
                uri = "tcp://127.0.0.1:9001/newAuctions?keep";
            }

            RemoteSpace auction = new RemoteSpace(uri);
            System.out.print("Enter your name: ");
            String name = input.readLine();
            new Thread(() -> {
                try {
                    while (true) {
                        String item = input.readLine();
                        String[] itemArray = item.split("\\s+");
                        auction.put(itemArray[0], name, parseInt(itemArray[1]));
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

            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
