package at.qe.skeleton.model;

public enum MailInterval {
    OFF(0),DAILY(1),WEEKLY(7),MONTHLY(30);

    private final int interval;

    MailInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }
}
