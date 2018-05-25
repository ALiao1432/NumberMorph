package com.example.ian.numbermorph;

import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class DataPath extends Path {

    private static final String TAG = "DataPath";

    private final String[] CMD_STRINGS = {
            "M",
            "C", "c",
            "L", "l",
            "H", "h",
            "V", "v",
            "Z",
    };
    private final float[] scaleFactors;
    private final List<String> pathDataList;
    private List<PointF> pointFList = new ArrayList<>();
    private PointF lastPointF = new PointF();

    DataPath(List<String> data, float[] factors) {
        scaleFactors = factors;
        pathDataList = data;

        setPathData();
    }

    private void setPathData() {
        for (String pathData : pathDataList) {
            for (String aCmd : getFullPathCmdList(getCmdPositionList(pathData), pathData)) {
                switch (aCmd.charAt(0)) {
                    case 'M':
                        addMoveToCmd(aCmd);
                        break;
                    case 'C':
                        addCubicToCmd(aCmd);
                        break;
                    case 'c':
                        addRCubicToCmd(aCmd);
                        break;
                    case 'L':
                        addLineToCmd(aCmd);
                        break;
                    case 'l':
                        addRLineToCmd(aCmd);
                        break;
                    case 'H':
                        addHtoCmd(aCmd);
                        break;
                    case 'h':
                        addRHtoCmd(aCmd);
                        break;
                    case 'V':
                        addVtoCmd(aCmd);
                        break;
                    case 'v':
                        addRVtoCmd(aCmd);
                        break;
                    case 'Z':
                        addCloseCmd();
                        break;
                }
            }
        }
    }

    private List<String> getFullPathCmdList(List<Integer> positionList, String pathData) {
        List<String> fullCmdList = new ArrayList<>();

        for (int i = 0; i < positionList.size() - 1; i++) {
            fullCmdList.add(pathData.substring(positionList.get(i), positionList.get(i + 1)));
        }
        return fullCmdList;
    }

    private List<Integer> getCmdPositionList(String pathData) {
        List<Integer> cmdPositionList = new ArrayList<>();

        for (int i = 0; i < pathData.length(); i++) {
            for (String cmd : CMD_STRINGS) {
                if (pathData.substring(i, i + 1).contains(cmd)) {
                    cmdPositionList.add(i);
                }
            }
        }
        cmdPositionList.add(pathData.length());

        return cmdPositionList;
    }

    private void addMoveToCmd(String moveCmd) {
        PointF[] movePointFS = getPointFromCmd(moveCmd);
        int size = movePointFS.length;

        for (int i = 0; i < size; i++) {
            if (i == 0) {
                this.moveTo(movePointFS[i].x, movePointFS[i].y);
            } else {
                this.lineTo(movePointFS[i].x, movePointFS[i].y);
            }
        }

        lastPointF = movePointFS[size - 1];
    }

    private void addCubicToCmd(String cubicCmd) {
        PointF[] cubicPointFS = getPointFromCmd(cubicCmd);

        this.cubicTo(
                cubicPointFS[0].x, cubicPointFS[0].y,
                cubicPointFS[1].x, cubicPointFS[1].y,
                cubicPointFS[2].x, cubicPointFS[2].y
        );
        lastPointF = cubicPointFS[2];
    }

    private void addRCubicToCmd(String rCubicCmd) {
        PointF[] rCubicPointFS = getPointFromCmd(rCubicCmd);

        this.rCubicTo(
                rCubicPointFS[0].x, rCubicPointFS[0].y,
                rCubicPointFS[1].x, rCubicPointFS[1].y,
                rCubicPointFS[2].x, rCubicPointFS[2].y
        );
        lastPointF = rCubicPointFS[2];
    }

    private void addLineToCmd(String lineCmd) {
        PointF[] linePointFS = getPointFromCmd(lineCmd);
        int size = linePointFS.length;

        for (PointF pointF : linePointFS) {
            this.lineTo(pointF.x, pointF.y);
        }
        lastPointF = linePointFS[size - 1];
    }

    private void addRLineToCmd(String rLineCmd) {
        PointF[] rLinePointFS = getPointFromCmd(rLineCmd);
        int size = rLinePointFS.length;

        for (PointF pointF : rLinePointFS) {
            this.rLineTo(pointF.x, pointF.y);
        }
        lastPointF = rLinePointFS[size - 1];
    }

    private void addHtoCmd(String hCmd) {
        PointF[] hPointFS = getHPointFromCmd(hCmd);
        int size = hPointFS.length;

        for (PointF pointF : hPointFS) {
            this.lineTo(pointF.x, pointF.y);
        }
        lastPointF = hPointFS[size - 1];
    }

    private void addRHtoCmd(String rHCmd) {
        PointF[] rHPointFS = getHPointFromCmd(rHCmd);
        int size = rHPointFS.length;

        for (PointF pointF : rHPointFS) {
            this.rLineTo(pointF.x, 0);
        }
        lastPointF = rHPointFS[size - 1];
    }

    private void addVtoCmd(String vCmd) {
        PointF[] vPointFS = getVPointFromCmd(vCmd);
        int size = vPointFS.length;

        for (PointF pointF : vPointFS) {
            this.lineTo(pointF.x, pointF.y);
        }
        lastPointF = vPointFS[size - 1];
    }

    private void addRVtoCmd(String rVCmd) {
        PointF[] rVPointFS = getVPointFromCmd(rVCmd);
        int size = rVPointFS.length;

        for (PointF pointF : rVPointFS) {
            this.rLineTo(0, pointF.y);
        }
        lastPointF = rVPointFS[size - 1];
    }

    private void addCloseCmd() {
        this.close();
    }

    private PointF[] getPointFromCmd(String cmd) {
        cmd = cmd.substring(1, cmd.length());

        String[] pointStrings = cmd.split(" ");
        PointF[] pointFS = new PointF[pointStrings.length];

        for (int i = 0; i < pointStrings.length; i++) {
            String[] xyString = pointStrings[i].split(",");
            pointFS[i] = new PointF(
                    Float.valueOf(xyString[0]) * scaleFactors[0],
                    Float.valueOf(xyString[1]) * scaleFactors[1]
            );
            pointFList.add(pointFS[i]);
        }

        return pointFS;
    }

    private PointF[] getHPointFromCmd(String cmd) {
        cmd = cmd.substring(1, cmd.length());

        String[] pointStrings = cmd.split(" ");
        PointF[] pointFS = new PointF[pointStrings.length];

        for (int i = 0; i < pointStrings.length; i++) {
            pointFS[i] = new PointF(
                    Float.valueOf(pointStrings[i]) * scaleFactors[0],
                    lastPointF.y
            );
            pointFList.add(pointFS[i]);
        }

        return pointFS;
    }

    private PointF[] getVPointFromCmd(String cmd) {
        cmd = cmd.substring(1, cmd.length());

        String[] pointStrings = cmd.split(" ");
        PointF[] pointFS = new PointF[pointStrings.length];

        for (int i = 0; i < pointStrings.length; i++) {
            pointFS[i] = new PointF(
                    lastPointF.x,
                    Float.valueOf(pointStrings[i]) * scaleFactors[1]
            );
            pointFList.add(pointFS[i]);
        }

        return pointFS;
    }
}
