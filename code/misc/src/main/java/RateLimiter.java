import java.util.concurrent.TimeUnit;

public class RateLimiter {
	private static int messages = 2;
	private static long timeInMS = TimeUnit.SECONDS.toMillis(3);
	private double allowance = messages;
	private long lastCheck = System.currentTimeMillis();

	public <T> T checkLimit(T message) {
		long current = System.currentTimeMillis();
		long timePassed = current - lastCheck;
		lastCheck = current;
		allowance += timePassed * ((double) messages / timeInMS);
		if (messages < allowance) allowance = messages;

		if (1 <= allowance) {
			allowance -= 1;
			return message;
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		RateLimiter limiter = new RateLimiter();
		long startTime = System.currentTimeMillis();
		long runFor = 3000; // sec
		while (System.currentTimeMillis() < startTime + runFor) {
			long message = System.nanoTime();
			Long result = limiter.checkLimit(message);
			if (null != result) System.out.println(result);
		}
	}
}
