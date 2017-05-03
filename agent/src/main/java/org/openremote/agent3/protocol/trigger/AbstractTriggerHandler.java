/*
 * Copyright 2017, OpenRemote Inc.
 *
 * See the CONTRIBUTORS.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openremote.agent3.protocol.trigger;

import elemental.json.JsonValue;
import org.openremote.agent3.protocol.AbstractProtocol;
import org.openremote.container.Container;
import org.openremote.model.AttributeEvent;
import org.openremote.model.AttributeRef;
import org.openremote.model.AttributeState;

public abstract class AbstractTriggerHandler {

    protected static TriggerProtocol protocol;

    protected abstract String getName();

    protected abstract boolean isValidValue(JsonValue triggerValue);

    protected abstract void registerTrigger(AttributeRef triggerRef, JsonValue value, boolean isEnabled);

    protected abstract void updateTrigger(AttributeRef triggerRef, JsonValue value, boolean isEnabled);

    protected abstract void unregisterTrigger(AttributeRef triggerRef);

    /**
     * The {@link AbstractTriggerHandler} implementation should override this method
     * to handle the request based on the propertyName and the {@link AttributeEvent}.
     *
     * The propertyName should relate to a request to read that property from the trigger
     * handler and the trigger handler is responsible for pushing updates to the attribute
     * by calling {@link #updateAttributeValue(AttributeState)}
     *
     */
    protected abstract void registerAttribute(AttributeRef attributeRef, AttributeRef triggerRef, String propertyName);

    protected abstract void updateAttribute(AttributeRef attributeRef, AttributeRef triggerRef, String propertyName);

    protected abstract void unregisterAttribute(AttributeRef attributeRef, AttributeRef triggerRef);

    /**
     * A {@link AbstractProtocol#sendToActuator(AttributeEvent)} has occurred on the protocol.
     *
     * The {@link AbstractTriggerHandler} implementation should override this method
     * to handle the request based on the propertyName and the {@link AttributeEvent}.
     *
     * {@link TriggerProtocol#TRIGGER_PROPERTY_ENABLED} writes are handled here and are
     * applicable for all trigger types (implementations can override but should still
     * call this super method).
     *
     * If the handler wishes to alter the trigger's value as a result of the request
     * (e.g. request was to adjust the time of a TimeTrigger) then the handler
     * should call {@link #updateTriggerValue(AttributeState)} with the new desired value.
     */
    protected abstract void processAttributeWrite(AttributeRef attributeRef, AttributeRef triggerRef, String propertyName, AttributeEvent event);

    /**
     * Called by the protocol init
     */
    protected void init(Container container) throws Exception {}

    /**
     * Called by the protocol start
     */
    protected void start(Container container) throws Exception {}

    /**
     * Called by the protocol stop
     */
    protected void stop(Container container) throws Exception {}

    /**
     * Allows trigger handlers to update a trigger's value
     */
    protected void updateTriggerValue(AttributeState triggerRefAndValue) {
        protocol.updateTriggerValue(triggerRefAndValue);
    }

    /**
     * Allows trigger handlers to update the value of linked attributes
     */
    protected void updateAttributeValue(AttributeState attributeState) {
        protocol.updateAttribute(attributeState);
    }

    /**
     * Allows trigger handlers to request execution of the trigger action
     */
    protected void executeTrigger(AttributeRef triggerRef) {
        protocol.doTriggerAction(triggerRef);
    }
}
