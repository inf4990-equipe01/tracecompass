package org.eclipse.tracecompass.lttng2.kernel.ui;

import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeViewerEntry;

public class KernelMemoryUsageEntry extends TmfTreeViewerEntry{

    private final String fTid;
    private final String fProcessName;
    private final Double fPercent;
    private final Long fTime;
    /**
     * @param tid
     *              The TID of the process
     * @param name
     *              The thread's name
     * @param percent
     *              The percentage of Kernel Memory usage
     * @param time
     *              The total amount of time
     */
    public KernelMemoryUsageEntry(String tid, String name, double percent, long time) {
        super(tid);
        fTid = tid;
        fProcessName = name;
        fPercent = percent;
        fTime = time;
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

    /**
     * Get the percentage of time spent on CPU in the time interval represented
     * by this entry.
     *
     * @return The percentage of time spent on CPU
     */
    public Double getPercent() {
        return fPercent;
    }

    /**
     * Get the total time spent on CPU in the time interval represented by this
     * entry.
     *
     * @return The total time spent on CPU
     */
    public Long getTime() {
        return fTime;
    }

}
