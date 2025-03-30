package org.jfree.chart.plot;


public class CategoryPlot extends org.jfree.chart.plot.Plot implements java.io.Serializable , java.lang.Cloneable , org.jfree.chart.event.RendererChangeListener , org.jfree.chart.plot.ValueAxisPlot , org.jfree.chart.plot.Zoomable , org.jfree.chart.util.PublicCloneable {
    private static final long serialVersionUID = -3537691700434728188L;

    public static final boolean DEFAULT_DOMAIN_GRIDLINES_VISIBLE = false;

    public static final boolean DEFAULT_RANGE_GRIDLINES_VISIBLE = true;

    public static final java.awt.Stroke DEFAULT_GRIDLINE_STROKE = new java.awt.BasicStroke(0.5F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0.0F, new float[]{ 2.0F, 2.0F }, 0.0F);

    public static final java.awt.Paint DEFAULT_GRIDLINE_PAINT = java.awt.Color.WHITE;

    public static final java.awt.Font DEFAULT_VALUE_LABEL_FONT = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10);

    public static final boolean DEFAULT_CROSSHAIR_VISIBLE = false;

    public static final java.awt.Stroke DEFAULT_CROSSHAIR_STROKE = org.jfree.chart.plot.CategoryPlot.DEFAULT_GRIDLINE_STROKE;

    public static final java.awt.Paint DEFAULT_CROSSHAIR_PAINT = java.awt.Color.blue;

    protected static java.util.ResourceBundle localizationResources = java.util.ResourceBundle.getBundle("org.jfree.chart.plot.LocalizationBundle");

    private org.jfree.chart.plot.PlotOrientation orientation;

    private org.jfree.chart.util.RectangleInsets axisOffset;

    private org.jfree.chart.util.ObjectList domainAxes;

    private org.jfree.chart.util.ObjectList domainAxisLocations;

    private boolean drawSharedDomainAxis;

    private org.jfree.chart.util.ObjectList rangeAxes;

    private org.jfree.chart.util.ObjectList rangeAxisLocations;

    private org.jfree.chart.util.ObjectList datasets;

    private org.jfree.chart.util.ObjectList datasetToDomainAxisMap;

    private org.jfree.chart.util.ObjectList datasetToRangeAxisMap;

    private org.jfree.chart.util.ObjectList renderers;

    private org.jfree.chart.plot.DatasetRenderingOrder renderingOrder = DatasetRenderingOrder.REVERSE;

    private org.jfree.chart.util.SortOrder columnRenderingOrder = org.jfree.chart.util.SortOrder.ASCENDING;

    private org.jfree.chart.util.SortOrder rowRenderingOrder = org.jfree.chart.util.SortOrder.ASCENDING;

    private boolean domainGridlinesVisible;

    private org.jfree.chart.axis.CategoryAnchor domainGridlinePosition;

    private transient java.awt.Stroke domainGridlineStroke;

    private transient java.awt.Paint domainGridlinePaint;

    private boolean rangeGridlinesVisible;

    private transient java.awt.Stroke rangeGridlineStroke;

    private transient java.awt.Paint rangeGridlinePaint;

    private double anchorValue;

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

    private int weight;

    private org.jfree.chart.axis.AxisSpace fixedDomainAxisSpace;

    private org.jfree.chart.axis.AxisSpace fixedRangeAxisSpace;

    private org.jfree.chart.LegendItemCollection fixedLegendItems;

    public CategoryPlot() {
        this(null, null, null, null);
    }

    public CategoryPlot(org.jfree.data.category.CategoryDataset dataset, org.jfree.chart.axis.CategoryAxis domainAxis, org.jfree.chart.axis.ValueAxis rangeAxis, org.jfree.chart.renderer.category.CategoryItemRenderer renderer) {
        super();
        this.orientation = PlotOrientation.VERTICAL;
        this.domainAxes = new org.jfree.chart.util.ObjectList();
        this.domainAxisLocations = new org.jfree.chart.util.ObjectList();
        this.rangeAxes = new org.jfree.chart.util.ObjectList();
        this.rangeAxisLocations = new org.jfree.chart.util.ObjectList();
        this.datasetToDomainAxisMap = new org.jfree.chart.util.ObjectList();
        this.datasetToRangeAxisMap = new org.jfree.chart.util.ObjectList();
        this.renderers = new org.jfree.chart.util.ObjectList();
        this.datasets = new org.jfree.chart.util.ObjectList();
        this.datasets.set(0, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }
        this.axisOffset = new org.jfree.chart.util.RectangleInsets(4.0, 4.0, 4.0, 4.0);
        setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT, false);
        setRangeAxisLocation(AxisLocation.TOP_OR_LEFT, false);
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
        this.drawSharedDomainAxis = false;
        this.rangeAxes.set(0, rangeAxis);
        this.mapDatasetToRangeAxis(0, 0);
        if (rangeAxis != null) {
            rangeAxis.setPlot(this);
            rangeAxis.addChangeListener(this);
        }
        configureDomainAxes();
        configureRangeAxes();
        this.domainGridlinesVisible = org.jfree.chart.plot.CategoryPlot.DEFAULT_DOMAIN_GRIDLINES_VISIBLE;
        this.domainGridlinePosition = org.jfree.chart.axis.CategoryAnchor.MIDDLE;
        this.domainGridlineStroke = org.jfree.chart.plot.CategoryPlot.DEFAULT_GRIDLINE_STROKE;
        this.domainGridlinePaint = org.jfree.chart.plot.CategoryPlot.DEFAULT_GRIDLINE_PAINT;
        this.rangeGridlinesVisible = org.jfree.chart.plot.CategoryPlot.DEFAULT_RANGE_GRIDLINES_VISIBLE;
        this.rangeGridlineStroke = org.jfree.chart.plot.CategoryPlot.DEFAULT_GRIDLINE_STROKE;
        this.rangeGridlinePaint = org.jfree.chart.plot.CategoryPlot.DEFAULT_GRIDLINE_PAINT;
        this.foregroundDomainMarkers = new java.util.HashMap();
        this.backgroundDomainMarkers = new java.util.HashMap();
        this.foregroundRangeMarkers = new java.util.HashMap();
        this.backgroundRangeMarkers = new java.util.HashMap();
        org.jfree.chart.plot.Marker baseline = new org.jfree.chart.plot.ValueMarker(0.0, new java.awt.Color(0.8F, 0.8F, 0.8F, 0.5F), new java.awt.BasicStroke(1.0F), new java.awt.Color(0.85F, 0.85F, 0.95F, 0.5F), new java.awt.BasicStroke(1.0F), 0.6F);
        addRangeMarker(baseline, Layer.BACKGROUND);
        this.anchorValue = 0.0;
        this.rangeCrosshairVisible = org.jfree.chart.plot.CategoryPlot.DEFAULT_CROSSHAIR_VISIBLE;
        this.rangeCrosshairValue = 0.0;
        this.rangeCrosshairStroke = org.jfree.chart.plot.CategoryPlot.DEFAULT_CROSSHAIR_STROKE;
        this.rangeCrosshairPaint = org.jfree.chart.plot.CategoryPlot.DEFAULT_CROSSHAIR_PAINT;
        this.annotations = new java.util.ArrayList();
    }

    public java.lang.String getPlotType() {
        return org.jfree.chart.plot.CategoryPlot.localizationResources.getString("Category_Plot");
    }

    public org.jfree.chart.plot.PlotOrientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(org.jfree.chart.plot.PlotOrientation orientation) {
        if (orientation == null) {
            throw new java.lang.IllegalArgumentException("Null 'orientation' argument.");
        }
        this.orientation = orientation;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public org.jfree.chart.util.RectangleInsets getAxisOffset() {
        return this.axisOffset;
    }

    public void setAxisOffset(org.jfree.chart.util.RectangleInsets offset) {
        if (offset == null) {
            throw new java.lang.IllegalArgumentException("Null 'offset' argument.");
        }
        this.axisOffset = offset;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public org.jfree.chart.axis.CategoryAxis getDomainAxis() {
        return getDomainAxis(0);
    }

    public org.jfree.chart.axis.CategoryAxis getDomainAxis(int index) {
        org.jfree.chart.axis.CategoryAxis result = null;
        if (index < (this.domainAxes.size())) {
            result = ((org.jfree.chart.axis.CategoryAxis) (this.domainAxes.get(index)));
        }
        if (result == null) {
            org.jfree.chart.plot.Plot parent = getParent();
            if (parent instanceof org.jfree.chart.plot.CategoryPlot) {
                org.jfree.chart.plot.CategoryPlot cp = ((org.jfree.chart.plot.CategoryPlot) (parent));
                result = cp.getDomainAxis(index);
            }
        }
        return result;
    }

    public void setDomainAxis(org.jfree.chart.axis.CategoryAxis axis) {
        setDomainAxis(0, axis);
    }

    public void setDomainAxis(int index, org.jfree.chart.axis.CategoryAxis axis) {
        setDomainAxis(index, axis, true);
    }

    public void setDomainAxis(int index, org.jfree.chart.axis.CategoryAxis axis, boolean notify) {
        org.jfree.chart.axis.CategoryAxis existing = ((org.jfree.chart.axis.CategoryAxis) (this.domainAxes.get(index)));
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
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
        }
    }

    public void setDomainAxes(org.jfree.chart.axis.CategoryAxis[] axes) {
        for (int i = 0; i < (axes.length); i++) {
            setDomainAxis(i, axes[i], false);
        }
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public int getDomainAxisIndex(org.jfree.chart.axis.CategoryAxis axis) {
        if (axis == null) {
            throw new java.lang.IllegalArgumentException("Null 'axis' argument.");
        }
        return this.domainAxes.indexOf(axis);
    }

    public org.jfree.chart.axis.AxisLocation getDomainAxisLocation() {
        return getDomainAxisLocation(0);
    }

    public org.jfree.chart.axis.AxisLocation getDomainAxisLocation(int index) {
        org.jfree.chart.axis.AxisLocation result = null;
        if (index < (this.domainAxisLocations.size())) {
            result = ((org.jfree.chart.axis.AxisLocation) (this.domainAxisLocations.get(index)));
        }
        if (result == null) {
            result = org.jfree.chart.axis.AxisLocation.getOpposite(getDomainAxisLocation(0));
        }
        return result;
    }

    public void setDomainAxisLocation(org.jfree.chart.axis.AxisLocation location) {
        setDomainAxisLocation(0, location, true);
    }

    public void setDomainAxisLocation(org.jfree.chart.axis.AxisLocation location, boolean notify) {
        setDomainAxisLocation(0, location, notify);
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
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
        }
    }

    public org.jfree.chart.util.RectangleEdge getDomainAxisEdge() {
        return getDomainAxisEdge(0);
    }

    public org.jfree.chart.util.RectangleEdge getDomainAxisEdge(int index) {
        org.jfree.chart.util.RectangleEdge result = null;
        org.jfree.chart.axis.AxisLocation location = getDomainAxisLocation(index);
        if (location != null) {
            result = org.jfree.chart.plot.Plot.resolveDomainAxisLocation(location, this.orientation);
        }else {
            result = org.jfree.chart.util.RectangleEdge.opposite(getDomainAxisEdge(0));
        }
        return result;
    }

    public int getDomainAxisCount() {
        return this.domainAxes.size();
    }

    public void clearDomainAxes() {
        for (int i = 0; i < (this.domainAxes.size()); i++) {
            org.jfree.chart.axis.CategoryAxis axis = ((org.jfree.chart.axis.CategoryAxis) (this.domainAxes.get(i)));
            if (axis != null) {
                axis.removeChangeListener(this);
            }
        }
        this.domainAxes.clear();
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public void configureDomainAxes() {
        for (int i = 0; i < (this.domainAxes.size()); i++) {
            org.jfree.chart.axis.CategoryAxis axis = ((org.jfree.chart.axis.CategoryAxis) (this.domainAxes.get(i)));
            if (axis != null) {
                axis.configure();
            }
        }
    }

    public org.jfree.chart.axis.ValueAxis getRangeAxis() {
        return getRangeAxis(0);
    }

    public org.jfree.chart.axis.ValueAxis getRangeAxis(int index) {
        org.jfree.chart.axis.ValueAxis result = null;
        if (index < (this.rangeAxes.size())) {
            result = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(index)));
        }
        if (result == null) {
            org.jfree.chart.plot.Plot parent = getParent();
            if (parent instanceof org.jfree.chart.plot.CategoryPlot) {
                org.jfree.chart.plot.CategoryPlot cp = ((org.jfree.chart.plot.CategoryPlot) (parent));
                result = cp.getRangeAxis(index);
            }
        }
        return result;
    }

    public void setRangeAxis(org.jfree.chart.axis.ValueAxis axis) {
        setRangeAxis(0, axis);
    }

    public void setRangeAxis(int index, org.jfree.chart.axis.ValueAxis axis) {
        setRangeAxis(index, axis, true);
    }

    public void setRangeAxis(int index, org.jfree.chart.axis.ValueAxis axis, boolean notify) {
        org.jfree.chart.axis.ValueAxis existing = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(index)));
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
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
        }
    }

    public void setRangeAxes(org.jfree.chart.axis.ValueAxis[] axes) {
        for (int i = 0; i < (axes.length); i++) {
            setRangeAxis(i, axes[i], false);
        }
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public int getRangeAxisIndex(org.jfree.chart.axis.ValueAxis axis) {
        if (axis == null) {
            throw new java.lang.IllegalArgumentException("Null 'axis' argument.");
        }
        int result = this.rangeAxes.indexOf(axis);
        if (result < 0) {
            org.jfree.chart.plot.Plot parent = getParent();
            if (parent instanceof org.jfree.chart.plot.CategoryPlot) {
                org.jfree.chart.plot.CategoryPlot p = ((org.jfree.chart.plot.CategoryPlot) (parent));
                result = p.getRangeAxisIndex(axis);
            }
        }
        return result;
    }

    public org.jfree.chart.axis.AxisLocation getRangeAxisLocation() {
        return getRangeAxisLocation(0);
    }

    public org.jfree.chart.axis.AxisLocation getRangeAxisLocation(int index) {
        org.jfree.chart.axis.AxisLocation result = null;
        if (index < (this.rangeAxisLocations.size())) {
            result = ((org.jfree.chart.axis.AxisLocation) (this.rangeAxisLocations.get(index)));
        }
        if (result == null) {
            result = org.jfree.chart.axis.AxisLocation.getOpposite(getRangeAxisLocation(0));
        }
        return result;
    }

    public void setRangeAxisLocation(org.jfree.chart.axis.AxisLocation location) {
        setRangeAxisLocation(location, true);
    }

    public void setRangeAxisLocation(org.jfree.chart.axis.AxisLocation location, boolean notify) {
        setRangeAxisLocation(0, location, notify);
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
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
        }
    }

    public org.jfree.chart.util.RectangleEdge getRangeAxisEdge() {
        return getRangeAxisEdge(0);
    }

    public org.jfree.chart.util.RectangleEdge getRangeAxisEdge(int index) {
        org.jfree.chart.axis.AxisLocation location = getRangeAxisLocation(index);
        org.jfree.chart.util.RectangleEdge result = org.jfree.chart.plot.Plot.resolveRangeAxisLocation(location, this.orientation);
        if (result == null) {
            result = org.jfree.chart.util.RectangleEdge.opposite(getRangeAxisEdge(0));
        }
        return result;
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
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public void configureRangeAxes() {
        for (int i = 0; i < (this.rangeAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis axis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(i)));
            if (axis != null) {
                axis.configure();
            }
        }
    }

    public org.jfree.data.category.CategoryDataset getDataset() {
        return getDataset(0);
    }

    public org.jfree.data.category.CategoryDataset getDataset(int index) {
        org.jfree.data.category.CategoryDataset result = null;
        if ((this.datasets.size()) > index) {
            result = ((org.jfree.data.category.CategoryDataset) (this.datasets.get(index)));
        }
        return result;
    }

    public void setDataset(org.jfree.data.category.CategoryDataset dataset) {
        setDataset(0, dataset);
    }

    public void setDataset(int index, org.jfree.data.category.CategoryDataset dataset) {
        org.jfree.data.category.CategoryDataset existing = ((org.jfree.data.category.CategoryDataset) (this.datasets.get(index)));
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

    public void mapDatasetToDomainAxis(int index, int axisIndex) {
        this.datasetToDomainAxisMap.set(index, new java.lang.Integer(axisIndex));
        datasetChanged(new org.jfree.data.general.DatasetChangeEvent(this, getDataset(index)));
    }

    public org.jfree.chart.axis.CategoryAxis getDomainAxisForDataset(int index) {
        org.jfree.chart.axis.CategoryAxis result = getDomainAxis();
        java.lang.Integer axisIndex = ((java.lang.Integer) (this.datasetToDomainAxisMap.get(index)));
        if (axisIndex != null) {
            result = getDomainAxis(axisIndex.intValue());
        }
        return result;
    }

    public void mapDatasetToRangeAxis(int index, int axisIndex) {
        this.datasetToRangeAxisMap.set(index, new java.lang.Integer(axisIndex));
        datasetChanged(new org.jfree.data.general.DatasetChangeEvent(this, getDataset(index)));
    }

    public org.jfree.chart.axis.ValueAxis getRangeAxisForDataset(int index) {
        org.jfree.chart.axis.ValueAxis result = getRangeAxis();
        java.lang.Integer axisIndex = ((java.lang.Integer) (this.datasetToRangeAxisMap.get(index)));
        if (axisIndex != null) {
            result = getRangeAxis(axisIndex.intValue());
        }
        return result;
    }

    public org.jfree.chart.renderer.category.CategoryItemRenderer getRenderer() {
        return getRenderer(0);
    }

    public org.jfree.chart.renderer.category.CategoryItemRenderer getRenderer(int index) {
        org.jfree.chart.renderer.category.CategoryItemRenderer result = null;
        if ((this.renderers.size()) > index) {
            result = ((org.jfree.chart.renderer.category.CategoryItemRenderer) (this.renderers.get(index)));
        }
        return result;
    }

    public void setRenderer(org.jfree.chart.renderer.category.CategoryItemRenderer renderer) {
        setRenderer(0, renderer, true);
    }

    public void setRenderer(org.jfree.chart.renderer.category.CategoryItemRenderer renderer, boolean notify) {
        setRenderer(0, renderer, notify);
    }

    public void setRenderer(int index, org.jfree.chart.renderer.category.CategoryItemRenderer renderer) {
        setRenderer(index, renderer, true);
    }

    public void setRenderer(int index, org.jfree.chart.renderer.category.CategoryItemRenderer renderer, boolean notify) {
        org.jfree.chart.renderer.category.CategoryItemRenderer existing = ((org.jfree.chart.renderer.category.CategoryItemRenderer) (this.renderers.get(index)));
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
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
        }
    }

    public void setRenderers(org.jfree.chart.renderer.category.CategoryItemRenderer[] renderers) {
        for (int i = 0; i < (renderers.length); i++) {
            setRenderer(i, renderers[i], false);
        }
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public org.jfree.chart.renderer.category.CategoryItemRenderer getRendererForDataset(org.jfree.data.category.CategoryDataset dataset) {
        org.jfree.chart.renderer.category.CategoryItemRenderer result = null;
        for (int i = 0; i < (this.datasets.size()); i++) {
            if ((this.datasets.get(i)) == dataset) {
                result = ((org.jfree.chart.renderer.category.CategoryItemRenderer) (this.renderers.get(i)));
                break;
            }
        }
        return result;
    }

    public int getIndexOf(org.jfree.chart.renderer.category.CategoryItemRenderer renderer) {
        return this.renderers.indexOf(renderer);
    }

    public org.jfree.chart.plot.DatasetRenderingOrder getDatasetRenderingOrder() {
        return this.renderingOrder;
    }

    public void setDatasetRenderingOrder(org.jfree.chart.plot.DatasetRenderingOrder order) {
        if (order == null) {
            throw new java.lang.IllegalArgumentException("Null 'order' argument.");
        }
        this.renderingOrder = order;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public org.jfree.chart.util.SortOrder getColumnRenderingOrder() {
        return this.columnRenderingOrder;
    }

    public void setColumnRenderingOrder(org.jfree.chart.util.SortOrder order) {
        if (order == null) {
            throw new java.lang.IllegalArgumentException("Null 'order' argument.");
        }
        this.columnRenderingOrder = order;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public org.jfree.chart.util.SortOrder getRowRenderingOrder() {
        return this.rowRenderingOrder;
    }

    public void setRowRenderingOrder(org.jfree.chart.util.SortOrder order) {
        if (order == null) {
            throw new java.lang.IllegalArgumentException("Null 'order' argument.");
        }
        this.rowRenderingOrder = order;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public boolean isDomainGridlinesVisible() {
        return this.domainGridlinesVisible;
    }

    public void setDomainGridlinesVisible(boolean visible) {
        if ((this.domainGridlinesVisible) != visible) {
            this.domainGridlinesVisible = visible;
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
        }
    }

    public org.jfree.chart.axis.CategoryAnchor getDomainGridlinePosition() {
        return this.domainGridlinePosition;
    }

    public void setDomainGridlinePosition(org.jfree.chart.axis.CategoryAnchor position) {
        if (position == null) {
            throw new java.lang.IllegalArgumentException("Null 'position' argument.");
        }
        this.domainGridlinePosition = position;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public java.awt.Stroke getDomainGridlineStroke() {
        return this.domainGridlineStroke;
    }

    public void setDomainGridlineStroke(java.awt.Stroke stroke) {
        if (stroke == null) {
            throw new java.lang.IllegalArgumentException("Null 'stroke' not permitted.");
        }
        this.domainGridlineStroke = stroke;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public java.awt.Paint getDomainGridlinePaint() {
        return this.domainGridlinePaint;
    }

    public void setDomainGridlinePaint(java.awt.Paint paint) {
        if (paint == null) {
            throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainGridlinePaint = paint;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public boolean isRangeGridlinesVisible() {
        return this.rangeGridlinesVisible;
    }

    public void setRangeGridlinesVisible(boolean visible) {
        if ((this.rangeGridlinesVisible) != visible) {
            this.rangeGridlinesVisible = visible;
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
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
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public java.awt.Paint getRangeGridlinePaint() {
        return this.rangeGridlinePaint;
    }

    public void setRangeGridlinePaint(java.awt.Paint paint) {
        if (paint == null) {
            throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeGridlinePaint = paint;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public org.jfree.chart.LegendItemCollection getFixedLegendItems() {
        return this.fixedLegendItems;
    }

    public void setFixedLegendItems(org.jfree.chart.LegendItemCollection items) {
        this.fixedLegendItems = items;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public org.jfree.chart.LegendItemCollection getLegendItems() {
        org.jfree.chart.LegendItemCollection result = this.fixedLegendItems;
        if (result == null) {
            result = new org.jfree.chart.LegendItemCollection();
            int count = this.datasets.size();
            for (int datasetIndex = 0; datasetIndex < count; datasetIndex++) {
                org.jfree.data.category.CategoryDataset dataset = getDataset(datasetIndex);
                if (dataset != null) {
                    org.jfree.chart.renderer.category.CategoryItemRenderer renderer = getRenderer(datasetIndex);
                    if (renderer != null) {
                        int seriesCount = dataset.getRowCount();
                        for (int i = 0; i < seriesCount; i++) {
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

    public void handleClick(int x, int y, org.jfree.chart.plot.PlotRenderingInfo info) {
        java.awt.geom.Rectangle2D dataArea = info.getDataArea();
        if (dataArea.contains(x, y)) {
            double java2D = 0.0;
            if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
                java2D = x;
            }else
                if ((this.orientation) == (PlotOrientation.VERTICAL)) {
                    java2D = y;
                }

            org.jfree.chart.util.RectangleEdge edge = org.jfree.chart.plot.Plot.resolveRangeAxisLocation(getRangeAxisLocation(), this.orientation);
            double value = getRangeAxis().java2DToValue(java2D, info.getDataArea(), edge);
            setAnchorValue(value);
            setRangeCrosshairValue(value);
        }
    }

    public void zoom(double percent) {
        if (percent > 0.0) {
            double range = getRangeAxis().getRange().getLength();
            double scaledRange = range * percent;
            getRangeAxis().setRange(((this.anchorValue) - (scaledRange / 2.0)), ((this.anchorValue) + (scaledRange / 2.0)));
        }else {
            getRangeAxis().setAutoRange(true);
        }
    }

    public void datasetChanged(org.jfree.data.general.DatasetChangeEvent event) {
        int count = this.rangeAxes.size();
        for (int axisIndex = 0; axisIndex < count; axisIndex++) {
            org.jfree.chart.axis.ValueAxis yAxis = getRangeAxis(axisIndex);
            if (yAxis != null) {
                yAxis.configure();
            }
        }
        if ((getParent()) != null) {
            getParent().datasetChanged(event);
        }else {
            org.jfree.chart.event.PlotChangeEvent e = new org.jfree.chart.event.PlotChangeEvent(this);
            e.setType(ChartChangeEventType.DATASET_UPDATED);
            notifyListeners(e);
        }
    }

    public void rendererChanged(org.jfree.chart.event.RendererChangeEvent event) {
        org.jfree.chart.plot.Plot parent = getParent();
        if (parent != null) {
            if (parent instanceof org.jfree.chart.event.RendererChangeListener) {
                org.jfree.chart.event.RendererChangeListener rcl = ((org.jfree.chart.event.RendererChangeListener) (parent));
                rcl.rendererChanged(event);
            }else {
                throw new java.lang.RuntimeException("The renderer has changed and I don't know what to do!");
            }
        }else {
            configureRangeAxes();
            org.jfree.chart.event.PlotChangeEvent e = new org.jfree.chart.event.PlotChangeEvent(this);
            notifyListeners(e);
        }
    }

    public void addDomainMarker(org.jfree.chart.plot.CategoryMarker marker) {
        addDomainMarker(marker, Layer.FOREGROUND);
    }

    public void addDomainMarker(org.jfree.chart.plot.CategoryMarker marker, org.jfree.chart.util.Layer layer) {
        addDomainMarker(0, marker, layer);
    }

    public void addDomainMarker(int index, org.jfree.chart.plot.CategoryMarker marker, org.jfree.chart.util.Layer layer) {
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
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
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
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public java.util.Collection getDomainMarkers(org.jfree.chart.util.Layer layer) {
        return getDomainMarkers(0, layer);
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
        if ((this.foregroundDomainMarkers) != null) {
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
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public void addRangeMarker(org.jfree.chart.plot.Marker marker) {
        addRangeMarker(marker, Layer.FOREGROUND);
    }

    public void addRangeMarker(org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
        addRangeMarker(0, marker, layer);
    }

    public void addRangeMarker(int index, org.jfree.chart.plot.Marker marker, org.jfree.chart.util.Layer layer) {
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
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
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
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public java.util.Collection getRangeMarkers(org.jfree.chart.util.Layer layer) {
        return getRangeMarkers(0, layer);
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
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public boolean isRangeCrosshairVisible() {
        return this.rangeCrosshairVisible;
    }

    public void setRangeCrosshairVisible(boolean flag) {
        if ((this.rangeCrosshairVisible) != flag) {
            this.rangeCrosshairVisible = flag;
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
        }
    }

    public boolean isRangeCrosshairLockedOnData() {
        return this.rangeCrosshairLockedOnData;
    }

    public void setRangeCrosshairLockedOnData(boolean flag) {
        if ((this.rangeCrosshairLockedOnData) != flag) {
            this.rangeCrosshairLockedOnData = flag;
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
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
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
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
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public java.awt.Paint getRangeCrosshairPaint() {
        return this.rangeCrosshairPaint;
    }

    public void setRangeCrosshairPaint(java.awt.Paint paint) {
        if (paint == null) {
            throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeCrosshairPaint = paint;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public java.util.List getAnnotations() {
        return this.annotations;
    }

    public void addAnnotation(org.jfree.chart.annotations.CategoryAnnotation annotation) {
        if (annotation == null) {
            throw new java.lang.IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public boolean removeAnnotation(org.jfree.chart.annotations.CategoryAnnotation annotation) {
        if (annotation == null) {
            throw new java.lang.IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(annotation);
        if (removed) {
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
        }
        return removed;
    }

    public void clearAnnotations() {
        this.annotations.clear();
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
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
            org.jfree.chart.util.RectangleEdge domainEdge = org.jfree.chart.plot.Plot.resolveDomainAxisLocation(getDomainAxisLocation(), this.orientation);
            if (this.drawSharedDomainAxis) {
                space = getDomainAxis().reserveSpace(g2, this, plotArea, domainEdge, space);
            }
            for (int i = 0; i < (this.domainAxes.size()); i++) {
                org.jfree.chart.axis.Axis xAxis = ((org.jfree.chart.axis.Axis) (this.domainAxes.get(i)));
                if (xAxis != null) {
                    org.jfree.chart.util.RectangleEdge edge = getDomainAxisEdge(i);
                    space = xAxis.reserveSpace(g2, this, plotArea, edge, space);
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
                org.jfree.chart.axis.Axis yAxis = ((org.jfree.chart.axis.Axis) (this.rangeAxes.get(i)));
                if (yAxis != null) {
                    org.jfree.chart.util.RectangleEdge edge = getRangeAxisEdge(i);
                    space = yAxis.reserveSpace(g2, this, plotArea, edge, space);
                }
            }
        }
        return space;
    }

    protected org.jfree.chart.axis.AxisSpace calculateAxisSpace(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D plotArea) {
        org.jfree.chart.axis.AxisSpace space = new org.jfree.chart.axis.AxisSpace();
        space = calculateRangeAxisSpace(g2, plotArea, space);
        space = calculateDomainAxisSpace(g2, plotArea, space);
        return space;
    }

    public void draw(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D area, java.awt.geom.Point2D anchor, org.jfree.chart.plot.PlotState parentState, org.jfree.chart.plot.PlotRenderingInfo state) {
        boolean b1 = (area.getWidth()) <= (MINIMUM_WIDTH_TO_DRAW);
        boolean b2 = (area.getHeight()) <= (MINIMUM_HEIGHT_TO_DRAW);
        if (b1 || b2) {
            return;
        }
        if (state == null) {
            state = new org.jfree.chart.plot.PlotRenderingInfo(null);
        }
        state.setPlotArea(area);
        org.jfree.chart.util.RectangleInsets insets = getInsets();
        insets.trim(area);
        org.jfree.chart.axis.AxisSpace space = calculateAxisSpace(g2, area);
        java.awt.geom.Rectangle2D dataArea = space.shrink(area, null);
        this.axisOffset.trim(dataArea);
        state.setDataArea(dataArea);
        if ((getRenderer()) != null) {
            getRenderer().drawBackground(g2, this, dataArea);
        }else {
            drawBackground(g2, dataArea);
        }
        java.util.Map axisStateMap = drawAxes(g2, area, dataArea, state);
        java.awt.Shape savedClip = g2.getClip();
        g2.clip(dataArea);
        drawDomainGridlines(g2, dataArea);
        org.jfree.chart.axis.AxisState rangeAxisState = ((org.jfree.chart.axis.AxisState) (axisStateMap.get(getRangeAxis())));
        if (rangeAxisState == null) {
            if (parentState != null) {
                rangeAxisState = ((org.jfree.chart.axis.AxisState) (parentState.getSharedAxisStates().get(getRangeAxis())));
            }
        }
        if (rangeAxisState != null) {
            drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());
        }
        for (int i = 0; i < (this.renderers.size()); i++) {
            drawDomainMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }
        for (int i = 0; i < (this.renderers.size()); i++) {
            drawRangeMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }
        boolean foundData = false;
        java.awt.Composite originalComposite = g2.getComposite();
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, getForegroundAlpha()));
        org.jfree.chart.plot.DatasetRenderingOrder order = getDatasetRenderingOrder();
        if (order == (DatasetRenderingOrder.FORWARD)) {
            int datasetCount = this.datasets.size();
            for (int i = 0; i < datasetCount; i++) {
                org.jfree.chart.renderer.category.CategoryItemRenderer r = getRenderer(i);
                if (r != null) {
                    org.jfree.chart.axis.CategoryAxis domainAxis = getDomainAxisForDataset(i);
                    org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.BACKGROUND, state);
                }
            }
            for (int i = 0; i < datasetCount; i++) {
                foundData = (render(g2, dataArea, i, state)) || foundData;
            }
            for (int i = 0; i < datasetCount; i++) {
                org.jfree.chart.renderer.category.CategoryItemRenderer r = getRenderer(i);
                if (r != null) {
                    org.jfree.chart.axis.CategoryAxis domainAxis = getDomainAxisForDataset(i);
                    org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.FOREGROUND, state);
                }
            }
        }else {
            int datasetCount = this.datasets.size();
            for (int i = datasetCount - 1; i >= 0; i--) {
                org.jfree.chart.renderer.category.CategoryItemRenderer r = getRenderer(i);
                if (r != null) {
                    org.jfree.chart.axis.CategoryAxis domainAxis = getDomainAxisForDataset(i);
                    org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.BACKGROUND, state);
                }
            }
            for (int i = (this.datasets.size()) - 1; i >= 0; i--) {
                foundData = (render(g2, dataArea, i, state)) || foundData;
            }
            for (int i = datasetCount - 1; i >= 0; i--) {
                org.jfree.chart.renderer.category.CategoryItemRenderer r = getRenderer(i);
                if (r != null) {
                    org.jfree.chart.axis.CategoryAxis domainAxis = getDomainAxisForDataset(i);
                    org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.FOREGROUND, state);
                }
            }
        }
        for (int i = 0; i < (this.renderers.size()); i++) {
            drawDomainMarkers(g2, dataArea, i, Layer.FOREGROUND);
        }
        for (int i = 0; i < (this.renderers.size()); i++) {
            drawRangeMarkers(g2, dataArea, i, Layer.FOREGROUND);
        }
        drawAnnotations(g2, dataArea, state);
        g2.setClip(savedClip);
        g2.setComposite(originalComposite);
        if (!foundData) {
            drawNoDataMessage(g2, dataArea);
        }
        if (isRangeCrosshairVisible()) {
            drawRangeCrosshair(g2, dataArea, getOrientation(), getRangeCrosshairValue(), getRangeAxis(), getRangeCrosshairStroke(), getRangeCrosshairPaint());
        }
        if ((getRenderer()) != null) {
            getRenderer().drawOutline(g2, this, dataArea);
        }else {
            drawOutline(g2, dataArea);
        }
    }

    public void drawBackground(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D area) {
        fillBackground(g2, area, this.orientation);
        drawBackgroundImage(g2, area);
    }

    protected java.util.Map drawAxes(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D plotArea, java.awt.geom.Rectangle2D dataArea, org.jfree.chart.plot.PlotRenderingInfo plotState) {
        org.jfree.chart.axis.AxisCollection axisCollection = new org.jfree.chart.axis.AxisCollection();
        for (int index = 0; index < (this.domainAxes.size()); index++) {
            org.jfree.chart.axis.CategoryAxis xAxis = ((org.jfree.chart.axis.CategoryAxis) (this.domainAxes.get(index)));
            if (xAxis != null) {
                axisCollection.add(xAxis, getDomainAxisEdge(index));
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
            org.jfree.chart.axis.Axis axis = ((org.jfree.chart.axis.Axis) (iterator.next()));
            if (axis != null) {
                org.jfree.chart.axis.AxisState axisState = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.TOP, plotState);
                cursor = axisState.getCursor();
                axisStateMap.put(axis, axisState);
            }
        } 
        cursor = (dataArea.getMaxY()) + (this.axisOffset.calculateBottomOutset(dataArea.getHeight()));
        iterator = axisCollection.getAxesAtBottom().iterator();
        while (iterator.hasNext()) {
            org.jfree.chart.axis.Axis axis = ((org.jfree.chart.axis.Axis) (iterator.next()));
            if (axis != null) {
                org.jfree.chart.axis.AxisState axisState = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.BOTTOM, plotState);
                cursor = axisState.getCursor();
                axisStateMap.put(axis, axisState);
            }
        } 
        cursor = (dataArea.getMinX()) - (this.axisOffset.calculateLeftOutset(dataArea.getWidth()));
        iterator = axisCollection.getAxesAtLeft().iterator();
        while (iterator.hasNext()) {
            org.jfree.chart.axis.Axis axis = ((org.jfree.chart.axis.Axis) (iterator.next()));
            if (axis != null) {
                org.jfree.chart.axis.AxisState axisState = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.LEFT, plotState);
                cursor = axisState.getCursor();
                axisStateMap.put(axis, axisState);
            }
        } 
        cursor = (dataArea.getMaxX()) + (this.axisOffset.calculateRightOutset(dataArea.getWidth()));
        iterator = axisCollection.getAxesAtRight().iterator();
        while (iterator.hasNext()) {
            org.jfree.chart.axis.Axis axis = ((org.jfree.chart.axis.Axis) (iterator.next()));
            if (axis != null) {
                org.jfree.chart.axis.AxisState axisState = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.RIGHT, plotState);
                cursor = axisState.getCursor();
                axisStateMap.put(axis, axisState);
            }
        } 
        return axisStateMap;
    }

    public boolean render(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, int index, org.jfree.chart.plot.PlotRenderingInfo info) {
        boolean foundData = false;
        org.jfree.data.category.CategoryDataset currentDataset = getDataset(index);
        org.jfree.chart.renderer.category.CategoryItemRenderer renderer = getRenderer(index);
        org.jfree.chart.axis.CategoryAxis domainAxis = getDomainAxisForDataset(index);
        org.jfree.chart.axis.ValueAxis rangeAxis = getRangeAxisForDataset(index);
        boolean hasData = !(org.jfree.data.general.DatasetUtilities.isEmptyOrNull(currentDataset));
        if (hasData && (renderer != null)) {
            foundData = true;
            org.jfree.chart.renderer.category.CategoryItemRendererState state = renderer.initialise(g2, dataArea, this, index, info);
            int columnCount = currentDataset.getColumnCount();
            int rowCount = currentDataset.getRowCount();
            int passCount = renderer.getPassCount();
            for (int pass = 0; pass < passCount; pass++) {
                if ((this.columnRenderingOrder) == (org.jfree.chart.util.SortOrder.ASCENDING)) {
                    for (int column = 0; column < columnCount; column++) {
                        if ((this.rowRenderingOrder) == (org.jfree.chart.util.SortOrder.ASCENDING)) {
                            for (int row = 0; row < rowCount; row++) {
                                renderer.drawItem(g2, state, dataArea, this, domainAxis, rangeAxis, currentDataset, row, column, pass);
                            }
                        }else {
                            for (int row = rowCount - 1; row >= 0; row--) {
                                renderer.drawItem(g2, state, dataArea, this, domainAxis, rangeAxis, currentDataset, row, column, pass);
                            }
                        }
                    }
                }else {
                    for (int column = columnCount - 1; column >= 0; column--) {
                        if ((this.rowRenderingOrder) == (org.jfree.chart.util.SortOrder.ASCENDING)) {
                            for (int row = 0; row < rowCount; row++) {
                                renderer.drawItem(g2, state, dataArea, this, domainAxis, rangeAxis, currentDataset, row, column, pass);
                            }
                        }else {
                            for (int row = rowCount - 1; row >= 0; row--) {
                                renderer.drawItem(g2, state, dataArea, this, domainAxis, rangeAxis, currentDataset, row, column, pass);
                            }
                        }
                    }
                }
            }
        }
        return foundData;
    }

    protected void drawDomainGridlines(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea) {
        if (isDomainGridlinesVisible()) {
            org.jfree.chart.axis.CategoryAnchor anchor = getDomainGridlinePosition();
            org.jfree.chart.util.RectangleEdge domainAxisEdge = getDomainAxisEdge();
            java.awt.Stroke gridStroke = getDomainGridlineStroke();
            java.awt.Paint gridPaint = getDomainGridlinePaint();
            if ((gridStroke != null) && (gridPaint != null)) {
                org.jfree.data.category.CategoryDataset data = getDataset();
                if (data != null) {
                    org.jfree.chart.axis.CategoryAxis axis = getDomainAxis();
                    if (axis != null) {
                        int columnCount = data.getColumnCount();
                        for (int c = 0; c < columnCount; c++) {
                            double xx = axis.getCategoryJava2DCoordinate(anchor, c, columnCount, dataArea, domainAxisEdge);
                            org.jfree.chart.renderer.category.CategoryItemRenderer renderer1 = getRenderer();
                            if (renderer1 != null) {
                                renderer1.drawDomainGridline(g2, this, dataArea, xx);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void drawRangeGridlines(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, java.util.List ticks) {
        if (isRangeGridlinesVisible()) {
            java.awt.Stroke gridStroke = getRangeGridlineStroke();
            java.awt.Paint gridPaint = getRangeGridlinePaint();
            if ((gridStroke != null) && (gridPaint != null)) {
                org.jfree.chart.axis.ValueAxis axis = getRangeAxis();
                if (axis != null) {
                    java.util.Iterator iterator = ticks.iterator();
                    while (iterator.hasNext()) {
                        org.jfree.chart.axis.ValueTick tick = ((org.jfree.chart.axis.ValueTick) (iterator.next()));
                        org.jfree.chart.renderer.category.CategoryItemRenderer renderer1 = getRenderer();
                        if (renderer1 != null) {
                            renderer1.drawRangeGridline(g2, this, getRangeAxis(), dataArea, tick.getValue());
                        }
                    } 
                }
            }
        }
    }

    protected void drawAnnotations(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, org.jfree.chart.plot.PlotRenderingInfo info) {
        java.util.Iterator iterator = getAnnotations().iterator();
        while (iterator.hasNext()) {
            org.jfree.chart.annotations.CategoryAnnotation annotation = ((org.jfree.chart.annotations.CategoryAnnotation) (iterator.next()));
            annotation.draw(g2, this, dataArea, getDomainAxis(), getRangeAxis(), 0, info);
        } 
    }

    protected void drawDomainMarkers(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, int index, org.jfree.chart.util.Layer layer) {
        org.jfree.chart.renderer.category.CategoryItemRenderer r = getRenderer(index);
        if (r == null) {
            return;
        }
        java.util.Collection markers = getDomainMarkers(index, layer);
        org.jfree.chart.axis.CategoryAxis axis = getDomainAxisForDataset(index);
        if ((markers != null) && (axis != null)) {
            java.util.Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                org.jfree.chart.plot.CategoryMarker marker = ((org.jfree.chart.plot.CategoryMarker) (iterator.next()));
                r.drawDomainMarker(g2, this, axis, marker, dataArea);
            } 
        }
    }

    protected void drawRangeMarkers(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, int index, org.jfree.chart.util.Layer layer) {
        org.jfree.chart.renderer.category.CategoryItemRenderer r = getRenderer(index);
        if (r == null) {
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

    protected void drawRangeLine(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, double value, java.awt.Stroke stroke, java.awt.Paint paint) {
        double java2D = getRangeAxis().valueToJava2D(value, dataArea, getRangeAxisEdge());
        java.awt.geom.Line2D line = null;
        if ((this.orientation) == (PlotOrientation.HORIZONTAL)) {
            line = new java.awt.geom.Line2D.Double(java2D, dataArea.getMinY(), java2D, dataArea.getMaxY());
        }else
            if ((this.orientation) == (PlotOrientation.VERTICAL)) {
                line = new java.awt.geom.Line2D.Double(dataArea.getMinX(), java2D, dataArea.getMaxX(), java2D);
            }

        g2.setStroke(stroke);
        g2.setPaint(paint);
        g2.draw(line);
    }

    protected void drawRangeCrosshair(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, org.jfree.chart.plot.PlotOrientation orientation, double value, org.jfree.chart.axis.ValueAxis axis, java.awt.Stroke stroke, java.awt.Paint paint) {
        if (!(axis.getRange().contains(value))) {
            return;
        }
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

    public org.jfree.data.Range getDataRange(org.jfree.chart.axis.ValueAxis axis) {
        org.jfree.data.Range result = null;
        java.util.List mappedDatasets = new java.util.ArrayList();
        int rangeIndex = this.rangeAxes.indexOf(axis);
        if (rangeIndex >= 0) {
            mappedDatasets.addAll(datasetsMappedToRangeAxis(rangeIndex));
        }else
            if (axis == (getRangeAxis())) {
                mappedDatasets.addAll(datasetsMappedToRangeAxis(0));
            }

        java.util.Iterator iterator = mappedDatasets.iterator();
        while (iterator.hasNext()) {
            org.jfree.data.category.CategoryDataset d = ((org.jfree.data.category.CategoryDataset) (iterator.next()));
            org.jfree.chart.renderer.category.CategoryItemRenderer r = getRendererForDataset(d);
            if (r != null) {
                result = org.jfree.data.Range.combine(result, r.findRangeBounds(d));
            }
        } 
        return result;
    }

    private java.util.List datasetsMappedToDomainAxis(int axisIndex) {
        java.util.List result = new java.util.ArrayList();
        for (int datasetIndex = 0; datasetIndex < (this.datasets.size()); datasetIndex++) {
            java.lang.Object dataset = this.datasets.get(datasetIndex);
            if (dataset != null) {
                java.lang.Integer m = ((java.lang.Integer) (this.datasetToDomainAxisMap.get(datasetIndex)));
                if (m == null) {
                    if (axisIndex == 0) {
                        result.add(dataset);
                    }
                }else {
                    if ((m.intValue()) == axisIndex) {
                        result.add(dataset);
                    }
                }
            }
        }
        return result;
    }

    private java.util.List datasetsMappedToRangeAxis(int index) {
        java.util.List result = new java.util.ArrayList();
        for (int i = 0; i < (this.datasets.size()); i++) {
            java.lang.Object dataset = this.datasets.get(i);
            if (dataset != null) {
                java.lang.Integer m = ((java.lang.Integer) (this.datasetToRangeAxisMap.get(i)));
                if (m == null) {
                    if (index == 0) {
                        result.add(dataset);
                    }
                }else {
                    if ((m.intValue()) == index) {
                        result.add(dataset);
                    }
                }
            }
        }
        return result;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public org.jfree.chart.axis.AxisSpace getFixedDomainAxisSpace() {
        return this.fixedDomainAxisSpace;
    }

    public void setFixedDomainAxisSpace(org.jfree.chart.axis.AxisSpace space) {
        this.fixedDomainAxisSpace = space;
    }

    public org.jfree.chart.axis.AxisSpace getFixedRangeAxisSpace() {
        return this.fixedRangeAxisSpace;
    }

    public void setFixedRangeAxisSpace(org.jfree.chart.axis.AxisSpace space) {
        this.fixedRangeAxisSpace = space;
    }

    public java.util.List getCategories() {
        java.util.List result = null;
        if ((getDataset()) != null) {
            result = java.util.Collections.unmodifiableList(getDataset().getColumnKeys());
        }
        return result;
    }

    public java.util.List getCategoriesForAxis(org.jfree.chart.axis.CategoryAxis axis) {
        java.util.List result = new java.util.ArrayList();
        int axisIndex = this.domainAxes.indexOf(axis);
        java.util.List datasets = datasetsMappedToDomainAxis(axisIndex);
        java.util.Iterator iterator = datasets.iterator();
        while (iterator.hasNext()) {
            org.jfree.data.category.CategoryDataset dataset = ((org.jfree.data.category.CategoryDataset) (iterator.next()));
            for (int i = 0; i < (dataset.getColumnCount()); i++) {
                java.lang.Comparable category = dataset.getColumnKey(i);
                if (!(result.contains(category))) {
                    result.add(category);
                }
            }
        } 
        return result;
    }

    public boolean getDrawSharedDomainAxis() {
        return this.drawSharedDomainAxis;
    }

    public void setDrawSharedDomainAxis(boolean draw) {
        this.drawSharedDomainAxis = draw;
        notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
    }

    public boolean isDomainZoomable() {
        return false;
    }

    public boolean isRangeZoomable() {
        return true;
    }

    public void zoomDomainAxes(double factor, org.jfree.chart.plot.PlotRenderingInfo state, java.awt.geom.Point2D source) {
    }

    public void zoomDomainAxes(double lowerPercent, double upperPercent, org.jfree.chart.plot.PlotRenderingInfo state, java.awt.geom.Point2D source) {
    }

    public void zoomDomainAxes(double factor, org.jfree.chart.plot.PlotRenderingInfo info, java.awt.geom.Point2D source, boolean useAnchor) {
    }

    public void zoomRangeAxes(double factor, org.jfree.chart.plot.PlotRenderingInfo state, java.awt.geom.Point2D source) {
        zoomRangeAxes(factor, state, source, false);
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

    public void zoomRangeAxes(double lowerPercent, double upperPercent, org.jfree.chart.plot.PlotRenderingInfo state, java.awt.geom.Point2D source) {
        for (int i = 0; i < (this.rangeAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis rangeAxis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(i)));
            if (rangeAxis != null) {
                rangeAxis.zoomRange(lowerPercent, upperPercent);
            }
        }
    }

    public double getAnchorValue() {
        return this.anchorValue;
    }

    public void setAnchorValue(double value) {
        setAnchorValue(value, true);
    }

    public void setAnchorValue(double value, boolean notify) {
        this.anchorValue = value;
        if (notify) {
            notifyListeners(new org.jfree.chart.event.PlotChangeEvent(this));
        }
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == (this)) {
            return true;
        }
        if (!(obj instanceof org.jfree.chart.plot.CategoryPlot)) {
            return false;
        }
        if (!(super.equals(obj))) {
            return false;
        }
        org.jfree.chart.plot.CategoryPlot that = ((org.jfree.chart.plot.CategoryPlot) (obj));
        if ((this.orientation) != (that.orientation)) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.axisOffset, that.axisOffset))) {
            return false;
        }
        if (!(this.domainAxes.equals(that.domainAxes))) {
            return false;
        }
        if (!(this.domainAxisLocations.equals(that.domainAxisLocations))) {
            return false;
        }
        if ((this.drawSharedDomainAxis) != (that.drawSharedDomainAxis)) {
            return false;
        }
        if (!(this.rangeAxes.equals(that.rangeAxes))) {
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
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.renderers, that.renderers))) {
            return false;
        }
        if ((this.renderingOrder) != (that.renderingOrder)) {
            return false;
        }
        if ((this.columnRenderingOrder) != (that.columnRenderingOrder)) {
            return false;
        }
        if ((this.rowRenderingOrder) != (that.rowRenderingOrder)) {
            return false;
        }
        if ((this.domainGridlinesVisible) != (that.domainGridlinesVisible)) {
            return false;
        }
        if ((this.domainGridlinePosition) != (that.domainGridlinePosition)) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.domainGridlineStroke, that.domainGridlineStroke))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.domainGridlinePaint, that.domainGridlinePaint))) {
            return false;
        }
        if ((this.rangeGridlinesVisible) != (that.rangeGridlinesVisible)) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.rangeGridlineStroke, that.rangeGridlineStroke))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.rangeGridlinePaint, that.rangeGridlinePaint))) {
            return false;
        }
        if ((this.anchorValue) != (that.anchorValue)) {
            return false;
        }
        if ((this.rangeCrosshairVisible) != (that.rangeCrosshairVisible)) {
            return false;
        }
        if ((this.rangeCrosshairValue) != (that.rangeCrosshairValue)) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.rangeCrosshairStroke, that.rangeCrosshairStroke))) {
            return false;
        }
        if (!(org.jfree.chart.util.PaintUtilities.equal(this.rangeCrosshairPaint, that.rangeCrosshairPaint))) {
            return false;
        }
        if ((this.rangeCrosshairLockedOnData) != (that.rangeCrosshairLockedOnData)) {
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
        if ((this.weight) != (that.weight)) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.fixedDomainAxisSpace, that.fixedDomainAxisSpace))) {
            return false;
        }
        if (!(org.jfree.chart.util.ObjectUtilities.equal(this.fixedRangeAxisSpace, that.fixedRangeAxisSpace))) {
            return false;
        }
        return true;
    }

    public java.lang.Object clone() throws java.lang.CloneNotSupportedException {
        org.jfree.chart.plot.CategoryPlot clone = ((org.jfree.chart.plot.CategoryPlot) (super.clone()));
        clone.domainAxes = new org.jfree.chart.util.ObjectList();
        for (int i = 0; i < (this.domainAxes.size()); i++) {
            org.jfree.chart.axis.CategoryAxis xAxis = ((org.jfree.chart.axis.CategoryAxis) (this.domainAxes.get(i)));
            if (xAxis != null) {
                org.jfree.chart.axis.CategoryAxis clonedAxis = ((org.jfree.chart.axis.CategoryAxis) (xAxis.clone()));
                clone.setDomainAxis(i, clonedAxis);
            }
        }
        clone.domainAxisLocations = ((org.jfree.chart.util.ObjectList) (this.domainAxisLocations.clone()));
        clone.rangeAxes = new org.jfree.chart.util.ObjectList();
        for (int i = 0; i < (this.rangeAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis yAxis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(i)));
            if (yAxis != null) {
                org.jfree.chart.axis.ValueAxis clonedAxis = ((org.jfree.chart.axis.ValueAxis) (yAxis.clone()));
                clone.setRangeAxis(i, clonedAxis);
            }
        }
        clone.rangeAxisLocations = ((org.jfree.chart.util.ObjectList) (this.rangeAxisLocations.clone()));
        clone.datasets = ((org.jfree.chart.util.ObjectList) (this.datasets.clone()));
        for (int i = 0; i < (clone.datasets.size()); i++) {
            org.jfree.data.category.CategoryDataset dataset = clone.getDataset(i);
            if (dataset != null) {
                dataset.addChangeListener(clone);
            }
        }
        clone.datasetToDomainAxisMap = ((org.jfree.chart.util.ObjectList) (this.datasetToDomainAxisMap.clone()));
        clone.datasetToRangeAxisMap = ((org.jfree.chart.util.ObjectList) (this.datasetToRangeAxisMap.clone()));
        clone.renderers = ((org.jfree.chart.util.ObjectList) (this.renderers.clone()));
        if ((this.fixedDomainAxisSpace) != null) {
            clone.fixedDomainAxisSpace = ((org.jfree.chart.axis.AxisSpace) (org.jfree.chart.util.ObjectUtilities.clone(this.fixedDomainAxisSpace)));
        }
        if ((this.fixedRangeAxisSpace) != null) {
            clone.fixedRangeAxisSpace = ((org.jfree.chart.axis.AxisSpace) (org.jfree.chart.util.ObjectUtilities.clone(this.fixedRangeAxisSpace)));
        }
        return clone;
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
        stream.defaultWriteObject();
        org.jfree.chart.util.SerialUtilities.writeStroke(this.domainGridlineStroke, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.domainGridlinePaint, stream);
        org.jfree.chart.util.SerialUtilities.writeStroke(this.rangeGridlineStroke, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.rangeGridlinePaint, stream);
        org.jfree.chart.util.SerialUtilities.writeStroke(this.rangeCrosshairStroke, stream);
        org.jfree.chart.util.SerialUtilities.writePaint(this.rangeCrosshairPaint, stream);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, java.lang.ClassNotFoundException {
        stream.defaultReadObject();
        this.domainGridlineStroke = org.jfree.chart.util.SerialUtilities.readStroke(stream);
        this.domainGridlinePaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        this.rangeGridlineStroke = org.jfree.chart.util.SerialUtilities.readStroke(stream);
        this.rangeGridlinePaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        this.rangeCrosshairStroke = org.jfree.chart.util.SerialUtilities.readStroke(stream);
        this.rangeCrosshairPaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
        for (int i = 0; i < (this.domainAxes.size()); i++) {
            org.jfree.chart.axis.CategoryAxis xAxis = ((org.jfree.chart.axis.CategoryAxis) (this.domainAxes.get(i)));
            if (xAxis != null) {
                xAxis.setPlot(this);
                xAxis.addChangeListener(this);
            }
        }
        for (int i = 0; i < (this.rangeAxes.size()); i++) {
            org.jfree.chart.axis.ValueAxis yAxis = ((org.jfree.chart.axis.ValueAxis) (this.rangeAxes.get(i)));
            if (yAxis != null) {
                yAxis.setPlot(this);
                yAxis.addChangeListener(this);
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
            org.jfree.chart.renderer.category.CategoryItemRenderer renderer = ((org.jfree.chart.renderer.category.CategoryItemRenderer) (this.renderers.get(i)));
            if (renderer != null) {
                renderer.addChangeListener(this);
            }
        }
    }
}

