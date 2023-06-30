package org.vaadin.addons.componentfactory.enhancedmap;

import elemental.json.Json;
import elemental.json.JsonObject;

public class DrawingLayerOptions {

	public String fillColor = "rgba(0,255,0,0.4)";
	public String strokeColor = "green";
	public double strokeWidth = 2;
	public boolean onePolygonOnly = false;
	
	public DrawingLayerOptions() {}
		
	public DrawingLayerOptions(String fillColor, String strokeColor, double strokeWidth) {
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.strokeWidth = strokeWidth;
	}
	
	public DrawingLayerOptions(String fillColor, String strokeColor, double strokeWidth, boolean onePolygonOnly) {
		this(fillColor, strokeColor, strokeWidth);
		this.onePolygonOnly = onePolygonOnly;
	}

	public String toJSON() {
	    JsonObject js = Json.createObject();
	    js.put("fillColor", fillColor);
	    js.put("strokeColor", strokeColor);
	    js.put("strokeWidth", strokeWidth);
	    js.put("onePolygonOnly", onePolygonOnly);
	    return js.toJson();
	  }
	
}
