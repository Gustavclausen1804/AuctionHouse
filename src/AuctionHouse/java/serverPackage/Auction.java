package serverPackage;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.QueueSpace;
import org.jspace.RemoteSpace;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

public class Auction {
    private final QueueSpace auctionSpace;
    private final Item item;
    Object[] bid;
    int userWallet;
    RemoteSpace walletSpace;
    RemoteSpace oldWalletSpace;
    String uri = "tcp://127.0.0.1:9001/";
    public Auction(QueueSpace space,Item item) {
        this.auctionSpace = space;
        this.item = item;
        startAuction();
    }

    public void startAuction() {
        new Thread(() -> {
            //auctionSpace.put("Starting bid: " + item.getStartingPrice());
            int topBid = item.getStartingPrice();
            int oldTopBid;
            String oldUserID = "";
            String topUser = "";
            String topUserID = "";
            String userId = "";

            while (currentTimeMillis() < item.getEndTime()) {
                try {
                    ArrayList<String> seenList = new ArrayList<String>();
                    //topBidTuple = auctionSpace.get(new FormalField(String.class), new FormalField(String.class), new FormalField(ArrayList.class));
                    auctionSpace.getp(new ActualField("time"), new FormalField(Long.class));
                    auctionSpace.put("topBid", topBid, seenList);
                    //auctionSpace.put("time", (item.getEndTime() - currentTimeMillis())/1000);
                    bid = auctionSpace.get(new FormalField(String.class), new FormalField(String.class),new FormalField(Integer.class)); //userID, userName, bid size
                    oldUserID = userId;
                    userId = (String) bid[0];
                    oldWalletSpace = new RemoteSpace(uri + oldUserID + "wallet?keep");
                    System.out.println(uri + oldUserID + "wallet?keep");
                    walletSpace = new RemoteSpace(uri + userId + "wallet?keep");
                    userWallet = (int) walletSpace.get(new FormalField(Integer.class))[0];
                    if (userWallet >= (int) bid[2]) {
                        userWallet -= (int) bid[2];
                        walletSpace.put(userWallet);
                    }
                    else {
                        walletSpace.put(userWallet);
                        continue;
                    }
                    if ((int) bid[2] > topBid) {
                        oldTopBid = topBid;
                        if (oldUserID != "") {
                            System.out.println("putting " + oldTopBid + " into " + oldUserID);
                            int balance = (int) oldWalletSpace.get(new FormalField(Integer.class))[0];
                            balance += oldTopBid;
                            oldWalletSpace.put(balance);
                        }
                        topBid = (int) bid[2];
                        topUser = (String) bid[1];
                        topUserID = (String) bid[0];
                        auctionSpace.get(new ActualField("topBid"), new FormalField(Integer.class), new FormalField(ArrayList.class));
                        System.out.println("Current top bid: " + topBid + "by " + topUser);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Sold for " + topBid + " kr to " + topUser);
        }).start();
    }
}
