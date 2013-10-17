package dk.xpages.log;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.time.StopWatch;

import dk.xpages.utils.NotesStrings;

public class Clock implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient final Map<String, StopWatch> watches = new TreeMap<String, StopWatch>();
	private final String SYSTEM_WATCH = "*system*";

	public void start(String clockname) {
		StopWatch watch;
		//Reuse existing watch ?
		if (watches.containsKey(clockname)) {
			watch = watches.get(clockname);
			watch.resume();
		} else { //Create a new watch
			watch = new StopWatch();
			watch.start();
			watches.put(clockname, watch);
		}
	}

	public void start() {
		start(SYSTEM_WATCH);
	}

	public void stop(String clockname) {
		StopWatch watch = getWatch(clockname);
		watch.suspend();
	}

	public void stop() {
		stop(SYSTEM_WATCH);
	}

	public void reset(String clockname) {
		if (watches.containsKey(clockname)) {
			watches.remove(clockname);
		}
	}

	public void reset() {
		reset(SYSTEM_WATCH);
	}

	public void resetAll() {
		watches.clear();
	}

	public boolean hasWatch(String clockname) {
		return watches.containsKey(clockname);
	}

	public boolean hasWatch() {
		return hasWatch(SYSTEM_WATCH);
	}

	public StopWatch getWatch(String clockname) {
		return watches.get(clockname);
	}

	public StopWatch getWatch() {
		return getWatch(SYSTEM_WATCH);
	}

	/**
	 * 
	 * @param formatPattern {0} is the key, {1} is the full clock result.  {2} is the time in ms
	 * @return a string with all the clock formatted as specified by the pattern
	 * 
	 * Sample: getFormayyedString( "{0}: {2} ms " )
	 */
	public String getFormattedString(String formatPattern) {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, StopWatch> watch : watches.entrySet()) {
			sb.append(NotesStrings.messageFormat(formatPattern, watch.getKey(), watch.getValue().toString(), watch.getValue().getTime()));
		}

		return sb.toString();

	}

	/**
	 * Returns a string with readings from all watches
	 */
	@Override
	public String toString() {
		return getFormattedString("Clock {0}={1};");

	}
}
