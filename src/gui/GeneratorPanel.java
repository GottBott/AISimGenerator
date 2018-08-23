package gui;
import generate.Distribution;
import generate.IDurationDistribution;
import generate.ILoadDistribution;
import generate.IRewardDistribution;
import generate.ITimePressureDistribution;
import generate.LoadFirstDurationGenerator;
import generate.MethodGenerator;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import project.Distribute;
import sexpr.Sexpr;
import sexpr.SexprParser;

/**
 *
 * @author abimbolaakinmeji
 */

@SuppressWarnings("serial")
public class GeneratorPanel extends javax.swing.JPanel {
	ChartPanel loadPanel;
	ChartPanel slackPanel;
	ChartPanel rewardPanel;
	GridBagLayout Layout = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	JPanel TpContainer = new JPanel();
	JPanel SdContainer = new JPanel();
	JSlider sliderx = new JSlider();
	JSlider slidery = new JSlider(0, 20);
	JLabel labelx = new JLabel();
	JLabel labely = new JLabel();
	JLabel setdomain = new JLabel();
	NumericTextField domaininput = new NumericTextField();
	JLabel setDuration = new JLabel();
	NumericTextField durationInput = new NumericTextField();
	JFreeChart loadChart;
	JFreeChart slackChart;
	JFreeChart rewardChart;
	double sliderxv;
	double slideryv;
	JButton Openfile = new JButton("Open File");
	JButton SaveFile = new JButton("Save File");
	JButton Toggle = new JButton("Toggle");
	JButton generate = new JButton("Generate");
	JButton Normal = new JButton("Normal");
	JButton Uniform = new JButton("Uniform");
	JButton Poisson = new JButton("Poisson");
	JButton slack = new JButton("Slack");
	JButton reward = new JButton("Reward");
	JButton load = new JButton("Load");
	JButton Submit = new JButton("Submit");
	JButton SubmitDuration = new JButton("Submit");
	JButton Random = new JButton("Random");
	JButton Clear = new JButton("Clear");
	JButton Export = new JButton("Export");
	FlowLayout Fl2 = new FlowLayout(FlowLayout.CENTER);
	XYSeries currentSeries;
	String currentSeriesName;
	XYSeries LdSeries = new XYSeries("Load", false, false);
	XYSeries slackSeries = new XYSeries("Slack", false, false);
	XYSeries Reward = new XYSeries("Reward", false, false);
	XYSeries generatedLoadSeries = new XYSeries("Generated Load", false, false);
	XYSeries generatedSlackSeries = new XYSeries("Generated Slack", false,
			false);
	XYSeries generatedRewardSeries = new XYSeries("Generated Reward", false,
			false);
	XYSeriesCollection loadCollection = new XYSeriesCollection();
	XYSeriesCollection slackCollection = new XYSeriesCollection();
	XYSeriesCollection rewardCollection = new XYSeriesCollection();
	double NormalV;
	double PoissonV;
	IntervalXYDataset loadDataset;
	IntervalXYDataset slackDataset;
	IntervalXYDataset rewardDataset;
	String FileText;
	MethodGenerator methodGenerator;
	boolean generated = false;
	boolean opened = false;
	JPanel self = this;
	Frame frame;
	JPanel currentPanel;
	double domainV;
	double lastDomain;
	int duration = 10;
	JList<Integer> list;

	private ToggleState toggleState = ToggleState.Input;

	enum ToggleState {
		Input, GeneratedAndInput, Generated,
	}

	/**
	 * Creates new form NewJPanel
	 * 
	 * @param frame2
	 */

