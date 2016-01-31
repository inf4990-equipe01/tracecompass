/**********************************************************************
 * Copyright (c) 2016 Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Samuel Gagnon - Initial implementation
 **********************************************************************/
package org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.analysis.os.linux.core.kernelanalysis.KernelTidAspect;
import org.eclipse.tracecompass.analysis.os.linux.core.trace.IKernelAnalysisEventLayout;
import org.eclipse.tracecompass.internal.lttng2.kernel.core.Activator;
import org.eclipse.tracecompass.lttng2.kernel.core.trace.LttngKernelTrace;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.statesystem.AbstractTmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;

/**
 * @author Samuel Gagnon
 * @since 2.0
 *
 */
public class KernelMemoryStateProvider extends AbstractTmfStateProvider {

    /* Version of this state provider */
    private static final int VERSION = 1;

    private static final int PAGE_SIZE = 4096;

    private @NonNull IKernelAnalysisEventLayout fLayout;

    /**
     * Constructor
     *
     * @param trace
     *            trace
     */
    public KernelMemoryStateProvider(@NonNull LttngKernelTrace trace) {
        super(trace, "Kernel:Memory"); //$NON-NLS-1$
        fLayout = trace.getKernelEventLayout();
    }

    @Override
    public int getVersion() {
        return VERSION;
    }

    @Override
    public @NonNull ITmfStateProvider getNewInstance() {
        return new KernelMemoryStateProvider(getTrace());
    }

    @Override
    public LttngKernelTrace getTrace() {
        return (LttngKernelTrace) super.getTrace();
    }

    @Override
    protected void eventHandle(@NonNull ITmfEvent event) {
        String name = event.getName();

        long inc;
        if (name.equals(fLayout.eventKmemPageAlloc())) {
            inc = PAGE_SIZE;
        } else if (name.equals(fLayout.eventKmemPageFree())) {
            inc = -PAGE_SIZE;
        } else {
            return;
        }

        try {
            ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
            long ts = event.getTimestamp().getValue();

            Integer tidField = KernelTidAspect.INSTANCE.resolve(event);
            String tid;
            if (tidField == null) {
                // this can be due to a race if the state system is not yet
                // built, or that the information is not yet available.
                tid = "other"; //$NON-NLS-1$
            } else {
                tid = tidField.toString();
            }

            int tidQuark = ss.getQuarkAbsoluteAndAdd(tid);

            ITmfStateValue prevMem = ss.queryOngoingState(tidQuark);
            if (prevMem.isNull()) {
                prevMem = TmfStateValue.newValueLong(0);
            }

            long prevMemValue = prevMem.unboxLong();
            prevMemValue += inc;
            ss.modifyAttribute(ts, TmfStateValue.newValueLong(prevMemValue), tidQuark);

        } catch (AttributeNotFoundException e) {
            Activator.getDefault().logError(e.getMessage(), e);
        }
    }

}
