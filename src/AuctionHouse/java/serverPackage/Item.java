package serverPackage;

import static java.lang.System.currentTimeMillis;

public class Item {
    private String name;
    private String seller;
    private int startingPrice;
    private long startTime;
    private long endTime;

    public Item(String name, String seller, int startingPrice) {
        this.name = name;
        this.seller = seller;
        this.startingPrice = startingPrice;
        startAuction();
    }

    public void startAuction() {
        this.startTime = currentTimeMillis();
        this.endTime = currentTimeMillis() + 600 * 1000;
    }

    public String getName() {
        return name;
    }

    public int getStartingPrice() {
        return startingPrice;
    }
}
