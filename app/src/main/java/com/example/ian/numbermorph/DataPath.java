package com.example.ian.numbermorph;

import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class DataPath extends Path {

    private static final String TAG = "DataPath";

    private final String[] commandString = {"M", "C", "L", "H", "V"};
    private float[] scaleFactors;
    private String pathData;
    private PointF lastPointF = new PointF();

    DataPath(String data, float[] factors) {
        scaleFactors = factors;
        pathData = data;

        setPathData();
    }

    private void setPathData() {
        for (String fullCommand : getAPathCommand(getCommandPosition(pathData))) {
            switch (fullCommand.charAt(0)) {
                case 'M':
                    addMoveToCommand(fullCommand);
                    break;
                case 'C':
                    addCubicToCommand(fullCommand);
                    break;
                case 'L':
                    addLineToCommand(fullCommand);
                    break;
                case 'H':
                    addHtoCommand(fullCommand);
                    break;
                case 'V':
                    addVtoCommand(fullCommand);
                    break;
            }
        }
    }

    private List<String> getAPathCommand(List<Integer> positionList) {
        List<String> fullCommandList = new ArrayList<>();

        for (int i = 0; i < positionList.size() - 1; i++) {
            fullCommandList.add(pathData.substring(positionList.get(i), positionList.get(i + 1)));
        }
        return fullCommandList;
    }

    private List<Integer> getCommandPosition(String s) {
        List<Integer> commandPositionList = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            for (String command : commandString) {
                if (s.substring(i, i + 1).contains(command)) {
                    commandPositionList.add(i);
                }
            }
        }
        commandPositionList.add(s.length());

        return commandPositionList;
    }

    private void addMoveToCommand(String moveCommand) {
        PointF[] movePointFS = getPointFromCommand(moveCommand);
        int size = movePointFS.length;

        for (PointF pointF : movePointFS) {
            this.moveTo(pointF.x, pointF.y);
        }
        lastPointF = movePointFS[size - 1];
    }

    private void addCubicToCommand(String cubicCommand) {
        PointF[] cubicPointFS = getPointFromCommand(cubicCommand);

        this.cubicTo(
                cubicPointFS[0].x, cubicPointFS[0].y,
                cubicPointFS[1].x, cubicPointFS[1].y,
                cubicPointFS[2].x, cubicPointFS[2].y
        );
        lastPointF = cubicPointFS[2];
    }

    private void addLineToCommand(String lineCommand) {
        PointF[] linePointFS = getPointFromCommand(lineCommand);
        int size = linePointFS.length;

        for (PointF pointF : linePointFS) {
            this.lineTo(pointF.x, pointF.y);
        }
        lastPointF = linePointFS[size - 1];
    }

    private void addHtoCommand(String hCommand) {
        PointF[] hPointFS = getHPointFromCommand(hCommand);
        int size = hPointFS.length;

        for (PointF pointF : hPointFS) {
            this.lineTo(pointF.x, pointF.y);
        }
        lastPointF = hPointFS[size - 1];
    }

    private void addVtoCommand(String vCommand) {
        PointF[] vPointFS = getVPointFromCommand(vCommand);
        for (PointF pointF : vPointFS) {
            Log.d(TAG, "point : " + pointF.x + ", " + pointF.y);
        }

        int size = vPointFS.length;

        for (PointF pointF : vPointFS) {
            this.lineTo(pointF.x, pointF.y);
        }
        lastPointF = vPointFS[size - 1];
    }

    private PointF[] getPointFromCommand(String command) {
        command = command.substring(1, command.length());

        String[] pointStrings = command.split(" ");
        PointF[] pointFS = new PointF[pointStrings.length];

        for (int i = 0; i < pointStrings.length; i++) {
            String[] xyString = pointStrings[i].split(",");
            pointFS[i] = new PointF(
                    Float.valueOf(xyString[0]) * scaleFactors[0],
                    Float.valueOf(xyString[1]) * scaleFactors[1]
            );
        }

        return pointFS;
    }

    private PointF[] getHPointFromCommand(String command) {
        command = command.substring(1, command.length());

        String[] pointStrings = command.split(" ");
        PointF[] pointFS = new PointF[pointStrings.length];

        for (int i = 0; i < pointStrings.length; i++) {
            pointFS[i] = new PointF(
                Float.valueOf(pointStrings[i]) * scaleFactors[0],
                lastPointF.y
            );
        }

        return pointFS;
    }

    private PointF[] getVPointFromCommand(String command) {
        command = command.substring(1, command.length());

        String[] pointStrings = command.split(" ");
        PointF[] pointFS = new PointF[pointStrings.length];

        for (int i = 0; i < pointStrings.length; i++) {
            pointFS[i] = new PointF(
                    lastPointF.x,
                    Float.valueOf(pointStrings[i]) * scaleFactors[1]
            );
        }

        return pointFS;
    }
}
