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
import org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory.KernelMemoryAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.AbstractTmfTreeViewer;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.ITmfTreeColumnDataProvider;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.ITmfTreeViewerEntry;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeColumnData;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeViewerEntry;
/**
 * Tree viewer to display Kernel memory usage information in a specified time range. It
 * shows the process's TID, its name, the time spent on the CPU during that
 * range, in % and absolute value.
 *
 * @author Geneviève Bastien
 * @author Mahdi Zolnouri
 *
 */
public class KernelMemoryUsageComposite extends AbstractTmfTreeViewer{

//    Timeout between to wait for in the updateElements method
//    private static final long BUILD_UPDATE_TIMEOUT = 500;
    private KernelMemoryAnalysisModule fModule = null;
    private String fSelectedThread = null;
    private static final String[] COLUMN_NAMES = new String[] {
            Messages.KernelMemoryUsageComposite_ColumnTID,
           Messages.KernelMemoryUsageComposite_ColumnProcess
    };

    /* A map that saves the mapping of a thread ID to its executable name */
    private final Map<String, String> fProcessNameMap = new HashMap<>();

    /** Provides label for the Kernel memory usage tree viewer cells */
    protected static class KernelMemoryLabelProvider extends TreeLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            KernelMemoryUsageEntry obj = (KernelMemoryUsageEntry) element;
            if (columnIndex == 0) {
                return obj.getTid();
            } else if (columnIndex == 1) {
                return obj.getProcessName();
            }
            return element.toString();
        }

    }
    /**
     * Constructor
     *
     * @param parent
     *            The parent composite that holds this viewer
     */
    public KernelMemoryUsageComposite(Composite parent) {
        super(parent, false);
        // TODO Auto-generated constructor stub
        setLabelProvider(new KernelMemoryLabelProvider());
    }

    @Override
    protected ITmfTreeColumnDataProvider getColumnDataProvider() {
        return new ITmfTreeColumnDataProvider() {

            @Override
            public List<TmfTreeColumnData> getColumnData() {
                /* All columns are sortable */
                List<TmfTreeColumnData> columns = new ArrayList<>();
                TmfTreeColumnData column = new TmfTreeColumnData(COLUMN_NAMES[0]);
                column.setComparator(new ViewerComparator() {
                    @Override
                    public int compare(Viewer viewer, Object e1, Object e2) {
                        KernelMemoryUsageEntry n1 = (KernelMemoryUsageEntry) e1;
                        KernelMemoryUsageEntry n2 = (KernelMemoryUsageEntry) e2;

                        return n1.getTid().compareTo(n2.getTid());
                    }
                });
                columns.add(column);
                column = new TmfTreeColumnData(COLUMN_NAMES[1]);
                column.setComparator(new ViewerComparator() {
                    @Override
                    public int compare(Viewer viewer, Object e1, Object e2) {
                        KernelMemoryUsageEntry n1 = (KernelMemoryUsageEntry) e1;
                        KernelMemoryUsageEntry n2 = (KernelMemoryUsageEntry) e2;

                        return n1.getProcessName().compareTo(n2.getProcessName());
                    }
                });
                columns.add(column);
                return columns;
            }
        };
    }

 // ------------------------------------------------------------------------
    // Operations
    // ------------------------------------------------------------------------

    @Override
    protected void contentChanged(ITmfTreeViewerEntry rootEntry) {
        String selectedThread = fSelectedThread;
        if (selectedThread != null) {
            /* Find the selected thread among the inputs */
            for (ITmfTreeViewerEntry entry : rootEntry.getChildren()) {
                if (entry instanceof KernelMemoryUsageEntry) {
                    if (selectedThread.equals(((KernelMemoryUsageEntry) entry).getTid())) {
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
        // TODO Auto-generated method stub
//        if(isSelection || (start == end)) {
//            return null;
//        }
//        if(getTrace() == null || fModule == null) {
//            return null;
//        }
//        fModule.waitForInitialization();
//        ITmfStateSystem ss = fModule.getStateSystem();
//        if(ss == null) {
//            return null;
//        }
//        boolean complete = false;
//        long currentEnd = start;
//        while(!complete && currentEnd < end){
//            complete = ss.waitUntilBuilt(BUILD_UPDATE_TIMEOUT);
//            currentEnd = ss.getCurrentEndTime();
//        }
        /* Initialize the data */
        TmfTreeViewerEntry root = new TmfTreeViewerEntry(""); //$NON-NLS-1$
//        List<ITmfTreeViewerEntry> entryList = root.getChildren();
        return root;

    }




    /**
     * Set the currently selected thread ID
     *
     * @param tid
     *            The selected thread ID
     */
    public void setSelectedThread(String tid) {
        fSelectedThread = tid;
    }

}
