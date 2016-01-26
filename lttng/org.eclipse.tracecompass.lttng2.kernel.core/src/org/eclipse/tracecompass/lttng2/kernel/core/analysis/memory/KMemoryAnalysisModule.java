package org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory;


import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory.MemoryStateProvider;
import org.eclipse.tracecompass.lttng2.kernel.core.trace.LttngKernelTrace;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfAnalysisException;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

/**
 * @since 2.0
 */
@SuppressWarnings("javadoc")
public class KMemoryAnalysisModule extends TmfStateSystemAnalysisModule{


    public static final @NonNull String ID = "org.eclipse.linuxtools.lttng2.kernel.analysis.memory"; //$NON-NLS-1$


    @Override
    protected ITmfStateProvider createStateProvider() {
        return new MemoryStateProvider(checkNotNull(getTrace()));
    }

    @Override
    protected LttngKernelTrace getTrace() {
        return (LttngKernelTrace) super.getTrace();
    }

    @Override
    public boolean setTrace(ITmfTrace trace) throws TmfAnalysisException {
        if (!(trace instanceof LttngKernelTrace)) {
            return false;
        }
        return super.setTrace(trace);
    }
}
