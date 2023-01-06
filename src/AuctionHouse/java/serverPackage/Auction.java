package serverPackage;

import org.jspace.FormalField;
import org.jspace.QueueSpace;

import static java.lang.System.currentTimeMillis;

public class Auction {
    private QueueSpace auctionSpace;
    private Item item;
    public Auction(QueueSpace space,Item item) {
        this.auctionSpace = space;
        this.item = item;
        startAuction();
    }

    public void startAuction() {
        try {
            auctionSpace.put("Starting bid: " + item.getStartingPrice());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int topBid = item.getStartingPrice();
        String topUser = "";
        while (currentTimeMillis() < item.getEndTime()) {
            try {
                Object[] bid = auctionSpace.get(new FormalField(String.class),new FormalField(Integer.class),new FormalField(Long.class));
                if ((int) bid[1] > topBid) {
                    topBid = (int) bid[1];
                    topUser = (String) bid[0];
                    System.out.println("Current top bid: " + topBid + "by " + topUser);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Sold for " + topBid + " kr to " + topUser);
    }
}
