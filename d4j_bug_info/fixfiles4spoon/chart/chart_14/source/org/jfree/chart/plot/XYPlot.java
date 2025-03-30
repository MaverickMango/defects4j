package org.jfree.chart.plot;


public class XYPlot extends org.jfree.chart.plot.Plot implements java.io.Serializable , java.lang.Cloneable , org.jfree.chart.event.RendererChangeListener , org.jfree.chart.plot.ValueAxisPlot , org.jfree.chart.plot.Zoomable , org.jfree.chart.util.PublicCloneable {
    private static final long serialVersionUID = 7044148245716569264L;

    public static final java.awt.Stroke DEFAULT_GRIDLINE_STROKE = new java.awt.BasicStroke(0.5F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0.0F, new float[]{ 2.0F, 2.0F }, 0.0F);

    public static final java.awt.Paint DEFAULT_GRIDLINE_PAINT = java.awt.Color.WHITE;

    public static final boolean DEFAULT_CROSSHAIR_VISIBLE = false;

    public static final java.awt.Stroke DEFAULT_CROSSHAIR_STROKE = org.jfree.chart.plot.XYPlot.DEFAULT_GRIDLINE_STROKE;

    public static final java.awt.Paint DEFAULT_CROSSHAIR_PAINT = java.awt.Color.blue;

    protected static java.util.ResourceBundle localizationResources = java.util.ResourceBundle.getBundle("org.jfree.chart.plot.LocalizationBundle");

    private org.jfree.chart.plot.PlotOrientation orientation;

    private org.jfree.chart.util.RectangleInsets axisOffset;

    private org.jfree.chart.util.ObjectList domainAxes;

    private org.jfree.chart.util.ObjectList domainAxisLocations;

    private org.jfree.chart.util.ObjectList rangeAxes;

    private org.jfree.chart.util.ObjectList rangeAxisLocations;

    private org.jfree.chart.util.ObjectList datasets;

    private org.jfree.chart.util.ObjectList renderers;

    private java.util.Map datasetToDomainAxisMap;

    private java.util.Map datasetToRangeAxisMap;

    private transient java.awt.geom.Point2D quadrantOrigin = new java.awt.geom.Point2D.Double(0.0, 0.0);

    private transient java.awt.Paint[] quadrantPaint = new java.awt.Paint[]{ null, null, null, null };

    private boolean domainGridlinesVisible;

    private transient java.awt.Stroke domainGridlineStroke;

    private transient java.awt.Paint domainGridlinePaint;

    private boolean rangeGridlinesVisible;

    private transient java.awt.Stroke rangeGridlineStroke;

    private transient java.awt.Paint rangeGridlinePaint;

    private boolean domainZeroBaselineVisible;

    private transient java.awt.Stroke domainZeroBaselineStroke;

    private transient java.awt.Paint domainZeroBaselinePaint;

    private boolean rangeZeroBaselineVisible;

    private transient java.awt.Stroke rangeZeroBaselineStroke;

    private transient java.awt.Paint rangeZeroBaselinePaint;

    private boolean domainCrosshairVisible;

    private double domainCrosshairValue;

    private transient java.awt.Stroke domainCrosshairStroke;

    private transient java.awt.Paint domainCrosshairPaint;

    private boolean domainCrosshairLockedOnData = true;

    private boolean rangeCrosshairVisible;

    private double rangeCrosshairValue;

    private transient java.awt.Stroke rangeCrosshairStroke;

    private transient java.awt.Paint rangeCrosshairPaint;

    private boolean rangeCrosshairLockedOnData = true;

    private java.util.Map foregroundDomainMarkers;

    private java.util.Map backgroundDomainMarkers;

    private java.util.Map foregroundRangeMarkers;

    private java.util.Map backgroundRangeMarkers;

    private java.util.List annotations;

    private transient java.awt.Paint domainTickBandPaint;

    private transient java.awt.Paint rangeTickBandPaint;

    private org.jfree.chart.axis.AxisSpace fixedDomainAxisSpace;

    private org.jfree.chart.axis.AxisSpace fixedRangeAxisSpace;

    private org.jfree.chart.plot.DatasetRenderingOrder datasetRenderingOrder = DatasetRenderingOrder.REVERSE;

    private org.jfree.chart.plot.SeriesRenderingOrder seriesRenderingOrder = SeriesRenderingOrder.REVERSE;

    private int weight;

    private org.jfree.chart.LegendItemCollection fixedLegendItems;

    public XYPlot() {
        this(null, null, null, null);
    }

