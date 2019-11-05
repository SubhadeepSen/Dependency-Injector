package sdp.injctr.util;

public final class InjectorLogger {

	private InjectorLogger() {

	}

	public static void info(String message) {
		System.out.println(String.format("[INFO] :: " + " %s", message));
	}

	public static void error(String message) {
		System.err.println(String.format("[ERROR] :: " + " %s", message));
	}
}
