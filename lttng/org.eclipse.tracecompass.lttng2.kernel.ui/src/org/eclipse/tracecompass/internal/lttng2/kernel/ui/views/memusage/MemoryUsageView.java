/**********************************************************************
 * Copyright (c) 2014, 2015, 2016 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Matthew Khouzam - Initial API and implementation
 *   Mahdi Zolnouri - Change for it to be adapted with Kernel.
 **********************************************************************/
package org.eclipse.tracecompass.internal.lttng2.kernel.ui.views.memusage;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.TmfXYChartViewer;
import org.eclipse.tracecompass.tmf.ui.views.TmfChartView;

public class MemoryUsageView extends TmfChartView {
    /** ID string */
    public static final String ID = "org.eclipse.linuxtools.lttng2.kernel.memoryusage"; //$NON-NLS-1$

    /**
     * Constructor
     */
    public MemoryUsageView() {
        super(Messages.MemoryUsageView_Title);
    }

    @Override
    protected TmfXYChartViewer createChartViewer(Composite parent) {
        return new MemoryUsageViewer(parent);
    }
}
