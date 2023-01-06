package clientPackage;

public class Item {

    int startPrice;
    int currentPrice;
    String seller;
    long startTime;
    long endTime;

    public Item(int startPrice, int currentPrice, String seller, long startTime, long endTime) {
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.seller = seller;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}
