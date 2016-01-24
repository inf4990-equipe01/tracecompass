package org.eclipse.tracecompass.lttng2.kernel.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.event.TmfEvent;
import org.eclipse.tracecompass.tmf.core.request.ITmfEventRequest;
import org.eclipse.tracecompass.tmf.core.request.TmfEventRequest;
import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimeRange;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfCommonXLineChartViewer;

public class KernelMemoryUsageViewer extends TmfCommonXLineChartViewer {

    private double[] xVal ;
    private double[] yVal ;

    public KernelMemoryUsageViewer(Composite parent) {
        super(parent, "Kernel Memory Usage", "Time", "kb");
    }


    @Override
    protected void initializeDataSource() {
        ITmfTrace trace = getTrace();

        if(trace != null){

            TmfEventRequest req = new TmfEventRequest(TmfEvent.class, TmfTimeRange.ETERNITY, 0, ITmfEventRequest.ALL_DATA, ITmfEventRequest.ExecutionType.BACKGROUND) {

                double mem = 0;
                ArrayList<Double> xValues = new ArrayList<>();
                ArrayList<Double> yValues = new ArrayList<>();

                @Override
                public void handleData(ITmfEvent data) {
                    super.handleData(data);
                    String name = data.getName();
                    if(name.equals("kmem_mm_page_alloc")){
                        xValues.add((double) data.getTimestamp().getValue());
                        mem+=4096;
                        yValues.add(mem);
                    }else if(name.equals("kmem_mm_page_free")){
                        xValues.add((double) data.getTimestamp().getValue());
                        mem-=4096;
                        yValues.add(mem);
                    }
                }

                @Override
                public void handleSuccess() {
                    // Request failed, not more data available
                    super.handleSuccess();

                    xVal = toArray(xValues);
                    yVal = toArray(yValues);

                    Display.getDefault().asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            setXAxis(xVal);
                            setSeries("System usage", yVal);
                            updateDisplay();
                        }
                    });
                }

                @Override
                public void handleFailure() {
                    // Request failed, not more data available
                    super.handleFailure();
                }

                /**
                 * Convert List<Double> to double[]
                 */
                private double[] toArray(List<Double> list) {
                    double[] d = new double[list.size()];
                    for (int i = 0; i < list.size(); ++i) {
                        d[i] = list.get(i);
                    }

                    return d;
                }
            };
            trace.sendRequest(req);
        }
    }


    @Override
    protected void updateData(long start, long end, int nb, IProgressMonitor monitor) {
    }
}
