package MoogleGaps;

public class Geometry {

    // takes latitude and longitude of point
    // returns n-vector, i.e. to earth's surface perpendicular vector through point
    public static double[] getNVector(double latitude, double longitude) {
        double[] nVector = new double[3];
        nVector[0] = Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(longitude));
        nVector[1] = Math.cos(Math.toRadians(latitude)) * Math.sin(Math.toRadians(longitude));
        nVector[2] = Math.sin(Math.toRadians(latitude));
        return nVector;
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

    // takes two vector a and b
    // returns their dot product ab
    private static double getDotProduct(double[] a, double[] b) {
        double dotProduct = 0;
        for (int i = 0; i < 3; i++) {
            dotProduct += a[i] * b[i];
        }
        return dotProduct;
    }


    // takes n-vectors of two points a and b
    // returns their distance in km with R = 6371 km
    public static double getDistance(double[] nVectorA, double[] nVectorB) {
        return 6371 * Math.toDegrees(Math.atan2(getEuclideanNorm(getCrossProduct(nVectorA, nVectorB)), getDotProduct(nVectorA, nVectorB)));
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

    /**
     * Winding number algorithm
     * Weiler, Kevin (1994), "An Incremental Angle Point in Polygon Test", in Heckbert, Paul S. (ed.), Graphics Gems IV, San Diego, CA, USA: Academic Press Professional, Inc., pp. 16–23, ISBN 0-12-336155-9.
     *
     * @param index     the index of the Polygon to test
     * @param longitude the longitude of the point to test collision with
     * @param latitude  the latitude to test collision with
     * @return true if point is in polygon, else false
     */
    private static boolean pointInPoly(int index, double longitude, double latitude) {
        int n = Polygons.getWayLength(index);
        int firstNode = Polygons.polygonIds.get(index);
        int angle = 0;
        double longitudeA = Polygons.longitudes[firstNode];
        double latitudeA = Polygons.latitudes[firstNode];
        int quad = quadrant(longitudeA, latitudeA, longitude, latitude);
        int nextQuad, delta;
        double longitudeB, latitudeB;

        for (int i = 0; i < n; i++) {
            longitudeB = Polygons.longitudes[firstNode + ((i + 1) % n)];
            latitudeB = Polygons.latitudes[firstNode + ((i + 1) % n)];
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
    private static double xIntercept(double xA, double yA, double xB, double yB, double y) {
        return xB - ((yB - y) * ((xA - xB) / (yA - yB)));
    }

    /**
     * takes point with coordinates as longitude and latitude as double
     *
     * @param longitude
     * @param latitude
     * @return true if point is in a polygon, i.e. on land; false if point is in water
     */
    public static boolean pointInPolygonTest(double longitude, double latitude) {
        for (int i = 0; i < Polygons.polygonIds.size(); i++) {
            if (longitude > Polygons.boundingLonMin[i] && longitude < Polygons.boundingLonMax[i]
                    && latitude > Polygons.boundingLatMin[i] && latitude < Polygons.boundingLatMax[i]) {
                if (pointInPoly(i, longitude, latitude)) {
                    return true;
                }
            }
        }
        return false;
    }
}
