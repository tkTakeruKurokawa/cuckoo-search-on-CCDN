package main;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

import java.lang.Math;

public class Parameters implements Control {
    private static final String PAR_ORIGIN_ID = "originId";
    private static int originId;
    private static final String PAR_TOTAL_CYCLE = "totalCycles";
    private static int totalCycles;
    private static final String PAR_WEIBULL_SHAPE = "weibullShape";
    private static double weibullShape;
    private static final String PAR_WEIBULL_SCALE = "weibullScale";
    private static double weibullScale;
    private static final String PAR_NORMAL_AVERAGE = "normalAverage";
    private static double normalAverage;
    private static final String PAR_NORMAL_VARIANCE = "normalVariance";
    private static double normalVariance;
    private static final String PAR_NORMAL_MIN = "normalMin";
    private static int normalMin;
    private static final String PAR_NORMAL_MAX = "normalMax";
    private static int normalMax;

    private static double[] failureRate;
    private static double[] magnification;

    public Parameters(String prefix) {
        originId = Configuration.getInt(prefix + "." + PAR_ORIGIN_ID);
        totalCycles = Configuration.getInt(prefix + "." + PAR_TOTAL_CYCLE);
        weibullShape = Configuration.getDouble(prefix + "." + PAR_WEIBULL_SHAPE);
        weibullScale = Configuration.getDouble(prefix + "." + PAR_WEIBULL_SCALE);
        normalAverage = Configuration.getDouble(prefix + "." + PAR_NORMAL_AVERAGE);
        normalVariance = Configuration.getDouble(prefix + "." + PAR_NORMAL_VARIANCE);
        normalMin = Configuration.getInt(prefix + "." + PAR_NORMAL_MIN);
        normalMax = Configuration.getInt(prefix + "." + PAR_NORMAL_MAX);
    }

    public static void initializeDistributionModels() {
        failureRate = new double[totalCycles];
        magnification = new double[Network.size()];

        weibullDistribution();
        normalDistribution();
    }

    public static double getFailureRate(int nodeId, int position) {
        return failureRate[position] * magnification[nodeId];
    }

    // private static void paretoDistribution() {
    // for (int i = 1; i <= frMaxCycle; i++) {
    // double x = (double) i;
    // double f = frAlpha * Math.pow(1, frAlpha) / Math.pow(x, frAlpha + 1);
    // System.out.println(f);
    // failureRate[i - 1] = f;
    // }
    // }

    private static int setMagnification(double pdf, int index, int value) {
        int nodeCount = (int) Math.round(pdf * ((double) Network.size()));

        int count = 0;
        while (count < nodeCount) {
            if (index == originId) {
                index++;
                continue;
            }
            magnification[index] = (double) value;
            index++;
            count++;
        }

        return index;
    }

    private static void normalDistribution() {
        double mu = normalAverage;
        double sigma = normalVariance;
        int min = normalMin;
        int max = normalMax;

        int index = 0;
        for (int value = min; value <= max; value++) {
            double x = (double) value;
            double pdf = (1 / (Math.sqrt(2.0 * Math.PI * sigma)))
                    * Math.exp(-1 * (Math.pow(x - mu, 2.0) / (2.0 * sigma)));

            index = setMagnification(pdf, index, value);
        }
    }

    private static void weibullDistribution() {
        double k = weibullShape;
        double lambda = weibullScale;

        for (int i = 0; i < totalCycles; i++) {
            double x = (double) i;
            failureRate[i] = (k / lambda) * Math.pow((x / lambda), k - 1.0) * Math.exp(-1.0 * Math.pow(x / lambda, k));
        }
    }

    // private static boolean isOccur(double value) {
    // double rand = Math.random();
    // if (rand < value) {
    // return true;
    // }

    // return false;
    // }

    @Override
    public boolean execute() {

        initializeDistributionModels();

        return false;
    }

}