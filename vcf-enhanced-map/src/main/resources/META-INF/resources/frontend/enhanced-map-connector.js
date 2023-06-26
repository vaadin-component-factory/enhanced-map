import Map from 'ol/Map.js';
import View from 'ol/View.js';
import { Draw, Modify, Snap, Select } from 'ol/interaction.js';
import { OSM, Vector as VectorSource } from 'ol/source.js';
import { Tile as TileLayer, Vector as VectorLayer } from 'ol/layer.js';
import { get } from 'ol/proj.js';

window.enhancedMap = {

    addDrawingCapabilities: function (vaadinWCmap) {
        setTimeout(() => this._addDrawingCapabilities(vaadinWCmap))
    },

    _addDrawingCapabilities: function (vaadinWCmap) {
        let map = vaadinWCmap._configuration;

        const source = new VectorSource();
        const vector = new VectorLayer({
            source: source
        });
        map.addLayer(vector);

        const modify = new Modify({ source: source });
        map.addInteraction(modify);
        let draw, snap;
        draw = new Draw({
            source: source,
            type: 'Polygon'
        });
        draw.on('drawend', function(event) {
            vaadinWCmap.$server.sendVectorLayer(event.feature.getGeometry().getCoordinates());
        });
        snap = new Snap({ source: source });
        map.addInteraction(draw);
        map.addInteraction(snap);
        
    },

    addDrawingCapabilities: function (vaadinWCmap) {
        setTimeout(() => this._addDrawingCapabilities(vaadinWCmap))
    },

    _addDrawingCapabilities: function (vaadinWCmap) {
        let map = vaadinWCmap._configuration;

        const source = new VectorSource();
        const vector = new VectorLayer({
            source: source
        });
        map.addLayer(vector);

        const modify = new Modify({ source: source });
        map.addInteraction(modify);
        let draw, snap;
        draw = new Draw({
            source: source,
            type: 'Polygon'
        });
        draw.on('drawend', function(event) {
            vaadinWCmap.$server.sendVectorLayer(event.feature.getGeometry().getCoordinates());
        });
        snap = new Snap({ source: source });
        map.addInteraction(draw);
        map.addInteraction(snap);

        select = new Select();
        select.getFeatures().on('add', function(feature){
            source.removeFeature(feature.element);
                feature.target.remove(feature.element);
        });
        map.addInteraction(select);
        
    },

}