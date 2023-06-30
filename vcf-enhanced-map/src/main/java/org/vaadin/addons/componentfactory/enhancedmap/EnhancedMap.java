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
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.function.SerializableRunnable;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonArray;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * Enhanced Map component that provides extra features like drawing polygons on
 * an extra Maps Layer
 * 
 * @author mlopez
 *
 */
@SuppressWarnings("serial")
@JsModule("./enhanced-map-connector.js")
public class EnhancedMap extends Map {

	private DrawingLayerOptions drawingLayerOptions;
	private boolean attached = false;

	public EnhancedMap() {
		super();
		init();
	}

	public EnhancedMap(DrawingLayerOptions drawingLayerOptions) {
		super();
		this.drawingLayerOptions = drawingLayerOptions;
		init();
	}

	private void init() {
		executeWhenAttached(() -> getElement().executeJs(
				"enhancedMap.addDrawingLayer($0, $1); enhancedMap.setDrawingInteractions($0);", this, drawingLayerOptions.toJSON()));
	}
		
	/**
	 * Load the supplied polygons in the polygon layer
	 * 
	 * @param polygons
	 */
	public void loadPolygons(List<Polygon> polygons) {
		List<String> coordinates = new ArrayList<>();
		polygons.forEach(polygon -> {
			for (int i = 0; i < polygon.getCoordinates().length; i++) {
				Coordinate coordinate = polygon.getCoordinates()[i];
				String stringCoordinate = "[" + coordinate.getX() + ", " + coordinate.getY() + "]";
				coordinates.add(stringCoordinate);
			}
			String polygonCoordinates = coordinates.stream().collect(Collectors.joining(","));
			executeWhenAttached(() -> getElement().executeJs("enhancedMap.loadPolygon($0, $1);", this,
					"[[" + polygonCoordinates + "]]"));
		});
	}

	public void clearPolygons() {
		executeWhenAttached(() -> getElement().executeJs("enhancedMap.clearPolygons($0);", this));
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		attached = true;
	}

	public void setRemoveMode() {
		executeWhenAttached(() -> getElement().executeJs("enhancedMap.setRemovalInteractions($0);", this));
	}

	public void setDrawingMode() {
		executeWhenAttached(() -> getElement().executeJs("enhancedMap.setDrawingInteractions($0);", this));
	}

	/**
	 * Call from client when a new polygon was drawn.
	 * 
	 * @param data new polygon coordinates data as json value
	 */
	@ClientCallable
	public void sendGeometryCoordinates(JsonValue data) {
		JreJsonArray coordinatesFromClientArray = (JreJsonArray) ((JreJsonArray) data).get(0);

		List<Coordinate> coordinatesList = new ArrayList<>();

		IntStream.range(0, coordinatesFromClientArray.length())
				.mapToObj(index -> (JreJsonArray) coordinatesFromClientArray.get(index))
				.forEach(item -> coordinatesList.add(new Coordinate(item.get(0).asNumber(), item.get(1).asNumber())));

		GeometryFactory factory = new GeometryFactory();
		Polygon polygon = factory.createPolygon(coordinatesList.toArray(Coordinate[]::new));
		fireNewPolygonEvent(polygon, true);
	}

	/**
	 * Adds a listener for {@link NewPolygonEvent}.
	 *
	 * @param listener the listener to be added
	 */
	public void addNewPolygonListener(ComponentEventListener<NewPolygonEvent> listener) {
		addListener(NewPolygonEvent.class, listener);
	}

	/**
	 * Fires a {@link NewPolygonEvent}.
	 *
	 * @param polygon the new polygon created
	 */
	protected void fireNewPolygonEvent(Polygon polygon, boolean fromClient) {
		fireEvent(new NewPolygonEvent(this, polygon, fromClient));
	}

	/** Event thrown when a polygon is created. */
	public static class NewPolygonEvent extends ComponentEvent<EnhancedMap> {

		private final Polygon polygon;

		public NewPolygonEvent(EnhancedMap source, Polygon polygon, boolean fromClient) {
			super(source, fromClient);
			this.polygon = polygon;
		}

		public Polygon getPolygon() {
			return polygon;
		}

		public EnhancedMap getEnhancedMap() {
			return (EnhancedMap) source;
		}
	}

	private void executeWhenAttached(SerializableRunnable runnable) {
		if (attached) {
			runnable.run();
		} else {
			getElement().addAttachListener(ev -> runnable.run());
		}
	}
	
}
