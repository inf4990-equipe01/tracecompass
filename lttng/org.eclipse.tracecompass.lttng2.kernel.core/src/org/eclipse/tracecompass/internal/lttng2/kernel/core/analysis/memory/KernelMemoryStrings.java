package org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory;

/**
 * Strings for the memory usage state system using the LTTng UST libc
 * instrumentation
 *
 * @author Matthew Khouzam
 * @author Geneviève Bastien
 * @author Mahdi Zolnouri
 * @since 2.0
 */
@SuppressWarnings("nls")
public interface KernelMemoryStrings {
    /** Memory state system attribute name */
    String KERNEL_MEMORY_MEMORY_ATTRIBUTE = "Memory";

    /** Procname state system attribute name */
    String KERNEL_MEMORY_PROCNAME_ATTRIBUTE = "Procname";

    /** Name of the attribute to store memory usage of events with no context */
    String OTHERS = "Others";

}
