package org.vaadin.addons.componentfactory.demo;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.vaadin.addons.componentfactory.enhancedmap.DrawingLayerOptions;
import org.vaadin.addons.componentfactory.enhancedmap.EnhancedMap;

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
        
        // Define the needed options for the drawing layer (optional)
        DrawingLayerOptions options = new DrawingLayerOptions("rgba(0,255,0,0.4)","purple", 3);
                
        // Create a map, center viewport on New York using
        // coordinates in EPSG:3857 projection
        map = new EnhancedMap();
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
        
        Button remove = new Button("Remove polygon");
        remove.setDisableOnClick(true);
        Button draw = new Button("Draw polygon");
        draw.setDisableOnClick(true);
        draw.setEnabled(false);
        Button removeAll = new Button("Remove all polygons");
        removeAll.setDisableOnClick(true);
        
        remove.addClickListener(ev->{
            map.setRemoveMode();
            ev.getSource().setEnabled(false);
            draw.setEnabled(true);
        });
        draw.addClickListener(ev->{
            map.setDrawingMode();
            ev.getSource().setEnabled(false);
            remove.setEnabled(true);
            removeAll.setEnabled(true);
        });        
        removeAll.addClickListener(ev-> {
        	map.setRemoveMode();
            map.clearPolygons();
            ev.getSource().setEnabled(false);
            draw.setEnabled(true);
            remove.setEnabled(false);
        });
        
        add(new HorizontalLayout(draw, remove, removeAll)); 
        
        map.addNewPolygonListener(ev -> {
        	Polygon newPolygon = ev.getPolygon();
        	Coordinate[] coordinates = newPolygon.getCoordinates();
        	Div message = new Div();
        	message.add(new Text("New polygon created with coordinates:"));
        	for (Coordinate c : coordinates) {
        		message.add(new HtmlComponent("br"));
	        	message.add(c.toString());	
			}
        	
        	Notification notification = new Notification();
        	
        	Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        	closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_CONTRAST);
        	closeButton.addClickListener(event -> {
        	    notification.close();
        	});

        	HorizontalLayout layout = new HorizontalLayout(message, closeButton);
        	layout.setAlignItems(Alignment.CENTER);
        	
        	notification.add(layout);
        	notification.open();
        });        
    }
}
