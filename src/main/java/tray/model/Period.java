package tray.model;

import java.util.ArrayList;

public class Period {

    private ArrayList<Week> weeks;
    private int totalDiff = 0;

    public Period() {
        this.weeks = new ArrayList<Week>();
    }

    public void clear() {
        weeks.clear();
        totalDiff = 0;
    }

    public int getTotalDiff() {
        return totalDiff;
    }

    public int getAverageDiff() {
        return totalDiff / weeks.size();
    }

    public int getWeeks() {
        return weeks.size();
    }

    public void add(Week week) {

        int index = 0;
        for (index = 0; index < weeks.size(); index++) {
            if (week.getName().compareTo(weeks.get(index).getName()) <= 0)
                break;
        }

        weeks.add(index, week);
        totalDiff += week.getActualDiff();
        //System.out.println("Diff\t"+week.getName()+"\t"+week.getActualDiff());
    }

    public Week get(Integer weekIndex) {
        return weeks.get(weekIndex);
    }

}
