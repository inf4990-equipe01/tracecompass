package org.eclipse.tracecompass.internal.lttng2.kernel.ui.views.memusage;

//import java.util.ArrayList;
//import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory.KernelMemoryAnalysisModule;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
//import org.eclipse.tracecompass.tmf.core.event.TmfEvent;
//import org.eclipse.tracecompass.tmf.core.request.ITmfEventRequest;
//import org.eclipse.tracecompass.tmf.core.request.TmfEventRequest;
//import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimeRange;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfCommonXLineChartViewer;

// Q1. L'utilité d'avoir un viewer
public class MemoryUsageViewer extends TmfCommonXLineChartViewer {

    // Declare fModule : Abstract analysis module to generate a state system
    private TmfStateSystemAnalysisModule fModule = null;

    public MemoryUsageViewer(Composite parent) {
        super(parent, Messages.MemoryUsageViewer_Title, Messages.MemoryUsageViewer_XAxis, Messages.MemoryUsageViewer_YAxis);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void updateData(long start, long end, int nb, IProgressMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initializeDataSource() {
        ITmfTrace trace = getTrace();
        if (trace != null) { // if we have trace
            fModule = TmfTraceUtils.getAnalysisModuleOfClass(trace, TmfStateSystemAnalysisModule.class, KernelMemoryAnalysisModule.ID);
            if (fModule == null) {
                return;
            }
            fModule.schedule();
        }
    }

}
