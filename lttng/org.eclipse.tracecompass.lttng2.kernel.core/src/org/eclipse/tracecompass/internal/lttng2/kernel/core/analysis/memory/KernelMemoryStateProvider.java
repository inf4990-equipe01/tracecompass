package org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.analysis.os.linux.core.kernelanalysis.KernelTidAspect;
import org.eclipse.tracecompass.lttng2.kernel.core.trace.LttngKernelTrace;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.statesystem.AbstractTmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;

/**
 * @author sam
 * @since 2.0 <<-- Pourquoi il faut que j'ajoute ça ? (Samuel)
 *
 */
public class KernelMemoryStateProvider extends AbstractTmfStateProvider {

    /* Version of this state provider */
    private static final int VERSION = 1;

    private static final int PAGE_SIZE = 4096;

    /**
     * Constructor
     *
     * @param trace
     *            trace
     */
    public KernelMemoryStateProvider(@NonNull LttngKernelTrace trace) {
        super(trace, "Kernel:Memory"); //$NON-NLS-1$
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
        if (name.equals("kmem_mm_page_alloc")) { //$NON-NLS-1$
            inc = PAGE_SIZE;
        } else if (name.equals("kmem_mm_page_free")) { //$NON-NLS-1$
            inc = -PAGE_SIZE;
        } else {
            return;
        }

        try {
            ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
            long ts = event.getTimestamp().getValue();

            // !!! Ce n'est pas tout les event page_alloc/page_free qui ont un thread ID... Pourquoi??
            Integer tidField = KernelTidAspect.INSTANCE.resolve(event);
            String tid;
            if (tidField == null) {
                tid = "other";
            } else {
                tid = tidField.toString();
            }

            int tidQuark = ss.getQuarkAbsoluteAndAdd(tid);
            int tidMemQuark = ss.getQuarkRelativeAndAdd(tidQuark, "kmem_allocation");

            ITmfStateValue prevMem = ss.queryOngoingState(tidMemQuark);
            if (prevMem.isNull()) {
                prevMem = TmfStateValue.newValueLong(0);
            }

            long prevMemValue = prevMem.unboxLong();
            prevMemValue += inc;
            ss.modifyAttribute(ts, TmfStateValue.newValueLong(prevMemValue), tidMemQuark);

        } catch (AttributeNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
