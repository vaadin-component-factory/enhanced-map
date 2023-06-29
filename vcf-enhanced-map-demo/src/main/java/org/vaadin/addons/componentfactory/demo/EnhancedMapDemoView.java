package org.vaadin.addons.componentfactory.demo;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.vaadin.addons.componentfactory.enhancedmap.EnhancedMap;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * View for {@link EnhancedMap} demo.
 *
 * @author Vaadin Ltd
 */
@SuppressWarnings("serial")
@Route("")
public class EnhancedMapDemoView extends VerticalLayout {
    
    private EnhancedMap map;

    public EnhancedMapDemoView() {
        setSizeFull();
        // Set user projection to EPSG:3857
        Map.setUserProjection("EPSG:3857");
        
        // Create a map, center viewport on New York using
        // coordinates in EPSG:3857 projection
        map = new EnhancedMap("rgba(0,255,0,0.4)","purple", 3);
        map.setSizeFull();
        map.setZoom(5);
        map.setCenter(new com.vaadin.flow.component.map.configuration.Coordinate(1238809.9589261413,6044337.659220713));
        
        List<Polygon> polygons = new ArrayList<>();
        GeometryFactory factory = new GeometryFactory();    
        Polygon polygon = factory.createPolygon(new Coordinate[]{
                new Coordinate(1238809.9589261413,6044337.659220713),
                new Coordinate(838311.4548593741,5478624.1969639165),
                new Coordinate(3299147.79980723,5377107.95485271),
                new Coordinate(1238809.9589261413,6044337.659220713),
                });
        polygons.add(polygon);
        map.loadPolygons(polygons);
        
        add(map);
        Button remove = new Button("Remove polygons");
        remove.setDisableOnClick(true);
        Button draw = new Button("Draw polygons");
        draw.setDisableOnClick(true);
        draw.setEnabled(false);
        remove.addClickListener(ev->{
            map.setRemoveMode();
            ev.getSource().setEnabled(false);
            draw.setEnabled(true);
        });
        draw.addClickListener(ev->{
            map.setDrawingMode();
            ev.getSource().setEnabled(false);
            remove.setEnabled(true);
        });
        add(new HorizontalLayout(draw,remove));
    }
}
