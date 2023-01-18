package serverPackage;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.QueueSpace;
import org.jspace.RemoteSpace;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;

public class Auction {
    private final QueueSpace auctionSpace;
    private final Item item;
    Object[] bid;
    int userWallet;
    RemoteSpace walletSpace;
    RemoteSpace oldWalletSpace;
    String uri = Main.serverAddress;
    private int topBid;
    private int oldTopBid;
    private String oldUserID;
    private String topUser;
    private String topUserID;
    private String userId;
    public Auction(QueueSpace space,Item item) {
        this.auctionSpace = space;
        this.item = item;
        startAuction();
    }

    public void startAuction() {
        Thread auction = new Thread(new Runnable() {
            public void run() {
                //auctionSpace.put("Starting bid: " + item.getStartingPrice());
                topBid = item.getStartingPrice();
                topUser = "";
                topUserID = "";
                userId = "";
                long timeOfBid = 0;
                long oldTimeOfBid;
                while (true) {
                    try {
                        ArrayList<String> seenList = new ArrayList<>();
                        auctionSpace.put("topBid", topBid, seenList, topUser);
                        bid = auctionSpace.get(new FormalField(String.class), new FormalField(String.class),new FormalField(Integer.class)); //userID, userName, bid size
                        System.out.println("got bid");
                        oldTimeOfBid = timeOfBid;
                        timeOfBid = currentTimeMillis();
                        oldUserID = userId;
                        userId = (String) bid[0];
                        oldWalletSpace = new RemoteSpace(uri + oldUserID + "wallet?keep");
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
                        if ((int) bid[2] > topBid || (int) bid[2] == topBid && timeOfBid < oldTimeOfBid) {
                            oldTopBid = topBid;
                            if (!Objects.equals(oldUserID, "")) {
                                int balance = (int) oldWalletSpace.get(new FormalField(Integer.class))[0];
                                balance += oldTopBid;
                                oldWalletSpace.put(balance);
                            }
                            topBid = (int) bid[2];
                            topUser = (String) bid[1];
                            topUserID = (String) bid[0];
                            auctionSpace.get(new ActualField("topBid"), new FormalField(Integer.class), new FormalField(ArrayList.class), new FormalField(String.class));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        auction.start();

        new Thread(() -> {
            while (currentTimeMillis() <= item.getEndTime()) {
                if (currentTimeMillis() == item.getEndTime()) {
                    auction.interrupt();
                    try {
                        System.out.println(item.getName() + " sold for " + topBid + " kr to " + topUser);
                        RemoteSpace userInventory = new RemoteSpace(uri + "user" + topUserID + "?keep");
                        userInventory.put(item);
                        String sellerID = item.getSeller();
                        RemoteSpace sellerWallet = new RemoteSpace(uri + sellerID + "wallet?keep");
                        int sellerBalance = (int) sellerWallet.get(new FormalField(Integer.class))[0];
                        sellerBalance += topBid;
                        sellerWallet.put(sellerBalance);
                        Controller.auctionSpace.get(new FormalField(Integer.class), new ActualField(item));
                        auctionSpace.put("end");
                    } catch (UnknownHostException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    return;
                }
            }
        }).start();


    }
}
