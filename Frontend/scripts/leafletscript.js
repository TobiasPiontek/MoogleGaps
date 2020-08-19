//dummy geojson objekt
var myLines;
/*var myLines = [{
        "type": "LineString",
        "coordinates": [
            [8.1, 53.1],
            [8.1, 51.6],
            [8.1, 50.1]
        ]
    },
    {
        "type": "LineString",
        "coordinates": [
            [8.1, 51.6],
            [9.5, 51.6]
        ]
    },
    {
        "type": "LineString",
        "coordinates": [
            [9.5, 53.1],
            [9.5, 51.6],
            [9.5, 50.1]
        ]
    },
    {
        "type": "LineString",
        "coordinates": [
            [11, 53.1],
            [11, 50.1]
        ]
    }
];
*/

var myStyle = {
    "color": "#ff0000",
    "weight": 10,
    "opacity": 1.00
}

//
document.getElementById("back").disabled = true;
document.getElementById("next").disabled = false;
document.getElementById("compute").disabled = true;

//die karte
var mymap = L.map('mapcontainer').setView([51.316, 10.734], 6);

//marker
var firstmarker;
var secondmarker;

//gui steuerungsstatus
var state = 0;

//hilfsvariablen für koordinaten, möglicherweise nicht nötig
var x1;
var y1;
var x2;
var y2;

//geojson-objekt zum testen (und später anzeigen) eines weges auf der karte
var weg;

//laden der map in leaflet
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
    maxZoom: 18,
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
        '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="http://mapbox.com">Mapbox</a>',
    id: 'mapbox.streets'
}).addTo(mymap);

//funktion zum setzten der marker
mymap.on('click', function(e) {

    var k = e.latlng;
    console.log(k);
    //prüfen des status, bei 0 ersten marker setzen, bei 1 (oder anderem) den zweiten marker setzen
    if (state == 0) {
        //alten marker löschen und neuen setzen
        if (firstmarker) {
            mymap.removeLayer(firstmarker);
        }
        firstmarker = new L.Marker(k).addTo(mymap);
        x1 = k.lat;
        y1 = k.lng;
        //rausschreiben der koordinaten in html dokument
        document.getElementById("lat1").innerHTML = x1;
        document.getElementById("long1").innerHTML = y1;
    } else {
        if (secondmarker) {
            mymap.removeLayer(secondmarker);
        }
        secondmarker = new L.Marker(k).addTo(mymap);
        x2 = k.lat;
        y2 = k.lng;
        document.getElementById("lat2").innerHTML = x2;
        document.getElementById("long2").innerHTML = y2;
    }
});

//hooks für buttons
function next() {
    if (state == 0) {
        state = 1;
        document.getElementById("Out1").innerHTML = "Setzen des zweiten Markers";
        document.getElementById("Out2").innerHTML = "Vorherigen Marker neu setzen";
        document.getElementById("Out3").innerHTML = "Keine Funktion";
        document.getElementById("Out4").innerHTML = "Berechne Weg zwischen Markern";
        document.getElementById("back").disabled = false;
        document.getElementById("next").disabled = true;
        document.getElementById("compute").disabled = false;
    }
};

function back() {
    if (state == 1) {
        state = 0;
        document.getElementById("Out1").innerHTML = "Setzen des ersten Markers";
        document.getElementById("Out2").innerHTML = "Keine Funktion";
        document.getElementById("Out3").innerHTML = "Nächsten Marker setzen";
        document.getElementById("Out4").innerHTML = "Keine Funktion";
        document.getElementById("back").disabled = true;
        document.getElementById("next").disabled = false;
        document.getElementById("compute").disabled = true;
    }
};

function compute() {
    if ((firstmarker != null) && (secondmarker != null)) {
        if (weg) {
            mymap.removeLayer(weg);
        }

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                console.log(this.responseText);
                myLines = JSON.parse(this.responseText);
                console.log(myLines);
                weg = L.geoJSON(myLines, { style: myStyle }).addTo(mymap);
            }
        };
        xhttp.open("POST", "http://localhost:8004/MoogleGaps", true);
        xhttp.send("calculateRoute;" + document.getElementById("lat1").innerHTML + "," + document.getElementById("long1").innerHTML + ";" + document.getElementById("lat2").innerHTML + "," + document.getElementById("long2").innerHTML);
        //        document.getElementById("Out5").innerHTML = "Noch wird nur ein Dummyweg angezeigt, da die Berechnung noch nicht mit dem Frontend verknüpft ist. Es soll beim fertigen Projekt ein Weg von Start bis Ziel als GeoJSON Objekt dem Frontend übergeben und angezeigt werden.";
    }
};

function checkFirst() {
    if (firstmarker != null) {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var response = this.responseText;
                var split = response.split(",");
                document.getElementById("lat1").innerHTML = split[0];
                document.getElementById("long1").innerHTML = split[1];
                if (firstmarker) {
                    mymap.removeLayer(firstmarker);
                }
                firstmarker = new L.Marker([split[0], split[1]]).addTo(mymap);
            }
        };
        xhttp.open("POST", "http://localhost:8004/MoogleGaps", true);
        xhttp.send("SetStartnode" + ";" + document.getElementById("lat1").innerHTML + "," + document.getElementById("long1").innerHTML);
    }
};

function checkSecond() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
            var split = response.split(",");
            document.getElementById("lat2").innerHTML = split[0];
            document.getElementById("long2").innerHTML = split[1];
            if (secondmarker) {
                mymap.removeLayer(secondmarker);
            }
            secondmarker = new L.Marker([split[0], split[1]]).addTo(mymap);
        }
    };
    xhttp.open("POST", "http://localhost:8004/MoogleGaps", true);
    xhttp.send("SetStartnode" + ";" + document.getElementById("lat2").innerHTML + "," + document.getElementById("long2").innerHTML);
};