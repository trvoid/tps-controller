package trvoid.tps;

public class TpsInfo {
    private final int allowedTps;

    private long startTime;
    private int requestCount;

    public TpsInfo(int allowedTps) {
        if (allowedTps <= 0) {
            throw new IllegalArgumentException("allowedTps must be larger than 0.");
        }

        this.allowedTps = allowedTps;

        this.startTime = 0L;
        this.requestCount = 0;
    }

    private void reset() {
        startTime = System.currentTimeMillis();
        requestCount = 1;
    }

    public boolean incrementRequestCount(boolean sleepOnOverAllowedTps) {
        boolean withinAllowedTps = true;

        if (requestCount == 0) {
            reset();
        } else if (requestCount < allowedTps) {
            requestCount++;
        } else {
            long diff = startTime + 1000L - System.currentTimeMillis();

            if (diff <= 0) {
                reset();
            } else {
                if (sleepOnOverAllowedTps) {
                    try {
                        Thread.sleep(diff);

                        reset();
                    } catch (InterruptedException e) {
                        withinAllowedTps = false;
                    }
                } else {
                    withinAllowedTps = false;
                }
            }
        }

        return withinAllowedTps;
    }
}
