package org.eclipse.tracecompass.lttng2.kernel.ui;

import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeViewerEntry;

/**
 * @author Wassim Nasrallah
 *
 */
public class MemoryUsageEntry extends TmfTreeViewerEntry  {

    private final String fTid;
    private final String fProcessName;

    /**
     * Constructor
     *
     * @param tid
     *            The TID of the process
     * @param name
     *            The thread's name
     */
    public MemoryUsageEntry(String tid, String name) {
        super(tid);
        fTid = tid;
        fProcessName = name;

    }

    /**
     * Get the TID of the thread represented by this entry
     *
     * @return The thread's TID
     */
    public String getTid() {
        return fTid;
    }

    /**
     * Get the process name
     *
     * @return The process name
     */
    public String getProcessName() {
        return fProcessName;
    }
}