    public XYPlot(org.jfree.data.xy.XYDataset dataset, org.jfree.chart.axis.ValueAxis domainAxis, org.jfree.chart.axis.ValueAxis rangeAxis, org.jfree.chart.renderer.xy.XYItemRenderer renderer) {
        super();
        this.orientation = PlotOrientation.VERTICAL;
        this.weight = 1;
        this.axisOffset = new org.jfree.chart.util.RectangleInsets(4.0, 4.0, 4.0, 4.0);
        this.domainAxes = new org.jfree.chart.util.ObjectList();
        this.domainAxisLocations = new org.jfree.chart.util.ObjectList();
        this.foregroundDomainMarkers = new java.util.HashMap();
        this.backgroundDomainMarkers = new java.util.HashMap();
        this.rangeAxes = new org.jfree.chart.util.ObjectList();
        this.rangeAxisLocations = new org.jfree.chart.util.ObjectList();
        this.foregroundRangeMarkers = new java.util.HashMap();
        this.backgroundRangeMarkers = new java.util.HashMap();
        this.datasets = new org.jfree.chart.util.ObjectList();
        this.renderers = new org.jfree.chart.util.ObjectList();
        this.datasetToDomainAxisMap = new java.util.TreeMap();
        this.datasetToRangeAxisMap = new java.util.TreeMap();
        this.datasets.set(0, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }
        this.renderers.set(0, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }
        this.domainAxes.set(0, domainAxis);
        this.mapDatasetToDomainAxis(0, 0);
        if (domainAxis != null) {
            domainAxis.setPlot(this);
            domainAxis.addChangeListener(this);
        }
        this.domainAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);
        this.rangeAxes.set(0, rangeAxis);
        this.mapDatasetToRangeAxis(0, 0);
        if (rangeAxis != null) {
            rangeAxis.setPlot(this);
            rangeAxis.addChangeListener(this);
        }
        this.rangeAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);
        configureDomainAxes();
        configureRangeAxes();
        this.domainGridlinesVisible = true;
        this.domainGridlineStroke = org.jfree.chart.plot.XYPlot.DEFAULT_GRIDLINE_STROKE;
        this.domainGridlinePaint = org.jfree.chart.plot.XYPlot.DEFAULT_GRIDLINE_PAINT;
        this.domainZeroBaselineVisible = false;
        this.domainZeroBaselinePaint = java.awt.Color.black;
        this.domainZeroBaselineStroke = new java.awt.BasicStroke(0.5F);
        this.rangeGridlinesVisible = true;
        this.rangeGridlineStroke = org.jfree.chart.plot.XYPlot.DEFAULT_GRIDLINE_STROKE;
        this.rangeGridlinePaint = org.jfree.chart.plot.XYPlot.DEFAULT_GRIDLINE_PAINT;
        this.rangeZeroBaselineVisible = false;
        this.rangeZeroBaselinePaint = java.awt.Color.black;
        this.rangeZeroBaselineStroke = new java.awt.BasicStroke(0.5F);
        this.domainCrosshairVisible = false;
        this.domainCrosshairValue = 0.0;
        this.domainCrosshairStroke = org.jfree.chart.plot.XYPlot.DEFAULT_CROSSHAIR_STROKE;
        this.domainCrosshairPaint = org.jfree.chart.plot.XYPlot.DEFAULT_CROSSHAIR_PAINT;
        this.rangeCrosshairVisible = false;
        this.rangeCrosshairValue = 0.0;
        this.rangeCrosshairStroke = org.jfree.chart.plot.XYPlot.DEFAULT_CROSSHAIR_STROKE;
        this.rangeCrosshairPaint = org.jfree.chart.plot.XYPlot.DEFAULT_CROSSHAIR_PAINT;
        this.annotations = new java.util.ArrayList();
    }

    public java.lang.String getPlotType() {
        return org.jfree.chart.plot.XYPlot.localizationResources.getString("XY_Plot");
    }

    public org.jfree.chart.plot.PlotOrientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(org.jfree.chart.plot.PlotOrientation orientation) {
        if (orientation == null) {
            throw new java.lang.IllegalArgumentException("Null 'orientation' argument.");
        }
        if (orientation != (this.orientation)) {
            this.orientation = orientation;
            fireChangeEvent();
        }
    }

    public org.jfree.chart.util.RectangleInsets getAxisOffset() {
        return this.axisOffset;
    }

    public void setAxisOffset(org.jfree.chart.util.RectangleInsets offset) {
        if (offset == null) {
            throw new java.lang.IllegalArgumentException("Null 'offset' argument.");
        }
        this.axisOffset = offset;
        fireChangeEvent();
    }

    public org.jfree.chart.axis.ValueAxis getDomainAxis() {
        return getDomainAxis(0);
    }

    public org.jfree.chart.axis.ValueAxis getDomainAxis(int index) {
        org.jfree.chart.axis.ValueAxis result = null;
        if (index < (this.domainAxes.size())) {
            result = ((org.jfree.chart.axis.ValueAxis) (this.domainAxes.get(index)));
        }
        if (result == null) {
            org.jfree.chart.plot.Plot parent = getParent();
            if (parent instanceof org.jfree.chart.plot.XYPlot) {
                org.jfree.chart.plot.XYPlot xy = ((org.jfree.chart.plot.XYPlot) (parent));
                result = xy.getDomainAxis(index);
            }
        }
        return result;
    }

    public void setDomainAxis(org.jfree.chart.axis.ValueAxis axis) {
        setDomainAxis(0, axis);
    }

    public void setDomainAxis(int index, org.jfree.chart.axis.ValueAxis axis) {
        setDomainAxis(index, axis, true);
    }

    public void setDomainAxis(int index, org.jfree.chart.axis.ValueAxis axis, boolean notify) {
        org.jfree.chart.axis.ValueAxis existing = getDomainAxis(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        if (axis != null) {
            axis.setPlot(this);
        }
        this.domainAxes.set(index, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        if (notify) {
            fireChangeEvent();
        }
    }

    public void setDomainAxes(org.jfree.chart.axis.ValueAxis[] axes) {
        for (int i = 0; i < (axes.length); i++) {
            setDomainAxis(i, axes[i], false);
        }
        fireChangeEvent();
    }

    public org.jfree.chart.axis.AxisLocation getDomainAxisLocation() {
        return ((org.jfree.chart.axis.AxisLocation) (this.domainAxisLocations.get(0)));
    }

    public void setDomainAxisLocation(org.jfree.chart.axis.AxisLocation location) {
        setDomainAxisLocation(0, location, true);
    }

    public void setDomainAxisLocation(org.jfree.chart.axis.AxisLocation location, boolean notify) {
        setDomainAxisLocation(0, location, notify);
    }

    public org.jfree.chart.util.RectangleEdge getDomainAxisEdge() {
        return org.jfree.chart.plot.Plot.resolveDomainAxisLocation(getDomainAxisLocation(), this.orientation);
    }

    public int getDomainAxisCount() {
        return this.domainAxes.size();
    }

    public void clearDomainAxes() {
        for (int i = 0; i < (this.domainAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (this.domainAxes.get(i)));
            if (axis != null) {
                axis.removeChangeListener(this);
            }
        }
        this.domainAxes.clear();
        fireChangeEvent();
    }

    public void configureDomainAxes() {
        for (int i = 0; i < (this.domainAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (this.domainAxes.get(i)));
            if (axis != null) {
                axis.configure();
            }
        }
    }

    public org.jfree.chart.axis.AxisLocation getDomainAxisLocation(int index) {
        org.jfree.chart.axis.AxisLocation result = null;
        if (index < (this.domainAxisLocations.size())) {
            result = ((org.jfree.chart.axis.AxisLocation) (this.domainAxisLocations.get(index)));
        }
        if (result == null) {
            result = org.jfree.chart.axis.AxisLocation.getOpposite(getDomainAxisLocation());
        }
        return result;
    }

    public void setDomainAxisLocation(int index, org.jfree.chart.axis.AxisLocation location) {
        setDomainAxisLocation(index, location, true);
    }

    public void setDomainAxisLocation(int index, org.jfree.chart.axis.AxisLocation location, boolean notify) {
        if ((index == 0) && (location == null)) {
            throw new java.lang.IllegalArgumentException("Null 'location' for index 0 not permitted.");
        }
        this.domainAxisLocations.set(index, location);
        if (notify) {
            fireChangeEvent();
        }
    }

    public org.jfree.chart.util.RectangleEdge getDomainAxisEdge(int index) {
        org.jfree.chart.axis.AxisLocation location = getDomainAxisLocation(index);
        org.jfree.chart.util.RectangleEdge result = org.jfree.chart.plot.Plot.resolveDomainAxisLocation(location, this.orientation);
        if (result == null) {
            result = org.jfree.chart.util.RectangleEdge.opposite(getDomainAxisEdge());
        }
        return result;
    }

    public org.jfree.chart.axis.ValueAxis getRangeAxis() {
        return getRangeAxis(0);
    }

    public void setRangeAxis(org.jfree.chart.axis.ValueAxis axis) {
        if (axis != null) {
            axis.setPlot(this);
        }
        org.jfree.chart.axis.ValueAxis existing = getRangeAxis();
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.rangeAxes.set(0, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        fireChangeEvent();
    }

    public org.jfree.chart.axis.AxisLocation getRangeAxisLocation() {
        return ((org.jfree.chart.axis.AxisLocation) (this.rangeAxisLocations.get(0)));
    }

    public void setRangeAxisLocation(org.jfree.chart.axis.AxisLocation location) {
        setRangeAxisLocation(0, location, true);
    }

    public void setRangeAxisLocation(org.jfree.chart.axis.AxisLocation location, boolean notify) {
        setRangeAxisLocation(0, location, notify);
    }

    public org.jfree.chart.util.RectangleEdge getRangeAxisEdge() {
        return org.jfree.chart.plot.Plot.resolveRangeAxisLocation(getRangeAxisLocation(), this.orientation);
    }

    public org.jfree.chart.axis.ValueAxis getRangeAxis(int index) {
        org.jfree.chart.axis.ValueAxis result = null;
        if (index < (this.rangeAxes.size())) {
            result = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(index)));
        }
        if (result == null) {
            org.jfree.chart.plot.Plot parent = getParent();
            if (parent instanceof org.jfree.chart.plot.XYPlot) {
                org.jfree.chart.plot.XYPlot xy = ((org.jfree.chart.plot.XYPlot) (parent));
                result = xy.getRangeAxis(index);
            }
        }
        return result;
    }

    public void setRangeAxis(int index, org.jfree.chart.axis.ValueAxis axis) {
        setRangeAxis(index, axis, true);
    }

    public void setRangeAxis(int index, org.jfree.chart.axis.ValueAxis axis, boolean notify) {
        org.jfree.chart.axis.ValueAxis existing = getRangeAxis(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        if (axis != null) {
            axis.setPlot(this);
        }
        this.rangeAxes.set(index, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        if (notify) {
            fireChangeEvent();
        }
    }

    public void setRangeAxes(org.jfree.chart.axis.ValueAxis[] axes) {
        for (int i = 0; i < (axes.length); i++) {
            setRangeAxis(i, axes[i], false);
        }
        fireChangeEvent();
    }

    public int getRangeAxisCount() {
        return this.rangeAxes.size();
    }

    public void clearRangeAxes() {
        for (int i = 0; i < (this.rangeAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(i)));
            if (axis != null) {
                axis.removeChangeListener(this);
            }
        }
        this.rangeAxes.clear();
        fireChangeEvent();
    }

    public void configureRangeAxes() {
        for (int i = 0; i < (this.rangeAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(i)));
            if (axis != null) {
                axis.configure();
            }
        }
    }

    public org.jfree.chart.axis.AxisLocation getRangeAxisLocation(int index) {
        org.jfree.chart.axis.AxisLocation result = null;
        if (index < (this.rangeAxisLocations.size())) {
            result = ((org.jfree.chart.axis.AxisLocation) (this.rangeAxisLocations.get(index)));
        }
        if (result == null) {
            result = org.jfree.chart.axis.AxisLocation.getOpposite(getRangeAxisLocation());
        }
        return result;
    }

    public void setRangeAxisLocation(int index, org.jfree.chart.axis.AxisLocation location) {
        setRangeAxisLocation(index, location, true);
    }

    public void setRangeAxisLocation(int index, org.jfree.chart.axis.AxisLocation location, boolean notify) {
        if ((index == 0) && (location == null)) {
            throw new java.lang.IllegalArgumentException("Null 'location' for index 0 not permitted.");
        }
        this.rangeAxisLocations.set(index, location);
        if (notify) {
            fireChangeEvent();
        }
    }

    public org.jfree.chart.util.RectangleEdge getRangeAxisEdge(int index) {
        org.jfree.chart.axis.AxisLocation location = getRangeAxisLocation(index);
        org.jfree.chart.util.RectangleEdge result = org.jfree.chart.plot.Plot.resolveRangeAxisLocation(location, this.orientation);
        if (result == null) {
            result = org.jfree.chart.util.RectangleEdge.opposite(getRangeAxisEdge());
        }
        return result;
    }

    public org.jfree.data.xy.XYDataset getDataset() {
        return getDataset(0);
    }

    public org.jfree.data.xy.XYDataset getDataset(int index) {
        org.jfree.data.xy.XYDataset result = null;
        if ((this.datasets.size()) > index) {
            result = ((org.jfree.data.xy.XYDataset) (this.datasets.get(index)));
        }
        return result;
    }

    public void setDataset(org.jfree.data.xy.XYDataset dataset) {
        setDataset(0, dataset);
    }

    public void setDataset(int index, org.jfree.data.xy.XYDataset dataset) {
        org.jfree.data.xy.XYDataset existing = getDataset(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.datasets.set(index, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }
        org.jfree.data.general.DatasetChangeEvent event = new org.jfree.data.general.DatasetChangeEvent(this, dataset);
        datasetChanged(event);
    }

    public int getDatasetCount() {
        return this.datasets.size();
    }

    public int indexOf(org.jfree.data.xy.XYDataset dataset) {
        int result = -1;
        for (int i = 0; i < (this.datasets.size()); i++) {
            if (dataset == (this.datasets.get(i))) {
                result = i;
                break;
            }
        }
        return result;
    }

    public void mapDatasetToDomainAxis(int index, int axisIndex) {
        this.datasetToDomainAxisMap.put(new java.lang.Integer(index), new java.lang.Integer(axisIndex));
        datasetChanged(new org.jfree.data.general.DatasetChangeEvent(this, getDataset(index)));
    }

    public void mapDatasetToRangeAxis(int index, int axisIndex) {
        this.datasetToRangeAxisMap.put(new java.lang.Integer(index), new java.lang.Integer(axisIndex));
        datasetChanged(new org.jfree.data.general.DatasetChangeEvent(this, getDataset(index)));
    }

    public org.jfree.chart.renderer.xy.XYItemRenderer getRenderer() {
        return getRenderer(0);
    }

    public org.jfree.chart.renderer.xy.XYItemRenderer getRenderer(int index) {
        org.jfree.chart.renderer.xy.XYItemRenderer result = null;
        if ((this.renderers.size()) > index) {
            result = ((org.jfree.chart.renderer.xy.XYItemRenderer) (this.renderers.get(index)));
        }
        return result;
    }

    public void setRenderer(org.jfree.chart.renderer.xy.XYItemRenderer renderer) {
        setRenderer(0, renderer);
    }

    public void setRenderer(int index, org.jfree.chart.renderer.xy.XYItemRenderer renderer) {
        setRenderer(index, renderer, true);
    }

    public void setRenderer(int index, org.jfree.chart.renderer.xy.XYItemRenderer renderer, boolean notify) {
        org.jfree.chart.renderer.xy.XYItemRenderer existing = getRenderer(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.renderers.set(index, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }
        configureDomainAxes();
        configureRangeAxes();
        if (notify) {
            fireChangeEvent();
        }
    }

    public void setRenderers(org.jfree.chart.renderer.xy.XYItemRenderer[] renderers) {
        for (int i = 0; i < (renderers.length); i++) {
            setRenderer(i, renderers[i], false);
        }
        fireChangeEvent();
    }

    public org.jfree.chart.plot.DatasetRenderingOrder getDatasetRenderingOrder() {
        return this.datasetRenderingOrder;
    }

    public void setDatasetRenderingOrder(org.jfree.chart.plot.DatasetRenderingOrder order) {
        if (order == null) {
            throw new java.lang.IllegalArgumentException("Null 'order' argument.");
        }
        this.datasetRenderingOrder = order;
        fireChangeEvent();
    }

    public org.jfree.chart.plot.SeriesRenderingOrder getSeriesRenderingOrder() {
        return this.seriesRenderingOrder;
    }

    public void setSeriesRenderingOrder(org.jfree.chart.plot.SeriesRenderingOrder order) {
        if (order == null) {
            throw new java.lang.IllegalArgumentException("Null 'order' argument.");
        }
        this.seriesRenderingOrder = order;
        fireChangeEvent();
    }

    public int getIndexOf(org.jfree.chart.renderer.xy.XYItemRenderer renderer) {
        return this.renderers.indexOf(renderer);
    }

    public org.jfree.chart.renderer.xy.XYItemRenderer getRendererForDataset(org.jfree.data.xy.XYDataset dataset) {
        org.jfree.chart.renderer.xy.XYItemRenderer result = null;
        for (int i = 0; i < (this.datasets.size()); i++) {
            if ((this.datasets.get(i)) == dataset) {
                result = ((org.jfree.chart.renderer.xy.XYItemRenderer) (this.renderers.get(i)));
                if (result == null) {
                    result = getRenderer();
                }
                break;
            }
        }
        return result;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        fireChangeEvent();
    }

    public boolean isDomainGridlinesVisible() {
        return this.domainGridlinesVisible;
    }

    public void setDomainGridlinesVisible(boolean visible) {
        if ((this.domainGridlinesVisible) != visible) {
            this.domainGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    public java.awt.Stroke getDomainGridlineStroke() {
        return this.domainGridlineStroke;
    }

    public void setDomainGridlineStroke(java.awt.Stroke stroke) {
        if (stroke == null) {
            throw new java.lang.IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainGridlineStroke = stroke;
        fireChangeEvent();
    }

    public java.awt.Paint getDomainGridlinePaint() {
        return this.domainGridlinePaint;
    }

    public void setDomainGridlinePaint(java.awt.Paint paint) {
        if (paint == null) {
            throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainGridlinePaint = paint;
        fireChangeEvent();
    }

    public boolean isRangeGridlinesVisible() {
        return this.rangeGridlinesVisible;
    }

    public void setRangeGridlinesVisible(boolean visible) {
        if ((this.rangeGridlinesVisible) != visible) {
            this.rangeGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    public java.awt.Stroke getRangeGridlineStroke() {
        return this.rangeGridlineStroke;
    }

    public void setRangeGridlineStroke(java.awt.Stroke stroke) {
        if (stroke == null) {
            throw new java.lang.IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeGridlineStroke = stroke;
        fireChangeEvent();
    }

    public java.awt.Paint getRangeGridlinePaint() {
        return this.rangeGridlinePaint;
    }

    public void setRangeGridlinePaint(java.awt.Paint paint) {
        if (paint == null) {
            throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeGridlinePaint = paint;
        fireChangeEvent();
    }

    public boolean isDomainZeroBaselineVisible() {
        return this.domainZeroBaselineVisible;
    }

    public void setDomainZeroBaselineVisible(boolean visible) {
        this.domainZeroBaselineVisible = visible;
        fireChangeEvent();
    }

    public java.awt.Stroke getDomainZeroBaselineStroke() {
        return this.domainZeroBaselineStroke;
    }

    public void setDomainZeroBaselineStroke(java.awt.Stroke stroke) {
        if (stroke == null) {
            throw new java.lang.IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainZeroBaselineStroke = stroke;
        fireChangeEvent();
    }

    public java.awt.Paint getDomainZeroBaselinePaint() {
        return this.domainZeroBaselinePaint;
    }

    public void setDomainZeroBaselinePaint(java.awt.Paint paint) {
        if (paint == null) {
            throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainZeroBaselinePaint = paint;
        fireChangeEvent();
    }

    public boolean isRangeZeroBaselineVisible() {
        return this.rangeZeroBaselineVisible;
    }

    public void setRangeZeroBaselineVisible(boolean visible) {
        this.rangeZeroBaselineVisible = visible;
        fireChangeEvent();
    }

    public java.awt.Stroke getRangeZeroBaselineStroke() {
        return this.rangeZeroBaselineStroke;
    }

    public void setRangeZeroBaselineStroke(java.awt.Stroke stroke) {
        if (stroke == null) {
            throw new java.lang.IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeZeroBaselineStroke = stroke;
        fireChangeEvent();
    }

    public java.awt.Paint getRangeZeroBaselinePaint() {
        return this.rangeZeroBaselinePaint;
    }

    public void setRangeZeroBaselinePaint(java.awt.Paint paint) {
        if (paint == null) {
            throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeZeroBaselinePaint = paint;
        fireChangeEvent();
    }

    public java.awt.Paint getDomainTickBandPaint() {
        return this.domainTickBandPaint;
    }

    public void setDomainTickBandPaint(java.awt.Paint paint) {
        this.domainTickBandPaint = paint;
        fireChangeEvent();
    }

    public java.awt.Paint getRangeTickBandPaint() {
        return this.rangeTickBandPaint;
    }

    public void setRangeTickBandPaint(java.awt.Paint paint) {
        this.rangeTickBandPaint = paint;
        fireChangeEvent();
    }

    public java.awt.geom.Point2D getQuadrantOrigin() {
        return this.quadrantOrigin;
    }

    public void setQuadrantOrigin(java.awt.geom.Point2D origin) {
        if (origin == null) {
            throw new java.lang.IllegalArgumentException("Null 'origin' argument.");
        }
        this.quadrantOrigin = origin;
        fireChangeEvent();
    }

    public java.awt.Paint getQuadrantPaint(int index) {
        if ((index < 0) || (index > 3)) {
            throw new java.lang.IllegalArgumentException((("The index value (" + index) + ") should be in the range 0 to 3."));
        }
        return this.quadrantPaint[index];
    }

    public void setQuadrantPaint(int index, java.awt.Paint paint) {
        if ((index < 0) || (index > 3)) {
            throw new java.lang.IllegalArgumentException((("The index value (" + index) + ") should be in the range 0 to 3."));
        }
        this.quadrantPaint[index] = paint;
        fireChangeEvent();
    }

    public void addDomainMarker(org.jfree.chart.plot.Marker marker) {
        addDomainMarker(marker, Layer.FOREGROUND);
    }

    public void addDomainMarker(org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
        addDomainMarker(0, marker, layer);
    }

    public void clearDomainMarkers() {
        if ((this.backgroundDomainMarkers) != null) {
            java.util.Set keys = this.backgroundDomainMarkers.keySet();
            java.util.Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                java.lang.Integer key = ((java.lang.Integer) (iterator.next()));
                clearDomainMarkers(key.intValue());
            } 
            this.backgroundDomainMarkers.clear();
        }
        if ((this.foregroundDomainMarkers) != null) {
            java.util.Set keys = this.foregroundDomainMarkers.keySet();
            java.util.Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                java.lang.Integer key = ((java.lang.Integer) (iterator.next()));
                clearDomainMarkers(key.intValue());
            } 
            this.foregroundDomainMarkers.clear();
        }
        fireChangeEvent();
    }

    public void clearDomainMarkers(int index) {
        java.lang.Integer key = new java.lang.Integer(index);
        if ((this.backgroundDomainMarkers) != null) {
            java.util.Collection markers = ((java.util.Collection) (this.backgroundDomainMarkers.get(key)));
            if (markers != null) {
                java.util.Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    org.jfree.chart.plot.Marker m = ((org.jfree.chart.plot.Marker) (iterator.next()));
                    m.removeChangeListener(this);
                } 
                markers.clear();
            }
        }
        if ((this.foregroundRangeMarkers) != null) {
            java.util.Collection markers = ((java.util.Collection) (this.foregroundDomainMarkers.get(key)));
            if (markers != null) {
                java.util.Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    org.jfree.chart.plot.Marker m = ((org.jfree.chart.plot.Marker) (iterator.next()));
                    m.removeChangeListener(this);
                } 
                markers.clear();
            }
        }
        fireChangeEvent();
    }

    public void addDomainMarker(int index, org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
        addDomainMarker(index, marker, layer, true);
    }

    public void addDomainMarker(int index, org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer, boolean notify) {
        if (marker == null) {
            throw new java.lang.IllegalArgumentException("Null 'marker' not permitted.");
        }
        if (layer == null) {
            throw new java.lang.IllegalArgumentException("Null 'layer' not permitted.");
        }
        java.util.Collection markers;
        if (layer == (org.jfree.chart.util.Layer.FOREGROUND)) {
            markers = ((java.util.Collection) (this.foregroundDomainMarkers.get(new java.lang.Integer(index))));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundDomainMarkers.put(new java.lang.Integer(index), markers);
            }
            markers.add(marker);
        }else
            if (layer == (org.jfree.chart.util.Layer.BACKGROUND)) {
                markers = ((java.util.Collection) (this.backgroundDomainMarkers.get(new java.lang.Integer(index))));
                if (markers == null) {
                    markers = new java.util.ArrayList();
                    this.backgroundDomainMarkers.put(new java.lang.Integer(index), markers);
                }
                markers.add(marker);
            }

        marker.addChangeListener(this);
        if (notify) {
            fireChangeEvent();
        }
    }

    public boolean removeDomainMarker(org.jfree.chart.plot.Marker marker) {
        return removeDomainMarker(marker, Layer.FOREGROUND);
    }

    public boolean removeDomainMarker(org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
        return removeDomainMarker(0, marker, layer);
    }

    public boolean removeDomainMarker(int index, org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
        return removeDomainMarker(index, marker, layer, true);
    }

    public boolean removeDomainMarker(int index, org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer, boolean notify) {
        java.util.ArrayList markers;
        if (layer == (org.jfree.chart.util.Layer.FOREGROUND)) {
            markers = ((java.util.ArrayList) (this.foregroundDomainMarkers.get(new java.lang.Integer(index))));
        }else {
            markers = ((java.util.ArrayList) (this.backgroundDomainMarkers.get(new java.lang.Integer(index))));
        }
        if (markers == null) {
            return false;
        }
        boolean removed = markers.remove(marker);
        if (removed && notify) {
            fireChangeEvent();
        }
        return removed;
    }

    public void addRangeMarker(org.jfree.chart.plot.Marker marker) {
        addRangeMarker(marker, Layer.FOREGROUND);
    }

    public void addRangeMarker(org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
        addRangeMarker(0, marker, layer);
    }

    public void clearRangeMarkers() {
        if ((this.backgroundRangeMarkers) != null) {
            java.util.Set keys = this.backgroundRangeMarkers.keySet();
            java.util.Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                java.lang.Integer key = ((java.lang.Integer) (iterator.next()));
                clearRangeMarkers(key.intValue());
            } 
            this.backgroundRangeMarkers.clear();
        }
        if ((this.foregroundRangeMarkers) != null) {
            java.util.Set keys = this.foregroundRangeMarkers.keySet();
            java.util.Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                java.lang.Integer key = ((java.lang.Integer) (iterator.next()));
                clearRangeMarkers(key.intValue());
            } 
            this.foregroundRangeMarkers.clear();
        }
        fireChangeEvent();
    }

    public void addRangeMarker(int index, org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
        addRangeMarker(index, marker, layer, true);
    }

    public void addRangeMarker(int index, org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer, boolean notify) {
        java.util.Collection markers;
        if (layer == (org.jfree.chart.util.Layer.FOREGROUND)) {
            markers = ((java.util.Collection) (this.foregroundRangeMarkers.get(new java.lang.Integer(index))));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundRangeMarkers.put(new java.lang.Integer(index), markers);
            }
            markers.add(marker);
        }else
            if (layer == (org.jfree.chart.util.Layer.BACKGROUND)) {
                markers = ((java.util.Collection) (this.backgroundRangeMarkers.get(new java.lang.Integer(index))));
                if (markers == null) {
                    markers = new java.util.ArrayList();
                    this.backgroundRangeMarkers.put(new java.lang.Integer(index), markers);
                }
                markers.add(marker);
            }

        marker.addChangeListener(this);
        if (notify) {
            fireChangeEvent();
        }
    }

    public void clearRangeMarkers(int index) {
        java.lang.Integer key = new java.lang.Integer(index);
        if ((this.backgroundRangeMarkers) != null) {
            java.util.Collection markers = ((java.util.Collection) (this.backgroundRangeMarkers.get(key)));
            if (markers != null) {
                java.util.Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    org.jfree.chart.plot.Marker m = ((org.jfree.chart.plot.Marker) (iterator.next()));
                    m.removeChangeListener(this);
                } 
                markers.clear();
            }
        }
        if ((this.foregroundRangeMarkers) != null) {
            java.util.Collection markers = ((java.util.Collection) (this.foregroundRangeMarkers.get(key)));
            if (markers != null) {
                java.util.Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    org.jfree.chart.plot.Marker m = ((org.jfree.chart.plot.Marker) (iterator.next()));
                    m.removeChangeListener(this);
                } 
                markers.clear();
            }
        }
        fireChangeEvent();
    }

    public boolean removeRangeMarker(org.jfree.chart.plot.Marker marker) {
        return removeRangeMarker(marker, Layer.FOREGROUND);
    }

    public boolean removeRangeMarker(org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
        return removeRangeMarker(0, marker, layer);
    }

    public boolean removeRangeMarker(int index, org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
        return removeRangeMarker(index, marker, layer, true);
    }

    public boolean removeRangeMarker(int index, org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer, boolean notify) {
        if (marker == null) {
            throw new java.lang.IllegalArgumentException("Null 'marker' argument.");
        }
        java.util.ArrayList markers;
        if (layer == (org.jfree.chart.util.Layer.FOREGROUND)) {
            markers = ((java.util.ArrayList) (this.foregroundRangeMarkers.get(new java.lang.Integer(index))));
        }else {
            markers = ((java.util.ArrayList) (this.backgroundRangeMarkers.get(new java.lang.Integer(index))));
        }
        if (markers == null) {
            return false;
        }
        boolean removed = markers.remove(marker);
        if (removed && notify) {
            fireChangeEvent();
        }
        return removed;
    }

    public void addAnnotation(org.jfree.chart.annotations.XYAnnotation annotation) {
        addAnnotation(annotation, true);
    }

    public void addAnnotation(org.jfree.chart.annotations.XYAnnotation annotation, boolean notify) {
        if (annotation == null) {
            throw new java.lang.IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        if (notify) {
            fireChangeEvent();
        }
    }

    public boolean removeAnnotation(org.jfree.chart.annotations.XYAnnotation annotation) {
        return removeAnnotation(annotation, true);
    }

    public boolean removeAnnotation(org.jfree.chart.annotations.XYAnnotation annotation, boolean notify) {
        if (annotation == null) {
            throw new java.lang.IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(annotation);
        if (removed && notify) {
            fireChangeEvent();
        }
        return removed;
    }

    public java.util.List getAnnotations() {
        return new java.util.ArrayList(this.annotations);
    }

    public void clearAnnotations() {
        this.annotations.clear();
        fireChangeEvent();
    }

    protected org.jfree.chart.axis.AxisSpace calculateAxisSpace(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D plotArea) {
        org.jfree.chart.axis.AxisSpace space = new org.jfree.chart.axis.AxisSpace();
        space = calculateDomainAxisSpace(g2, plotArea, space);
        space = calculateRangeAxisSpace(g2, plotArea, space);
        return space;
    }

    protected org.jfree.chart.axis.AxisSpace calculateDomainAxisSpace(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D plotArea, org.jfree.chart.axis.AxisSpace space) {
        if (space == null) {
            space = new org.jfree.chart.axis.AxisSpace();
        }
        if ((this.fixedDomainAxisSpace) != null) {
            if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
                space.ensureAtLeast(this.fixedDomainAxisSpace.getLeft(), RectangleEdge.LEFT);
                space.ensureAtLeast(this.fixedDomainAxisSpace.getRight(), RectangleEdge.RIGHT);
            }else
                if ((this.orientation) == (PlotOrientation.VERTICAL)) {
                    space.ensureAtLeast(this.fixedDomainAxisSpace.getTop(), RectangleEdge.TOP);
                    space.ensureAtLeast(this.fixedDomainAxisSpace.getBottom(), RectangleEdge.BOTTOM);
                }

        }else {
            for (int i = 0; i < (this.domainAxes.size()); i++) {
                org.jfree.chart.axis.Axis axis = ((org.jfree.chart.axis.Axis) (this.domainAxes.get(i)));
                if (axis != null) {
                    org.jfree.chart.util.RectangleEdge edge = getDomainAxisEdge(i);
                    space = axis.reserveSpace(g2, this, plotArea, edge, space);
                }
            }
        }
        return space;
    }

    protected org.jfree.chart.axis.AxisSpace calculateRangeAxisSpace(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D plotArea, org.jfree.chart.axis.AxisSpace space) {
        if (space == null) {
            space = new org.jfree.chart.axis.AxisSpace();
        }
        if ((this.fixedRangeAxisSpace) != null) {
            if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
                space.ensureAtLeast(this.fixedRangeAxisSpace.getTop(), RectangleEdge.TOP);
                space.ensureAtLeast(this.fixedRangeAxisSpace.getBottom(), RectangleEdge.BOTTOM);
            }else
                if ((this.orientation) == (PlotOrientation.VERTICAL)) {
                    space.ensureAtLeast(this.fixedRangeAxisSpace.getLeft(), RectangleEdge.LEFT);
                    space.ensureAtLeast(this.fixedRangeAxisSpace.getRight(), RectangleEdge.RIGHT);
                }

        }else {
            for (int i = 0; i < (this.rangeAxes.size()); i++) {
                org.jfree.chart.axis.Axis axis = ((org.jfree.chart.axis.Axis) (this.rangeAxes.get(i)));
                if (axis != null) {
                    org.jfree.chart.util.RectangleEdge edge = getRangeAxisEdge(i);
                    space = axis.reserveSpace(g2, this, plotArea, edge, space);
                }
            }
        }
        return space;
    }

    public void draw(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D area, java.awt.geom.Point2D anchor, org.jfree.chart.plot.PlotState parentState, org.jfree.chart.plot.PlotRenderingInfo info) {
        boolean b1 = (area.getWidth()) <= (MINIMUM_WIDTH_TO_DRAW);
        boolean b2 = (area.getHeight()) <= (MINIMUM_HEIGHT_TO_DRAW);
        if (b1 || b2) {
            return;
        }
        if (info != null) {
            info.setPlotArea(area);
        }
        org.jfree.chart.util.RectangleInsets insets = getInsets();
        insets.trim(area);
        org.jfree.chart.axis.AxisSpace space = calculateAxisSpace(g2, area);
        java.awt.geom.Rectangle2D dataArea = space.shrink(area, null);
        this.axisOffset.trim(dataArea);
        if (info != null) {
            info.setDataArea(dataArea);
        }
        drawBackground(g2, dataArea);
        java.util.Map axisStateMap = drawAxes(g2, area, dataArea, info);
        org.jfree.chart.plot.PlotOrientation orient = getOrientation();
        if ((anchor != null) && (!(dataArea.contains(anchor)))) {
            anchor = null;
        }
        org.jfree.chart.plot.CrosshairState crosshairState = new org.jfree.chart.plot.CrosshairState();
        crosshairState.setCrosshairDistance(java.lang.Double.POSITIVE_INFINITY);
        crosshairState.setAnchor(anchor);
        crosshairState.setAnchorX(java.lang.Double.NaN);
        crosshairState.setAnchorY(java.lang.Double.NaN);
        if (anchor != null) {
            org.jfree.chart.axis.ValueAxis domainAxis = getDomainAxis();
            if (domainAxis != null) {
                double x;
                if (orient == (PlotOrientation.VERTICAL)) {
                    x = domainAxis.java2DToValue(anchor.getX(), dataArea, getDomainAxisEdge());
                }else {
                    x = domainAxis.java2DToValue(anchor.getY(), dataArea, getDomainAxisEdge());
                }
                crosshairState.setAnchorX(x);
            }
            org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxis();
            if (rangeAxis != null) {
                double y;
                if (orient == (PlotOrientation.VERTICAL)) {
                    y = rangeAxis.java2DToValue(anchor.getY(), dataArea, getRangeAxisEdge());
                }else {
                    y = rangeAxis.java2DToValue(anchor.getX(), dataArea, getRangeAxisEdge());
                }
                crosshairState.setAnchorY(y);
            }
        }
        crosshairState.setCrosshairX(getDomainCrosshairValue());
        crosshairState.setCrosshairY(getRangeCrosshairValue());
        java.awt.Shape originalClip = g2.getClip();
        java.awt.Composite originalComposite = g2.getComposite();
        g2.clip(dataArea);
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, getForegroundAlpha()));
        org.jfree.chart.axis.AxisState domainAxisState = ((org.jfree.chart.axis.AxisState) (axisStateMap.get(getDomainAxis())));
        if (domainAxisState == null) {
            if (parentState != null) {
                domainAxisState = ((org.jfree.chart.axis.AxisState) (parentState.getSharedAxisStates().get(getDomainAxis())));
            }
        }
        org.jfree.chart.axis.AxisState rangeAxisState = ((org.jfree.chart.axis.AxisState) (axisStateMap.get(getRangeAxis())));
        if (rangeAxisState == null) {
            if (parentState != null) {
                rangeAxisState = ((org.jfree.chart.axis.AxisState) (parentState.getSharedAxisStates().get(getRangeAxis())));
            }
        }
        if (domainAxisState != null) {
            drawDomainTickBands(g2, dataArea, domainAxisState.getTicks());
        }
        if (rangeAxisState != null) {
            drawRangeTickBands(g2, dataArea, rangeAxisState.getTicks());
        }
        if (domainAxisState != null) {
            drawDomainGridlines(g2, dataArea, domainAxisState.getTicks());
            drawZeroDomainBaseline(g2, dataArea);
        }
        if (rangeAxisState != null) {
            drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());
            drawZeroRangeBaseline(g2, dataArea);
        }
        for (int i = 0; i < (this.renderers.size()); i++) {
            drawDomainMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }
        for (int i = 0; i < (this.renderers.size()); i++) {
            drawRangeMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }
        boolean foundData = false;
        org.jfree.chart.plot.DatasetRenderingOrder order = getDatasetRenderingOrder();
        if (order == (DatasetRenderingOrder.FORWARD)) {
            int rendererCount = this.renderers.size();
            for (int i = 0; i < rendererCount; i++) {
                org.jfree.chart.renderer.xy.XYItemRenderer r = getRenderer(i);
                if (r != null) {
                    org.jfree.chart.axis.ValueAxis domainAxis = getDomainAxisForDataset(i);
                    org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.BACKGROUND, info);
                }
            }
            for (int i = 0; i < (getDatasetCount()); i++) {
                foundData = (render(g2, dataArea, i, info, crosshairState)) || foundData;
            }
            for (int i = 0; i < rendererCount; i++) {
                org.jfree.chart.renderer.xy.XYItemRenderer r = getRenderer(i);
                if (r != null) {
                    org.jfree.chart.axis.ValueAxis domainAxis = getDomainAxisForDataset(i);
                    org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.FOREGROUND, info);
                }
            }
        }else
            if (order == (DatasetRenderingOrder.REVERSE)) {
                int rendererCount = this.renderers.size();
                for (int i = rendererCount - 1; i >= 0; i--) {
                    org.jfree.chart.renderer.xy.XYItemRenderer r = getRenderer(i);
                    if (i >= (getDatasetCount())) {
                        continue;
                    }
                    if (r != null) {
                        org.jfree.chart.axis.ValueAxis domainAxis = getDomainAxisForDataset(i);
                        org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxisForDataset(i);
                        r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.BACKGROUND, info);
                    }
                }
                for (int i = (getDatasetCount()) - 1; i >= 0; i--) {
                    foundData = (render(g2, dataArea, i, info, crosshairState)) || foundData;
                }
                for (int i = rendererCount - 1; i >= 0; i--) {
                    org.jfree.chart.renderer.xy.XYItemRenderer r = getRenderer(i);
                    if (i >= (getDatasetCount())) {
                        continue;
                    }
                    if (r != null) {
                        org.jfree.chart.axis.ValueAxis domainAxis = getDomainAxisForDataset(i);
                        org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxisForDataset(i);
                        r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.FOREGROUND, info);
                    }
                }
            }

        int xAxisIndex = crosshairState.getDomainAxisIndex();
        org.jfree.chart.axis.ValueAxis xAxis = getDomainAxis(xAxisIndex);
        org.jfree.chart.util.RectangleEdge xAxisEdge = getDomainAxisEdge(xAxisIndex);
        if ((!(this.domainCrosshairLockedOnData)) && (anchor != null)) {
            double xx;
            if (orient == (PlotOrientation.VERTICAL)) {
                xx = xAxis.java2DToValue(anchor.getX(), dataArea, xAxisEdge);
            }else {
                xx = xAxis.java2DToValue(anchor.getY(), dataArea, xAxisEdge);
            }
            crosshairState.setCrosshairX(xx);
        }
        setDomainCrosshairValue(crosshairState.getCrosshairX(), false);
        if (isDomainCrosshairVisible()) {
            double x = getDomainCrosshairValue();
            java.awt.Paint paint = getDomainCrosshairPaint();
            java.awt.Stroke stroke = getDomainCrosshairStroke();
            drawDomainCrosshair(g2, dataArea, orient, x, xAxis, stroke, paint);
        }
        int yAxisIndex = crosshairState.getRangeAxisIndex();
        org.jfree.chart.axis.ValueAxis yAxis = getRangeAxis(yAxisIndex);
        org.jfree.chart.util.RectangleEdge yAxisEdge = getRangeAxisEdge(yAxisIndex);
        if ((!(this.rangeCrosshairLockedOnData)) && (anchor != null)) {
            double yy;
            if (orient == (PlotOrientation.VERTICAL)) {
                yy = yAxis.java2DToValue(anchor.getY(), dataArea, yAxisEdge);
            }else {
                yy = yAxis.java2DToValue(anchor.getX(), dataArea, yAxisEdge);
            }
            crosshairState.setCrosshairY(yy);
        }
        setRangeCrosshairValue(crosshairState.getCrosshairY(), false);
        if (isRangeCrosshairVisible()) {
            double y = getRangeCrosshairValue();
            java.awt.Paint paint = getRangeCrosshairPaint();
            java.awt.Stroke stroke = getRangeCrosshairStroke();
            drawRangeCrosshair(g2, dataArea, orient, y, yAxis, stroke, paint);
        }
        if (!foundData) {
            drawNoDataMessage(g2, dataArea);
        }
        for (int i = 0; i < (this.renderers.size()); i++) {
            drawDomainMarkers(g2, dataArea, i, Layer.FOREGROUND);
        }
        for (int i = 0; i < (this.renderers.size()); i++) {
            drawRangeMarkers(g2, dataArea, i, Layer.FOREGROUND);
        }
        drawAnnotations(g2, dataArea, info);
        g2.setClip(originalClip);
        g2.setComposite(originalComposite);
        drawOutline(g2, dataArea);
    }

    public void drawBackground(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D area) {
        fillBackground(g2, area, this.orientation);
        drawQuadrants(g2, area);
        drawBackgroundImage(g2, area);
    }

    protected void drawQuadrants(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D area) {
        boolean somethingToDraw = false;
        org.jfree.chart.axis.ValueAxis xAxis = getDomainAxis();
        double x = xAxis.getRange().constrain(this.quadrantOrigin.getX());
        double xx = xAxis.valueToJava2D(x, area, getDomainAxisEdge());
        org.jfree.chart.axis.ValueAxis yAxis = getRangeAxis();
        double y = yAxis.getRange().constrain(this.quadrantOrigin.getY());
        double yy = yAxis.valueToJava2D(y, area, getRangeAxisEdge());
        double xmin = xAxis.getLowerBound();
        double xxmin = xAxis.valueToJava2D(xmin, area, getDomainAxisEdge());
        double xmax = xAxis.getUpperBound();
        double xxmax = xAxis.valueToJava2D(xmax, area, getDomainAxisEdge());
        double ymin = yAxis.getLowerBound();
        double yymin = yAxis.valueToJava2D(ymin, area, getRangeAxisEdge());
        double ymax = yAxis.getUpperBound();
        double yymax = yAxis.valueToJava2D(ymax, area, getRangeAxisEdge());
        java.awt.geom.Rectangle2D[] r = new java.awt.geom.Rectangle2D[]{ null, null, null, null };
        if ((this.quadrantPaint[0]) != null) {
            if ((x > xmin) && (y < ymax)) {
                if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
                    r[0] = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(yymax, yy), java.lang.Math.min(xxmin, xx), java.lang.Math.abs((yy - yymax)), java.lang.Math.abs((xx - xxmin)));
                }else {
                    r[0] = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(xxmin, xx), java.lang.Math.min(yymax, yy), java.lang.Math.abs((xx - xxmin)), java.lang.Math.abs((yy - yymax)));
                }
                somethingToDraw = true;
            }
        }
        if ((this.quadrantPaint[1]) != null) {
            if ((x < xmax) && (y < ymax)) {
                if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
                    r[1] = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(yymax, yy), java.lang.Math.min(xxmax, xx), java.lang.Math.abs((yy - yymax)), java.lang.Math.abs((xx - xxmax)));
                }else {
                    r[1] = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(xx, xxmax), java.lang.Math.min(yymax, yy), java.lang.Math.abs((xx - xxmax)), java.lang.Math.abs((yy - yymax)));
                }
                somethingToDraw = true;
            }
        }
        if ((this.quadrantPaint[2]) != null) {
            if ((x > xmin) && (y > ymin)) {
                if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
                    r[2] = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(yymin, yy), java.lang.Math.min(xxmin, xx), java.lang.Math.abs((yy - yymin)), java.lang.Math.abs((xx - xxmin)));
                }else {
                    r[2] = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(xxmin, xx), java.lang.Math.min(yymin, yy), java.lang.Math.abs((xx - xxmin)), java.lang.Math.abs((yy - yymin)));
                }
                somethingToDraw = true;
            }
        }
        if ((this.quadrantPaint[3]) != null) {
            if ((x < xmax) && (y > ymin)) {
                if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
                    r[3] = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(yymin, yy), java.lang.Math.min(xxmax, xx), java.lang.Math.abs((yy - yymin)), java.lang.Math.abs((xx - xxmax)));
                }else {
                    r[3] = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(xx, xxmax), java.lang.Math.min(yymin, yy), java.lang.Math.abs((xx - xxmax)), java.lang.Math.abs((yy - yymin)));
                }
                somethingToDraw = true;
            }
        }
        if (somethingToDraw) {
            java.awt.Composite originalComposite = g2.getComposite();
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, getBackgroundAlpha()));
            for (int i = 0; i < 4; i++) {
                if (((this.quadrantPaint[i]) != null) && ((r[i]) != null)) {
                    g2.setPaint(this.quadrantPaint[i]);
                    g2.fill(r[i]);
                }
            }
            g2.setComposite(originalComposite);
        }
    }

    public void drawDomainTickBands(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, java.util.List ticks) {
        java.awt.Paint bandPaint = getDomainTickBandPaint();
        if (bandPaint != null) {
            boolean fillBand = false;
            org.jfree.chart.axis.ValueAxis xAxis = getDomainAxis();
            double previous = xAxis.getLowerBound();
            java.util.Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                org.jfree.chart.axis.ValueTick tick = ((org.jfree.chart.axis.ValueTick) (iterator.next()));
                double current = tick.getValue();
                if (fillBand) {
                    getRenderer().fillDomainGridBand(g2, this, xAxis, dataArea, previous, current);
                }
                previous = current;
                fillBand = !fillBand;
            } 
            double end = xAxis.getUpperBound();
            if (fillBand) {
                getRenderer().fillDomainGridBand(g2, this, xAxis, dataArea, previous, end);
            }
        }
    }

    public void drawRangeTickBands(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, java.util.List ticks) {
        java.awt.Paint bandPaint = getRangeTickBandPaint();
        if (bandPaint != null) {
            boolean fillBand = false;
            org.jfree.chart.axis.ValueAxis axis = getRangeAxis();
            double previous = axis.getLowerBound();
            java.util.Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                org.jfree.chart.axis.ValueTick tick = ((org.jfree.chart.axis.ValueTick) (iterator.next()));
                double current = tick.getValue();
                if (fillBand) {
                    getRenderer().fillRangeGridBand(g2, this, axis, dataArea, previous, current);
                }
                previous = current;
                fillBand = !fillBand;
            } 
            double end = axis.getUpperBound();
            if (fillBand) {
                getRenderer().fillRangeGridBand(g2, this, axis, dataArea, previous, end);
            }
        }
    }

    protected java.util.Map drawAxes(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D plotArea, java.awt.geom.Rectangle2D dataArea, org.jfree.chart.plot.PlotRenderingInfo plotState) {
        org.jfree.chart.axis.AxisCollection axisCollection = new org.jfree.chart.axis.AxisCollection();
        for (int index = 0; index < (this.domainAxes.size()); index++) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (this.domainAxes.get(index)));
            if (axis != null) {
                axisCollection.add(axis, getDomainAxisEdge(index));
            }
        }
        for (int index = 0; index < (this.rangeAxes.size()); index++) {
            org.jfree.chart.axis.ValueAxis yAxis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(index)));
            if (yAxis != null) {
                axisCollection.add(yAxis, getRangeAxisEdge(index));
            }
        }
        java.util.Map axisStateMap = new java.util.HashMap();
        double cursor = (dataArea.getMinY()) - (this.axisOffset.calculateTopOutset(dataArea.getHeight()));
        java.util.Iterator iterator = axisCollection.getAxesAtTop().iterator();
        while (iterator.hasNext()) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (iterator.next()));
            org.jfree.chart.axis.AxisState info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.TOP, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        } 
        cursor = (dataArea.getMaxY()) + (this.axisOffset.calculateBottomOutset(dataArea.getHeight()));
        iterator = axisCollection.getAxesAtBottom().iterator();
        while (iterator.hasNext()) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (iterator.next()));
            org.jfree.chart.axis.AxisState info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.BOTTOM, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        } 
        cursor = (dataArea.getMinX()) - (this.axisOffset.calculateLeftOutset(dataArea.getWidth()));
        iterator = axisCollection.getAxesAtLeft().iterator();
        while (iterator.hasNext()) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (iterator.next()));
            org.jfree.chart.axis.AxisState info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.LEFT, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        } 
        cursor = (dataArea.getMaxX()) + (this.axisOffset.calculateRightOutset(dataArea.getWidth()));
        iterator = axisCollection.getAxesAtRight().iterator();
        while (iterator.hasNext()) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (iterator.next()));
            org.jfree.chart.axis.AxisState info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.RIGHT, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        } 
        return axisStateMap;
    }

    public boolean render(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, int index, org.jfree.chart.plot.PlotRenderingInfo info, org.jfree.chart.plot.CrosshairState crosshairState) {
        boolean foundData = false;
        org.jfree.data.xy.XYDataset dataset = getDataset(index);
        if (!(org.jfree.data.general.DatasetUtilities.isEmptyOrNull(dataset))) {
            foundData = true;
            org.jfree.chart.axis.ValueAxis xAxis = getDomainAxisForDataset(index);
            org.jfree.chart.axis.ValueAxis yAxis = getRangeAxisForDataset(index);
            org.jfree.chart.renderer.xy.XYItemRenderer renderer = getRenderer(index);
            if (renderer == null) {
                renderer = getRenderer();
                if (renderer == null) {
                    return foundData;
                }
            }
            org.jfree.chart.renderer.xy.XYItemRendererState state = renderer.initialise(g2, dataArea, this, dataset, info);
            int passCount = renderer.getPassCount();
            org.jfree.chart.plot.SeriesRenderingOrder seriesOrder = getSeriesRenderingOrder();
            if (seriesOrder == (SeriesRenderingOrder.REVERSE)) {
                for (int pass = 0; pass < passCount; pass++) {
                    int seriesCount = dataset.getSeriesCount();
                    for (int series = seriesCount - 1; series >= 0; series--) {
                        int firstItem = 0;
                        int lastItem = (dataset.getItemCount(series)) - 1;
                        if (lastItem == (-1)) {
                            continue;
                        }
                        if (state.getProcessVisibleItemsOnly()) {
                            int[] itemBounds = org.jfree.chart.renderer.RendererUtilities.findLiveItems(dataset, series, xAxis.getLowerBound(), xAxis.getUpperBound());
                            firstItem = itemBounds[0];
                            lastItem = itemBounds[1];
                        }
                        for (int item = firstItem; item <= lastItem; item++) {
                            renderer.drawItem(g2, state, dataArea, info, this, xAxis, yAxis, dataset, series, item, crosshairState, pass);
                        }
                    }
                }
            }else {
                for (int pass = 0; pass < passCount; pass++) {
                    int seriesCount = dataset.getSeriesCount();
                    for (int series = 0; series < seriesCount; series++) {
                        int firstItem = 0;
                        int lastItem = (dataset.getItemCount(series)) - 1;
                        if (state.getProcessVisibleItemsOnly()) {
                            int[] itemBounds = org.jfree.chart.renderer.RendererUtilities.findLiveItems(dataset, series, xAxis.getLowerBound(), xAxis.getUpperBound());
                            firstItem = itemBounds[0];
                            lastItem = itemBounds[1];
                        }
                        for (int item = firstItem; item <= lastItem; item++) {
                            renderer.drawItem(g2, state, dataArea, info, this, xAxis, yAxis, dataset, series, item, crosshairState, pass);
                        }
                    }
                }
            }
        }
        return foundData;
    }

    public org.jfree.chart.axis.ValueAxis getDomainAxisForDataset(int index) {
        if ((index < 0) || (index >= (getDatasetCount()))) {
            throw new java.lang.IllegalArgumentException((("Index " + index) + " out of bounds."));
        }
        org.jfree.chart.axis.ValueAxis valueAxis = null;
        java.lang.Integer axisIndex = ((java.lang.Integer) (this.datasetToDomainAxisMap.get(new java.lang.Integer(index))));
        if (axisIndex != null) {
            valueAxis = getDomainAxis(axisIndex.intValue());
        }else {
            valueAxis = getDomainAxis(0);
        }
        return valueAxis;
    }

    public org.jfree.chart.axis.ValueAxis getRangeAxisForDataset(int index) {
        if ((index < 0) || (index >= (getDatasetCount()))) {
            throw new java.lang.IllegalArgumentException((("Index " + index) + " out of bounds."));
        }
        org.jfree.chart.axis.ValueAxis valueAxis = null;
        java.lang.Integer axisIndex = ((java.lang.Integer) (this.datasetToRangeAxisMap.get(new java.lang.Integer(index))));
        if (axisIndex != null) {
            valueAxis = getRangeAxis(axisIndex.intValue());
        }else {
            valueAxis = getRangeAxis(0);
        }
        return valueAxis;
    }

    protected void drawDomainGridlines(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, java.util.List ticks) {
        if ((getRenderer()) == null) {
            return;
        }
        if (isDomainGridlinesVisible()) {
            java.awt.Stroke gridStroke = getDomainGridlineStroke();
            java.awt.Paint gridPaint = getDomainGridlinePaint();
            java.util.Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                org.jfree.chart.axis.ValueTick tick = ((org.jfree.chart.axis.ValueTick) (iterator.next()));
                getRenderer().drawDomainLine(g2, this, getDomainAxis(), dataArea, tick.getValue(), gridPaint, gridStroke);
            } 
        }
    }

    protected void drawRangeGridlines(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D area, java.util.List ticks) {
        if ((getRenderer()) == null) {
            return;
        }
        if (isRangeGridlinesVisible()) {
            java.awt.Stroke gridStroke = getRangeGridlineStroke();
            java.awt.Paint gridPaint = getRangeGridlinePaint();
            org.jfree.chart.axis.ValueAxis axis = getRangeAxis();
            if (axis != null) {
                java.util.Iterator iterator = ticks.iterator();
                while (iterator.hasNext()) {
                    org.jfree.chart.axis.ValueTick tick = ((org.jfree.chart.axis.ValueTick) (iterator.next()));
                    if (((tick.getValue()) != 0.0) || (!(isRangeZeroBaselineVisible()))) {
                        getRenderer().drawRangeLine(g2, this, getRangeAxis(), area, tick.getValue(), gridPaint, gridStroke);
                    }
                } 
            }
        }
    }

    protected void drawZeroDomainBaseline(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D area) {
        if (isDomainZeroBaselineVisible()) {
            org.jfree.chart.renderer.xy.XYItemRenderer r = getRenderer();
            r.drawDomainLine(g2, this, getDomainAxis(), area, 0.0, this.domainZeroBaselinePaint, this.domainZeroBaselineStroke);
        }
    }

    protected void drawZeroRangeBaseline(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D area) {
        if (isRangeZeroBaselineVisible()) {
            getRenderer().drawRangeLine(g2, this, getRangeAxis(), area, 0.0, this.rangeZeroBaselinePaint, this.rangeZeroBaselineStroke);
        }
    }

    public void drawAnnotations(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, org.jfree.chart.plot.PlotRenderingInfo info) {
        java.util.Iterator iterator = this.annotations.iterator();
        while (iterator.hasNext()) {
            org.jfree.chart.annotations.XYAnnotation annotation = ((org.jfree.chart.annotations.XYAnnotation) (iterator.next()));
            org.jfree.chart.axis.ValueAxis xAxis = getDomainAxis();
            org.jfree.chart.axis.ValueAxis yAxis = getRangeAxis();
            annotation.draw(g2, this, dataArea, xAxis, yAxis, 0, info);
        } 
    }

    protected void drawDomainMarkers(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, int index, org.jfree.chart.util.Layer layer) {
        org.jfree.chart.renderer.xy.XYItemRenderer r = getRenderer(index);
        if (r == null) {
            return;
        }
        if (index >= (getDatasetCount())) {
            return;
        }
        java.util.Collection markers = getDomainMarkers(index, layer);
        org.jfree.chart.axis.ValueAxis axis = getDomainAxisForDataset(index);
        if ((markers != null) && (axis != null)) {
            java.util.Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                org.jfree.chart.plot.Marker marker = ((org.jfree.chart.plot.Marker) (iterator.next()));
                r.drawDomainMarker(g2, this, axis, marker, dataArea);
            } 
        }
    }

    protected void drawRangeMarkers(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, int index, org.jfree.chart.util.Layer layer) {
        org.jfree.chart.renderer.xy.XYItemRenderer r = getRenderer(index);
        if (r == null) {
            return;
        }
        if (index >= (getDatasetCount())) {
            return;
        }
        java.util.Collection markers = getRangeMarkers(index, layer);
        org.jfree.chart.axis.ValueAxis axis = getRangeAxisForDataset(index);
        if ((markers != null) && (axis != null)) {
            java.util.Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                org.jfree.chart.plot.Marker marker = ((org.jfree.chart.plot.Marker) (iterator.next()));
                r.drawRangeMarker(g2, this, axis, marker, dataArea);
            } 
        }
    }

    public java.util.Collection getDomainMarkers(org.jfree.chart.util.Layer layer) {
        return getDomainMarkers(0, layer);
    }

    public java.util.Collection getRangeMarkers(org.jfree.chart.util.Layer layer) {
        return getRangeMarkers(0, layer);
    }

    public java.util.Collection getDomainMarkers(int index, org.jfree.chart.util.Layer layer) {
        java.util.Collection result = null;
        java.lang.Integer key = new java.lang.Integer(index);
        if (layer == (org.jfree.chart.util.Layer.FOREGROUND)) {
            result = ((java.util.Collection) (this.foregroundDomainMarkers.get(key)));
        }else
            if (layer == (org.jfree.chart.util.Layer.BACKGROUND)) {
                result = ((java.util.Collection) (this.backgroundDomainMarkers.get(key)));
            }

        if (result != null) {
            result = java.util.Collections.unmodifiableCollection(result);
        }
        return result;
    }

    public java.util.Collection getRangeMarkers(int index, org.jfree.chart.util.Layer layer) {
        java.util.Collection result = null;
        java.lang.Integer key = new java.lang.Integer(index);
        if (layer == (org.jfree.chart.util.Layer.FOREGROUND)) {
            result = ((java.util.Collection) (this.foregroundRangeMarkers.get(key)));
        }else
            if (layer == (org.jfree.chart.util.Layer.BACKGROUND)) {
                result = ((java.util.Collection) (this.backgroundRangeMarkers.get(key)));
            }

        if (result != null) {
            result = java.util.Collections.unmodifiableCollection(result);
        }
        return result;
    }

    protected void drawHorizontalLine(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, double value, java.awt.Stroke stroke, java.awt.Paint paint) {
        org.jfree.chart.axis.ValueAxis axis = getRangeAxis();
        if ((getOrientation()) == (PlotOrientation.HORIZONTAL)) {
            axis = getDomainAxis();
        }
        if (axis.getRange().contains(value)) {
            double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
            java.awt.geom.Line2D line = new java.awt.geom.Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }

    protected void drawDomainCrosshair(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, org.jfree.chart.plot.PlotOrientation orientation, double value, org.jfree.chart.axis.ValueAxis axis, java.awt.Stroke stroke, java.awt.Paint paint) {
        if (axis.getRange().contains(value)) {
            java.awt.geom.Line2D line = null;
            if (orientation == (PlotOrientation.VERTICAL)) {
                double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
                line = new java.awt.geom.Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
            }else {
                double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
                line = new java.awt.geom.Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
            }
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }

    protected void drawVerticalLine(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, double value, java.awt.Stroke stroke, java.awt.Paint paint) {
        org.jfree.chart.axis.ValueAxis axis = getDomainAxis();
        if ((getOrientation()) == (PlotOrientation.HORIZONTAL)) {
            axis = getRangeAxis();
        }
        if (axis.getRange().contains(value)) {
            double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
            java.awt.geom.Line2D line = new java.awt.geom.Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }

    protected void drawRangeCrosshair(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, org.jfree.chart.plot.PlotOrientation orientation, double value, org.jfree.chart.axis.ValueAxis axis, java.awt.Stroke stroke, java.awt.Paint paint) {
        if (axis.getRange().contains(value)) {
            java.awt.geom.Line2D line = null;
            if (orientation == (PlotOrientation.HORIZONTAL)) {
                double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
                line = new java.awt.geom.Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
            }else {
                double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
                line = new java.awt.geom.Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
            }
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }

    public void handleClick(int x, int y, org.jfree.chart.plot.PlotRenderingInfo info) {
        java.awt.geom.Rectangle2D dataArea = info.getDataArea();
        if (dataArea.contains(x, y)) {
            org.jfree.chart.axis.ValueAxis da = getDomainAxis();
            if (da != null) {
                double hvalue = da.java2DToValue(x, info.getDataArea(), getDomainAxisEdge());
                setDomainCrosshairValue(hvalue);
            }
            org.jfree.chart.axis.ValueAxis ra = getRangeAxis();
            if (ra != null) {
                double vvalue = ra.java2DToValue(y, info.getDataArea(), getRangeAxisEdge());
                setRangeCrosshairValue(vvalue);
            }
        }
    }

    private java.util.List getDatasetsMappedToDomainAxis(java.lang.Integer axisIndex) {
        if (axisIndex == null) {
            throw new java.lang.IllegalArgumentException("Null 'axisIndex' argument.");
        }
        java.util.List result = new java.util.ArrayList();
        for (int i = 0; i < (this.datasets.size()); i++) {
            java.lang.Integer mappedAxis = ((java.lang.Integer) (this.datasetToDomainAxisMap.get(new java.lang.Integer(i))));
            if (mappedAxis == null) {
                if (axisIndex.equals(org.jfree.chart.plot.ZERO)) {
                    result.add(this.datasets.get(i));
                }
            }else {
                if (mappedAxis.equals(axisIndex)) {
                    result.add(this.datasets.get(i));
                }
            }
        }
        return result;
    }

    private java.util.List getDatasetsMappedToRangeAxis(java.lang.Integer axisIndex) {
        if (axisIndex == null) {
            throw new java.lang.IllegalArgumentException("Null 'axisIndex' argument.");
        }
        java.util.List result = new java.util.ArrayList();
        for (int i = 0; i < (this.datasets.size()); i++) {
            java.lang.Integer mappedAxis = ((java.lang.Integer) (this.datasetToRangeAxisMap.get(new java.lang.Integer(i))));
            if (mappedAxis == null) {
                if (axisIndex.equals(org.jfree.chart.plot.ZERO)) {
                    result.add(this.datasets.get(i));
                }
            }else {
                if (mappedAxis.equals(axisIndex)) {
                    result.add(this.datasets.get(i));
                }
            }
        }
        return result;
    }

    public int getDomainAxisIndex(org.jfree.chart.axis.ValueAxis axis) {
        int result = this.domainAxes.indexOf(axis);
        if (result < 0) {
            org.jfree.chart.plot.Plot parent = getParent();
            if (parent instanceof org.jfree.chart.plot.XYPlot) {
                org.jfree.chart.plot.XYPlot p = ((org.jfree.chart.plot.XYPlot) (parent));
                result = p.getDomainAxisIndex(axis);
            }
        }
        return result;
    }

    public int getRangeAxisIndex(org.jfree.chart.axis.ValueAxis axis) {
        int result = this.rangeAxes.indexOf(axis);
        if (result < 0) {
            org.jfree.chart.plot.Plot parent = getParent();
            if (parent instanceof org.jfree.chart.plot.XYPlot) {
                org.jfree.chart.plot.XYPlot p = ((org.jfree.chart.plot.XYPlot) (parent));
                result = p.getRangeAxisIndex(axis);
            }
        }
        return result;
    }

    public org.jfree.data.Range getDataRange(org.jfree.chart.axis.ValueAxis axis) {
        org.jfree.data.Range result = null;
        java.util.List mappedDatasets = new java.util.ArrayList();
        boolean isDomainAxis = true;
        int domainIndex = getDomainAxisIndex(axis);
        if (domainIndex >= 0) {
            isDomainAxis = true;
            mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new java.lang.Integer(domainIndex)));
        }
        int rangeIndex = getRangeAxisIndex(axis);
        if (rangeIndex >= 0) {
            isDomainAxis = false;
            mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new java.lang.Integer(rangeIndex)));
        }
        java.util.Iterator iterator = mappedDatasets.iterator();
        while (iterator.hasNext()) {
            org.jfree.data.xy.XYDataset d = ((org.jfree.data.xy.XYDataset) (iterator.next()));
            if (d != null) {
                org.jfree.chart.renderer.xy.XYItemRenderer r = getRendererForDataset(d);
                if (isDomainAxis) {
                    if (r != null) {
                        result = org.jfree.data.Range.combine(result, r.findDomainBounds(d));
                    }else {
                        result = org.jfree.data.Range.combine(result, org.jfree.data.general.DatasetUtilities.findDomainBounds(d));
                    }
                }else {
                    if (r != null) {
                        result = org.jfree.data.Range.combine(result, r.findRangeBounds(d));
                    }else {
                        result = org.jfree.data.Range.combine(result, org.jfree.data.general.DatasetUtilities.findRangeBounds(d));
                    }
                }
            }
        } 
        return result;
    }

    public void datasetChanged(org.jfree.data.general.DatasetChangeEvent event) {
        configureDomainAxes();
        configureRangeAxes();
        if ((getParent()) != null) {
            getParent().datasetChanged(event);
        }else {
            org.jfree.chart.event.PlotChangeEvent e = new org.jfree.chart.event.PlotChangeEvent(this);
            e.setType(ChartChangeEventType.DATASET_UPDATED);
            notifyListeners(e);
        }
    }

    public void rendererChanged(org.jfree.chart.event.RendererChangeEvent event) {
        fireChangeEvent();
    }

    public boolean isDomainCrosshairVisible() {
        return this.domainCrosshairVisible;
    }

    public void setDomainCrosshairVisible(boolean flag) {
        if ((this.domainCrosshairVisible) != flag) {
            this.domainCrosshairVisible = flag;
            fireChangeEvent();
        }
    }

    public boolean isDomainCrosshairLockedOnData() {
        return this.domainCrosshairLockedOnData;
    }

    public void setDomainCrosshairLockedOnData(boolean flag) {
        if ((this.domainCrosshairLockedOnData) != flag) {
            this.domainCrosshairLockedOnData = flag;
            fireChangeEvent();
        }
    }

    public double getDomainCrosshairValue() {
        return this.domainCrosshairValue;
    }

    public void setDomainCrosshairValue(double value) {
        setDomainCrosshairValue(value, true);
    }

    public void setDomainCrosshairValue(double value, boolean notify) {
        this.domainCrosshairValue = value;
        if ((isDomainCrosshairVisible()) && notify) {
            fireChangeEvent();
        }
    }

    public java.awt.Stroke getDomainCrosshairStroke() {
        return this.domainCrosshairStroke;
    }

    public void setDomainCrosshairStroke(java.awt.Stroke stroke) {
        if (stroke == null) {
            throw new java.lang.IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainCrosshairStroke = stroke;
        fireChangeEvent();
    }

    public java.awt.Paint getDomainCrosshairPaint() {
        return this.domainCrosshairPaint;
    }

    public void setDomainCrosshairPaint(java.awt.Paint paint) {
        if (paint == null) {
            throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainCrosshairPaint = paint;
        fireChangeEvent();
    }

    public boolean isRangeCrosshairVisible() {
        return this.rangeCrosshairVisible;
    }

    public void setRangeCrosshairVisible(boolean flag) {
        if ((this.rangeCrosshairVisible) != flag) {
            this.rangeCrosshairVisible = flag;
            fireChangeEvent();
        }
    }

    public boolean isRangeCrosshairLockedOnData() {
        return this.rangeCrosshairLockedOnData;
    }

    public void setRangeCrosshairLockedOnData(boolean flag) {
        if ((this.rangeCrosshairLockedOnData) != flag) {
            this.rangeCrosshairLockedOnData = flag;
            fireChangeEvent();
        }
    }

    public double getRangeCrosshairValue() {
        return this.rangeCrosshairValue;
    }

    public void setRangeCrosshairValue(double value) {
        setRangeCrosshairValue(value, true);
    }

    public void setRangeCrosshairValue(double value, boolean notify) {
        this.rangeCrosshairValue = value;
        if ((isRangeCrosshairVisible()) && notify) {
            fireChangeEvent();
        }
    }

    public java.awt.Stroke getRangeCrosshairStroke() {
        return this.rangeCrosshairStroke;
    }

    public void setRangeCrosshairStroke(java.awt.Stroke stroke) {
        if (stroke == null) {
            throw new java.lang.IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeCrosshairStroke = stroke;
        fireChangeEvent();
    }

    public java.awt.Paint getRangeCrosshairPaint() {
        return this.rangeCrosshairPaint;
    }

    public void setRangeCrosshairPaint(java.awt.Paint paint) {
        if (paint == null) {
            throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeCrosshairPaint = paint;
        fireChangeEvent();
    }

    public org.jfree.chart.axis.AxisSpace getFixedDomainAxisSpace() {
        return this.fixedDomainAxisSpace;
    }

    public void setFixedDomainAxisSpace(org.jfree.chart.axis.AxisSpace space) {
        setFixedDomainAxisSpace(space, true);
    }

    public void setFixedDomainAxisSpace(org.jfree.chart.axis.AxisSpace space, boolean notify) {
        this.fixedDomainAxisSpace = space;
        if (notify) {
            fireChangeEvent();
        }
    }

    public org.jfree.chart.axis.AxisSpace getFixedRangeAxisSpace() {
        return this.fixedRangeAxisSpace;
    }

    public void setFixedRangeAxisSpace(org.jfree.chart.axis.AxisSpace space) {
        setFixedRangeAxisSpace(space, true);
    }

    public void setFixedRangeAxisSpace(org.jfree.chart.axis.AxisSpace space, boolean notify) {
        this.fixedRangeAxisSpace = space;
        if (notify) {
            fireChangeEvent();
        }
    }

    public void zoomDomainAxes(double factor, org.jfree.chart.plot.PlotRenderingInfo info, java.awt.geom.Point2D source) {
        zoomDomainAxes(factor, info, source, false);
    }

    public void zoomDomainAxes(double factor, org.jfree.chart.plot.PlotRenderingInfo info, java.awt.geom.Point2D source, boolean useAnchor) {
        for (int i = 0; i < (this.domainAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis domainAxis = ((org.jfree.chart.axis.ValueAxis) (this.domainAxes.get(i)));
            if (domainAxis != null) {
                if (useAnchor) {
                    double sourceX = source.getX();
                    if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
                        sourceX = source.getY();
                    }
                    double anchorX = domainAxis.java2DToValue(sourceX, info.getDataArea(), getDomainAxisEdge());
                    domainAxis.resizeRange(factor, anchorX);
                }else {
                    domainAxis.resizeRange(factor);
                }
            }
        }
    }

    public void zoomDomainAxes(double lowerPercent, double upperPercent, org.jfree.chart.plot.PlotRenderingInfo info, java.awt.geom.Point2D source) {
        for (int i = 0; i < (this.domainAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis domainAxis = ((org.jfree.chart.axis.ValueAxis) (this.domainAxes.get(i)));
            if (domainAxis != null) {
                domainAxis.zoomRange(lowerPercent, upperPercent);
            }
        }
    }

    public void zoomRangeAxes(double factor, org.jfree.chart.plot.PlotRenderingInfo info, java.awt.geom.Point2D source) {
        zoomRangeAxes(factor, info, source, false);
    }

    public void zoomRangeAxes(double factor, org.jfree.chart.plot.PlotRenderingInfo info, java.awt.geom.Point2D source, boolean useAnchor) {
        for (int i = 0; i < (this.rangeAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis rangeAxis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(i)));
            if (rangeAxis != null) {
                if (useAnchor) {
                    double sourceY = source.getY();
                    if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
                        sourceY = source.getX();
                    }
                    double anchorY = rangeAxis.java2DToValue(sourceY, info.getDataArea(), getRangeAxisEdge());
                    rangeAxis.resizeRange(factor, anchorY);
                }else {
                    rangeAxis.resizeRange(factor);
                }
            }
        }
    }

    public void zoomRangeAxes(double lowerPercent, double upperPercent, org.jfree.chart.plot.PlotRenderingInfo info, java.awt.geom.Point2D source) {
        for (int i = 0; i < (this.rangeAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis rangeAxis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(i)));
            if (rangeAxis != null) {
                rangeAxis.zoomRange(lowerPercent, upperPercent);
            }
        }
    }

    public boolean isDomainZoomable() {
        return true;
    }

    public boolean isRangeZoomable() {
        return true;
    }

    public int getSeriesCount() {
        int result = 0;
        org.jfree.data.xy.XYDataset dataset = getDataset();
        if (dataset != null) {
            result = dataset.getSeriesCount();
        }
        return result;
    }

    public org.jfree.chart.LegendItemCollection getFixedLegendItems() {
        return this.fixedLegendItems;
    }

    public void setFixedLegendItems(org.jfree.chart.LegendItemCollection items) {
        this.fixedLegendItems = items;
        fireChangeEvent();
    }

    public org.jfree.chart.LegendItemCollection getLegendItems() {
        if ((this.fixedLegendItems) != null) {
            return this.fixedLegendItems;
        }
        org.jfree.chart.LegendItemCollection result = new org.jfree.chart.LegendItemCollection();
        int count = this.datasets.size();
        for (int datasetIndex = 0; datasetIndex < count; datasetIndex++) {
            org.jfree.data.xy.XYDataset dataset = getDataset(datasetIndex);
            if (dataset != null) {
                org.jfree.chart.renderer.xy.XYItemRenderer renderer = getRenderer(datasetIndex);
                if (renderer == null) {
                    renderer = getRenderer(0);
                }
                if (renderer != null) {
                    int seriesCount = dataset.getSeriesCount();
                    for (int i = 0; i < seriesCount; i++) {
                        if ((renderer.isSeriesVisible(i)) && (renderer.isSeriesVisibleInLegend(i))) {
                            org.jfree.chart.LegendItem item = renderer.getLegendItem(datasetIndex, i);
                            if (item != null) {
                                result.add(item);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == (this)) {
            return true;
        }
        if (!(obj instanceof org.jfree.chart.plot.XYPlot)) {
            return false;
        }
        org.jfree.chart.plot.XYPlot that = ((org.jfree.chart.plot.XYPlot) (obj));
        if ((this.weight) != (that.weight)) {
            return false;
        }
        if ((this.orientation) != (that.orientation)) {
            return false;
        }
        if (!(this.domainAxes.equals(that.domainAxes))) {
            return false;
        }
        if (!(this.domainAxisLocations.equals(that.domainAxisLocations))) {
            return false;
        }
        if ((this.rangeCrosshairLockedOnData) != (that.rangeCrosshairLockedOnData)) {
            return false;
        }
        if ((this.domainGridlinesVisible) != (that.domainGridlinesVisible)) {
            return false;
        }
        if ((this.rangeGridlinesVisible) != (that.rangeGridlinesVisible)) {
            return false;
        }
        if ((this.domainZeroBaselineVisible) != (that.domainZeroBaselineVisible)) {
            return false;
        }
        if ((this.rangeZeroBaselineVisible) != (that.rangeZeroBaselineVisible)) {
            return false;
        }
        if ((this.domainCrosshairVisible) != (that.domainCrosshairVisible)) {
            return false;
        }
        if ((this.domainCrosshairValue) != (that.domainCrosshairValue)) {
            return false;
        }
        if ((this.domainCrosshairLockedOnData) != (that.domainCrosshairLockedOnData)) {
            return false;
        }
        if ((this.rangeCrosshairVisible) != (that.rangeCrosshairVisible)) {
            return false;
        }
        if ((this.rangeCrosshairValue) != (that.rangeCrosshairValue)) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.axisOffset, that.axisOffset))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.renderers, that.renderers))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.rangeAxes, that.rangeAxes))) {
            return false;
        }
        if (!(this.rangeAxisLocations.equals(that.rangeAxisLocations))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.datasetToDomainAxisMap, that.datasetToDomainAxisMap))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.datasetToRangeAxisMap, that.datasetToRangeAxisMap))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.domainGridlineStroke, that.domainGridlineStroke))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.domainGridlinePaint, that.domainGridlinePaint))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.rangeGridlineStroke, that.rangeGridlineStroke))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.rangeGridlinePaint, that.rangeGridlinePaint))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.domainZeroBaselinePaint, that.domainZeroBaselinePaint))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.domainZeroBaselineStroke, that.domainZeroBaselineStroke))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.rangeZeroBaselinePaint, that.rangeZeroBaselinePaint))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.rangeZeroBaselineStroke, that.rangeZeroBaselineStroke))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.domainCrosshairStroke, that.domainCrosshairStroke))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.domainCrosshairPaint, that.domainCrosshairPaint))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.rangeCrosshairStroke, that.rangeCrosshairStroke))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.rangeCrosshairPaint, that.rangeCrosshairPaint))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.foregroundDomainMarkers, that.foregroundDomainMarkers))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.backgroundDomainMarkers, that.backgroundDomainMarkers))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.foregroundRangeMarkers, that.foregroundRangeMarkers))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.backgroundRangeMarkers, that.backgroundRangeMarkers))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.foregroundDomainMarkers, that.foregroundDomainMarkers))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.backgroundDomainMarkers, that.backgroundDomainMarkers))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.foregroundRangeMarkers, that.foregroundRangeMarkers))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.backgroundRangeMarkers, that.backgroundRangeMarkers))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.annotations, that.annotations))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.domainTickBandPaint, that.domainTickBandPaint))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.rangeTickBandPaint, that.rangeTickBandPaint))) {
            return false;
        }
        if (!(this.quadrantOrigin.equals(that.quadrantOrigin))) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!(org.jfree.chart.util.PaintUtilities.equal(this.quadrantPaint[i], that.quadrantPaint[i]))) {
                return false;
            }
        }
        return super.equals(obj);
    }

    public java.lang.Object clone() throws java.lang.CloneNotSupportedException {
        org.jfree.chart.plot.XYPlot clone = ((org.jfree.chart.plot.XYPlot) (super.clone()));
        clone.domainAxes = ((org.jfree.chart.util.ObjectList) (org.jfree.chart.util.ObjectUtilities.clone(this.domainAxes)));
        for (int i = 0; i < (this.domainAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (this.domainAxes.get(i)));
            if (axis != null) {
                org.jfree.chart.axis.ValueAxis clonedAxis = ((org.jfree.chart.axis.ValueAxis) (axis.clone()));
                clone.domainAxes.set(i, clonedAxis);
                clonedAxis.setPlot(clone);
                clonedAxis.addChangeListener(clone);
            }
        }
        clone.domainAxisLocations = ((org.jfree.chart.util.ObjectList) (this.domainAxisLocations.clone()));
        clone.rangeAxes = ((org.jfree.chart.util.ObjectList) (org.jfree.chart.util.ObjectUtilities.clone(this.rangeAxes)));
        for (int i = 0; i < (this.rangeAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(i)));
            if (axis != null) {
                org.jfree.chart.axis.ValueAxis clonedAxis = ((org.jfree.chart.axis.ValueAxis) (axis.clone()));
                clone.rangeAxes.set(i, clonedAxis);
                clonedAxis.setPlot(clone);
                clonedAxis.addChangeListener(clone);
            }
        }
        clone.rangeAxisLocations = ((org.jfree.chart.util.ObjectList) (org.jfree.chart.util.ObjectUtilities.clone(this.rangeAxisLocations)));
        clone.datasets = ((org.jfree.chart.util.ObjectList) (org.jfree.chart.util.ObjectUtilities.clone(this.datasets)));
        for (int i = 0; i < (clone.datasets.size()); ++i) {
            org.jfree.data.xy.XYDataset d = getDataset(i);
            if (d != null) {
                d.addChangeListener(clone);
            }
        }
        clone.datasetToDomainAxisMap = new java.util.TreeMap();
        clone.datasetToDomainAxisMap.putAll(this.datasetToDomainAxisMap);
        clone.datasetToRangeAxisMap = new java.util.TreeMap();
        clone.datasetToRangeAxisMap.putAll(this.datasetToRangeAxisMap);
        clone.renderers = ((org.jfree.chart.util.ObjectList) (org.jfree.chart.util.ObjectUtilities.clone(this.renderers)));
        for (int i = 0; i < (this.renderers.size()); i++) {
            org.jfree.chart.renderer.xy.XYItemRenderer renderer2 = ((org.jfree.chart.renderer.xy.XYItemRenderer) (this.renderers.get(i)));
            if (renderer2 instanceof org.jfree.chart.util.PublicCloneable) {
                org.jfree.chart.util.PublicCloneable pc = ((org.jfree.chart.util.PublicCloneable) (renderer2));
                clone.renderers.set(i, pc.clone());
            }
        }
        clone.foregroundDomainMarkers = ((java.util.Map) (org.jfree.chart.util.ObjectUtilities.clone(this.foregroundDomainMarkers)));
        clone.backgroundDomainMarkers = ((java.util.Map) (org.jfree.chart.util.ObjectUtilities.clone(this.backgroundDomainMarkers)));
        clone.foregroundRangeMarkers = ((java.util.Map) (org.jfree.chart.util.ObjectUtilities.clone(this.foregroundRangeMarkers)));
        clone.backgroundRangeMarkers = ((java.util.Map) (org.jfree.chart.util.ObjectUtilities.clone(this.backgroundRangeMarkers)));
        clone.annotations = ((java.util.List) (org.jfree.chart.util.ObjectUtilities.deepClone(this.annotations)));
        if ((this.fixedDomainAxisSpace) != null) {
            clone.fixedDomainAxisSpace = ((org.jfree.chart.axis.AxisSpace) (org.jfree.chart.util.ObjectUtilities.clone(this.fixedDomainAxisSpace)));
        }
        if ((this.fixedRangeAxisSpace) != null) {
            clone.fixedRangeAxisSpace = ((org.jfree.chart.axis.AxisSpace) (org.jfree.chart.util.ObjectUtilities.clone(this.fixedRangeAxisSpace)));
        }
        clone.quadrantOrigin = ((java.awt.geom.Point2D) (org.jfree.chart.util.ObjectUtilities.clone(this.quadrantOrigin)));
        clone.quadrantPaint = ((java.awt.Paint[]) (this.quadrantPaint.clone()));
        return clone;
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
        stream.defaultWriteObject();
        org.jfree.chart.util.SerialUtilities.writeStroke(this.domainGridlineStroke, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.domainGridlinePaint, stream);
        org.jfree.chart.util.SerialUtilities.writeStroke(this.rangeGridlineStroke, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.rangeGridlinePaint, stream);
        org.jfree.chart.util.SerialUtilities.writeStroke(this.rangeZeroBaselineStroke, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.rangeZeroBaselinePaint, stream);
        org.jfree.chart.util.SerialUtilities.writeStroke(this.domainCrosshairStroke, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.domainCrosshairPaint, stream);
        org.jfree.chart.util.SerialUtilities.writeStroke(this.rangeCrosshairStroke, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.rangeCrosshairPaint, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.domainTickBandPaint, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.rangeTickBandPaint, stream);
        org.jfree.chart.util.SerialUtilities.writePoint2D(this.quadrantOrigin, stream);
        for (int i = 0; i < 4; i++) {
            org.jfree.chart.util.SerialUtilities.writePaint(this.quadrantPaint[i], stream);
        }
        org.jfree.chart.util.SerialUtilities.writeStroke(this.domainZeroBaselineStroke, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.domainZeroBaselinePaint, stream);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, java.lang.ClassNotFoundException {
        stream.defaultReadObject();
        this.domainGridlineStroke = org.jfree.chart.util.SerialUtilities.readStroke(stream);
        this.domainGridlinePaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        this.rangeGridlineStroke = org.jfree.chart.util.SerialUtilities.readStroke(stream);
        this.rangeGridlinePaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        this.rangeZeroBaselineStroke = org.jfree.chart.util.SerialUtilities.readStroke(stream);
        this.rangeZeroBaselinePaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        this.domainCrosshairStroke = org.jfree.chart.util.SerialUtilities.readStroke(stream);
        this.domainCrosshairPaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        this.rangeCrosshairStroke = org.jfree.chart.util.SerialUtilities.readStroke(stream);
        this.rangeCrosshairPaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        this.domainTickBandPaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        this.rangeTickBandPaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        this.quadrantOrigin = org.jfree.chart.util.SerialUtilities.readPoint2D(stream);
        this.quadrantPaint = new java.awt.Paint[4];
        for (int i = 0; i < 4; i++) {
            this.quadrantPaint[i] = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        }
        this.domainZeroBaselineStroke = org.jfree.chart.util.SerialUtilities.readStroke(stream);
        this.domainZeroBaselinePaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        int domainAxisCount = this.domainAxes.size();
        for (int i = 0; i < domainAxisCount; i++) {
            org.jfree.chart.axis.Axis axis = ((org.jfree.chart.axis.Axis) (this.domainAxes.get(i)));
            if (axis != null) {
                axis.setPlot(this);
                axis.addChangeListener(this);
            }
        }
        int rangeAxisCount = this.rangeAxes.size();
        for (int i = 0; i < rangeAxisCount; i++) {
            org.jfree.chart.axis.Axis axis = ((org.jfree.chart.axis.Axis) (this.rangeAxes.get(i)));
            if (axis != null) {
                axis.setPlot(this);
                axis.addChangeListener(this);
            }
        }
        int datasetCount = this.datasets.size();
        for (int i = 0; i < datasetCount; i++) {
            org.jfree.data.general.Dataset dataset = ((org.jfree.data.general.Dataset) (this.datasets.get(i)));
            if (dataset != null) {
                dataset.addChangeListener(this);
            }
        }
        int rendererCount = this.renderers.size();
        for (int i = 0; i < rendererCount; i++) {
            org.jfree.chart.renderer.xy.XYItemRenderer renderer = ((org.jfree.chart.renderer.xy.XYItemRenderer) (this.renderers.get(i)));
            if (renderer != null) {
                renderer.addChangeListener(this);
            }
        }
    }
}

