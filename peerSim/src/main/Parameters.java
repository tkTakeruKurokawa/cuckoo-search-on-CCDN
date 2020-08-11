package main;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

import java.lang.Math;

public class Parameters implements Control {
    private static final String PAR_TOTAL_CYCLE = "totalCycles";
    private static int totalCycles;
    private static final String PAR_WEIBULL_SHAPE = "weibullShape";
    private static double weibullShape;
    private static final String PAR_WEIBULL_SCALE = "weibullScale";
    private static double weibullScale;
    private static final String PAR_TOTAL_NODES = "totalNodes";
    private static int totalNodes;
    private static final String PAR_ORIGIN_ID = "originId";
    private static int originId;
    private static final String PAR_NORMAL_AVERAGE = "normalAverage";
    private static double normalAverage;
    private static final String PAR_NORMAL_VARIANCE = "normalVariance";
    private static double normalVariance;
    private static final String PAR_NORMAL_MIN = "normalMin";
    private static int normalMin;
    private static final String PAR_NORMAL_MAX = "normalMax";
    private static int normalMax;
    private static final String PAR_TOTAL_CONTENTS = "totalContents";
    private static int totalContents;
    private static final String PAR_USERS = "users";
    private static int users;
    private static final String PAR_ZIPF_SHAPE = "zipfShape";
    private static double zipfShape;
    private static final String PAR_PARETO_SHAPE = "paretoShape";
    private static double paretoShape;
    private static final String PAR_PARETO_MIN = "paretoMin";
    private static int paretoMin;
    private static final String PAR_PARETO_MAX = "paretoMax";
    private static int paretoMax;
    private static final String PAR_POISSON_Average = "poissonAverage";
    private static int poissonAverage;

    private static double[] failureRate;
    private static double[] magnification;
    private static double[] size;
    private static double[] popularity;
    private static int[] totalRequest;

    public Parameters(String prefix) {
        totalCycles = Configuration.getInt(prefix + "." + PAR_TOTAL_CYCLE);
        weibullShape = Configuration.getDouble(prefix + "." + PAR_WEIBULL_SHAPE);
        weibullScale = Configuration.getDouble(prefix + "." + PAR_WEIBULL_SCALE);
        totalNodes = Configuration.getInt(prefix + "." + PAR_TOTAL_NODES);
        originId = Configuration.getInt(prefix + "." + PAR_ORIGIN_ID);
        normalAverage = Configuration.getDouble(prefix + "." + PAR_NORMAL_AVERAGE);
        normalVariance = Configuration.getDouble(prefix + "." + PAR_NORMAL_VARIANCE);
        normalMin = Configuration.getInt(prefix + "." + PAR_NORMAL_MIN);
        normalMax = Configuration.getInt(prefix + "." + PAR_NORMAL_MAX);
        totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
        users = Configuration.getInt(prefix + "." + PAR_USERS);
        zipfShape = Configuration.getDouble(prefix + "." + PAR_ZIPF_SHAPE);
        paretoShape = Configuration.getDouble(prefix + "." + PAR_PARETO_SHAPE);
        paretoMin = Configuration.getInt(prefix + "." + PAR_PARETO_MIN);
        paretoMax = Configuration.getInt(prefix + "." + PAR_PARETO_MAX);
        poissonAverage = Configuration.getInt(prefix + "." + PAR_POISSON_Average);
    }

    public static void initializeDistributionModels() {
        failureRate = new double[totalCycles];
        magnification = new double[Network.size()];
        size = new double[totalContents];
        popularity = new double[totalContents];
        totalRequest = new int[totalContents];

        weibullDistribution();
        normalDistribution();
        zipfDistribution();
        paretoDistribution();
    }

    public static int[] getRequests(int contentId) {
        int startCycle = SharedData.getRandomInt(totalCycles - poissonAverage * 2);
        return poissonDistribution(contentId, startCycle);
    }

    public static int getTotalRequest(int contentId) {
        return totalRequest[contentId];
    }

    public static double getSize(int contentId) {
        return size[contentId];
    }

    public static double getPopularity(int contentId) {
        return popularity[contentId];
    }

    public static double getFailureRate(int nodeId, int position) {
        return failureRate[position] * magnification[nodeId];
    }

    private static double factorial(int src) {
        if (src == 0) {
            return ((double) 1.0);
        }
        double value = 1;
        for (int i = 1; i <= src; i++) {
            value *= i;
        }

        return ((double) value);
    }

    private static int[] setRequest(double[] cdf, int total, int startCycle, int duration) {
        int[] requests = new int[totalCycles];
        int count = 0;
        while (count < total) {
            double rand = SharedData.getRandomDouble();

            boolean flag = false;
            for (int cycle = 0; cycle <= duration * 2; cycle++) {
                if (rand < cdf[cycle]) {
                    if (cycle == 0) {
                        flag = true;
                        break;
                    }
                    requests[startCycle + cycle]++;
                    break;
                } else {
                    if (cycle == duration * 2) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                continue;
            }

            count++;
        }

        return requests;
    }

    private static int[] poissonDistribution(int contentId, int startCycle) {
        int duration = poissonAverage;
        double lambda = (double) duration;

        double[] cdf = new double[duration * 2 + 1];
        for (int cycle = 0; cycle <= duration * 2; cycle++) {
            double p = 0.0;

            for (int i = 0; i <= cycle; i++) {
                p += ((Math.pow(lambda, i)) / (factorial(i)));
            }

            cdf[cycle] = p * Math.exp(-1.0 * lambda);
        }

        return setRequest(cdf, totalRequest[contentId], startCycle, duration);
    }

    private static void setSize(double[] cdf, int sizeCount) {
        int contentId = 0;
        while (contentId < totalContents) {
            double rand = SharedData.getRandomDouble();

            for (int cdfId = 0; cdfId < sizeCount; cdfId++) {
                if (rand < cdf[cdfId]) {
                    size[contentId] = cdfId;
                    break;
                }
            }

            contentId++;
        }
    }

    private static void paretoDistribution() {
        double alpha = paretoShape;
        double min = (double) paretoMin;
        double max = (double) paretoMax + 1.0;
        int sizeCount = paretoMax + 1;

        double cdf[] = new double[sizeCount];
        for (int id = 1; id <= sizeCount; id++) {
            double x = (double) id;
            cdf[id - 1] = ((1 - Math.pow(min, alpha) * Math.pow(x, -1.0 * alpha)) / (1 - Math.pow(min / max, alpha)));
        }

        setSize(cdf, sizeCount);
    }

    private static void zipfDistribution() {
        double s = -1.0 * zipfShape;

        double denominator = 0.0;
        for (int id = 1; id <= totalContents; id++) {
            double n = (double) id;
            denominator += Math.pow(n, s);
        }

        for (int id = 1; id <= totalContents; id++) {
            double k = (double) id;
            double pdf = Math.pow(k, s) / denominator;

            popularity[id - 1] = pdf;
            totalRequest[id - 1] = (int) Math.round(pdf * ((double) users) * ((double) totalNodes - 1.0));
        }
    }

    private static int setMagnification(double pdf, int index, int value) {
        int nodeCount = (int) Math.round(pdf * ((double) totalNodes));

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

    @Override
    public boolean execute() {

        initializeDistributionModels();

        return false;
    }

}