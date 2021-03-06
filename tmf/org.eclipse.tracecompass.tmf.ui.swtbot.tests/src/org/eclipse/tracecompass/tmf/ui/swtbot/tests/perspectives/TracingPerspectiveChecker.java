/*******************************************************************************
 * Copyright (c) 2014, 2015 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Matthew Khouzam - Initial API and implementation
 *******************************************************************************/

package org.eclipse.tracecompass.tmf.ui.swtbot.tests.perspectives;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.tracecompass.tmf.ui.swtbot.tests.shared.AbstractPerspectiveChecker;
import org.eclipse.tracecompass.tmf.ui.views.TracingPerspectiveFactory;
import org.eclipse.tracecompass.tmf.ui.views.histogram.HistogramView;
import org.eclipse.tracecompass.tmf.ui.views.statistics.TmfStatisticsView;
import org.eclipse.ui.IPageLayout;
import org.junit.Before;

/**
 * Tracing perspective view checker
 *
 * @author Matthew Khouzam
 */
public class TracingPerspectiveChecker extends AbstractPerspectiveChecker {

    /**
     * Set up arrays for test
     */
    @Before
    public void init() {
        fPerspectiveId = TracingPerspectiveFactory.ID;
        fViewIds = new ArrayList<>();
        fViewIds.addAll(Arrays.asList(new String[] {
                HistogramView.ID,
                TmfStatisticsView.ID,
                // Standard Eclipse views
                IPageLayout.ID_PROJECT_EXPLORER,
                IPageLayout.ID_PROP_SHEET,
                IPageLayout.ID_BOOKMARKS
        }));
    }

}
