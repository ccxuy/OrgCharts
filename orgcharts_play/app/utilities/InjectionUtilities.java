package utilities;

public class InjectionUtilities {

	public void printCallMethodName() {
		try {
			System.out.println(InjectionUtilities.getMethodName(2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the method name for a depth in call stack. <br />
	 * Utility function
	 * 
	 * @param depth
	 *            depth in the call stack (0 means current method, 1 means call
	 *            method, ...)
	 * @return method name
	 */
	public static String getMethodName(final int depth) {
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

		// System.
		// out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
		// return ste[ste.length - depth].getMethodName(); //Wrong, fails for
		// depth = 0
		return ste[2 + depth].getMethodName();
		// return ste[ste.length - 1 - depth].getMethodName(); //Thank you Tom
		// Tresansky
	}
}
