package org.jfree.chart.plot;


public class ValueMarker extends org.jfree.chart.plot.Marker {
    private double value;

    public ValueMarker(double value) {
        super();
        this.value = value;
    }

    public ValueMarker(double value, java.awt.Paint paint, java.awt.Stroke stroke) {
        this(value, paint, stroke, paint, stroke, 1.0F);
    }

    public ValueMarker(double value, java.awt.Paint paint, java.awt.Stroke stroke, java.awt.Paint outlinePaint, java.awt.Stroke outlineStroke, float alpha) {
        super(paint, stroke, outlinePaint, outlineStroke, alpha);
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
        notifyListeners(new org.jfree.chart.event.MarkerChangeEvent(this));
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == (this)) {
            return true;
        }
        if (!(super.equals(obj))) {
            return false;
        }
        if (!(obj instanceof org.jfree.chart.plot.ValueMarker)) {
            return false;
        }
        org.jfree.chart.plot.ValueMarker that = ((org.jfree.chart.plot.ValueMarker) (obj));
        if ((this.value) != (that.value)) {
            return false;
        }
        return true;
    }
}

