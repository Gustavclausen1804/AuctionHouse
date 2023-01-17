package serverPackage;


import org.jspace.*;

import java.net.URI;
import java.net.URISyntaxException;

public class Controller implements Runnable{
    SpaceRepository repo = new SpaceRepository();
    SequentialSpace lobby = new SequentialSpace();
    SequentialSpace usersSpace = new SequentialSpace();
    QueueSpace auctionSpace = new QueueSpace();
    QueueSpace newAuctions = new QueueSpace();
    int counter = 0;


    public void run() {
        try {
            repo.add("lobby", lobby);
            repo.add("auctionSpace", auctionSpace);
            repo.add("newAuctions", newAuctions);
            repo.add("users", usersSpace);
            lobby.put(Lobby.generateOptions());
            getNewUserRequest();
            lobbyChoice();
            generateNewListings();
            openGate();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void generateNewListings() {
        new Thread(() -> {
            while (true) {
                try {
                    counter++;
                    QueueSpace auctionN = new QueueSpace();
                    repo.add( "auction" + counter, auctionN);
                    Object[] newInput = newAuctions.get(new FormalField(String.class),new FormalField(String.class), new FormalField(Integer.class));
                    Item item = new Item((String) newInput[0], (String) newInput[1], (int) newInput[2]);
                    auctionSpace.put(counter, item);
                    System.out.println(item.getName());
                    Auction auction = new Auction(auctionN, item);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void openGate() {
        try {
            String uri = "tcp://127.0.0.1:52644/?keep";
            URI myUri = new URI(uri);
            String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() +  "?keep";
            repo.addGate(gateUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }



    public void lobbyChoice() {
        new Thread(() -> {
            while (true) {
                try {
                    Object[] choice = lobby.get(new FormalField(Integer.class), new FormalField(String.class));
                    if ((int) choice[0] == 1) {
                        lobby.put("auctionSpace",choice[1]);
                    }
                    else if ((int) choice[0] == 2) {
                        lobby.put("newAuctions",choice[1]);
                    }
                    else if ((int) choice[0] == 3) {
                        lobby.put(choice[1] + "wallet",choice[1]);
                    }
                    else if ((int) choice[0] == 4) {
                        System.out.println("user" + choice[1]);
                        lobby.put("user" + choice[1],choice[1]);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void getNewUserRequest() {
        new Thread(() -> {
            while (true) {
                try {
                    Object[] newUserRequest = usersSpace.get(new FormalField(String.class), new FormalField(String.class), new FormalField(Long.class));
                    User user = new User((String) newUserRequest[0], (String) newUserRequest[1], (Long) newUserRequest[2], repo, usersSpace);
                    usersSpace.put(user.userId,user.userName);
                    System.out.println(user.userId + " " + user.userAddress + " " + user.userTimeStamp + " " + user.userSpace);
                    SequentialSpace wallet = new SequentialSpace();
                    repo.add((String) user.userId + "wallet",wallet);
                    wallet.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
