/**********************************************************************
 * Copyright (c) 2016 Ericsson, École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Samuel Gagnon - Initial API and implementation
 *   (SAMUEL : Do I put my name here like that?)
 **********************************************************************/
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
