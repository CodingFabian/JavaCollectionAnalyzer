package de.codecentric.performance;

/**
 * To run the Collection Analyzer just add:
 * -javaagent:C:\Users\fabian.lange\.m2\repository\org\aspectj\aspectjweaver\1.6.11\aspectjweaver-1.6.11.jar
 */
public class DemoRunner {

	public static void main(final String[] args) throws InterruptedException {
		System.out.println("Total Heap: " + Runtime.getRuntime().totalMemory());
		System.out.println("Free Heap: " + Runtime.getRuntime().freeMemory());
		new LeakDemo().runAndLeak();
	}

}
