package thornyabot.thornyabot.Utils;

public final class TimeUnit {

    public static final long MILLISECOND = 1;
    public static final long SECOND = MILLISECOND * 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;
    public static final long MONTH = DAY * 30;
    public static final long YEAR = DAY * 365;

    private TimeUnit() {
    }
}