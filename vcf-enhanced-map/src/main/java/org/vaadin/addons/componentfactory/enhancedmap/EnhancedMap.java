/**
 * Copyright 2000-2023 Vaadin Ltd.
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package org.vaadin.addons.componentfactory.enhancedmap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.function.SerializableRunnable;

import elemental.json.JsonValue;
import elemental.json.impl.JreJsonArray;

/**
 * Enhanced Map component that provides extra features like drawing polygons on an extra
 * Maps Layer
 * 
 * @author mlopez
 *
 */
@SuppressWarnings("serial")
@JsModule("./enhanced-map-connector.js")
public class EnhancedMap extends Map {
	
	private String fillColor = "rgba(0,255,0,0.4)";
	private String strokeColor = "green";
	private double strokeWidth = 2;
	private boolean attached = false;

    public EnhancedMap() {
        super();
        init();
    }
    
    public EnhancedMap(String fillColor, String strokeColor, double strokeWidth) {
        super();
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        init();
    }
    
    private void init() {
    	executeWhenAttached(()->getElement().executeJs("enhancedMap.addDrawingLayer($0, $1, $2, $3); enhancedMap.setDrawingInteractions($0);", this, fillColor, strokeColor, strokeWidth));
	}

	/**
     * Load the supplied polygons in the polygon layer
     * @param polygons
     */
    public void loadPolygons(List<Polygon> polygons) {
    	List<String> coordinates = new ArrayList<>();
    	polygons.forEach(polygon->{
    		for (int i = 0; i < polygon.getCoordinates().length; i++) {
				Coordinate coordinate = polygon.getCoordinates()[i];
				String stringCoordinate = "[" + coordinate.getX() + ", " + coordinate.getY() + "]";
				coordinates.add(stringCoordinate);
			}
        	String polygonCoordinates = coordinates.stream().collect(Collectors.joining(","));
        	executeWhenAttached(()->getElement().executeJs("enhancedMap.loadPolygon($0, $1);", this, "[[" + polygonCoordinates + "]]"));
        });
    }
    
    public void clearPolygons() {
    	executeWhenAttached(()->getElement().executeJs("enhancedMap.clearPolygons($0);", this));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attached = true;
    }

    public void setRemoveMode() {
    	executeWhenAttached(()->getElement().executeJs("enhancedMap.setRemovalInteractions($0);", this));
    }

    public void setDrawingMode() {
    	executeWhenAttached(()->getElement().executeJs("enhancedMap.setDrawingInteractions($0);", this));
    }

    @ClientCallable
    public void sendVectorLayer(JsonValue data) {
        JreJsonArray array = (JreJsonArray) data;
        for (int i=0;i<array.length();i++){
            JreJsonArray array2 = (JreJsonArray) array.get(i);
            for (int x=0;x<array2.length();x++) {
                JreJsonArray array3 = (JreJsonArray) array2.get(x);
                for (int z=0;z<array3.length();z++) {
                    System.out.println(array3.get(z).asNumber());
                }
            }
        }
    }
    
    private void executeWhenAttached(SerializableRunnable runnable) {
    	if (attached) {
    		runnable.run();
    	} else {
        	getElement().addAttachListener(ev->runnable.run());
    	}
    }

}
