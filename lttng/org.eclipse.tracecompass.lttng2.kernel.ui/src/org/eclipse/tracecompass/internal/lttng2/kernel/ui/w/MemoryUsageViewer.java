package org.eclipse.tracecompass.internal.lttng2.kernel.ui.w;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.internal.lttng2.kernel.ui.Activator;
import org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory.KMemoryAnalysisModule;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateSystemDisposedException;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfCommonXLineChartViewer;



/**
 * @since 2.0
 */
@SuppressWarnings("javadoc")
public class MemoryUsageViewer extends TmfCommonXLineChartViewer{

    public MemoryUsageViewer(Composite parent) {
        super(parent, Messages.MemoryUsageViewer_Title, Messages.MemoryUsageViewer_XAxis, Messages.MemoryUsageViewer_YAxis);
        // TODO Auto-generated constructor stub
    }

    private TmfStateSystemAnalysisModule fModule = null;
    private static final int BYTES_TO_KB = 1024;
    private static final long BUILD_UPDATE_TIMEOUT = 500;

    /**
     * @param parent
     */

    @Override
    protected void initializeDataSource() {
        ITmfTrace trace = getTrace();
        if (trace != null) {
            fModule = TmfTraceUtils.getAnalysisModuleOfClass(trace, TmfStateSystemAnalysisModule.class, KMemoryAnalysisModule.ID);
            if (fModule == null) {
                return;
            }
            fModule.schedule();
        }
    }

    @Override
    protected void updateData(long start, long end, int nb, IProgressMonitor monitor) {

        if (getTrace() == null || fModule == null) {
            return;
        }

        double[] xvalues = getXAxis(start, end, nb);
        setXAxis(xvalues);

        fModule.waitForInitialization();
        ITmfStateSystem ss = fModule.getStateSystem();
        /* Don't wait for the module completion, when it's ready, we'll know */
        if (ss == null) {
            return;
        }

        boolean complete = false;
        long currentEnd = start;

        while (!complete && currentEnd < end) {
            if (monitor.isCanceled()) {
                return;
            }
            complete = ss.waitUntilBuilt(BUILD_UPDATE_TIMEOUT);
            currentEnd = ss.getCurrentEndTime();

            try {
                List<Integer> tidQuarks = ss.getSubAttributes(-1, false);
                for (int quark : tidQuarks) {
                    double yvalue = 0.0;
                    double[] values = new double[xvalues.length];
                    for (int i = 0; i < xvalues.length; i++) {
                        if (monitor.isCanceled()) {
                            return;
                        }
                        double x = xvalues[i];

                        try{
                            Integer memQuark = ss.getQuarkRelative(quark, "kmem_allocation");
                            yvalue = ss.querySingleState((long) x + this.getTimeOffset(), memQuark.intValue()).getStateValue().unboxLong() / BYTES_TO_KB;
                            values[i] = yvalue;
                        } catch (AttributeNotFoundException | StateSystemDisposedException e) {
                            Activator.getDefault().logError(e.getMessage(), e);
                        }
                    }
                    setSeries(ss.getAttributeName(quark),values );
                    updateDisplay();
                }
            }catch (AttributeNotFoundException e1) {
                Activator.getDefault().logError(e1.getMessage(), e1);
            }
        }
    }
}