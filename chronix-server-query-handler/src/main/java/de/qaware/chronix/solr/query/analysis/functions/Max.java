/*
 * Copyright (C) 2016 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.qaware.chronix.solr.query.analysis.functions;

import de.qaware.chronix.timeseries.MetricTimeSeries;

/**
 * The maximum aggregation
 *
 * @author f.lautenschlager
 */
public final class Max implements ChronixAnalysis {

    /**
     * Calculates the maximum value of the first time series.
     *
     * @param args
     * @return the maximum or 0 if the list is empty
     */
    @Override
    public double execute(MetricTimeSeries... args) {

        if (args.length < 1) {
            throw new IllegalArgumentException("Max aggregation needs at least one time series");
        }

        MetricTimeSeries timeSeries = args[0];

        double current = 0;

        if (timeSeries.size() <= 0) {
            return current;
        }

        for (int i = 0; i < timeSeries.size(); i++) {

            double next = timeSeries.get(i);

            if (current < next) {
                current = next;
            }
        }
        return current;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public AnalysisType getType() {
        return AnalysisType.MAX;
    }

    @Override
    public boolean needSubquery() {
        return false;
    }

    @Override
    public String getSubquery() {
        return null;
    }


}
