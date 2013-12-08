package com.alvinnguyen.foodtracker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.alvinnguyen.foodtracker.database.JournalDataSource;
import com.alvinnguyen.foodtracker.object.FoodItem;

public class AnalysisTrendsActivity extends Activity {

	private Spinner trends_spinner;
    private LinearLayout trendChart;
	private JournalDataSource datasource;
	private long endDate;
	
    TreeMap<Long, Double> m_calories = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_protein = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_fat = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_carbs = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_fiber = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_suger = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_calcium = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_iron = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_sodium = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_vit_c = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_vit_a = new TreeMap<Long,Double>();
    TreeMap<Long, Double> m_cholesterol = new TreeMap<Long,Double>();
   
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analysis_trends);

		
		datasource = new JournalDataSource(this);
		datasource.open();
		endDate = getIntent().getLongExtra("endDate", 0);
		
		getData();
		initAllFields();
	}

	public void initAllFields() {
		trends_spinner = (Spinner) findViewById(R.id.analysis_trends_spinner);
		trendChart = (LinearLayout) findViewById(R.id.analysis_graph);
		

		trends_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	loadTrend();
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});
	}
	
	public void loadTrend() {
		String trend = trends_spinner.getSelectedItem().toString();
		TreeMap<Long, Double> selected = getSelectedMap(trend);
		
		TimeSeries time_series = new TimeSeries(trend);
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    GraphicalView chartView;
	    
	    initChartVariables(renderer, trend);
	    
	    dataset.addSeries(time_series);
		for(Map.Entry<Long, Double> entry: selected.entrySet()) {

			time_series.add(new Date(entry.getKey()), entry.getValue());
			
		}

		chartView = ChartFactory.getTimeChartView(this, dataset, renderer, "MM/dd");

		trendChart.removeAllViews();
		trendChart.addView(chartView);
		
	}

	
	public void initChartVariables(XYMultipleSeriesRenderer renderer, String title) {
		renderer.setAxisTitleTextSize(25);
		renderer.setChartTitleTextSize(30);
		renderer.setLabelsTextSize(25);
		renderer.setLegendTextSize(25);
		renderer.setPointSize(5f);
        renderer.setClickEnabled(false);
        renderer.setSelectableBuffer(20);
        renderer.setPanEnabled(true);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setChartTitle(title + " Weekly Trend");
        renderer.setXTitle("Date");
        renderer.setYTitle(title);
        renderer.setAxesColor(Color.BLACK);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setYAxisAlign(Align.LEFT, 0);
        renderer.setYLabelsAlign(Align.LEFT);
        
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.BLACK);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);

	}
	
	public TreeMap<Long, Double> getSelectedMap(String trend) {
		if(trend.compareTo("Calories") == 0)
			return m_calories;
		else if(trend.compareTo("Protein") == 0)
			return m_protein;
		else if(trend.compareTo("Total Fat") == 0)
			return m_fat;
		else if(trend.compareTo("Carbs") == 0)
			return m_carbs;
		else if(trend.compareTo("Fiber") == 0)
			return m_fiber;
		else if(trend.compareTo("Sugar") == 0)
			return m_suger;
		else if(trend.compareTo("Calcium") == 0)
			return m_calcium;
		else if(trend.compareTo("Iron") == 0)
			return m_iron;
		else if(trend.compareTo("Sodium") == 0)
			return m_sodium;
		else if(trend.compareTo("Vitamin C") == 0)
			return m_vit_c;
		else if(trend.compareTo("Vitamin A") == 0)
			return m_vit_a;
		else if(trend.compareTo("Cholesterol") == 0)
			return m_cholesterol;
		else
			return null;
	}
	
	
	//fill data 
	public void getData() {
		TreeMap<Long, String> past7days = getPast7Days();
		List<FoodItem> list;
		
		for(Map.Entry<Long, String> entry : past7days.entrySet()) {
			list = datasource.getAllItemsAtDate(entry.getValue());
			addPointToGraphData(entry.getKey() ,list);
		}
	}
	
	private void addPointToGraphData(long date, List<FoodItem> list) {
		FoodItem item;
		double calories = 0.0, protein = 0.0, fat = 0.0, carbs = 0.0, fiber = 0.0, suger = 0.0, calcium= 0.0;
		double iron= 0.0, sodium = 0.0, vit_c = 0.0, vit_a = 0.0, cholesterol = 0.0;
		
		for(int i=0; i<list.size(); i++) {
			item = list.get(i);
			calories += item.getCalories();
			protein += item.getProtein();
			fat +=item.getFat();
			carbs += item.getCarbs();
			fiber += item.getFiber();
			suger += item.getSuger();
			calcium += item.getCalcium();
			iron += item.getIron();
			sodium += item.getSodium();
			vit_c += item.getVitC();
			vit_a += item.getVitA();
			cholesterol += item.getChol();
		}
		
		m_calories.put(date, calories);
		m_protein.put(date, protein);
		m_fat.put(date, fat);
		m_carbs.put(date, carbs);
		m_fiber.put(date, fiber);
		m_suger.put(date, suger);
		m_calcium.put(date, calcium);
		m_iron.put(date, iron);
		m_sodium.put(date, sodium);
		m_vit_c.put(date, vit_c);
		m_vit_a.put(date, vit_a);
		m_cholesterol.put(date, cholesterol);
		
	}
	
	public TreeMap<Long, String> getPast7Days() {
		TreeMap<Long, String> past7 = new TreeMap<Long, String>();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(endDate);
		String dateString;
		
		for(int i=0;i<8;i++) {
			dateString = cal.get(Calendar.MONTH)+1+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR);
			past7.put(cal.getTimeInMillis(), dateString);
			cal.add(Calendar.DATE, -1);
		}
		
		return past7;
	}
	
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
	
	
}
