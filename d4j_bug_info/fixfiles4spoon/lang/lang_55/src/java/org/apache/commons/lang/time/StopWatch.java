package org.apache.commons.lang.time;


public class StopWatch {
    private static final int STATE_UNSTARTED = 0;

    private static final int STATE_RUNNING = 1;

    private static final int STATE_STOPPED = 2;

    private static final int STATE_SUSPENDED = 3;

    private static final int STATE_UNSPLIT = 10;

    private static final int STATE_SPLIT = 11;

    private int runningState = org.apache.commons.lang.time.StopWatch.STATE_UNSTARTED;

    private int splitState = org.apache.commons.lang.time.StopWatch.STATE_UNSPLIT;

    private long startTime = -1;

    private long stopTime = -1;

    public StopWatch() {
        super();
    }

    public void start() {
        if ((this.runningState) == (org.apache.commons.lang.time.StopWatch.STATE_STOPPED)) {
            throw new java.lang.IllegalStateException("Stopwatch must be reset before being restarted. ");
        }
        if ((this.runningState) != (org.apache.commons.lang.time.StopWatch.STATE_UNSTARTED)) {
            throw new java.lang.IllegalStateException("Stopwatch already started. ");
        }
        stopTime = -1;
        startTime = java.lang.System.currentTimeMillis();
        this.runningState = org.apache.commons.lang.time.StopWatch.STATE_RUNNING;
    }

    public void stop() {
        if (((this.runningState) != (org.apache.commons.lang.time.StopWatch.STATE_RUNNING)) && ((this.runningState) != (org.apache.commons.lang.time.StopWatch.STATE_SUSPENDED))) {
            throw new java.lang.IllegalStateException("Stopwatch is not running. ");
        }
        if ((this.runningState) == (org.apache.commons.lang.time.StopWatch.STATE_RUNNING)) {
            stopTime = java.lang.System.currentTimeMillis();
        }
        this.runningState = org.apache.commons.lang.time.StopWatch.STATE_STOPPED;
    }

    public void reset() {
        this.runningState = org.apache.commons.lang.time.StopWatch.STATE_UNSTARTED;
        this.splitState = org.apache.commons.lang.time.StopWatch.STATE_UNSPLIT;
        startTime = -1;
        stopTime = -1;
    }

    public void split() {
        if ((this.runningState) != (org.apache.commons.lang.time.StopWatch.STATE_RUNNING)) {
            throw new java.lang.IllegalStateException("Stopwatch is not running. ");
        }
        stopTime = java.lang.System.currentTimeMillis();
        this.splitState = org.apache.commons.lang.time.StopWatch.STATE_SPLIT;
    }

    public void unsplit() {
        if ((this.splitState) != (org.apache.commons.lang.time.StopWatch.STATE_SPLIT)) {
            throw new java.lang.IllegalStateException("Stopwatch has not been split. ");
        }
        stopTime = -1;
        this.splitState = org.apache.commons.lang.time.StopWatch.STATE_UNSPLIT;
    }

    public void suspend() {
        if ((this.runningState) != (org.apache.commons.lang.time.StopWatch.STATE_RUNNING)) {
            throw new java.lang.IllegalStateException("Stopwatch must be running to suspend. ");
        }
        stopTime = java.lang.System.currentTimeMillis();
        this.runningState = org.apache.commons.lang.time.StopWatch.STATE_SUSPENDED;
    }

    public void resume() {
        if ((this.runningState) != (org.apache.commons.lang.time.StopWatch.STATE_SUSPENDED)) {
            throw new java.lang.IllegalStateException("Stopwatch must be suspended to resume. ");
        }
        startTime += (java.lang.System.currentTimeMillis()) - (stopTime);
        stopTime = -1;
        this.runningState = org.apache.commons.lang.time.StopWatch.STATE_RUNNING;
    }

    public long getTime() {
        if (((this.runningState) == (org.apache.commons.lang.time.StopWatch.STATE_STOPPED)) || ((this.runningState) == (org.apache.commons.lang.time.StopWatch.STATE_SUSPENDED))) {
            return (this.stopTime) - (this.startTime);
        }else
            if ((this.runningState) == (org.apache.commons.lang.time.StopWatch.STATE_UNSTARTED)) {
                return 0;
            }else
                if ((this.runningState) == (org.apache.commons.lang.time.StopWatch.STATE_RUNNING)) {
                    return (java.lang.System.currentTimeMillis()) - (this.startTime);
                }


        throw new java.lang.RuntimeException("Illegal running state has occured. ");
    }

    public long getSplitTime() {
        if ((this.splitState) != (org.apache.commons.lang.time.StopWatch.STATE_SPLIT)) {
            throw new java.lang.IllegalStateException("Stopwatch must be split to get the split time. ");
        }
        return (this.stopTime) - (this.startTime);
    }

    public java.lang.String toString() {
        return org.apache.commons.lang.time.DurationFormatUtils.formatDurationHMS(getTime());
    }

    public java.lang.String toSplitString() {
        return org.apache.commons.lang.time.DurationFormatUtils.formatDurationHMS(getSplitTime());
    }
}

