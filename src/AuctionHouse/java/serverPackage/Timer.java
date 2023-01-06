package serverPackage;

public class Timer {

    long currentTime;

    public Timer() {
        this.currentTime = System.currentTimeMillis();
    }

    public long getTimeStamp() {
        return currentTime;
    }

}
