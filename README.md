# MoogleGaps
Repository for the University Stuttgart "Fachpraktikum"

- #### Task 1: Understand OSM Data Structures
    - as a first step, we need to get to know how the data is organized inside OSM.we are primarily interested in “nodes” and “ways”.
        - Resources
            - the OSM wiki explains the core data structures as well as interpretations of individual tags.
            - this youtube-playlist explains the OSM primitives and tags with lots of examples
- #### Task 2: Extract Coastlines from a PBF File
    - Read in a PBF file and extract all coastlines contained in the file. You can start by just outputting them to console.  To see your results, save them as GeoJson and use geojson.io. Merge touching coastline ways to enablethe further processing in the following tasks. It makes sense to store the coastlines in a format that is easy to read for future use.
        - Resources
            - Download of OSM data by region from geofabrik (Antarctica is a good starting point)
            - PBF Format
            - Definition of the coastline tag
            - Geojson format definition
            - Geojson.io a GeoJson visualization site
            - Geojson styling guide: adding CSS properties that are supported by geojson.io
            - Detailed tagging information for coastlines
            
            
## Notes
We try to use https://neis-one.org/2017/10/processing-osm-data-java/ as a library to parse the pbs data files of OSM