def wkt = "[..]"

if(wkt != '[..]'){
	return _.geom.with(geometry:wkt, crs:"EPSG:4258")
}

else return null