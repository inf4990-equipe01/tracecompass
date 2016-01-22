package org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory;

import org.eclipse.osgi.util.NLS;

/**
 * Message bundle for the ust memory analysis module
 *
 * @author Guilliano Molaire
 * @since 2.0
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.lttng2.ust.core.analysis.memory.messages"; //$NON-NLS-1$

    /** Information regarding events loading prior to the analysis execution */
    public static String KernelMemoryAnalysisModule_EventsLoadingInformation;

    /** Example of how to execute the application with the libc wrapper */
    public static String KernelMemoryAnalysisModule_EventsLoadingExampleInformation;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
