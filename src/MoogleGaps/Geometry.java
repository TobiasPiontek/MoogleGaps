package MoogleGaps;

public class Geometry {

    // takes longitude and latitude of point
    // returns n-vector, i.e. vector originating from center of earth through point
    public static double[] getNVector(double longitude, double latitude) {
        double[] nVector = new double[3];
        nVector[0] = Math.cos(latitude)*Math.cos(longitude);
        nVector[1] = Math.cos(latitude)*Math.sin(longitude);
        nVector[2] = Math.sin(latitude);
        return nVector;
    }

    // takes longitudes and latitudes of four points a, b, c, and d
    // returns n-vector of the intersection of ab and cd
    public static double[] getIntersection(double longitudeA, double latitudeA, double longitudeB, double latitudeB, double longitudeC, double latitudeC, double longitudeD, double latitudeD) {
        double[] nVectorA = getNVector(longitudeA, latitudeA);
        double[] nVectorB = getNVector(longitudeB, latitudeB);
        double[] nVectorC = getNVector(longitudeC, latitudeC);
        double[] nVectorD = getNVector(longitudeD, latitudeD);
        double[] greatCircle1 = getCrossProduct(nVectorA, nVectorB);
        double[] greatCircle2 = getCrossProduct(nVectorC, nVectorD);
        double[] nVectorIntersection = getCrossProduct(greatCircle1, greatCircle2);
        double[] nVectorMid = new double[3];
        for (int i = 0; i < 3; i++) {
            nVectorMid[i] = nVectorA[i] + nVectorB[i] + nVectorC[i] + nVectorD[i];
        }
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
}
