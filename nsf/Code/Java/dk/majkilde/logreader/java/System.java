package dk.majkilde.logreader.java;

import java.lang.management.ManagementFactory;

import org.apache.commons.lang3.StringEscapeUtils;

public class System {
	public static String getJVMVersion() {
		return java.lang.System.getProperty("java.version");
	}

	public static String getLineSeperator() {
		return java.lang.System.getProperty("line.separator");
	}

	public static String getEscapedLineSeperator() {
		return StringEscapeUtils.escapeJava(getLineSeperator());
	}

	public static long getUsedMemory() {
		return getTotalMemory() - getFreeMemory();
	}

	public static long getTotalMemory() {
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		return runtime.totalMemory();
	}

	public static long getFreeMemory() {
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		return runtime.freeMemory();
	}

	public static long getUsedHeapMemory() {
		return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
		//doc.replaceItemValue("nonHeapBefore", ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed()).recycle();
	}

	public static long getUsedNoneHeapMemory() {
		return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
	}
}
