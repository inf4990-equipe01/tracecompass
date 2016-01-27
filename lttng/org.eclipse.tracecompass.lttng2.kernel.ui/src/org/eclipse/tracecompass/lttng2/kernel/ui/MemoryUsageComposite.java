package org.eclipse.tracecompass.lttng2.kernel.ui;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.analysis.os.linux.ui.views.cpuusage.CpuUsageEntry;
import org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory.KernelMemoryAnalysisModule;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.AbstractTmfTreeViewer;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.ITmfTreeColumnDataProvider;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.ITmfTreeViewerEntry;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeColumnData;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeViewerEntry;

/**
 * @author Wassim
 *
 */
public class MemoryUsageComposite extends AbstractTmfTreeViewer{

    private KernelMemoryAnalysisModule fModule = null;
    private String fSelectedThread = null;

    /**
     * @param parent
     */
    public MemoryUsageComposite(Composite parent) {
        super(parent, false);
        setLabelProvider(new MemoryLabelProvider());
    }

    private static final String[] COLUMN_NAMES = new String[] {
            Messages.MemoryUsageComposite_ColumnProcess,
            Messages.MemoryUsageComposite_ColumnTID
    };

    private final Map<String, String> fProcessNameMap = new HashMap<>();


    /**
     * @author Wassim
     *
     */
    protected static class MemoryLabelProvider extends TreeLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            MemoryUsageEntry obj = (MemoryUsageEntry) element;
            if (columnIndex == 0) {
                return obj.getTid();
            } else if (columnIndex == 1) {
                return obj.getProcessName();
            }

            return element.toString();
        }
    }

    @Override
    protected ITmfTreeColumnDataProvider getColumnDataProvider() {
        return new ITmfTreeColumnDataProvider() {

            @Override
            public List<TmfTreeColumnData> getColumnData() {
                List<TmfTreeColumnData> columns = new ArrayList<>();
                TmfTreeColumnData column = new TmfTreeColumnData(COLUMN_NAMES[0]);

                column.setComparator(new ViewerComparator() {
                    @Override
                    public int compare(Viewer viewer, Object e1, Object e2) {
                        MemoryUsageEntry n1 = (MemoryUsageEntry) e1;
                        MemoryUsageEntry n2 = (MemoryUsageEntry) e2;

                        return n1.getTid().compareTo(n2.getTid());

                    }
                });

                columns.add(column);
                column = new TmfTreeColumnData(COLUMN_NAMES[1]);
                column.setComparator(new ViewerComparator() {

                    @Override
                    public int compare(Viewer viewer, Object e1, Object e2) {
                        MemoryUsageEntry n1 = (MemoryUsageEntry) e1;
                        MemoryUsageEntry n2 = (MemoryUsageEntry) e2;

                        return n1.getProcessName().compareTo(n2.getProcessName());
                    }
                });
                columns.add(column);
                return columns;
            }
        };
    }

    @Override
    protected void contentChanged(ITmfTreeViewerEntry rootEntry) {
        String selectedThread = fSelectedThread;
        if (selectedThread != null) {
            /* Find the selected thread among the inputs */
            for (ITmfTreeViewerEntry entry : rootEntry.getChildren()) {
                if (entry instanceof CpuUsageEntry) {
                    if (selectedThread.equals(((CpuUsageEntry) entry).getTid())) {
                        List<ITmfTreeViewerEntry> list = checkNotNull(Collections.singletonList(entry));
                        super.setSelection(list);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void initializeDataSource() {
        /* Should not be called while trace is still null */
        ITmfTrace trace = checkNotNull(getTrace());

        fModule = TmfTraceUtils.getAnalysisModuleOfClass(trace, KernelMemoryAnalysisModule.class, KernelMemoryAnalysisModule.ID);
        if (fModule == null) {
            return;
        }
        fModule.schedule();
        fModule.waitForInitialization();
        fProcessNameMap.clear();
    }


    @Override
    protected ITmfTreeViewerEntry updateElements(long start, long end, boolean isSelection) {
        if (isSelection || (start == end)) {
            return null;
        }
        if (getTrace() == null || fModule == null) {
            return null;
        }
        fModule.waitForInitialization();
        ITmfStateSystem ss = fModule.getStateSystem();
        if (ss == null) {
            return null;
        }

        /*boolean complete = false;
        long currentEnd = start;*/

        ss.waitUntilBuilt();

        TmfTreeViewerEntry root = new TmfTreeViewerEntry(""); //$NON-NLS-1$
        //List<ITmfTreeViewerEntry> entryList = root.getChildren();

        /*public String getColumnText(Object element, int columnIndex) {
            MemoryUsageEntry obj = (MemoryUsageEntry) element;
            if (columnIndex == 0) {
                return obj.getTid();
            } else if (columnIndex == 1) {
                return obj.getProcessName();
            }

            return element.toString();*/

        //MemoryUsageEntry obj = (MemoryUsageEntry) element;

       // MemoryUsageEntry obj = new MemoryUsageEntry(getTid(), getProcessName() );

        return root;
    }

    private String getProcessName(String tid) {
        String execName = fProcessNameMap.get(tid);
        if (execName != null) {
            return execName;
        }

        ITmfTrace trace = getTrace();
        if (trace == null) {
            return tid;
        }

        ITmfStateSystem kernelSs = TmfStateSystemAnalysisModule.getStateSystem(trace, KernelMemoryAnalysisModule.ID);
        if (kernelSs == null) {
            return tid;
        }

        return tid;
    }


    /**
     * @param tid
     */
    public void setSelectedThread(String tid) {
        fSelectedThread = tid;
    }
}
