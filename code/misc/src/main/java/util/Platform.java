package util;

public class Platform {
	private static final String os = System.getProperty("os.name");

	private static final boolean WINDOWS = os.startsWith("Windows");

	public static boolean isWindows() {
		return WINDOWS;
	}
}
