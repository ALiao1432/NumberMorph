package com.example.ian.numbermorph;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

class DataPath extends Path {

    private static final String TAG = "DataPath";

    private final String[] commandString = {"M", "C", "L", "H", "V"};
    private float[] scaleFactors;
    private String pathData;

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
                    break;
                case 'H':
                    break;
                case 'V':
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

        for (PointF pointF : movePointFS) {
            this.moveTo(pointF.x, pointF.y);
        }
    }

    private void addCubicToCommand(String cubicCommand) {
        PointF[] cubicPointFS = getPointFromCommand(cubicCommand);

        this.cubicTo(
                cubicPointFS[0].x, cubicPointFS[0].y,
                cubicPointFS[1].x, cubicPointFS[1].y,
                cubicPointFS[2].x, cubicPointFS[2].y
        );
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
}
