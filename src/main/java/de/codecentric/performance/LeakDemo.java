package de.codecentric.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeakDemo {

	private final List<DummyData> listLeak = new ArrayList<DummyData>();
	private final List<DummyData> cache = new ArrayList<DummyData>();
	private final Map<Integer, DummyData> mapLeak = new HashMap<Integer, DummyData>();

	public void runAndLeak() throws InterruptedException {
		int count = 0;
		while (true) {
			cache.add(new DummyData());
			listLeak.add(cache.get(count));
			mapLeak.put(count, cache.get(count));
			count++;
		}
	}
}
