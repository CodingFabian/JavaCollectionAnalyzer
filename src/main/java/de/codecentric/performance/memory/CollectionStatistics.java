package de.codecentric.performance.memory;

import java.util.HashSet;
import java.util.Set;

/**
 * Keeps track of code interaction with a specific collection. The flag stop is used to turn statistics off.
 */
public class CollectionStatistics {

	private static final int DANGEROUS_SIZE = 5000;
	private final String className;
	private final int id;
	/**
	 * This Set tracks unique calling lines of code. It can get very long and become a memory leak on its own!
	 */
	private final Set<String> interactingCode = new HashSet<String>();
	private long reads;
	private long deletes;

	private boolean stop = false;

	public CollectionStatistics(String className, int id) {
		this.className = className;
		this.id = id;
	}

	public void recordWrite(String invokingCode) {
		if (stop)
			return;
		interactingCode.add(invokingCode);
	}

	public void recordRead(String invokingCode) {
		if (stop)
			return;
		interactingCode.add(invokingCode);
		reads++;
	}

	public void recordDelete(String invokingCode) {
		if (stop)
			return;
		interactingCode.add(invokingCode);
		deletes++;
	}

	public void evaluate(int size) {
		if (stop)
			return;
		if (size >= DANGEROUS_SIZE) {
			System.out.printf("\nInformation for Collection %s (id: %d)\n", className, id);
			System.out.printf(" * Collection is very long (%d)!\n", size);

			if (reads == 0) {
				System.out.printf(" * Collection was never read!\n");
			}

			if (deletes == 0) {
				System.out.printf(" * Collection was never reduced!\n");
			}
			System.out.printf("Recorded usage for this Collection:\n");
			for (String code : interactingCode) {
				System.out.printf(" * %s\n", code);
			}
			System.out.printf(
					"Warned about Collection %s (id: %d). For performance reasons not warning about it anymore.\n",
					className, id);

			// at least somehow reduce our impact on CPU and memory
			stop = true;
			interactingCode.clear();
		}
	}
}
