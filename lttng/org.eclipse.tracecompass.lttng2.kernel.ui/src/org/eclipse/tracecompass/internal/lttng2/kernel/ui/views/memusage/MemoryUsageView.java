package org.eclipse.tracecompass.internal.lttng2.kernel.ui.views.memusage;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.TmfXYChartViewer;
import org.eclipse.tracecompass.tmf.ui.views.TmfChartView;

/**
 * @author mahdi
 *
 */
public class MemoryUsageView extends TmfChartView {
    /** ID string */
    public static final String ID = "org.eclipse.linuxtools.lttng2.kernel.memoryusage"; //$NON-NLS-1$

    /**
     * Constructor
     */
    public MemoryUsageView() {
        super(Messages.MemoryUsageView_Title);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected TmfXYChartViewer createChartViewer(Composite parent) {
        // TODO Auto-generated method stub
        return new MemoryUsageViewer(parent);
    }



}
