package org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory.KernelMemoryStateProvider;
import org.eclipse.tracecompass.lttng2.kernel.core.trace.LttngKernelTrace;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfAnalysisException;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

/**
 * @author sam
 * @since 2.0 <<-- Pourquoi il faut que j'ajoute ça ? (Samuel)
 *
 */
public class KernelMemoryAnalysisModule extends TmfStateSystemAnalysisModule {

    /**
     * Analysis ID, it should match that in the plugin.xml file
     */
    public static final @NonNull String ID = "org.eclipse.linuxtools.lttng2.kernel.analysis.memory";

    @Override
    protected @NonNull ITmfStateProvider createStateProvider() {
        System.out.println("CreateStateProvider");
        return new KernelMemoryStateProvider(checkNotNull(getTrace()));
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

        boolean traceIsSet = super.setTrace(trace);

        return traceIsSet;
    }
}
