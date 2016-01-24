/**********************************************************************
 * Copyright (c) 2014, 2015 Ericsson, École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Matthew Khouzam - Initial API and implementation
 *   Geneviève Bastien - Memory is per thread and only total is kept
 **********************************************************************/

 package org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

//import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.lttng2.kernel.core.trace.LttngKernelTrace;

import org.eclipse.tracecompass.lttng2.kernel.core.trace.layout.ILttngKernelEventLayout;

import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateValueTypeException;
import org.eclipse.tracecompass.statesystem.core.exceptions.TimeRangeException;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.event.ITmfEventField;
import org.eclipse.tracecompass.tmf.core.statesystem.AbstractTmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;

import com.google.common.collect.ImmutableMap;

/**
 * State provider to track the memory of the threads using the KERNEL libc wrapper
 * memory events.
 *
 * @author Matthew Khouzam
 * @author Geneviève Bastien
 * @author Mahdi Zolnouri
 * @since 2.0
 */
public class KernelMemoryStateProvider extends AbstractTmfStateProvider {

    /* Version of this state provider */
    private static final int VERSION = 1;
    private static final int PAGE_SIZE = 4096;

    private static final Long MINUS_ONE = Long.valueOf(-1);
    private static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private static final int KMEM_MM_PAGE_ALLOC_INDEX = 1;
    private static final int KMEM_MM_PAGE_FREE_INDEX = 2;

    private final @NonNull ILttngKernelEventLayout fLayout;
    private final @NonNull Map<String, Integer> fEventNames;

    /**
     * Constructor
     *
     * @param trace
     *            trace
     */
    public KernelMemoryStateProvider(@NonNull LttngKernelTrace trace) {
        super(trace, "Kernel:Memory"); //$NON-NLS-1$
        fLayout = trace.getEventLayout();
        fEventNames = buildEventNames(fLayout);
    }

    private static @NonNull Map<String, Integer> buildEventNames(ILttngKernelEventLayout layout) {
        ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
        builder.put(layout.eventKmemMmPageAlloc(), KMEM_MM_PAGE_ALLOC_INDEX);
        builder.put(layout.eventKmemMmPageFree(), KMEM_MM_PAGE_FREE_INDEX);
        return checkNotNull(builder.build());
    }

    @Override
    protected void eventHandle(ITmfEvent event) {
        String name = event.getName();
        Integer index = fEventNames.get(name);
        int intIndex = (index == null ? -1 : index.intValue());

        switch (intIndex) {
        case KMEM_MM_PAGE_ALLOC_INDEX: {
            setMemory(event, PAGE_SIZE);
        }
            break;
        case KMEM_MM_PAGE_FREE_INDEX: {
            setMemory(event, -PAGE_SIZE);
        }
            break;
        default:
            /* Ignore other event types */
            return;
        }

    }

    @Override
    public ITmfStateProvider getNewInstance() {
        return new KernelMemoryStateProvider(getTrace());
    }

    @Override
    public LttngKernelTrace getTrace() {
        return (LttngKernelTrace) super.getTrace();
    }

    @Override
    public int getVersion() {
        return VERSION;
    }

    private Long getVtid(ITmfEvent event) {
        ITmfEventField field = event.getContent().getField(fLayout.contextVtid());
        if (field == null) {
            return MINUS_ONE;
        }
        return (Long) field.getValue();
    }

    private String getProcname(ITmfEvent event) {
        ITmfEventField field = event.getContent().getField(fLayout.contextProcname());
        if (field == null) {
            return EMPTY_STRING;
        }
        return (String) field.getValue();
    }
    private void setMemory(ITmfEvent event, int size){
        ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
        long ts = event.getTimestamp().getValue();
        Long tid = getVtid(event);
        Long memoryDiff = new Long(size);

        try {
            int tidQuark = ss.getQuarkAbsoluteAndAdd(tid.toString());
            int tidMemQuark = ss.getQuarkRelativeAndAdd(tidQuark, KernelMemoryStrings.KERNEL_MEMORY_MEMORY_ATTRIBUTE);

            ITmfStateValue prevMem = ss.queryOngoingState(tidMemQuark);
            /* First time we set this value */
            if (prevMem.isNull()) {
                int procNameQuark = ss.getQuarkRelativeAndAdd(tidQuark, KernelMemoryStrings.KERNEL_MEMORY_PROCNAME_ATTRIBUTE);
                String procName = getProcname(event);
                /*
                 * No tid/procname for the event for the event, added to a
                 * 'others' thread
                 */
                if (tid.equals(MINUS_ONE)) {
                    procName = KernelMemoryStrings.OTHERS;
                }
                ss.modifyAttribute(ts, TmfStateValue.newValueString(procName), procNameQuark);
                prevMem = TmfStateValue.newValueLong(0);
            }

            long prevMemValue = prevMem.unboxLong();
            prevMemValue += memoryDiff.longValue();
            ss.modifyAttribute(ts, TmfStateValue.newValueLong(prevMemValue), tidMemQuark);
        } catch (AttributeNotFoundException | TimeRangeException | StateValueTypeException e) {
            throw new IllegalStateException(e);
       }
    }
}
