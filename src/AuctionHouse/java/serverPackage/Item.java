package serverPackage;

import static java.lang.System.currentTimeMillis;

public class Item {
    private final String name;
    private final String seller;
    private final int startingPrice;
    private long endTime;

    public Item(String name, String seller, int startingPrice) {
        this.name = name;
        this.seller = seller;
        this.startingPrice = startingPrice;
        startAuction();
    }

    public void startAuction() {
        long startTime = currentTimeMillis();
        //this.endTime = currentTimeMillis() + 600 * 1000;
        this.endTime = currentTimeMillis() + 30000;
    }

    public String getSeller() { return seller;}
    public String getName() {
        return name;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public long getEndTime() {
        return endTime;
    }
}
