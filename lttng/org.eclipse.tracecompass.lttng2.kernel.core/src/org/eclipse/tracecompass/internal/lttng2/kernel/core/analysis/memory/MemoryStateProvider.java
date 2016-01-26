package org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.analysis.os.linux.core.kernelanalysis.KernelTidAspect;
import org.eclipse.tracecompass.internal.lttng2.kernel.core.Activator;
import org.eclipse.tracecompass.lttng2.kernel.core.trace.LttngKernelTrace;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.statesystem.AbstractTmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

@SuppressWarnings("javadoc")
public class MemoryStateProvider extends AbstractTmfStateProvider {

    private static final int VERSION = 1;
    private static final int SIZE = 4096;


    public MemoryStateProvider(@NonNull ITmfTrace trace) {
        super(trace, "Kernel:Memory");
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getVersion() {
        return VERSION;
    }

    @Override
    public ITmfStateProvider getNewInstance() {
        return new MemoryStateProvider(getTrace());
    }

    @Override
    public LttngKernelTrace getTrace() {
        return (LttngKernelTrace) super.getTrace();
    }

    @Override
    protected void eventHandle(@NonNull ITmfEvent event) {
        String name = event.getName();
        long index;

        if (name.equals("kmem_mm_page_alloc")){
            index = SIZE;
        } else if (name.equals("kmem_mm_page_free")){
            index = -SIZE;
        } else {
            return;
        }

        ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
        long ts = event.getTimestamp().getValue();

        try{
            Integer tidField = KernelTidAspect.INSTANCE.resolve(event);
            String tid;
            if (tidField == null) {
                tid = "other"; //$NON-NLS-1$
            } else {
                tid = tidField.toString();
            }

            int tidQuark = ss.getQuarkAbsoluteAndAdd(tid);
            int tidMemQuark = ss.getQuarkRelativeAndAdd(tidQuark, "kmem_allocation"); //$NON-NLS-1$
            ITmfStateValue prevMem = ss.queryOngoingState(tidMemQuark);

            if (prevMem.isNull()){
                prevMem = TmfStateValue.newValueLong(0);
            }

            long prevMemValue = prevMem.unboxLong();
            prevMemValue += index;
            ss.modifyAttribute(ts, TmfStateValue.newValueLong(prevMemValue), tidMemQuark);
        } catch (AttributeNotFoundException e){
            Activator.getDefault().logError(e.getMessage(), e);
        }
    }
}
