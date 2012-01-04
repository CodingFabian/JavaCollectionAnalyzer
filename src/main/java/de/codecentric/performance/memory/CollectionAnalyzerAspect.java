package de.codecentric.performance.memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Note: this Aspect is for demo purpose only and is not threadsafe!
 */
@Aspect
@SuppressWarnings("rawtypes")
public final class CollectionAnalyzerAspect {

	private final Map<Integer, CollectionStatistics> statistics = new HashMap<Integer, CollectionStatistics>();

	/**
	 * Returns a CollectionStatistics instance for the given collection.
	 */
	private CollectionStatistics getStatistics(Object targetCollection) {
		// we use the IdentityHashCode as key for our statistics storage
		int identityHashCode = System.identityHashCode(targetCollection);
		CollectionStatistics stats = statistics.get(identityHashCode);
		if (stats == null) {
			stats = new CollectionStatistics(targetCollection.getClass().getName(), identityHashCode);
			statistics.put(identityHashCode, stats);
		}
		return stats;
	}

	/**
	 * Returns a developer usable String for the line of code the joinpoint is acting on.
	 * <code>de.codecentric.performance.LeakDemo:19</code>
	 */
	private String getLocation(final JoinPoint thisJoinPoint) {
		return thisJoinPoint.getStaticPart().getSourceLocation().getWithinType().getName() + ":"
				+ thisJoinPoint.getStaticPart().getSourceLocation().getLine();
	}

	@Before("   call(boolean java.util.List.add(..))")
	public void trackListAdds(final JoinPoint thisJoinPoint) {
		List target = (List) thisJoinPoint.getTarget();
		CollectionStatistics stats = getStatistics(target);
		stats.recordWrite(getLocation(thisJoinPoint));
		stats.evaluate(target.size());
	}

	@Before("   call(* java.util.List.get(..))")
	public void trackListGets(final JoinPoint thisJoinPoint) {
		List target = (List) thisJoinPoint.getTarget();
		CollectionStatistics stats = getStatistics(target);
		stats.recordRead(getLocation(thisJoinPoint));
		stats.evaluate(target.size());
	}

	@Before("   call(* java.util.List.remove(..))")
	public void trackListRemoves(final JoinPoint thisJoinPoint) {
		List target = (List) thisJoinPoint.getTarget();
		CollectionStatistics stats = getStatistics(target);
		stats.recordDelete(getLocation(thisJoinPoint));
		stats.evaluate(target.size());
	}

	/**
	 * We use a Map ourselves which we do not want to track (would recurse indefinitely)).
	 */
	@Before("   call(* java.util.Map.put(..)) && !this(de.codecentric.performance.memory.CollectionAnalyzerAspect)")
	public void trackMapPuts(final JoinPoint thisJoinPoint) {
		Map target = (Map) thisJoinPoint.getTarget();
		CollectionStatistics stats = getStatistics(target);
		stats.recordWrite(getLocation(thisJoinPoint));
		stats.evaluate(target.size());
	}

	/**
	 * We use a Map ourselves which we do not want to track (would recurse indefinitely)).
	 */
	@Before("   call(* java.util.Map.get(..)) && !this(de.codecentric.performance.memory.CollectionAnalyzerAspect)")
	public void trackMapGets(final JoinPoint thisJoinPoint) {
		Map target = (Map) thisJoinPoint.getTarget();
		CollectionStatistics stats = getStatistics(target);
		stats.recordRead(getLocation(thisJoinPoint));
		stats.evaluate(target.size());
	}

	/**
	 * We use a Map ourselves which we do not want to track (would recurse indefinitely)).
	 */
	@Before("   call(* java.util.Map.remove(..)) && !this(de.codecentric.performance.memory.CollectionAnalyzerAspect)")
	public void trackMapRemoves(final JoinPoint thisJoinPoint) {
		Map target = (Map) thisJoinPoint.getTarget();
		CollectionStatistics stats = getStatistics(target);
		stats.recordDelete(getLocation(thisJoinPoint));
		stats.evaluate(target.size());
	}

}
