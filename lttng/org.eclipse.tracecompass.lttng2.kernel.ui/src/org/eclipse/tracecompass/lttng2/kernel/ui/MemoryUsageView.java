package org.eclipse.tracecompass.lttng2.kernel.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.TmfXYChartViewer;
import org.eclipse.tracecompass.tmf.ui.views.TmfChartView;

public class MemoryUsageView extends TmfChartView {

    public MemoryUsageView() {
        super("Memory Usage");
    }

    @Override
    protected TmfXYChartViewer createChartViewer(Composite parent) {
        return new MemoryUsageViewer(parent);
    }

}
