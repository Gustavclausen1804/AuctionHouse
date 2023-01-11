package serverPackage;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.QueueSpace;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

public class Auction {
    private final QueueSpace auctionSpace;
    private final Item item;
    Object[] bid;
    public Auction(QueueSpace space,Item item) {
        this.auctionSpace = space;
        this.item = item;
        startAuction();
    }

    public void startAuction() {
        new Thread(() -> {
            //auctionSpace.put("Starting bid: " + item.getStartingPrice());
            int topBid = item.getStartingPrice();
            String topUser = "";
            while (currentTimeMillis() < item.getEndTime()) {
                try {
                    ArrayList<String> seenList = new ArrayList<String>();
                    System.out.println(seenList);
                    auctionSpace.getp(new ActualField("time"), new FormalField(Long.class));
                    auctionSpace.put("topBid", topBid, seenList);
                    //auctionSpace.put("time", (item.getEndTime() - currentTimeMillis())/1000);
                    bid = auctionSpace.get(new FormalField(String.class), new FormalField(String.class),new FormalField(Integer.class)); //userID, userName, bid size
                    System.out.println("got bid");
                    if ((int) bid[2] > topBid) {
                        topBid = (int) bid[2];
                        topUser = (String) bid[1];
                        //auctionSpace.get(new ActualField("topBid"), new FormalField(Integer.class), new FormalField(ArrayList.class));
                        System.out.println("Current top bid: " + topBid + "by " + topUser);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Sold for " + topBid + " kr to " + topUser);
        }).start();
    }
}
