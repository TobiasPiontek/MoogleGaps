# MoogleGaps 
Repository for the University Stuttgart "Fachpraktikum"
Join Meetings with this Link: https://jitsi-meet.fmi.uni-stuttgart.de/AlgLabCourseMeetingSS20

# Manual
- Import the project into [IntelliJ Community Edition](https://www.jetbrains.com/de-de/idea/download/#section=windows)
- Go to the VM-options of the main method and add
    - Xmx8192m as a parameter or higher to allow the project to allocate enough RAM
- Store the PBF-files in OSMMapData-folder
    - ***all files need to end on ".pbf"*** to be detected by the filereader in the backend 
- Start the backend
- Choose the wanted file in the dialog with its listed number and confirm with ***[Enter]***
- Go through the dialog as follows
    - the route will be displayed as a geojson LineString, which can be copy pasted into the http://geojson.io website
    

#### Task 1: Understand OSM Data Structures
- As a first step, we need to get to know how the data is organized inside OSM.we are primarily interested in “nodes” and “ways”.
    - Resources
        - the [OSM wiki](https://wiki.openstreetmap.org/wiki/Main_page) explains the [core data structures](https://wiki.openstreetmap.org/wiki/Elements) as well as interpretations of individual tags.
        - this [youtube-playlist](https://www.youtube.com/playlist?list=PLCE6296A33CF47955) explains the OSM primitives and tags with lots of examples
#### Task 2: Extract Coastlines from a PBF File
- Read in a PBF file and extract all coastlines contained in the file. You can start by just outputting them to console.  To see your results, save them as GeoJson and use geojson.io. Merge touching coastline ways to enablethe further processing in the following tasks. It makes sense to store the coastlines in a format that is easy to read for future use.
    - Resources
        - Download of OSM data by region from [geofabrik](https://download.geofabrik.de/index.html) (Antarctica is a good starting point)
        - [PBF Format](https://wiki.openstreetmap.org/wiki/PBF_Format)
        - Definition of the [coastline tag](https://wiki.openstreetmap.org/wiki/Coastline)
        - [Geojson format definition](https://geojson.org/)
        - [Geojson.io](http://geojson.io) a GeoJson visualization site
        - [Geojson styling guide](https://github.com/mapbox/simplestyle-spec/tree/master/1.1.0): adding CSS properties that are supported by geojson.io
        - Detailed [tagging information](https://wiki.openstreetmap.org/wiki/Tag:natural%3Dcoastline) for coastlines
 
#### Task 3: Distinguish between Water and Land
Implement the point in polygon test to determine if a certain position is inthe ocean (aka passable for ships). Be aware the latitude and longitude arenot coordinates on a plane. Use the spherical model of the earth to do your calculations.
- Resources
    - [Many calculations for lat long points](http://www.movable-type.co.uk/scripts/latlong.html)
    - [Calculations with lat long but converted into vectors first](http://www.movable-type.co.uk/scripts/latlong-vectors.html)
    - [In-polygon test with spherical polygons](https://link.springer.com/article/10.1007/BF00894449) (Uni network only)
    - https://en.wikipedia.org/wiki/Point_in_polygon
    - [Video with 3D animation of spherical vs vector coordinates](https://www.youtube.com/watch?v=FDyenWWlPdU)            
            
#### Task 4: Grid Graph
- Implement a grid graph representation which allows routing on the oceanscorresponding to the input. Use a bit vector to distinguish between accessible nodes (in the ocean) and non-accessible nodes (on land). Node position and edges should not explicitly be stored but be calculated on demand.

#### Task 5: Dijkstra’s Algorithm
Implement Dijkstra’s Algorithm for shortest paths on your grid data structure.

#### Task 6: Interactive Routing Front End
Add a GUI to your project. It should be possible to set start and end nodesas well as visualizing the route. The calculated distance should be displayedin your GUI.

#### Dependencies used (located in dependencies folder):
- Osmosis Pbf
    - Is used to parse PBF Files in java
    - Downloaded from https://mvnrepository.com/artifact/org.openstreetmap.osmosis/osmosis-pbf/0.46
    - Komplettes Osmosis Archiev: https://github.com/openstreetmap/osmosis/releases/tag/0.47.4
    - Tutorials to use: https://neis-one.org/2017/10/processing-osm-data-java/
- osm4j
    - used to parsePBF files in Java (alternative for Osmosis)
    - https://github.com/topobyte/osm4j-extra


        
#### Tutorial used
- ##### Task 3
    - [Tutorial for vector matheatics](http://www.movable-type.co.uk/scripts/latlong-vectors.html)
    - [Point in polygon test](http://geomalgorithms.com/a03-_inclusion.html)
    - Sorting Arrays with index List https://stackoverflow.com/questions/4859261/get-the-indices-of-an-array-after-sorting
    - https://howtodoinjava.com/sort/collections-sort/
        

