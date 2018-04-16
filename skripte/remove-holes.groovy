import com.vividsolutions.jts.geom.*
import com.vividsolutions.jts.io.WKTReader
import com.vividsolutions.jts.algorithm.CGAlgorithms

@Grapes(
    @Grab(group='com.vividsolutions', module='jts', version='1.13')
)
GeometryFactory factory = new GeometryFactory()

def geom = '''MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)),
  ((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),
  (30 20, 20 15, 20 25, 30 20)))'''

WKTReader reader = new WKTReader(factory)
Geometry g = reader.read(geom)

class Processor {

    static final GeometryFactory factory = new GeometryFactory()
    
    Polygon convert(Polygon p, areaThreshold) {
        if (p.numInteriorRing > 0) {
            // has holes
            LinearRing shell = p.exteriorRing
            List<LinearRing> holes = []
            
            for (int i = 0; i < p.numInteriorRing; i++) {
                LinearRing hole = p.getInteriorRingN(i)
                
                // determine area of hole
                try {
                    double area = CGAlgorithms.signedArea(hole.coordinates)
                    // println "Area of hole: $area"
                    if (area > areaThreshold) {
                        // keep hole
                        holes << hole
                    }
                    else {
                        // throw hole away
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
            
            factory.createPolygon(shell, holes.toArray(new LinearRing[holes.size()]))
        }
        else {
            // no holes
            p
        }
    }
    
    MultiPolygon convert(MultiPolygon mp, areaThreshold) {
        if (mp.numGeometries) {
            Polygon[] polygons = new Polygon[mp.numGeometries]
            for (int i = 0; i < mp.numGeometries; i++) {
                polygons[i] = convert(mp.getGeometryN(i), areaThreshold)
            }
            factory.createMultiPolygon(polygons)
        }
        else {
            // empty
            mp
        }
    }
    
    Geometry convert(Geometry g, areaThreshold) {
        // nothing to do
        g
    }

}

println 'Original:'
println g

println()

println 'Processed:'
println new Processor().convert(g, 51)


// return processor for use as snippet
new Processor()

/*

import com.vividsolutions.jts.geom.*
import com.vividsolutions.jts.io.WKTReader
import com.vividsolutions.jts.algorithm.CGAlgorithms
import eu.esdihumboldt.hale.common.instance.geometry.DefaultGeometryProperty

class 
_log.info("Loch in Zoning mit Fläche ${area} beibehalten")
...

// remove small holes created due to interpolation
def g = union.geometry
g = new Processor().convert(g, _project.variables.ERROR_AREA_THRESHOLD as double)
union = new DefaultGeometryProperty(union.CRSDefinition, g)

*/