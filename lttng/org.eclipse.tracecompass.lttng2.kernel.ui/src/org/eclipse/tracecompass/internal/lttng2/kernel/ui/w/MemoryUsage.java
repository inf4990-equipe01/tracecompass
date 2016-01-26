/*package org.eclipse.tracecompass.internal.lttng2.kernel.ui.w;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateSystemDisposedException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateValueTypeException;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfCommonXLineChartViewer;

*//**
 * @author Wassim
 *
 *//*
public class MemoryUsage extends TmfCommonXLineChartViewer{

    private TmfStateSystemAnalysisModule fModule = null;




    private static final int BYTES_TO_KB = 1024;
    private static final long BUILD_UPDATE_TIMEOUT = 500;


    *//**
     * @param parent
     *//*
    public MemoryUsage(Composite parent) {
        super(parent, "Memory Usage", "Time", "KB");
        // TODO Auto-generated constructor stub
    }


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
         Don't wait for the module completion, when it's ready, we'll know 
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


            double yvalue = 0.0;
            double[] values = new double[xvalues.length];
            for (int i = 0; i < xvalues.length; i++) {
                if (monitor.isCanceled()) {
                    return;
                }
                double x = xvalues[i];

                Integer quark;
                try{
                    quark = ss.getQuarkAbsolute("KernelMemory");
                    yvalue = ss.querySingleState((long) x, quark.intValue()).getStateValue().unboxLong() / BYTES_TO_KB;
                    values[i] = yvalue;
                } catch (AttributeNotFoundException | StateValueTypeException e){
                    e.printStackTrace();
                } catch (StateSystemDisposedException e){
                    e.printStackTrace();
                }

                setSeries("KernelMem",values );
                updateDisplay();
            }
        }
    }
}
*/