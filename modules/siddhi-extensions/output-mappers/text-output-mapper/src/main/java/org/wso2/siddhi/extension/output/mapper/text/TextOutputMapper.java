/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.extension.output.mapper.text;

import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.output.sink.OutputMapper;
import org.wso2.siddhi.core.util.transport.TemplateBuilder;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;

import java.util.Map;

@Extension(
        name = "text",
        namespace = "outputmapper",
        description = "Event to Text output mapper."
)
public class TextOutputMapper extends OutputMapper {
    private StreamDefinition streamDefinition;
    private static final String EVENT_ATTRIBUTE_SEPARATOR = ",";
    private static final String EVENT_ATTRIBUTE_VALUE_SEPARATOR = ":";

    /**
     * Initialize the mapper and the mapping configurations
     *  @param streamDefinition The stream definition
     * @param optionHolder     Unmapped dynamic options
     * @param payloadTemplateBuilder
     */
    @Override
    public void init(StreamDefinition streamDefinition, OptionHolder optionHolder, TemplateBuilder payloadTemplateBuilder) {
        this.streamDefinition = streamDefinition;
    }

    /**
     * Convert the given Event mapping to TEXT string
     *
     * @param event         Event object
     * @param mappedPayload mapped payload if any
     * @return the mapped TEXT string
     */
    @Override
    public Object mapEvent(Event event, String mappedPayload) {
        if (mappedPayload != null) {
            return mappedPayload;
        } else {
            return constructDefaultMapping(event);
        }
    }

    /**
     * Convert the given {@link Event} to Text string
     *
     * @param event Event object
     * @return the constructed TEXT string
     */
    private Object constructDefaultMapping(Event event) {
        StringBuilder eventText = new StringBuilder();
        Object[] data = event.getData();
        for (int i = 0; i < data.length; i++) {
            String attributeName = streamDefinition.getAttributeNameArray()[i];
            Object attributeValue = data[i];
            eventText.append(attributeName).append(EVENT_ATTRIBUTE_VALUE_SEPARATOR).append(attributeValue.toString()).append(EVENT_ATTRIBUTE_SEPARATOR).append("\n");
        }
        eventText.deleteCharAt(eventText.lastIndexOf(EVENT_ATTRIBUTE_SEPARATOR));
        eventText.deleteCharAt(eventText.lastIndexOf("\n"));

        // Get arbitrary data from event
        Map<String, Object> arbitraryDataMap = event.getArbitraryDataMap();
        if (arbitraryDataMap != null && !arbitraryDataMap.isEmpty()) {
            // Add arbitrary data key-value to the default template
            eventText.append(EVENT_ATTRIBUTE_SEPARATOR);
            for (Map.Entry<String, Object> entry : arbitraryDataMap.entrySet()) {
                eventText.append("\n" + entry.getKey() + EVENT_ATTRIBUTE_SEPARATOR + entry.getValue() + EVENT_ATTRIBUTE_SEPARATOR);
            }
            eventText.deleteCharAt(eventText.lastIndexOf(EVENT_ATTRIBUTE_SEPARATOR));
            eventText.deleteCharAt(eventText.lastIndexOf("\n"));
        }

        return eventText.toString();
    }

}