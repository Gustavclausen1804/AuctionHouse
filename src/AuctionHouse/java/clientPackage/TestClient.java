package clientPackage;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import serverPackage.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class TestClient implements Runnable {
    RemoteSpace lobbySpace;
    RemoteSpace auctionSpace;
    RemoteSpace usersSpace;
    RemoteSpace auction;
    Object[] lobbyChoice;
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    String uri;
    String name;
    User user;
    Object[] topBid;
    Object[] time;
    ArrayList<String> seenList = new ArrayList<>();
    public void run() {
        try {
            uri = "tcp://127.0.0.1:9001/";
            usersSpace = new RemoteSpace(uri + "users?keep");
            String[] userCreationInput = View.displayUsercreation();
            user = new User(userCreationInput[0], userCreationInput[1], System.currentTimeMillis());
            user.putUsertoServer(usersSpace);
            name = user.getUserName();
            user.setUserId((String) usersSpace.get(new FormalField(String.class),new ActualField(name))[0]);
            System.out.println(user.getUserId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startup();
    }

    public void startup() {
        // Set the URI of the chat space
        // Default value
        try {
            uri = "tcp://127.0.0.1:9001/";
            lobbySpace = new RemoteSpace(uri + "lobby?keep");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lobbySelection();
    }

    public void lobbySelection() {
        try {
            Object[] choices = lobbySpace.query(new FormalField(String.class),new FormalField(String.class), new FormalField(String.class));
            for (Object choice : choices) {
                System.out.println((String) choice);
            }
            String choiceInput = input.readLine();
            lobbySpace.put(parseInt(choiceInput),name);
            lobbyChoice = lobbySpace.get(new FormalField(String.class),new ActualField(name));
            auctionSpace = new RemoteSpace(uri + (String) lobbyChoice[0] + "?keep");
            bidding();
            submitting();
            depositFunds();
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
                System.out.println("0. Go back");
                for (Object[] listing : auctionList) {
                    Item item1 = (Item) listing[1];
                    int counter = (int) listing[0];
                    System.out.println(counter + ": " + item1.getName());
                }
                String auctionChoice = input.readLine();
                if (Objects.equals(auctionChoice,"0")) {
                    lobbySelection();
                    return;
                }
                while (true) {
                    try {
                        auction = new RemoteSpace(uri + "auction" + auctionChoice + "?keep");
                        topBid = auction.get(new ActualField("topBid"), new FormalField(Integer.class), new FormalField(ArrayList.class));
                        seenList = (ArrayList<String>) topBid[2];
                        if (seenList.contains(user.getUserId())) continue;
                        System.out.println("after if");
                        seenList.add(user.getUserId());
                        auction.put((String) topBid[0], (int) topBid[1], seenList);
                        System.out.println("Current top bid: " + (int) topBid[1]);
                        //time = auction.query(new ActualField("time"), new FormalField(Long.class));
                        new Thread(() -> {
                            while (true) {
                                try {
                                    String bid = input.readLine();
                                    auction.put(user.getUserId(), name, parseInt(bid));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        /*if ((long) time[1] <= 0) {
                            break;
                        }*/
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
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
                System.out.println("0. Go back");
                String item = input.readLine();
                if (Objects.equals(item,"0")) {
                    lobbySelection();
                    return;
                }
                String[] itemArray = item.split("\\s+");
                auctionSpace.put(itemArray[0], name, parseInt(itemArray[1]));
            } catch (IOException | InterruptedException | NumberFormatException e) {
                throw new RuntimeException(e);
            }
            lobbySelection();
        }
    }

    public void depositFunds() {
        if (Objects.equals((String) lobbyChoice[0], name + "wallet")) {
            try {
                int currentWallet = (int) auctionSpace.get(new FormalField(Integer.class))[0];
                System.out.println("Current balance: " + currentWallet);
                System.out.println("Sum to deposit: ");
                System.out.println("0. Go back");
                String deposit = input.readLine();
                if (Objects.equals(deposit,"0")) {
                    lobbySelection();
                    return;
                }
                int newWallet = currentWallet + parseInt(deposit);
                auctionSpace.put(newWallet);
                System.out.println(newWallet);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lobbySelection();
    }

}
