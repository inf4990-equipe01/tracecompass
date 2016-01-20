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

    private static final int BYTES_TO_KB = 1024;

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
            //ss = TmfStateSystemAnalysisModule.getStateSystem(trace, KernelMemoryAnalysisModule.ID);
            fModule.schedule();

//            //TEST----------------------
//            TmfEventRequest req = new TmfEventRequest(TmfEvent.class,
//                    TmfTimeRange.ETERNITY, 0, ITmfEventRequest.ALL_DATA,
//                    ITmfEventRequest.ExecutionType.BACKGROUND) {
//
//                double mem = 0;
//                ArrayList<Double> xValues = new ArrayList<>();
//                ArrayList<Double> yValues = new ArrayList<>();
//
//                @Override
//                public void handleData(ITmfEvent data) {
//                    super.handleData(data);
//                    String name = data.getName();
//                    if (name.equals("kmem_mm_page_alloc") ) {
//                        xValues.add((double) data.getTimestamp().getValue());
//                        mem += 4096;
//                        yValues.add(mem);
//                    } else if (name.equals("kmem_mm_page_free")) {
//                        xValues.add((double) data.getTimestamp().getValue());
//                        mem -= 4096;
//                        yValues.add(mem);
//                    }
//                }
//
//                @Override
//                public void handleSuccess() {
//                    super.handleSuccess();
//
//
//                    xVal = toArray(xValues);
//                    yVal = toArray(yValues);
//
//                    // J'ai aucune idée pourquoi je fais ça...
//                    Display.getDefault().asyncExec(new Runnable() {
//                        @Override
//                        public void run() {
//                            setXAxis(xVal);
//                            setSeries("System usage", yVal);
//                            updateDisplay();
//                        }
//                    });
//                }
//
//                @Override
//                public void handleFailure() {
//                    super.handleFailure();
//                }
//
//                private double[] toArray(List<Double> list) {
//                    double[] d = new double[list.size()];
//                    for (int i = 0; i < list.size(); ++i) {
//                        d[i] = list.get(i);
//                    }
//
//                    return d;
//                }
//            };
//            trace.sendRequest(req);
//            //TEST----------------------
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
                yvalue = ss.querySingleState((long) x, testQuark.intValue()).getStateValue().unboxLong() / BYTES_TO_KB;
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


//        System.out.println("updateData");
//
//        double[] xvalues = getXAxis(start, end, nb);
//        setXAxis(xvalues);
//
//        double [] testY = new double[xvalues.length];
//        System.out.println(testY.length);
//        for(int i=0; i<testY.length; i++) {
//            testY[i] = i;
//        }
//
//        setSeries("Kernel mem usage", testY);
//        updateDisplay();

    }

}
