package org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.statesystem.AbstractTmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

/**
 * @author mahdi
 * @since 2.0
 *
 */
public class KernelMemoryStateProvider extends AbstractTmfStateProvider{

    public KernelMemoryStateProvider(@NonNull ITmfTrace trace) {
        super(trace, "Kernel:Memory");
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public @NonNull ITmfStateProvider getNewInstance() {
        // TODO Auto-generated method stub
        return new KernelMemoryStateProvider(getTrace());
    }

    // eventHandle is the call-back that will be called for every event in the trace.
    @Override
    protected void eventHandle(@NonNull ITmfEvent event) {
        // TODO Auto-generated method stub

    }

}
