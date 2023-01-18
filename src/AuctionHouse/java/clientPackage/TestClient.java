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
    String choiceInput;
    User user;
    Object[] topBid;
    ArrayList<String> seenList = new ArrayList<>();
    public void run() {
        try {
            System.out.println("Input Server IP:");
            String ip = input.readLine();
            uri = "tcp://" + ip + ":9001/";
            usersSpace = new RemoteSpace(uri + "users?keep");
            String[] userCreationInput = View.displayUsercreation();
            user = new User(userCreationInput[0], userCreationInput[1], System.currentTimeMillis());
            user.putUsertoServer(usersSpace);
            name = user.getUserName();
            user.setUserId((String) usersSpace.get(new FormalField(String.class),new ActualField(name))[0]);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        startup();
    }

    public void startup() {
        // Set the URI of the chat space
        // Default value
        try {
            lobbySpace = new RemoteSpace(uri + "lobby?keep");
        } catch (IOException e) {
            e.printStackTrace();
        }
        lobbySelection();
    }

    public void lobbySelection() {
        try {
            Object[] choices = lobbySpace.query(new FormalField(String.class),new FormalField(String.class), new FormalField(String.class), new FormalField(String.class));
            for (Object choice : choices) {
                System.out.println((String) choice);
            }
            choiceInput = input.readLine();
            lobbySpace.put(parseInt(choiceInput),user.getUserId());
            lobbyChoice = lobbySpace.get(new FormalField(String.class),new ActualField(user.getUserId()));
            auctionSpace = new RemoteSpace(uri + (String) lobbyChoice[0] + "?keep");

            bidding();
            submitting();
            depositFunds();
            viewInventory();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("Incorrect input");
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
                    return;
                }
                auction = new RemoteSpace(uri + "auction" + auctionChoice + "?keep");
                Thread getTopBid = new Thread(new Runnable() {
                    public void run() {
                        while (true) {
                            try {
                                topBid = auction.get(new ActualField("topBid"), new FormalField(Integer.class), new FormalField(ArrayList.class), new FormalField(String.class));
                                seenList = (ArrayList<String>) topBid[2];
                                if (seenList.contains(user.getUserId())) {
                                    auction.put((String) topBid[0], (int) topBid[1], seenList, (String) topBid[3]);
                                    continue;
                                }
                                seenList.add(user.getUserId());
                                auction.put((String) topBid[0], (int) topBid[1], seenList, (String) topBid[3]);
                                System.out.println("Current top bid: " + (int) topBid[1] + " by " + (String) topBid[3]);
                                //time = auction.query(new ActualField("time"), new FormalField(Long.class));

                                 /*if ((long) time[1] <= 0) {
                                 break;
                                 }*/
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                    }
                });
                Thread makeBid = new Thread(new Runnable() {
                    public void run() {
                        while (true) {
                            try {
                                String bid = input.readLine();
                                if (Objects.equals(bid, "0")) {
                                    lobbySelection();
                                    //getTopbid.interrupt();
                                    return;
                                }
                                auction.put(user.getUserId(), name, parseInt(bid));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                    }
                });
                getTopBid.start();
                makeBid.start();
                while (true) {
                    try {
                        Object[] end = auction.get(new ActualField("end"));
                        auction.put((String) end[0]);
                        System.out.println("The auction has ended.");
                        getTopBid.interrupt();
                        makeBid.interrupt();
                        break;
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } catch (InterruptedException | IOException e) {
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
                    return;
                }
                String[] itemArray = item.split("\\s+");
                auctionSpace.put(itemArray[0], user.getUserId(), parseInt(itemArray[1]));
            } catch (IOException | InterruptedException | NumberFormatException e) {
                throw new RuntimeException(e);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Incorrect input");
            }
        }
    }

    public void depositFunds() {
        if (Objects.equals((String) lobbyChoice[0], user.getUserId() + "wallet")) {
            try {
                int currentWallet = (int) auctionSpace.get(new FormalField(Integer.class))[0];
                System.out.println("Current balance: " + currentWallet);
                System.out.println("Sum to deposit: ");
                System.out.println("0. Go back");
                String deposit = input.readLine();
                if (Objects.equals(deposit,"0")) {
                    auctionSpace.put(currentWallet);
                    return;
                }
                int newWallet = currentWallet + parseInt(deposit);
                auctionSpace.put(newWallet);
                System.out.println(newWallet);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void viewInventory() {
        if (Objects.equals((String) lobbyChoice[0], "user" + user.getUserId())) {
            try {
                int counter = 1;
                System.out.println("0. Go back");
                List<Object[]> itemList = auctionSpace.queryAll(new FormalField(Item.class));
                for (Object[] entry : itemList) {
                    Item item = (Item) entry[0];
                    System.out.println(counter + ": " + item.getName());
                    counter++;
                }
                String goBack = input.readLine();
                if (Objects.equals(goBack, "0")) {
                    lobbySelection();
                    return;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        lobbySelection();
    }
}
