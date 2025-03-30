package org.jfree.chart.plot;
public class MultiplePiePlot extends org.jfree.chart.plot.Plot implements java.lang.Cloneable , java.io.Serializable {
	private static final long serialVersionUID = -355377800470807389L;

	private org.jfree.chart.JFreeChart pieChart;

	private org.jfree.data.category.CategoryDataset dataset;

	private org.jfree.chart.util.TableOrder dataExtractOrder;

	private double limit = 0.0;

	private java.lang.Comparable aggregatedItemsKey;

	private transient java.awt.Paint aggregatedItemsPaint;

	private transient java.util.Map sectionPaints;

	public MultiplePiePlot() {
		this(null);
	}

	public MultiplePiePlot(org.jfree.data.category.CategoryDataset dataset) {
		super();
		this.dataset = dataset;
		org.jfree.chart.plot.PiePlot piePlot = new org.jfree.chart.plot.PiePlot(null);
		this.pieChart = new org.jfree.chart.JFreeChart(piePlot);
		this.pieChart.removeLegend();
		this.dataExtractOrder = org.jfree.chart.util.TableOrder.BY_COLUMN;
		this.pieChart.setBackgroundPaint(null);
		org.jfree.chart.title.TextTitle seriesTitle = new org.jfree.chart.title.TextTitle("Series Title", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
		seriesTitle.setPosition(org.jfree.chart.util.RectangleEdge.BOTTOM);
		this.pieChart.setTitle(seriesTitle);
		this.aggregatedItemsKey = "Other";
		this.aggregatedItemsPaint = java.awt.Color.lightGray;
		this.sectionPaints = new java.util.HashMap();
	}

	public org.jfree.data.category.CategoryDataset getDataset() {
		return this.dataset;
	}

	public void setDataset(org.jfree.data.category.CategoryDataset dataset) {
		if (this.dataset != null) {
			this.dataset.removeChangeListener(this);
		}
		this.dataset = dataset;
		if (dataset != null) {
			setDatasetGroup(dataset.getGroup());
			dataset.addChangeListener(this);
		}
		datasetChanged(new org.jfree.data.general.DatasetChangeEvent(this, dataset));
	}

	public org.jfree.chart.JFreeChart getPieChart() {
		return this.pieChart;
	}

	public void setPieChart(org.jfree.chart.JFreeChart pieChart) {
		if (pieChart == null) {
			throw new java.lang.IllegalArgumentException("Null 'pieChart' argument.");
		}
		if (!(pieChart.getPlot() instanceof org.jfree.chart.plot.PiePlot)) {
			throw new java.lang.IllegalArgumentException("The 'pieChart' argument must " + "be a chart based on a PiePlot.");
		}
		this.pieChart = pieChart;
		fireChangeEvent();
	}

	public org.jfree.chart.util.TableOrder getDataExtractOrder() {
		return this.dataExtractOrder;
	}

	public void setDataExtractOrder(org.jfree.chart.util.TableOrder order) {
		if (order == null) {
			throw new java.lang.IllegalArgumentException("Null 'order' argument");
		}
		this.dataExtractOrder = order;
		fireChangeEvent();
	}

	public double getLimit() {
		return this.limit;
	}

	public void setLimit(double limit) {
		this.limit = limit;
		fireChangeEvent();
	}

	public java.lang.Comparable getAggregatedItemsKey() {
		return this.aggregatedItemsKey;
	}

	public void setAggregatedItemsKey(java.lang.Comparable key) {
		if (key == null) {
			throw new java.lang.IllegalArgumentException("Null 'key' argument.");
		}
		this.aggregatedItemsKey = key;
		fireChangeEvent();
	}

	public java.awt.Paint getAggregatedItemsPaint() {
		return this.aggregatedItemsPaint;
	}

	public void setAggregatedItemsPaint(java.awt.Paint paint) {
		if (paint == null) {
			throw new java.lang.IllegalArgumentException("Null 'paint' argument.");
		}
		this.aggregatedItemsPaint = paint;
		fireChangeEvent();
	}

	public java.lang.String getPlotType() {
		return "Multiple Pie Plot";
	}

	public void draw(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D area, java.awt.geom.Point2D anchor, org.jfree.chart.plot.PlotState parentState, org.jfree.chart.plot.PlotRenderingInfo info) {
		org.jfree.chart.util.RectangleInsets insets = getInsets();
		insets.trim(area);
		drawBackground(g2, area);
		drawOutline(g2, area);
		if (org.jfree.data.general.DatasetUtilities.isEmptyOrNull(this.dataset)) {
			drawNoDataMessage(g2, area);
			return;
		}
		int pieCount = 0;
		if (this.dataExtractOrder == org.jfree.chart.util.TableOrder.BY_ROW) {
			pieCount = this.dataset.getRowCount();
		} else {
			pieCount = this.dataset.getColumnCount();
		}
		int displayCols = ((int) (java.lang.Math.ceil(java.lang.Math.sqrt(pieCount))));
		int displayRows = ((int) (java.lang.Math.ceil(((double) (pieCount)) / ((double) (displayCols)))));
		if ((displayCols > displayRows) && (area.getWidth() < area.getHeight())) {
			int temp = displayCols;
			displayCols = displayRows;
			displayRows = temp;
		}
		prefetchSectionPaints();
		int x = ((int) (area.getX()));
		int y = ((int) (area.getY()));
		int width = ((int) (area.getWidth())) / displayCols;
		int height = ((int) (area.getHeight())) / displayRows;
		int row = 0;
		int column = 0;
		int diff = (displayRows * displayCols) - pieCount;
		int xoffset = 0;
		java.awt.Rectangle rect = new java.awt.Rectangle();
		for (int pieIndex = 0; pieIndex < pieCount; pieIndex++) {
			rect.setBounds((x + xoffset) + (width * column), y + (height * row), width, height);
			java.lang.String title = null;
			if (this.dataExtractOrder == org.jfree.chart.util.TableOrder.BY_ROW) {
				title = this.dataset.getRowKey(pieIndex).toString();
			} else {
				title = this.dataset.getColumnKey(pieIndex).toString();
			}
			this.pieChart.setTitle(title);
			org.jfree.data.general.PieDataset piedataset = null;
			org.jfree.data.general.PieDataset dd = new org.jfree.data.category.CategoryToPieDataset(this.dataset, this.dataExtractOrder, pieIndex);
			if (this.limit > 0.0) {
				piedataset = org.jfree.data.general.DatasetUtilities.createConsolidatedPieDataset(dd, this.aggregatedItemsKey, this.limit);
			} else {
				piedataset = dd;
			}
			org.jfree.chart.plot.PiePlot piePlot = ((org.jfree.chart.plot.PiePlot) (this.pieChart.getPlot()));
			piePlot.setDataset(piedataset);
			piePlot.setPieIndex(pieIndex);
			for (int i = 0; i < piedataset.getItemCount(); i++) {
				java.lang.Comparable key = piedataset.getKey(i);
				java.awt.Paint p;
				if (key.equals(this.aggregatedItemsKey)) {
					p = this.aggregatedItemsPaint;
				} else {
					p = ((java.awt.Paint) (this.sectionPaints.get(key)));
				}
				piePlot.setSectionPaint(key, p);
			}
			org.jfree.chart.ChartRenderingInfo subinfo = null;
			if (info != null) {
				subinfo = new org.jfree.chart.ChartRenderingInfo();
			}
			this.pieChart.draw(g2, rect, subinfo);
			if (info != null) {
				info.getOwner().getEntityCollection().addAll(subinfo.getEntityCollection());
				info.addSubplotInfo(subinfo.getPlotInfo());
			}
			++column;
			if (column == displayCols) {
				column = 0;
				++row;
				if ((row == (displayRows - 1)) && (diff != 0)) {
					xoffset = (diff * width) / 2;
				}
			}
		}
	}

	private void prefetchSectionPaints() {
		org.jfree.chart.plot.PiePlot piePlot = ((org.jfree.chart.plot.PiePlot) (getPieChart().getPlot()));
		if (this.dataExtractOrder == org.jfree.chart.util.TableOrder.BY_ROW) {
			for (int c = 0; c < this.dataset.getColumnCount(); c++) {
				java.lang.Comparable key = this.dataset.getColumnKey(c);
				java.awt.Paint p = piePlot.getSectionPaint(key);
				if (p == null) {
					p = ((java.awt.Paint) (this.sectionPaints.get(key)));
					if (p == null) {
						p = getDrawingSupplier().getNextPaint();
					}
				}
				this.sectionPaints.put(key, p);
			}
		} else {
			for (int r = 0; r < this.dataset.getRowCount(); r++) {
				java.lang.Comparable key = this.dataset.getRowKey(r);
				java.awt.Paint p = piePlot.getSectionPaint(key);
				if (p == null) {
					p = ((java.awt.Paint) (this.sectionPaints.get(key)));
					if (p == null) {
						p = getDrawingSupplier().getNextPaint();
					}
				}
				this.sectionPaints.put(key, p);
			}
		}
	}

	public org.jfree.chart.LegendItemCollection getLegendItems() {
		org.jfree.chart.LegendItemCollection result = new org.jfree.chart.LegendItemCollection();
		if (this.dataset != null) {
			java.util.List keys = null;
			prefetchSectionPaints();
			if (this.dataExtractOrder == org.jfree.chart.util.TableOrder.BY_ROW) {
				keys = this.dataset.getColumnKeys();
			} else if (this.dataExtractOrder == org.jfree.chart.util.TableOrder.BY_COLUMN) {
				keys = this.dataset.getRowKeys();
			}
			if (keys != null) {
				int section = 0;
				java.util.Iterator iterator = keys.iterator();
				while (iterator.hasNext()) {
					java.lang.Comparable key = ((java.lang.Comparable) (iterator.next()));
					java.lang.String label = key.toString();
					java.lang.String description = label;
					java.awt.Paint paint = ((java.awt.Paint) (this.sectionPaints.get(key)));
					org.jfree.chart.LegendItem item = new org.jfree.chart.LegendItem(label, description, null, null, org.jfree.chart.plot.Plot.DEFAULT_LEGEND_ITEM_CIRCLE, paint, org.jfree.chart.plot.Plot.DEFAULT_OUTLINE_STROKE, paint);
					item.setDataset(getDataset());
					result.add(item);
					section++;
				} 
			}
			if (this.limit > 0.0) {
				result.add(new org.jfree.chart.LegendItem(this.aggregatedItemsKey.toString(), this.aggregatedItemsKey.toString(), null, null, org.jfree.chart.plot.Plot.DEFAULT_LEGEND_ITEM_CIRCLE, this.aggregatedItemsPaint, org.jfree.chart.plot.Plot.DEFAULT_OUTLINE_STROKE, this.aggregatedItemsPaint));
			}
		}
		return result;
	}

	public boolean equals(java.lang.Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof org.jfree.chart.plot.MultiplePiePlot)) {
			return false;
		}
		org.jfree.chart.plot.MultiplePiePlot that = ((org.jfree.chart.plot.MultiplePiePlot) (obj));
		if (this.dataExtractOrder != that.dataExtractOrder) {
			return false;
		}
		if (this.limit != that.limit) {
			return false;
		}
		if (!this.aggregatedItemsKey.equals(that.aggregatedItemsKey)) {
			return false;
		}
		if (!org.jfree.chart.util.PaintUtilities.equal(this.aggregatedItemsPaint, that.aggregatedItemsPaint)) {
			return false;
		}
		if (!org.jfree.chart.util.ObjectUtilities.equal(this.pieChart, that.pieChart)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		return true;
	}

	private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
		stream.defaultWriteObject();
		org.jfree.chart.util.SerialUtilities.writePaint(this.aggregatedItemsPaint, stream);
	}

	private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, java.lang.ClassNotFoundException {
		stream.defaultReadObject();
		this.aggregatedItemsPaint = org.jfree.chart.util.SerialUtilities.readPaint(stream);
		this.sectionPaints = new java.util.HashMap();
	}
}