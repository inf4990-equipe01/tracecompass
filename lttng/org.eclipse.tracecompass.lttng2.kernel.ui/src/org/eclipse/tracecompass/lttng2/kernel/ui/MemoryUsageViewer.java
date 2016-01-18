package org.eclipse.tracecompass.lttng2.kernel.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.event.TmfEvent;
import org.eclipse.tracecompass.tmf.core.request.ITmfEventRequest;
import org.eclipse.tracecompass.tmf.core.request.TmfEventRequest;
import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimeRange;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfCommonXLineChartViewer;

public class MemoryUsageViewer extends TmfCommonXLineChartViewer {

//    private TmfStateSystemAnalysisModule fModule = null;
    private double[] xVal;
    private double[] yVal;

    public MemoryUsageViewer(Composite parent) {
        super(parent, "Kernel Memory Usage", "Time", "Kb");
    }

    @Override
    protected void initializeDataSource() {
        ITmfTrace trace = getTrace();
        if (trace != null) {
//            fModule = TmfTraceUtils.getAnalysisModuleOfClass(trace, TmfStateSystemAnalysisModule.class, UstMemoryAnalysisModule.ID);
//            if (fModule == null) {
//                return;
//            }
//            fModule.schedule();


            //TEST----------------------
            TmfEventRequest req = new TmfEventRequest(TmfEvent.class,
                    TmfTimeRange.ETERNITY, 0, ITmfEventRequest.ALL_DATA,
                    ITmfEventRequest.ExecutionType.BACKGROUND) {

                double mem = 0;
                ArrayList<Double> xValues = new ArrayList<>();
                ArrayList<Double> yValues = new ArrayList<>();

                @Override
                public void handleData(ITmfEvent data) {
                    super.handleData(data);
                    String name = data.getName();
                    if (name.equals("kmem_mm_page_alloc") ) {
                        xValues.add((double) data.getTimestamp().getValue());
                        mem += 4096;
                        yValues.add(mem);
                    } else if (name.equals("kmem_mm_page_free")) {
                        xValues.add((double) data.getTimestamp().getValue());
                        mem -= 4096;
                        yValues.add(mem);
                    }
                }

                @Override
                public void handleSuccess() {
                    super.handleSuccess();

                    xVal = toArray(xValues);
                    yVal = toArray(yValues);
                }

                @Override
                public void handleFailure() {
                    super.handleFailure();
                }

                private double[] toArray(List<Double> list) {
                    double[] d = new double[list.size()];
                    for (int i = 0; i < list.size(); ++i) {
                        d[i] = list.get(i);
                    }

                    return d;
                }
            };
            trace.sendRequest(req);
            //TEST----------------------
        }
    }

    @Override
    protected void updateData(long start, long end, int nb, IProgressMonitor monitor) {
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



        setXAxis(xVal);
        setSeries("System usage", yVal);
        updateDisplay();
    }

}
