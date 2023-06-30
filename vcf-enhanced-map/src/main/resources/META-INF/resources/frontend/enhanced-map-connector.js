import Draw from 'ol/interaction/Draw.js';
import Modify from 'ol/interaction/Modify.js';
import Snap from 'ol/interaction/Snap.js';
import Select from 'ol/interaction/Select.js';
import VectorSource from 'ol/source/Vector.js';
import VectorLayer from 'ol/layer/Vector.js';
import Style from 'ol/style/Style.js';
import Fill from 'ol/style/Fill.js';
import Stroke from 'ol/style/Stroke.js';
import Feature from 'ol/Feature.js';
import Polygon from 'ol/geom/Polygon.js';

window.enhancedMap = {

    addDrawingLayer: function (vaadinWCmap, drawningOptionsJson) {
        setTimeout(() => this._addDrawingLayer(vaadinWCmap, drawningOptionsJson))
    },

    _addDrawingLayer: function (vaadinWCmap, drawningOptionsJson) {
        let map = vaadinWCmap._configuration;
        
        let parsedOptions = JSON.parse(drawningOptionsJson);

        const fill = new Fill({
            color: parsedOptions.fillColor,
          });
          const stroke = new Stroke({
            color: parsedOptions.strokeColor,
            width: parsedOptions.strokeWidth,
          });
          const styles = [
            new Style({
              fill: fill,
              stroke: stroke,
            }),
          ];

        let source = new VectorSource();
        let vector = new VectorLayer({
            source: source,
            style: styles
        });
        map.addLayer(vector);
        map.sourceVectorSource = source;
        map.vectorVectorLayer = vector;
        map.onePolygonOnly = parsedOptions.onePolygonOnly;
    },

    setDrawingInteractions: function (vaadinWCmap) {
        setTimeout(() => this._setDrawingInteractions(vaadinWCmap))
    },

    _setDrawingInteractions: function (vaadinWCmap) {
        let map = vaadinWCmap._configuration;
        let source = map.sourceVectorSource;

        if (map.interactionSelect) {
            map.removeInteraction(map.interactionSelect);
        }
        const modify = new Modify({ source: source });
        map.addInteraction(modify);
        let draw, snap;
        draw = new Draw({
            source: source,
            type: 'Polygon'
        });
        draw.on('drawstart', function() {
			if(map.onePolygonOnly){
				source.clear();	
			}  			
		});
        draw.on('drawend', function(event) {
            vaadinWCmap.$server.sendGeometryCoordinates(event.feature.getGeometry().getCoordinates());
        });
        snap = new Snap({ source: source });
        map.addInteraction(draw);
        map.addInteraction(snap);       
        map.interactionDraw = draw;
        map.interactionSnap = snap;
    },
  
    setRemovalInteractions: function (vaadinWCmap) {
        setTimeout(() => this._setRemovalInteractions(vaadinWCmap))
    },

    _setRemovalInteractions: function (vaadinWCmap) {
        let map = vaadinWCmap._configuration;
        let source = map.sourceVectorSource;

        if (map.interactionDraw) {
            map.removeInteraction(map.interactionDraw);
            map.removeInteraction(map.interactionSnap);
        }
        let select = new Select();
        select.getFeatures().on('add', function(feature){
            setTimeout(() => {
                source.removeFeature(feature.element);
                feature.target.remove(feature.element);
            });
        });
        map.addInteraction(select);
        map.interactionSelect = select;
    },
    
    clearPolygons: function (vaadinWCmap) {
        setTimeout(() => this._clearPolygons(vaadinWCmap))
    },
    
    _clearPolygons: function(vaadinWCmap) {
		let map = vaadinWCmap._configuration;
		let source = map.sourceVectorSource;
		
		var features = source.getFeatures();
		
		features.forEach(function(feature) {
		  source.removeFeature(feature);
		});
	},
    
    loadPolygon: function (vaadinWCmap, coordinateString) {
        setTimeout(() => this._loadPolygon(vaadinWCmap, coordinateString))
    },
    
    _loadPolygon: function(vaadinWCmap, coordinateString) {
		let map = vaadinWCmap._configuration;
		let source = map.sourceVectorSource;
		
		let coordinates = JSON.parse(coordinateString);
		
		var poly = new Feature({
			geometry: new Polygon(coordinates)
		})
		source.addFeature(poly);
	}

}