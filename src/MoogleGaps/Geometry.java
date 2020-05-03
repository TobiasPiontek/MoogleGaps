package MoogleGaps;

public class Geometry {

    // takes latitude and longitude of point
    // returns n-vector, i.e. to earth's surface perpendicular vector through point
    public static double[] getNVector(double latitude, double longitude) {
        double[] nVector = new double[3];
        nVector[0] = Math.cos(latitude)*Math.cos(longitude);
        nVector[1] = Math.cos(latitude)*Math.sin(longitude);
        nVector[2] = Math.sin(latitude);
        return nVector;
    }

    // takes latitudes and longitudes of four points a, b, c, and d
    // returns n-vector of the intersection of ab and cd
    public static double[] getIntersection(double latitudeA, double longitudeA, double latitudeB, double longitudeB, double latitudeC, double longitudeC, double latitudeD, double longitudeD) {
        double[] nVectorA = getNVector(latitudeA, longitudeA);
        double[] nVectorB = getNVector(latitudeB, longitudeB);
        double[] nVectorC = getNVector(latitudeC, longitudeC);
        double[] nVectorD = getNVector(latitudeD, longitudeD);
        double[] greatCircle1 = getCrossProduct(nVectorA, nVectorB);
        double[] greatCircle2 = getCrossProduct(nVectorC, nVectorD);

        // compute first candidate
        double[] nVectorIntersection = getCrossProduct(greatCircle1, greatCircle2);
        double[] nVectorMid = getMidpoint(nVectorA, getMidpoint(nVectorB, getMidpoint(nVectorC, nVectorD)));

        // return closer point
        if (getDotProduct(nVectorMid, nVectorIntersection) > 0) {
            return nVectorIntersection;
        } else {
            return getCrossProduct(greatCircle2, greatCircle1);
        }
    }

    // takes two vectors a and b
    // returns their cross product a x b
    public static double[] getCrossProduct(double[] a, double[] b) {
        double[] crossProduct = new double[3];
        crossProduct[0] = a[2]*b[3] - a[3]*b[2];
        crossProduct[1] = a[3]*b[1] - a[1]*b[3];
        crossProduct[2] = a[1]*b[2] - a[2]*b[1];
        return crossProduct;
    }

    // takes two vector a and b
    // returns their dot product ab
    public static double getDotProduct(double[] a, double[] b) {
        double dotProduct = 0;
        for (int i = 0; i < 3; i++) {
            dotProduct += a[i]*b[i];
        }
        return dotProduct;
    }

    // takes n-vector of point
    // returns its latitude and longitude
    public static double[] getCoordinates(double[] nVector) {
        double[] coordinates = new double[2];
        coordinates[0] = Math.atan2(nVector[2], Math.sqrt(nVector[0]*nVector[0] + nVector[1]*nVector[1]));
        coordinates[1] = Math.atan2(nVector[1], nVector[0]);
        return coordinates;
    }

    // takes n-vectors of two points a and b
    // returns their distance in km with R = 6371 km
    public static double getDistance(double[] nVectorA, double[] nVectorB) {
        return 6371*Math.atan2(getEuclideanNorm(getCrossProduct(nVectorA, nVectorB)), getDotProduct(nVectorA, nVectorB));
    }

    // takes vector
    // returns its Euclidean norm
    public static double getEuclideanNorm(double[] vector) {
        return Math.sqrt(getDotProduct(vector, vector));
    }

    // takes n-vectors of two points a and b
    // returns the n-vector of their midpoint
    public static double[] getMidpoint(double[] nVectorA, double[] nVectorB) {
        double[] midpoint = new double[3];
        for (int i = 0; i < 3; i++) {
            midpoint[i] = nVectorA[i] + nVectorB[i];
        }
        return midpoint;
    }
}
