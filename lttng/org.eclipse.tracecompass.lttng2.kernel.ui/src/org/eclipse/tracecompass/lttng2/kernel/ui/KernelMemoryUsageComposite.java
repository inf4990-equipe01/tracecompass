package org.eclipse.tracecompass.lttng2.kernel.ui;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.analysis.os.linux.core.cpuusage.KernelCpuUsageAnalysis;
import org.eclipse.tracecompass.analysis.os.linux.ui.views.cpuusage.CpuUsageEntry;
import org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory.KernelMemoryAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.AbstractTmfTreeViewer;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.ITmfTreeColumnDataProvider;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.ITmfTreeViewerEntry;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeColumnData;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeColumnData.ITmfColumnPercentageProvider;

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

    private KernelMemoryAnalysisModule fModule = null;
    private String fSelectedThread = null;
    private static final String[] COLUMN_NAMES = new String[] {
            Messages.KernelMemoryUsageComposite_ColumnTID,
            Messages.KernelMemoryUsageComposite_ColumnProcess,
            Messages.KernelMemoryUsageComposite_ColumnPercent,
            Messages.KernelMemoryUsageComposite_ColumnTime
    };

    /** Provides label for the Kernel memory usage tree viewer cells */
    protected static class KernelMemoryLabelProvider extends TreeLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            KernelMemoryUsageEntry obj = (KernelMemoryUsageEntry) element;
            if (columnIndex == 0) {
                return obj.getTid();
            } else if (columnIndex == 1) {
                return obj.getProcessName();
            } else if (columnIndex == 2) {
                return String.format(Messages.KernelMemoryUsageComposite_TextPercent, obj.getPercent());
            } else if (columnIndex == 3) {
                return NLS.bind(Messages.KernelMemoryUsageComposite_TextTime, obj.getTime());
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
                column = new TmfTreeColumnData(COLUMN_NAMES[2]);
                column.setComparator(new ViewerComparator() {
                    @Override
                    public int compare(Viewer viewer, Object e1, Object e2) {
                        KernelMemoryUsageEntry n1 = (KernelMemoryUsageEntry) e1;
                        KernelMemoryUsageEntry n2 = (KernelMemoryUsageEntry) e2;

                        return n1.getPercent().compareTo(n2.getPercent());

                    }
                });
                column.setPercentageProvider(new ITmfColumnPercentageProvider() {

                    @Override
                    public double getPercentage(Object data) {
                        KernelMemoryUsageEntry parent = (KernelMemoryUsageEntry) data;
                        return parent.getPercent() / 100;
                    }
                });
                columns.add(column);
                column = new TmfTreeColumnData(COLUMN_NAMES[3]);
                column.setComparator(new ViewerComparator() {
                    @Override
                    public int compare(Viewer viewer, Object e1, Object e2) {
                        KernelMemoryUsageEntry n1 = (KernelMemoryUsageEntry) e1;
                        KernelMemoryUsageEntry n2 = (KernelMemoryUsageEntry) e2;

                        return n1.getTime().compareTo(n2.getTime());

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

        fModule = TmfTraceUtils.getAnalysisModuleOfClass(trace, KernelCpuUsageAnalysis.class, KernelCpuUsageAnalysis.ID);
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
        return null;
    }

}
