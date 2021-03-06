/*
 * Copyright (C) 2018 QAware GmbH
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
package de.qaware.chronix.solr.type.metric;

import de.qaware.chronix.server.functions.ChronixFunction;
import de.qaware.chronix.server.types.ChronixTimeSeries;
import de.qaware.chronix.server.types.ChronixType;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Avg;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Count;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Difference;
import de.qaware.chronix.solr.type.metric.functions.aggregations.First;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Integral;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Last;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Max;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Min;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Percentile;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Range;
import de.qaware.chronix.solr.type.metric.functions.aggregations.SignedDifference;
import de.qaware.chronix.solr.type.metric.functions.aggregations.StdDev;
import de.qaware.chronix.solr.type.metric.functions.aggregations.Sum;
import de.qaware.chronix.solr.type.metric.functions.analyses.Frequency;
import de.qaware.chronix.solr.type.metric.functions.analyses.Outlier;
import de.qaware.chronix.solr.type.metric.functions.analyses.Trend;
import de.qaware.chronix.solr.type.metric.functions.transformation.Add;
import de.qaware.chronix.solr.type.metric.functions.transformation.Bottom;
import de.qaware.chronix.solr.type.metric.functions.transformation.Derivative;
import de.qaware.chronix.solr.type.metric.functions.transformation.Distinct;
import de.qaware.chronix.solr.type.metric.functions.transformation.Divide;
import de.qaware.chronix.solr.type.metric.functions.transformation.MovingAverage;
import de.qaware.chronix.solr.type.metric.functions.transformation.NonNegativeDerivative;
import de.qaware.chronix.solr.type.metric.functions.transformation.SampleMovingAverage;
import de.qaware.chronix.solr.type.metric.functions.transformation.Scale;
import de.qaware.chronix.solr.type.metric.functions.transformation.Subtract;
import de.qaware.chronix.solr.type.metric.functions.transformation.Timeshift;
import de.qaware.chronix.solr.type.metric.functions.transformation.Top;
import de.qaware.chronix.solr.type.metric.functions.transformation.Vectorization;
import de.qaware.chronix.timeseries.MetricTimeSeries;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementation of the metric type
 *
 * @author f.lautenschlager
 */
public class MetricType implements ChronixType<MetricTimeSeries> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricType.class);

    @Override
    public String getType() {
        return "metric";
    }

    @Override
    public ChronixTimeSeries<MetricTimeSeries> convert(String joinKey, List<SolrDocument> records, long queryStart, long queryEnd, boolean rawDataIsRequested) {
        MetricTimeSeries metricTimeSeries = SolrDocumentBuilder.reduceDocumentToTimeSeries(queryStart, queryEnd, records, rawDataIsRequested);
        return new ChronixMetricTimeSeries(joinKey, metricTimeSeries);
    }

    @Override
    public ChronixFunction<MetricTimeSeries> getFunction(String function) {

        switch (function) {
            //Aggregations
            case "avg":
                return new Avg();
            case "min":
                return new Min();
            case "max":
                return new Max();
            case "sum":
                return new Sum();
            case "count":
                return new Count();
            case "dev":
                return new StdDev();
            case "last":
                return new Last();
            case "first":
                return new First();
            case "range":
                return new Range();
            case "diff":
                return new Difference();
            case "sdiff":
                return new SignedDifference();
            case "p":
                return new Percentile();
            case "integral":
                return new Integral();
            case "trend":
                return new Trend();
            //Transformations
            case "add":
                return new Add();
            case "sub":
                return new Subtract();
            case "vector":
                return new Vectorization();
            case "bottom":
                return new Bottom();
            case "top":
                return new Top();
            case "movavg":
                return new MovingAverage();
            case "smovavg":
                return new SampleMovingAverage();
            case "scale":
                return new Scale();
            case "divide":
                return new Divide();
            case "derivative":
                return new Derivative();
            case "nnderivative":
                return new NonNegativeDerivative();
            case "timeshift":
                return new Timeshift();
            case "distinct":
                return new Distinct();
            //Analyses
            case "outlier":
                return new Outlier();
            case "frequency":
                return new Frequency();
            //TODO: fix this for future releases
            // case "fastdtw":
            // return new FastDtw(args);
            default:
                LOGGER.warn("{} is not part of the MetricType. Return 'null'. Maybe its a plugin?", function);
                return null;
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        MetricType rhs = (MetricType) obj;
        return new EqualsBuilder()
                .append(this.getType(), rhs.getType())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getType())
                .toHashCode();
    }
}
