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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.tracecompass.tmf.core.signal.TmfSignalManager;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceManager;
import org.eclipse.tracecompass.tmf.ui.signal.TmfTimeViewAlignmentSignal;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.TmfXYChartViewer;
import org.eclipse.tracecompass.tmf.ui.views.TmfChartView;

/**
 * Memory usage view
 *
 * @author Samuel Gagnon
 */
public class MemoryUsageView extends TmfChartView {

    private SashForm fSashForm;
    private Composite fXYViewerContainer;
    private TmfXYChartViewer fChartViewer;
    private Listener fSashDragListener;
    private static final int[] DEFAULT_WEIGHTS = {1, 3};


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

    @Override
    public void createPartControl(Composite parent){
        super.createPartControl(parent);

        fSashForm = new SashForm(parent, SWT.NONE);
        new Composite(fSashForm, SWT.NONE);
        fXYViewerContainer = new Composite(fSashForm, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        fXYViewerContainer.setLayout(layout);

        fChartViewer = createChartViewer(fXYViewerContainer);
        fChartViewer.setSendTimeAlignSignals(true);
        fChartViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        fChartViewer.getControl().addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                // Sashes in a SashForm are being created on layout so add the
                // drag listener here
                if (fSashDragListener == null) {
                    for (Control control : fSashForm.getChildren()) {
                        if (control instanceof Sash) {
                            fSashDragListener = new Listener() {

                                @Override
                                public void handleEvent(Event event) {
                                    TmfSignalManager.dispatchSignal(new TmfTimeViewAlignmentSignal(fSashForm, getTimeViewAlignmentInfo()));
                                }
                            };
                            control.removePaintListener(this);
                            control.addListener(SWT.Selection, fSashDragListener);
                            // There should be only one sash
                            break;
                        }
                    }
                }
            }
        });
        fSashForm.setWeights(DEFAULT_WEIGHTS);
        ITmfTrace trace = TmfTraceManager.getInstance().getActiveTrace();
        if (trace != null) {
            setTrace(trace);
            loadTrace();
        }
    }
}
