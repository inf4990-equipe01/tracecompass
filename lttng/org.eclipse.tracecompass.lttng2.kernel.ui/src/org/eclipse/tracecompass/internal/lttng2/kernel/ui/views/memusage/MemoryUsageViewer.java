package org.eclipse.tracecompass.internal.lttng2.kernel.ui.views.memusage;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfCommonXLineChartViewer;

public class MemoryUsageViewer extends TmfCommonXLineChartViewer {

    public MemoryUsageViewer(Composite parent) {
        super(parent, Messages.MemoryUsageViewer_Title, Messages.MemoryUsageViewer_XAxis, Messages.MemoryUsageViewer_YAxis);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void updateData(long start, long end, int nb, IProgressMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