	public GeneratorPanel(JFrame frame2) {
		initComponents();
		createLoadGraph();
		createSlackGraph();
		createRewardGraph();
		this.add(slackPanel);
		currentSeries = slackSeries;
		currentPanel = slackPanel;
		this.frame = frame2;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {
		setLayout(Layout);
		this.TpContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
		Fl2.setAlignOnBaseline(true);
		this.SdContainer.setLayout(Fl2);
		this.add(TpContainer);
		this.add(SdContainer);
		this.add(sliderx);
		this.add(slidery);
		this.add(labelx);
		this.add(labely);
		SdContainer.add(Normal);
		SdContainer.add(Poisson);
		SdContainer.add(Uniform);
		SdContainer.add(Random);
		SdContainer.add(load);
		SdContainer.add(slack);
		SdContainer.add(reward);
		SdContainer.add(Clear);
		SdContainer.add(Export);
		labely.setText("Y-VALUE : ");
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		Layout.addLayoutComponent(labely, c);
		labelx.setText("X-VALUE : ");
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.PAGE_END;
		Layout.addLayoutComponent(labelx, c);
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		Layout.addLayoutComponent(sliderx, c);
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		slidery.setOrientation(JSlider.VERTICAL);
		Layout.addLayoutComponent(slidery, c);
		TpContainer.add(Openfile);
		TpContainer.add(SaveFile);
		TpContainer.add(Toggle);
		TpContainer.add(generate);
		TpContainer.add(setdomain);
		TpContainer.add(domaininput);
		TpContainer.add(Submit);
		TpContainer.add(setDuration);
		TpContainer.add(durationInput);
		TpContainer.add(SubmitDuration);
		setDuration.setText("Duration");
		durationInput.setColumns(5);
		setdomain.setText("Time Horizon");
		domaininput.setColumns(5);
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		Layout.addLayoutComponent(this.TpContainer, c);
		c.gridx = 1;
		c.gridy = 2;
		// c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		Layout.addLayoutComponent(SdContainer, c);
		ButtonListeners();
		domainV = 100;
		durationInput.setText("10");
		domaininput.setText("100");
	}

	public int[] convertSeries(XYSeries series) {

		double[][] data = series.toArray();
		int size = (int) domainV;
		int[] converted = new int[size];
		for (int i = 0; i < data[1].length && i < size; i++) {
			if (data[0][i] < size)
				converted[(int) data[0][i]] = (int) data[1][i];
		}

		return converted;

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public final void createLoadGraph() {
		loadDataset = createLoadDataset();
		loadChart = createLoadChart(loadDataset);
		loadPanel = new ChartPanel(loadChart);
		loadPanel.setSize(400, 400);

	}

	public final void createSlackGraph() {
		slackDataset = createSlackDataset();
		slackChart = createSlackChart(slackDataset);
		slackPanel = new ChartPanel(slackChart);
		slackPanel.setSize(400, 400);
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		Layout.addLayoutComponent(slackPanel, c);

	}

	public final void createRewardGraph() {
		rewardDataset = createRewardDataset();
		rewardChart = createRewardChart(rewardDataset);
		rewardPanel = new ChartPanel(rewardChart);
		rewardPanel.setSize(400, 400);

	}

	public void ButtonListeners() {
		sliderx.setValue(0);
		slidery.setValue(0);

		sliderx.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (currentSeries == null) {
					JOptionPane
							.showMessageDialog(null,
									"Please Select A Type Of Graph Before Trying To Plot A Distribution");
				} else {

					sliderxv = ((JSlider) e.getSource()).getValue();
					labelx.setText("X-VALUE : "
							+ String.valueOf(sliderx.getValue()));
					
					double[][] data = currentSeries.toArray();
					boolean found = false;
					for (int i = 0; i < data[0].length; i++) {
						if (data[0][i] == sliderxv) {
							slidery.setValue((int)data[1][i]);
							found = true;
						}
					}
					if (!found) 
						slidery.setValue(0);
				}

			}

		});

