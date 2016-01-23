package org.eclipse.tracecompass.lttng2.kernel.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.TmfXYChartViewer;
import org.eclipse.tracecompass.tmf.ui.views.TmfChartView;

/**
 * Memory usage view
 *
 * @author Samuel Gagnon
 */
public class MemoryUsageView extends TmfChartView {

    /**
     * Constructor used by plugin.xml
     */
    public MemoryUsageView() {
        super(Messages.MemoryUsageView_title);
    }

    @Override
    protected TmfXYChartViewer createChartViewer(Composite parent) {
        return new MemoryUsageViewer(parent);
    }

}
