package org.eclipse.tracecompass.lttng2.kernel.ui;

import org.eclipse.osgi.util.NLS;

/**
 * @author Mahdi Zolnouri
 *
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.lttng2.kernel.ui.messages"; //$NON-NLS-1$
    public static String KernelMemoryUsageComposite_ColumnProcess;
    public static String KernelMemoryUsageComposite_ColumnTID;
    public static String MemoryUsageView_title;
    public static String MemoryUsageViewer_title;
    public static String MemoryUsageViewer_xAxis;
    public static String MemoryUsageViewer_yAxis;
    public static String MemoryUsageViewer_Total;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