		slidery.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (currentSeries == null) {
					JOptionPane
							.showMessageDialog(null,
									"Please Select A Type Of Graph Before Trying To Plot A Distribution");
				} else {
					slideryv = ((JSlider) e.getSource()).getValue();
					labely.setText("Y-VALUE : "
							+ String.valueOf(slidery.getValue()));

					try {
						currentSeries.remove(sliderxv);
						self.updateUI();
					} catch (Exception ex) {
						currentSeries.addOrUpdate(sliderxv, slideryv);
					}

					currentSeries.addOrUpdate(sliderxv, slideryv);
				}

			}

		});
		Normal.addActionListener(normalButtonActionListener);
		Random.addActionListener(randomButtonActionListener);
		Poisson.addActionListener(poissonButtonActionListener);
		Uniform.addActionListener(uniformButtonActionListener);

		load.addActionListener(loadButtonActionListener);
		slack.addActionListener(slackButtonActionListener);
		reward.addActionListener(rewardButtonActionListener);

		Clear.addActionListener(clearButtonActionListener);
		generate.addActionListener(generateButtonActionListener);
		Openfile.addActionListener(openfileButtonActionListener);
		SaveFile.addActionListener(savefileButtonActionListener);
		Toggle.addActionListener(toggleButtonActionListener);
		Submit.addActionListener(submitButtonActionListener);
		SubmitDuration.addActionListener(submitDurationButtonActionListener);
		Export.addActionListener(exportButtonActionListener);
	}

	public void clearExtra(double lastDomain2) {
		for (double i = domainV + 1; i <= lastDomain2; i++) {
			try {
				currentSeries.remove(i);
			} catch (Exception e) {

			}
		}
	}

	private XYSeriesCollection createLoadDataset() {

		try {
			loadCollection.addSeries(LdSeries);

		} catch (Exception exp) {

		}

		return loadCollection;

	}

	private XYSeriesCollection createSlackDataset() {

		try {
			slackCollection.addSeries(slackSeries);
		} catch (Exception exp) {

		}
		return slackCollection;

	}

	private XYSeriesCollection createRewardDataset() {

		try {
			rewardCollection.addSeries(Reward);
		} catch (Exception exp) {

		}
		return rewardCollection;

	}

	private JFreeChart createLoadChart(final IntervalXYDataset dataset) {

		loadChart = ChartFactory.createHistogram("Load", // chart title
				"Time", // x axis label
				"Load (Number of Tasks)", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		loadChart.setBackgroundPaint(Color.white);

		loadChart.setBackgroundPaint(new Color(230, 230, 230));
		XYPlot xyplot = (XYPlot) loadChart.getPlot();
		xyplot.setForegroundAlpha(0.7F);
		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(new Color(150, 150, 150));
		xyplot.setRangeGridlinePaint(new Color(150, 150, 150));
		XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
		NumberAxis domain = (NumberAxis) xyplot.getDomainAxis();
		domain.setRange(0.00, domainV);
		xybarrenderer.setShadowVisible(false);
		xybarrenderer.setBarPainter(new StandardXYBarPainter());
		return loadChart;
	}

	private JFreeChart createSlackChart(final IntervalXYDataset dataset) {

		slackChart = ChartFactory.createHistogram("Slack", // chart title
				"Time", // x axis label
				"Slack (Percent Extra of Duration", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		slackChart.setBackgroundPaint(Color.white);

		slackChart.setBackgroundPaint(new Color(230, 230, 230));
		XYPlot xyplot = (XYPlot) slackChart.getPlot();
		xyplot.setForegroundAlpha(0.7F);
		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(new Color(150, 150, 150));
		xyplot.setRangeGridlinePaint(new Color(150, 150, 150));
		XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
		NumberAxis domain = (NumberAxis) xyplot.getDomainAxis();
		domain.setRange(0.00, domainV);
		xybarrenderer.setShadowVisible(false);
		xybarrenderer.setBarPainter(new StandardXYBarPainter());
		// xybarrenderer.setDrawBarOutline(false);
		return slackChart;
	}

	private JFreeChart createRewardChart(final IntervalXYDataset dataset) {

		rewardChart = ChartFactory.createHistogram("Reward", // chart title
				"Time", // x axis label
				"Reward", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		rewardChart.setBackgroundPaint(Color.white);
		rewardChart.setBackgroundPaint(new Color(230, 230, 230));
		XYPlot xyplot = (XYPlot) rewardChart.getPlot();
		xyplot.setForegroundAlpha(0.7F);
		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(new Color(150, 150, 150));
		xyplot.setRangeGridlinePaint(new Color(150, 150, 150));
		XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
		NumberAxis domain = (NumberAxis) xyplot.getDomainAxis();
		domain.setRange(0.00, domainV);
		xybarrenderer.setShadowVisible(false);
		xybarrenderer.setBarPainter(new StandardXYBarPainter());
		return rewardChart;
	}

	private void setGraphWithPoissonDistribution(double lambda, double scaleX,
			double scaleY) {
		for (int i = 0; i <= domainV; i++) {
			int value = (int) (scaleY * Distribution.Poisson(lambda,
					(i * scaleX)));
			if (value > 0)
				currentSeries.addOrUpdate(i, value);
		}
		self.updateUI();
	}

	private void setGraphWithNormalDistribution(double mean, double std,
			double scalar) {
		for (int i = 0; i <= domainV; i++) {
			int value = (int) (scalar * Distribution.Normal(mean, std, i));
			if (value > 0)
				currentSeries.addOrUpdate(i, value);
		}
		self.updateUI();
	}

	private void setGraphWithUniformDistribution(double value, int rangeStart,
			int rangeEnd) {
		for (int i = rangeStart; i <= rangeEnd; i++) {
			currentSeries.addOrUpdate(i, value);
		}
		self.updateUI();
	}

	private void setGraphWithUniformRandomDistribution(double min, double max) {
		for (int i = 0; i <= domainV; i++) {
			double value = Distribution.UniformRandom(min, max);

			currentSeries.addOrUpdate(i, value);

		}
		self.updateUI();
	}

	private void setSeriesWithHistorgram(double[] data, XYSeries series) {
		// double[] tpgen = methodGenerator.histStructureTimePressure();
		// XYSeries TPGenSeries = new XYSeries("Time Pressure Generated");
		List<Integer> graph = new ArrayList<Integer>();
		for (int i = 0; i < data.length; i++) {
			while (data[i] >= graph.size())
				graph.add(0);
			graph.set((int) data[i], graph.get((int) data[i]) + 1);
		}
		series.clear();
		for (int i = 0; i < graph.size(); i++) {
			series.addOrUpdate(i, (double) graph.get(i));
		}
	}

	ActionListener generateButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			LoadFirstDurationGenerator lfg = new LoadFirstDurationGenerator();
			final int[] loadDist = convertSeries(LdSeries);
			// final int[] OpenDist = convertSeries(Open);
			final int[] TpDist = convertSeries(slackSeries);
			final int[] RewardDist = convertSeries(Reward);

			lfg.setLoadDist(new ILoadDistribution() {

				@Override
				public int getTimeScale() {
					return loadDist.length;
				}

				@Override
				public int getLoadAtTimeUnit(int timeUnit) {
					return loadDist[timeUnit];
				}
			});

			lfg.setDurationDist(new IDurationDistribution() {

				@Override
				public int getDurationAtArrivalTime(int arrivalTime) {
					// TODO Auto-generated method stub
					return duration;
				}
			});

			lfg.setRewardDist(new IRewardDistribution() {

				@Override
				public int getRewardWithArrivalTime(int arrivalTime) {
					return RewardDist[arrivalTime];
				}
			});

			lfg.setTimePressureDist(new ITimePressureDistribution() {

				@Override
				public double getTimePressureFor(int arrivalTime) {
					return TpDist[arrivalTime] / 100.0;
				}
			});
			try {
				methodGenerator = lfg;
				methodGenerator.generate();

				setSeriesWithHistorgram(methodGenerator.histStructureLoad(),
						generatedLoadSeries);
				setSeriesWithHistorgram(
						methodGenerator.histStructureTimePressure(),
						generatedSlackSeries);
				setSeriesWithHistorgram(methodGenerator.histStructureReward(),
						generatedRewardSeries);

				generated = true;
				JOptionPane.showMessageDialog(null, "Generated "
						+ methodGenerator.getGeneratedMethods().size()
						+ " methods from input graphs.");
			} catch (Exception exp) {
				JOptionPane.showMessageDialog(null,
						"Error during Generation, Please check The Console");
				exp.printStackTrace();
				generated = false;
			}
		}
	};

	ActionListener openfileButtonActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			JFileChooser chooser = new JFileChooser();
			chooser.setMultiSelectionEnabled(false);
			int option = chooser.showOpenDialog(GeneratorPanel.this);
			if (option == JFileChooser.APPROVE_OPTION) {
				File sf = chooser.getSelectedFile();

				try {
					FileText = readFile(sf.getPath(), Charset.defaultCharset());
					JOptionPane.showMessageDialog(null, sf.getName()
							+ " Has been Opened");
					opened = true;
				} catch (IOException e) {
					opened = false;
					e.printStackTrace();
				}

			}

		}
	};

	ActionListener savefileButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (opened == false || generated == false) {
				JOptionPane
						.showMessageDialog(null,
								"Please Make Sure you have Opened A file and Generated Distributions First");
			} else {
				SexprParser p = new SexprParser();
				List<Sexpr> structure = null;
				try {
					structure = p.parse(FileText);
					Distribute.ToSexprs(structure,
							methodGenerator.getGeneratedMethods());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.toString());
					return;
				}
				
				JOptionPane.showMessageDialog(null, "" + methodGenerator.getMethodsUsed() + " methods used out of " + methodGenerator.getGeneratedMethods().size() + " generated.");

				String finalStructure = "";
				for (Sexpr exp : structure)
					finalStructure += exp.Emit();
				
				// End On Save
				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showSaveDialog(GeneratorPanel.this);
				if (option == JFileChooser.APPROVE_OPTION) {

					File file = fileChooser.getSelectedFile();
					// save to file
					try (FileWriter fw = new FileWriter(file + ".txt")) {
						fw.write(finalStructure);
						fw.close();
					} catch (Exception ex) {

					}
				}

			}
		}
	};

	ActionListener exportButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			int[] items = convertSeries(currentSeries);
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showSaveDialog(GeneratorPanel.this);
			if (option == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();
				// save to file
				try (FileWriter fw = new FileWriter(file + ".txt")) {
					int total = 0;
					for (int i = 0; i <= items.length - 1; i++) {
						fw.write(i + " : " + String.valueOf(items[i] + "\n"));
						total += i;
					}
					fw.close();
				}

				catch (Exception ex) {

				}
			}
		}
	};

	ActionListener loadButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			self.validate();
			if (currentSeries == null) {
				currentSeries = LdSeries;
				currentSeriesName = "Load";
				currentPanel = loadPanel;

				c.gridx = 1;
				c.gridy = 1;
				c.anchor = GridBagConstraints.CENTER;
				self.add(loadPanel);
				Layout.addLayoutComponent(loadPanel, c);
				self.updateUI();
			} else {
				self.remove(currentPanel);
				currentSeries = LdSeries;
				currentSeriesName = "Load";
				currentPanel = loadPanel;

				c.gridx = 1;
				c.gridy = 1;
				c.anchor = GridBagConstraints.CENTER;
				self.add(currentPanel);
				Layout.addLayoutComponent(loadPanel, c);
				self.updateUI();
				;
			}

		}

	};

	ActionListener slackButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			self.validate();
			if (currentSeries == null) {
				currentSeries = slackSeries;
				currentSeriesName = "Slack";
				currentPanel = slackPanel;
				c.gridx = 1;
				c.gridy = 1;
				c.anchor = GridBagConstraints.CENTER;
				self.add(currentPanel);
				Layout.addLayoutComponent(slackPanel, c);
				self.updateUI();
			} else {
				self.remove(currentPanel);
				currentSeries = slackSeries;
				currentSeriesName = "Slack";
				currentPanel = slackPanel;
				c.gridx = 1;
				c.gridy = 1;
				c.anchor = GridBagConstraints.CENTER;
				self.add(currentPanel);
				Layout.addLayoutComponent(slackPanel, c);
				self.updateUI();
			}

		}

	};

	ActionListener rewardButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			self.validate();
			if (currentSeries == null) {
				currentSeries = Reward;
				currentSeriesName = "Reward";
				currentPanel = rewardPanel;
				c.gridx = 1;
				c.gridy = 1;
				c.anchor = GridBagConstraints.CENTER;
				self.add(rewardPanel);
				Layout.addLayoutComponent(rewardPanel, c);
				self.updateUI();

			} else {
				self.remove(currentPanel);
				currentSeries = Reward;
				currentSeriesName = "Reward";
				currentPanel = rewardPanel;
				c.gridx = 1;
				c.gridy = 1;
				c.anchor = GridBagConstraints.CENTER;
				self.add(rewardPanel);
				Layout.addLayoutComponent(rewardPanel, c);
				self.updateUI();

			}

		}

	};

	ActionListener clearButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				currentSeries.clear();
			} catch (Exception exp) {
				self.updateUI();
			} finally {
				self.updateUI();
			}

		}

	};

	ActionListener submitButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			lastDomain = domainV;
			domainV = Double.parseDouble(domaininput.getText());
			clearExtra(lastDomain);
			slackChart.getXYPlot().getDomainAxis().setRange(0, domainV);
			loadChart.getXYPlot().getDomainAxis().setRange(0, domainV);
			rewardChart.getXYPlot().getDomainAxis().setRange(0, domainV);
			self.updateUI();

		}

	};

	ActionListener submitDurationButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				duration = Integer.parseInt(durationInput.getText());
			} catch (Exception exp) {

			}
		}

	};

	ActionListener toggleButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (toggleState) {
			case Input:
				toggleState = ToggleState.GeneratedAndInput;
				break;
			case GeneratedAndInput:
				toggleState = ToggleState.Generated;
				break;
			case Generated:
				toggleState = ToggleState.Input;
			}

			switch (toggleState) {
			case Input:
				slackCollection.removeAllSeries();
				slackCollection.addSeries(slackSeries);
				rewardCollection.removeAllSeries();
				rewardCollection.addSeries(Reward);
				loadCollection.removeAllSeries();
				loadCollection.addSeries(LdSeries);
				break;
			case GeneratedAndInput:
				slackCollection.removeAllSeries();
				slackCollection.addSeries(slackSeries);
				slackCollection.addSeries(generatedSlackSeries);
				rewardCollection.removeAllSeries();
				rewardCollection.addSeries(Reward);
				rewardCollection.addSeries(generatedRewardSeries);
				loadCollection.removeAllSeries();
				loadCollection.addSeries(LdSeries);
				loadCollection.addSeries(generatedLoadSeries);
				break;
			case Generated:
				slackCollection.removeAllSeries();
				slackCollection.addSeries(generatedSlackSeries);
				rewardCollection.removeAllSeries();
				rewardCollection.addSeries(generatedRewardSeries);
				loadCollection.removeAllSeries();
				loadCollection.addSeries(generatedLoadSeries);
				break;
			}

			self.updateUI();
		}

	};

	ActionListener normalButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (currentSeries == null) {
				JOptionPane
						.showMessageDialog(null,
								"Please Select A Type Of Graph Before Trying To Plot A Distribution");
			} else {
				NumericTextField Mean = new NumericTextField();
				Mean.setColumns(5);
				NumericTextField STD = new NumericTextField();
				STD.setColumns(5);
				NumericTextField scalar = new NumericTextField();
				scalar.setColumns(5);
				scalar.setText("1");

				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Mean:"));
				myPanel.add(Mean);
				myPanel.add(Box.createHorizontalStrut(4)); // a spacer
				myPanel.add(new JLabel("Standard Deviation:"));
				myPanel.add(STD);
				myPanel.add(Box.createHorizontalStrut(4)); // a spacer
				myPanel.add(new JLabel("Scalar:"));
				myPanel.add(scalar);
				myPanel.add(Box.createHorizontalStrut(4)); // a spacer

				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Please Enter Mean, Standard Deviation",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					setGraphWithNormalDistribution(
							Double.parseDouble(Mean.getText()),
							Double.parseDouble(STD.getText()),
							Double.parseDouble(scalar.getText()));
				}
			}
		}
	};

	ActionListener poissonButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentSeries == null) {
				JOptionPane
						.showMessageDialog(null,
								"Please Select A Type Of Graph Before Trying To Plot A Distribution");
			} else {

				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Lambda:"));
				NumericTextField Lambda = new NumericTextField();
				Lambda.setColumns(5);
				myPanel.add(Lambda);
				myPanel.add(Box.createHorizontalStrut(4)); // a spacer
				NumericTextField scalarx = new NumericTextField();
				scalarx.setColumns(5);
				scalarx.setText("1");
				myPanel.add(new JLabel("Scalar X:"));
				myPanel.add(scalarx);
				myPanel.add(Box.createHorizontalStrut(4)); // a spacer
				NumericTextField scalary = new NumericTextField();
				scalary.setColumns(5);
				scalary.setText("1");
				myPanel.add(new JLabel("Scalar Y:"));
				myPanel.add(scalary);
				myPanel.add(Box.createHorizontalStrut(4)); // a spacer

				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Please Enter Lambda", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					setGraphWithPoissonDistribution(
							Double.parseDouble(Lambda.getText()),
							Double.parseDouble(scalarx.getText()),
							Double.parseDouble(scalary.getText()));
				}
			}
		}
	};

	ActionListener uniformButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (currentSeries == null) {
				JOptionPane
						.showMessageDialog(null,
								"Please Select A Type Of Graph Before Trying To Plot A Distribution");
			} else {
				NumericTextField Value = new NumericTextField();
				Value.setColumns(5);
				NumericTextField Domain = new NumericTextField();
				Domain.setColumns(5);
				NumericTextField EndDomain = new NumericTextField();
				EndDomain.setColumns(5);
				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Value:"));
				myPanel.add(Value);
				myPanel.add(new JLabel("Start Domain:"));
				myPanel.add(Domain);
				myPanel.add(new JLabel("Stop Domain:"));
				myPanel.add(EndDomain);
				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Please Enter Value", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					int xEnd = Integer.parseInt(EndDomain.getText());
					int xStart = Integer.parseInt(Domain.getText());
					if (xStart > xEnd) {
						JOptionPane
								.showMessageDialog(null,
										"Minimum Value is Greater than Maximum Values, Please Re-input Values");
					} else {

						setGraphWithUniformDistribution(
								Double.parseDouble(Value.getText()), xStart,
								xEnd);
					}
				}
			}
		}
	};

	ActionListener randomButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (currentSeries == null) {
				JOptionPane
						.showMessageDialog(null,
								"Please Select A Type Of Graph Before Trying To Plot A Distribution");
			} else {
				NumericTextField min = new NumericTextField();
				min.setColumns(5);
				NumericTextField max = new NumericTextField();
				max.setColumns(5);

				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Minimum Range:"));
				myPanel.add(min);
				myPanel.add(Box.createHorizontalStrut(4)); // a spacer
				myPanel.add(new JLabel("Maximum Range:"));
				myPanel.add(max);
				myPanel.add(Box.createHorizontalStrut(4)); // a spacer

				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Please Enter Minimum and Maximum Time Values",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					if (Double.parseDouble(min.getText()) > Double
							.parseDouble(max.getText())) {
						JOptionPane
								.showMessageDialog(null,
										"Minimum Range is Greater than Maximum Range, Please Re-input Values");
					} else {
						setGraphWithUniformRandomDistribution(
								Double.parseDouble(min.getText()),
								Double.parseDouble(max.getText()));
					}
				}
			}
		}
	};
}