package org.eclipse.tracecompass.lttng2.kernel.ui;

//import java.util.ArrayList;
//import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory.KernelMemoryAnalysisModule;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateSystemDisposedException;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
//import org.eclipse.tracecompass.tmf.core.event.TmfEvent;
//import org.eclipse.tracecompass.tmf.core.request.ITmfEventRequest;
//import org.eclipse.tracecompass.tmf.core.request.TmfEventRequest;
//import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimeRange;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
//import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
//import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfCommonXLineChartViewer;

public class MemoryUsageViewer extends TmfCommonXLineChartViewer {

    private TmfStateSystemAnalysisModule fModule = null;

    public MemoryUsageViewer(Composite parent) {
        super(parent, "Kernel Memory Usage", "Time", "Kb");
    }

    @Override
    protected void initializeDataSource() {
        ITmfTrace trace = getTrace();
        if (trace != null) {

            fModule = TmfTraceUtils.getAnalysisModuleOfClass(trace, TmfStateSystemAnalysisModule.class, KernelMemoryAnalysisModule.ID);
            if (fModule == null) {
                System.out.println("fModule KernelMem non trouve");
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
        fModule.waitForInitialization();
        ITmfStateSystem ss = fModule.getStateSystem();
        /* Don't wait for the module completion, when it's ready, we'll know */
        if (ss == null) {
            return;
        }

        double[] xvalues = getXAxis(start, end, nb);
        setXAxis(xvalues);

        ss.waitUntilBuilt();

        double yvalue = 0.0;
        double[] values = new double[xvalues.length];
        for (int i = 0; i < xvalues.length; i++) {
            if (monitor.isCanceled()) {
                return;
            }
            double x = xvalues[i];

            Integer testQuark;
            try {
                testQuark = ss.getQuarkAbsolute("TestKernelMemory");
                yvalue = ss.querySingleState((long) x + this.getTimeOffset(), testQuark.intValue()).getStateValue().unboxLong();
                values[i] = yvalue;
            } catch (AttributeNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (StateSystemDisposedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        setSeries("Kernel mem usage", values);
        updateDisplay();
    }

}
