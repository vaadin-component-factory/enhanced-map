/**
 * Copyright 2000-2023 Vaadin Ltd.
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package org.vaadin.addons.componentfactory.enhancedmap;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.map.Map;

import elemental.json.JsonValue;
import elemental.json.impl.JreJsonArray;

@JsModule("./enhanced-map-connector.js")
public class EnhancedMap extends Map {

    public EnhancedMap() {
        super();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs("enhancedMap.addDrawingCapabilities($0)", this);
    }

    @ClientCallable
    public void sendVectorLayer(JsonValue data) {
        JreJsonArray array = (JreJsonArray) data;
        for (int i = 0; i < array.length(); i++) {
            JreJsonArray array2 = (JreJsonArray) array.get(i);
            for (int x = 0; x < array2.length(); x++) {
                JreJsonArray array3 = (JreJsonArray) array2.get(x);
                for (int z = 0; z < array3.length(); z++) {
                    System.out.println(array3.get(z).asNumber());
                }
            }
        }
    }
}
