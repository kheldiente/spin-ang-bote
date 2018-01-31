package midien.kheldiente.spinangbote.customviews;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CalcUtils {

    // 1 will put the text in the border,
    // 0 will put the text in the center. Play with this to set the distance of your text.
    private static final float PLAYER_RADIUS_BIAS = 0.7f;

    public static List<float[]> getPoints(float cx, float cy, float r, int noOfPoints) {
        // No. of points are n > 1 or n <= 8
        if(noOfPoints < 1 || noOfPoints > 8)
            noOfPoints = 1;

        List<float[]> points = new ArrayList<>(noOfPoints);
        float[] point;

        double angle;

        for(int i = 0;i < noOfPoints;i++)
        {
            angle = i * (360 / noOfPoints);
            point = new float[2];
            point[0] = (float) (cx + r * Math.cos(Math.toRadians(angle)));
            point[1] = (float) (cy + r * Math.sin(Math.toRadians(angle)));
            points.add(point);
        }
        return points;
    }

    public static List<float[]> getStartAndSweepAngles(int noOfPoints) {
        // No. of points are n > 2 or n <= 8
        if(noOfPoints < 1 || noOfPoints > 8)
            noOfPoints = 1;

        List<float[]> angles = new ArrayList<>(noOfPoints);

        for(int i = 0;i < noOfPoints;i++) {
            float[] a = new float[2]; // Index 0 is startAngle and 1 is sweepAngle

            float startAngle =  i * (360 / noOfPoints);
            float sweepAngle = 360 / noOfPoints;

            a[0] = startAngle;
            a[1] = sweepAngle;

            angles.add(a);
        }

        return angles;
    }

    public static void plotPlayer(float cx, float cy, float radius, List<PlayerView> container) {
        List<float[]> angles = getStartAndSweepAngles(container.size());

        float temp = 0;
        // 1 will put the text in the border,
        // 0 will put the text in the center. Play with this to set the distance of your text.
        radius *= PLAYER_RADIUS_BIAS;

        for(int i = 0;i < angles.size();i++) {
            float sweepAngle = angles.get(i)[1];
            if (i > 0)
                temp += sweepAngle;

            // This angle will place the text in the center of the arc.
            float medianAngle = (temp + (sweepAngle / 2f)) * (float) Math.PI / 180f; // value is in radians

            PlayerView pv = container.get(i);
            pv.name = pv.name.isEmpty() ? "Player" : pv.name;
            pv.centerX = (float)(cx + (radius * Math.cos(medianAngle)));
            pv.centerY = (float)(cy + (radius * Math.sin(medianAngle)));
            pv.median = (float) Math.toDegrees(medianAngle); // convert to degrees
        }
    }
}
