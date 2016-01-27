/**********************************************************************
 * Copyright (c) 2016 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Samuel Gagnon - Initial implementation
 **********************************************************************/
package org.eclipse.tracecompass.lttng2.kernel.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

//import org.eclipse.swt.custom.SashForm;
//import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.ui.signal.TmfTimeViewAlignmentInfo;
//import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.TmfXYChartViewer;
import org.eclipse.tracecompass.tmf.ui.views.ITmfTimeAligned;
//import org.eclipse.tracecompass.tmf.ui.views.TmfChartView;
import org.eclipse.tracecompass.tmf.ui.views.TmfView;

/**
 * Memory usage view
 *
 * @author Samuel Gagnon
 */
public class MemoryUsageView extends TmfView implements ITmfTimeAligned {
    /** ID string */
    public static final String ID = "org.eclipse.tracecompass.lttng2.kernel.views.kernelmemoryusage"; //$NON-NLS-1$

    private KernelMemoryUsageComposite fTreeViewer = null;
    private SashForm fSashForm;
    private Listener fSashDragListener;
    private Composite fXYViewerContainer;

    /**
     * Constructor used by plugin.xml
     */
    public MemoryUsageView() {
        super(Messages.MemoryUsageView_title);
    }




    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        fSashForm = new SashForm(parent, SWT.NONE);
        fTreeViewer = new KernelMemoryUsageComposite(fSashForm);


    }

    @Override
    public TmfTimeViewAlignmentInfo getTimeViewAlignmentInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getAvailableWidth(int requestedOffset) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void performAlign(int offset, int width) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }

//    @Override
//    protected TmfXYChartViewer createChartViewer(Composite parent) {
//
//        return new MemoryUsageViewer(parent);
//    }

}
