package MoogleGaps;

import java.util.Arrays;

public class Geometry {

    //
    // FIRST POINT IN POLYGON TEST
    // Ray casting algorithm
    // http://www.movable-type.co.uk/scripts/latlong-vectors.html
    //

    // takes latitude and longitude of point
    // returns n-vector, i.e. to earth's surface perpendicular vector through point
    private static double[] getNVector(double latitude, double longitude) {
        double[] nVector = new double[3];
        nVector[0] = Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(longitude));
        nVector[1] = Math.cos(Math.toRadians(latitude)) * Math.sin(Math.toRadians(longitude));
        nVector[2] = Math.sin(Math.toRadians(latitude));
        return nVector;
    }

    // takes latitudes and longitudes of four points a, b, c, and d
    // returns n-vector of the intersection of ab and cd
    private static double[] getIntersection(double latitudeA, double longitudeA, double latitudeB, double longitudeB, double latitudeC, double longitudeC, double latitudeD, double longitudeD) {
        double[] nVectorA = getNVector(latitudeA, longitudeA);
        double[] nVectorB = getNVector(latitudeB, longitudeB);
        double[] nVectorC = getNVector(latitudeC, longitudeC);
        double[] nVectorD = getNVector(latitudeD, longitudeD);
        double[] greatCircle1 = getGreatCircle(nVectorA, nVectorB);
        double[] greatCircle2 = getGreatCircle(nVectorC, nVectorD);

        // compute first candidate
        double[] nVectorIntersection = getCrossProduct(greatCircle1, greatCircle2);
        double[] nVectorMid = getMidpoint(nVectorA, getMidpoint(nVectorB, getMidpoint(nVectorC, nVectorD)));

        // return closer point
        if (getDotProduct(nVectorMid, nVectorIntersection) > 0) {

            // DEBUG
            //System.out.println(getCoordinates(nVectorIntersection)[0] + ", "+ getCoordinates(nVectorIntersection)[1]);

            return nVectorIntersection;
        } else {

            // DEBUG
            //System.out.println(getCoordinates(getCrossProduct(greatCircle2, greatCircle1))[0] + ", "+ getCoordinates(getCrossProduct(greatCircle2, greatCircle1))[1]);

            return getCrossProduct(greatCircle2, greatCircle1);
        }
    }

    // takes two vectors a and b
    // returns their cross product a x b
    private static double[] getCrossProduct(double[] a, double[] b) {
        double[] crossProduct = new double[3];
        crossProduct[0] = a[1] * b[2] - a[2] * b[1];
        crossProduct[1] = a[2] * b[0] - a[0] * b[2];
        crossProduct[2] = a[0] * b[1] - a[1] * b[0];
        return crossProduct;
    }

    // takes two vectors a and b
    // returns the great circle containing them
    private static double[] getGreatCircle(double[] a, double[] b) {
        double[] n = getNVector(0, 0);
        double[] m = getNVector(1, 1);
        if (Arrays.equals(a, b)) {

            // vectors are the same
            if (!Arrays.equals(a, b)) {

                // point is not (0, 0)
                return getCrossProduct(a, n);
            } else {

                return getCrossProduct(a, m);
            }
        } else {

            // vectors are different
            return getCrossProduct(a, b);
        }
    }

    // takes two vector a and b
    // returns their dot product ab
    private static double getDotProduct(double[] a, double[] b) {
        double dotProduct = 0;
        for (int i = 0; i < 3; i++) {
            dotProduct += a[i]*b[i];
        }
        return dotProduct;
    }

    // takes n-vector of point
    // returns its latitude and longitude
    private static double[] getCoordinates(double[] nVector) {
        double[] coordinates = new double[2];
        coordinates[0] = Math.toDegrees(Math.atan2(nVector[2], Math.sqrt(nVector[0] * nVector[0] + nVector[1] * nVector[1])));
        coordinates[1] = Math.toDegrees(Math.atan2(nVector[1], nVector[0]));
        return coordinates;
    }

    // takes n-vectors of two points a and b
    // returns their distance in km with R = 6371 km
    private static double getDistance(double[] nVectorA, double[] nVectorB) {
        return 6371*Math.toDegrees(Math.atan2(getEuclideanNorm(getCrossProduct(nVectorA, nVectorB)), getDotProduct(nVectorA, nVectorB)));
    }

    // takes vector
    // returns its Euclidean norm
    private static double getEuclideanNorm(double[] vector) {
        return Math.sqrt(getDotProduct(vector, vector));
    }

    // takes n-vectors of two points a and b
    // returns the n-vector of their midpoint
    private static double[] getMidpoint(double[] nVectorA, double[] nVectorB) {
        double[] midpoint = new double[3];
        for (int i = 0; i < 3; i++) {
            midpoint[i] = nVectorA[i] + nVectorB[i];
        }
        return midpoint;
    }

    //
    // SECOND POINT IN POLYGON TEST
    // Winding number algorithm
    // https://stackoverflow.com/questions/4287780/detecting-whether-a-gps-coordinate-falls-within-a-polygon-on-a-map
    //

    // takes polygon and point with latitudes and longitudes
    // returns true if point is in the polygon
    private static boolean inPolygon(double[] latitudes, double[] longitudes, double latitude, double longitude) {
        int intersections = 0;
        int n = latitudes.length;

        // DEBUG
        double[] intersectionLatitudes = new double[n];
        double[] intersectionLongitudes = new double[n];

        // iterate over all edges of the polygon
        for (int i = 0; i < n; i++) {
            double[] intersection = getIntersection(latitudes[i], longitudes[i], latitudes[(i + 1) % n], longitudes[(i + 1) % n], latitude, longitude, 0.0, 0.0);
            double[] coordinates = getCoordinates(intersection);

            // DEBUG
            intersectionLatitudes[i] = coordinates[0];
            intersectionLongitudes[i] = coordinates[1];

            // increment intersections counter if the intersection is between point and fixed point
            if (Math.signum(coordinates[0]) == Math.signum(latitude) && Math.signum(coordinates[1]) == Math.signum(longitude)) {
                if (Math.abs(coordinates[0]) < Math.abs(latitude) && Math.abs(coordinates[0]) > 0.0 && Math.abs(coordinates[1]) < Math.abs(latitude) && Math.abs(coordinates[1]) > 0.0) {
                    intersections++;
                }
            }
        }

        // DEBUG

        System.out.println(intersections);
        GeoJson.printWayByCoordinates(intersectionLatitudes, intersectionLongitudes);

        // if odd number of intersections, the point lies in the polygon
        if (intersections % 2 == 1) {
            return true;
        } else {
            return false;
        }
    }

    // takes polygon and point with latitudes and longitudes
    // returns true if point is in the polygon
    // https://stackoverflow.com/questions/4287780/detecting-whether-a-gps-coordinate-falls-within-a-polygon-on-a-map
    private static boolean coordinateIsInsidePolygon (double[] longitudes, double[] latitudes, double longitude, double latitude) {
        double angle = 0;
        double latitudeA, longitudeA, latitudeB, longitudeB;
        int n = latitudes.length;

        for (int i = 0; i < n; i++) {
            latitudeA = latitudes[i] - latitude;
            longitudeA = longitudes[i] - longitude;
            latitudeB = latitudes[(i + 1) % n] - latitude;
            longitudeB = longitudes[(i + 1) % n] - longitude;
            angle += getAngle(latitudeA, longitudeA, latitudeB, longitudeB);
        }

        if (Math.abs(angle) < Math.PI) {
            return false;
        } else {
            return true;
        }
    }

    // takes two points with their x and y coordinates
    // returns the angle formed with the origin (0, 0)
    private static double getAngle(double xA, double yA, double xB, double yB) {
        double theta1 = Math.atan2(yA, xA);
        double theta2 = Math.atan2(yB, xB);
        double dtheta = theta2 - theta1;

        // DEBUG
        // System.out.println(dtheta);

        while (dtheta > Math.PI) {
            dtheta -= 2 * Math.PI;
        }

        while (dtheta < -Math.PI) {
            dtheta += 2 * Math.PI;
        }

        return(dtheta);
    }

    //
    // THIRD POINT IN POLYGON TEST
    // Winding number algorithm
    // Weiler, Kevin (1994), "An Incremental Angle Point in Polygon Test", in Heckbert, Paul S. (ed.), Graphics Gems IV, San Diego, CA, USA: Academic Press Professional, Inc., pp. 16–23, ISBN 0-12-336155-9.
    //

    // takes two points A and B with their x and y coordinates
    // returns the quadrant of A relative to B
    private static int quadrant(double xA, double yA, double xB, double yB) {
        if (xA > xB) {
            if (yA > yB) {
                return 0;
            } else {
                return 3;
            }
        } else {
            if (yA > yB) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    // takes polygon and point with latitudes and longitudes
    // returns true if point is in the polygon
    // Weiler, Kevin (1994), "An Incremental Angle Point in Polygon Test", in Heckbert, Paul S. (ed.), Graphics Gems IV, San Diego, CA, USA: Academic Press Professional, Inc., pp. 16–23, ISBN 0-12-336155-9.
    private static boolean pointInPoly(double[] longitudes, double[] latitudes, double longitude, double latitude) {
        int n = longitudes.length;
        int angle = 0;
        double longitudeA = longitudes[0];
        double latitudeA = latitudes[0];
        int quad = quadrant(longitudeA, latitudeA, longitude, latitude);
        int nextQuad, delta;
        double longitudeB, latitudeB;

        for (int i = 0; i < n; i++) {
            longitudeB = longitudes[(i + 1) % n];
            latitudeB = latitudes[(i + 1) % n];
            nextQuad = quadrant(longitudeB, latitudeB, longitude, latitude);
            delta = nextQuad - quad;
            delta = adjustDelta(delta, longitudeA, latitudeA, longitudeB, latitudeB, longitude, latitude);
            angle += delta;
            quad = nextQuad;
            longitudeA = longitudeB;
            latitudeA = latitudeB;
        }

        if (angle == 4 || angle == -4) {
            return true;
        } else {
            return false;
        }
    }

    // takes delta and three points A, B, and C with their x and y coordinates
    // returns the adjusted delta for summing the angle
    private static int adjustDelta(int delta, double xA, double yA, double xB, double yB, double xC, double yC) {
        switch (delta) {
            case 3:
                return -1;
            case -3:
                return 1;
            case 2:
            case -2:
                if (xIntercept(xA, yA, xB, yB, yC) > xC) {
                    return -delta;
                } else {
                    return delta;
                }
            default:
                return delta;
        }
    }

    // takes two points A and B with their x and y coordinates and a y coordinate
    // returns x-intercept of polygon edge with horizontal line at y value
    private static double xIntercept (double xA, double yA, double xB, double yB, double y) {
        return xB - ((yB - y) * ((xA - xB) / (yA - yB)));
    }

    /**
     * takes point with coordinates as longitude and latitude as double
     * @param longitude
     * @param latitude
     * @return true if point is in a polygon, i.e. on land; false if point is in water
     */
    public static boolean pointInPolygonTest(double longitude, double latitude) {
        for (int i = 0; i < Polygons.wayIds.size(); i++) {
            System.out.println("i = " + i);
            if (pointInPoly(Polygons.getPolygonLongitudes(i), Polygons.getPolygonLatitudes(i), longitude, latitude)) {
                return true;
            }
        }

        return false;
    }
}
