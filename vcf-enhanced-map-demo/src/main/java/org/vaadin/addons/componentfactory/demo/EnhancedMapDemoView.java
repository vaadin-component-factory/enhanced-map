package org.vaadin.addons.componentfactory.demo;

import org.vaadin.addons.componentfactory.enhancedmap.EnhancedMap;

import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * View for {@link EnhancedMap} demo.
 *
 * @author Vaadin Ltd
 */
@Route("")
public class EnhancedMapDemoView extends VerticalLayout {
    
    private EnhancedMap map;

    public EnhancedMapDemoView() {
        setSizeFull();
        // Set user projection to EPSG:3857
        Map.setUserProjection("EPSG:3857");
        
        // Create a map, center viewport on New York using
        // coordinates in EPSG:3857 projection
        map = new EnhancedMap();
        map.setSizeFull();
        map.setCenter(new Coordinate(-8235375.352932, 4967773.457877));
        map.setZoom(10);
        add(map);
    }
}
