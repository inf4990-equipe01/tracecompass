package org.eclipse.tracecompass.internal.lttng2.kernel.ui.views.memusage;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.internal.lttng2.kernel.ui.views.memusage.messages"; //$NON-NLS-1$
    /** Title of the memory usage xy view */
    public static String MemoryUsageView_Title;

    /** Title of the memory viewer */
    public static String MemoryUsageViewer_Title;
    /** X axis caption */
    public static String MemoryUsageViewer_XAxis;
    /** Y axis caption */
    public static String MemoryUsageViewer_YAxis;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}