package org.eclipse.tracecompass.lttng2.kernel.core.analysis.memory;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;
import static org.eclipse.tracecompass.common.core.NonNullUtils.nullToEmptyString;
import java.util.Set;
import org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.memory.KernelMemoryStateProvider;
import org.eclipse.tracecompass.lttng2.control.core.session.SessionConfigStrings;
import org.eclipse.tracecompass.lttng2.kernel.core.trace.LttngKernelTrace;
import org.eclipse.tracecompass.lttng2.kernel.core.trace.layout.ILttngKernelEventLayout;
import org.eclipse.tracecompass.tmf.core.analysis.TmfAnalysisRequirement;
import org.eclipse.tracecompass.tmf.core.analysis.TmfAnalysisRequirement.ValuePriorityLevel;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfAnalysisException;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

import com.google.common.collect.ImmutableSet;

/**
 * This analysis build a state system from the libc memory instrumentation on a
 * KERNEL trace
 *
 * @author Geneviève Bastien
 * @author Mahdi Zolnouri
 */
public class KernelMemoryAnalysisModule extends TmfStateSystemAnalysisModule {
    public static final @NonNull String ID = "org.eclipse.linuxtools.lttng2.kernel.analysis.memory"; //$NON-NLS-1$

    /** The analysis's requirements. Only set after the trace is set. */
    private @Nullable Set<TmfAnalysisRequirement> fAnalysisRequirements;

    @Override
    protected @NonNull ITmfStateProvider createStateProvider() {
        // TODO Auto-generated method stub
        return new KernelMemoryStateProvider(checkNotNull(getTrace()));
    }

    @Override
    public boolean setTrace(ITmfTrace trace) throws TmfAnalysisException {
        if (!(trace instanceof LttngKernelTrace)) {
            return false;
        }
        fAnalysisRequirements = requirementsForTrace((LttngKernelTrace) trace);
        boolean traceIsSet = super.setTrace(trace);
        if (!traceIsSet) {
            fAnalysisRequirements = null;
        }
        return traceIsSet;
    }

    @Override
    protected LttngKernelTrace getTrace() {
        return (LttngKernelTrace) super.getTrace();
    }

    private static Set<TmfAnalysisRequirement> requirementsForTrace(LttngKernelTrace trace) {
        ILttngKernelEventLayout layout = trace.getEventLayout();
        Set<String> requiredEvents = ImmutableSet.of(
                layout.eventKmemMmPageAlloc(),
                layout.eventKmemMmPageFree());
        /* Initialize the requirements for the analysis: domain and events */
        TmfAnalysisRequirement eventsReq = new TmfAnalysisRequirement(SessionConfigStrings.CONFIG_ELEMENT_EVENT, requiredEvents, ValuePriorityLevel.MANDATORY);
        /*
         * In order to have these events, the libc wrapper with probes should be
         * loaded
         */
        eventsReq.addInformation(nullToEmptyString(Messages.KernelMemoryAnalysisModule_EventsLoadingInformation));
        eventsReq.addInformation(nullToEmptyString(Messages.KernelMemoryAnalysisModule_EventsLoadingExampleInformation));

        /* The domain type of the analysis */
        TmfAnalysisRequirement domainReq = new TmfAnalysisRequirement(SessionConfigStrings.CONFIG_ELEMENT_DOMAIN);
        domainReq.addValue(SessionConfigStrings.CONFIG_DOMAIN_TYPE_KERNEL, ValuePriorityLevel.MANDATORY);

        return checkNotNull(ImmutableSet.of(domainReq, eventsReq));
    }

    @Override
    public Iterable<TmfAnalysisRequirement> getAnalysisRequirements() {
        Set<TmfAnalysisRequirement> reqs = fAnalysisRequirements;
        if (reqs == null) {
            throw new IllegalStateException("Cannot get the analysis requirements without an assigned trace."); //$NON-NLS-1$
        }
        return reqs;
    }

}
