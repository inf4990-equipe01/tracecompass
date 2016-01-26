package org.eclipse.tracecompass.internal.lttng2.kernel.ui.w;

import org.eclipse.osgi.util.NLS;


/**
 * @since 2.0
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS{

    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.internal.lttng2.kernel.ui.w.messages"; //$NON-NLS-1$

    public static String MemoryUsageView_Title;
    public static String MemoryUsageViewer_Title;
    public static String MemoryUsageViewer_XAxis;
    public static String MemoryUsageViewer_YAxis;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages(){
    }
}
