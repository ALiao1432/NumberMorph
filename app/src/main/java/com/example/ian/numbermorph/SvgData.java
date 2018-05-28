package com.example.ian.numbermorph;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class SvgData {

    private static final String TAG = "SvgData";

    private Context context;
    private List<String> fromCmdList;
    private List<String> toCmdList;
    private DataPath morphPath;

    SvgData(Context context) {
        this.context = context;
    }

    public void setMorphRes(int fromId, int toId) {
        float[] scaleFactors = getViewport(context, fromId);
        fromCmdList = this.getPathData(context, fromId);
        toCmdList = this.getPathData(context, toId);

        morphPath = new DataPath(scaleFactors);
        morphPath.setMorphPath(fromCmdList, toCmdList);
    }

    public DataPath getMorphPath(float mFactor) {
        morphPath.getMorphPath(mFactor);
        return morphPath;
    }

    // get path for single vector drawable
    public DataPath getPath(int vdId) {
        float[] viewports = getViewport(context, vdId);
        float[] scaleFactors = {
                NumberMorphView.W_SIZE / viewports[0],
                NumberMorphView.H_SIZE / viewports[1],
        };
        List<String> data = getPathData(context, vdId);
        DataPath dPath = new DataPath(data, scaleFactors);

        dPath.setPath();
        return dPath;
    }

    private List<String> getPathData(Context context, int vdId) {
        List<String> pathData;
        XmlLabelParser xmlLabelParser = new XmlLabelParser(context, vdId);
        pathData = xmlLabelParser.getLabelData("path", "pathData");

        return pathData;
    }

    private float[] getViewport(Context context, int vdId) {
        XmlLabelParser x1 = new XmlLabelParser(context, vdId);
        XmlLabelParser x2 = new XmlLabelParser(context, vdId);

        String[] viewportStrings = {
                x1.getLabelData("vector", "viewportWidth").get(0),
                x2.getLabelData("vector", "viewportHeight").get(0)
        };

        return new float[]{
                Float.valueOf(viewportStrings[0]),
                Float.valueOf(viewportStrings[1])
        };
    }
}
