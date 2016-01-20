package org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory;

import org.eclipse.jdt.annotation.NonNull;
import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory.KernelMemoryStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;


/**
 * @author mahdi
 *
 */
public class KernelMemoryAnalysisModule extends TmfStateSystemAnalysisModule {
    public static final @NonNull String ID = "org.eclipse.linuxtools.lttng2.kernel.analysis.memory"; //$NON-NLS-1$

    @Override
    protected @NonNull ITmfStateProvider createStateProvider() {
        // TODO Auto-generated method stub
        return new KernelMemoryStateProvider(checkNotNull(getTrace()));
    }

}
