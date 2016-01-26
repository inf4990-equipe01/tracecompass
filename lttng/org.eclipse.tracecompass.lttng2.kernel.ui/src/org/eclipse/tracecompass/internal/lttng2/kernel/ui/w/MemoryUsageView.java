package org.eclipse.tracecompass.internal.lttng2.kernel.ui.w;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.TmfXYChartViewer;
import org.eclipse.tracecompass.tmf.ui.views.TmfChartView;



/**
 * @since 2.0
 */
@SuppressWarnings("javadoc")
public class MemoryUsageView extends TmfChartView {

    public MemoryUsageView() {
        super(Messages.MemoryUsageView_Title);
    }

    @Override
    protected TmfXYChartViewer createChartViewer(Composite parent) {
        return new MemoryUsageViewer(parent);
    }
}
