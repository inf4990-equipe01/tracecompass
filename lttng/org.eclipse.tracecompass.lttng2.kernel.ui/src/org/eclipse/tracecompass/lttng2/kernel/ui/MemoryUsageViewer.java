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

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.internal.lttng2.kernel.ui.Activator;
import org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory.KernelMemoryAnalysisModule;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateSystemDisposedException;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfCommonXLineChartViewer;
import org.swtchart.Chart;

/**
 * Memory usage view
 *
 * @author Samuel Gagnon
 *
 */
public class MemoryUsageViewer extends TmfCommonXLineChartViewer {

    private static final class MemoryFormat extends Format {
        /**
         *
         */
        private static final long serialVersionUID = 3934127385682676804L;
        private static final String KB = "KB"; //$NON-NLS-1$
        private static final String MB = "MB"; //$NON-NLS-1$
        private static final String GB = "GB"; //$NON-NLS-1$
        private static final String TB = "TB"; //$NON-NLS-1$
        private static final long KILO = 1024;
        private static final Format FORMAT = new DecimalFormat("#.###"); //$NON-NLS-1$

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            if (obj instanceof Double) {
                Double value = (Double) obj;
                if (value == 0) {
                    return toAppendTo.append("0"); //$NON-NLS-1$
                }
                if (value > KILO * KILO * KILO * KILO) {
                    return toAppendTo.append(FORMAT.format(value / (KILO * KILO * KILO * KILO))).append(' ').append(TB);
                }
                if (value > KILO * KILO * KILO) {
                    return toAppendTo.append(FORMAT.format(value / (KILO * KILO * KILO))).append(' ').append(GB);
                }
                if (value > KILO * KILO) {
                    return toAppendTo.append(FORMAT.format(value / (KILO * KILO))).append(' ').append(MB);
                }
                return toAppendTo.append(FORMAT.format(value / (KILO))).append(' ').append(KB);
            }
            return toAppendTo;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private TmfStateSystemAnalysisModule fModule = null;

    /**
     * Constructor
     *
     * @param parent
     *            parent view
     */
    public MemoryUsageViewer(Composite parent) {
        super(parent, Messages.MemoryUsageViewer_title, Messages.MemoryUsageViewer_xAxis, Messages.MemoryUsageViewer_yAxis);
    }

    @Override
    protected void initializeDataSource() {
        ITmfTrace trace = getTrace();
        if (trace != null) {

            fModule = TmfTraceUtils.getAnalysisModuleOfClass(trace, TmfStateSystemAnalysisModule.class, KernelMemoryAnalysisModule.ID);
            if (fModule == null) {
                return;
            }
            fModule.schedule();
            Chart chart = getSwtChart();
            chart.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    chart.getAxisSet().getYAxis(0).getTick().setFormat(new MemoryFormat());
                }
            });
        }
    }

    @Override
    protected void updateData(long start, long end, int nb, IProgressMonitor monitor) {
        if (getTrace() == null || fModule == null) {
            return;
        }
        fModule.waitForInitialization();
        ITmfStateSystem ss = fModule.getStateSystem();
        /* Don't wait for the module completion, when it's ready, we'll know */
        if (ss == null) {
            return;
        }

        double[] xvalues = getXAxis(start, end, nb);
        setXAxis(xvalues);

        ss.waitUntilBuilt();

        try {
            List<Integer> tidQuarks = ss.getSubAttributes(-1, false);
            for (int quark : tidQuarks) {

                double yvalue = 0.0;
                double[] values = new double[xvalues.length];
                for (int i = 0; i < xvalues.length; i++) {
                    if (monitor.isCanceled()) {
                        return;
                    }
                    double x = xvalues[i];

                    try {
                        Integer memQuark = ss.getQuarkRelative(quark, "kmem_allocation"); //$NON-NLS-1$
                        yvalue = ss.querySingleState((long) x + this.getTimeOffset(), memQuark.intValue()).getStateValue().unboxLong();
                        values[i] = yvalue;
                    } catch (AttributeNotFoundException | StateSystemDisposedException e) {
                        Activator.getDefault().logError(e.getMessage(), e);
                    }
                }
                setSeries(ss.getAttributeName(quark), values);
                updateDisplay();

            }
        } catch (AttributeNotFoundException e1) {
            Activator.getDefault().logError(e1.getMessage(), e1);
        }

    }

}
