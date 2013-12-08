package com.alvinnguyen.foodtracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alvinnguyen.foodtracker.database.JournalDataSource;
import com.alvinnguyen.foodtracker.database.PantryDataSource;
import com.alvinnguyen.foodtracker.object.FoodIndicator;
import com.alvinnguyen.foodtracker.object.FoodItem;
import com.alvinnguyen.foodtracker.object.HealthInfo;

public class RecommenderActivity extends ListActivity {

	public static final int GREEN = 0;
	public static final int YELLOW = 1;
	public static final int RED = 2;
	
	public static final int BREAKFAST = 0;
	public static final int LUNCH = 1;
	public static final int DINNER = 2;
	
	private TextView explaination;
	private ListView list;
	private List<FoodItem> recommendations = new ArrayList<FoodItem>();
	private String date;
	private HashSet<String> reasons = new HashSet<String>();
	private String reason;
	private HealthInfo info;
	
	private int today_calories=0,  today_chol=0, today_sodium=0, today_vita=0,today_calcium=0;
	private Double today_fat=0.0, today_carbs=0.0, today_fiber=0.0, today_sugar=0.0, today_protein=0.0;
	private Double today_vitc=0.0,  today_iron=0.0;
	
	private int i_calories, i_fat, i_chol, i_sodium, i_carbs, i_fiber, i_sugar, i_protein, i_vita, i_vitc, i_calcium, i_iron;
	
	private int nextMeal, maxMealCalories;
	
	private JournalDataSource journal;
	private PantryDataSource pantry;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommender);

		initAllFields();
		getTodaysValues();
		getTodaysIndicators();
		getRecommendations();
	}

	public void initAllFields() {
		explaination = (TextView) findViewById(R.id.recommender_reason);
		list = (ListView) getListView();
		list.setEmptyView(findViewById(R.id.recommender_empty));
		journal = new JournalDataSource(this);
		pantry = new PantryDataSource(this);
		journal.open();
		pantry.open();
		date = getTodaysDate();
		reason = "";
		
		
		int age = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE).getInt("PERSONALINFO_AGE_INT", -1);
		String sex = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE).getString("PERSONALINFO_SEX_STRING", "");
		int weight = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE).getInt("PERSONALINFO_WEIGHT_INT", -1);
		int cal = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE).getInt("PERSONALINFO_CALORIES_INT", -1);
		info = new HealthInfo(age, sex, weight, cal);
		
		maxMealCalories = cal/3;
	}
	
	public void getTodaysValues() {	
		FoodItem currentItem;
		List<FoodItem> items = journal.getAllItemsAtDate(date);
		HashSet<String> meals = new HashSet<String>();
		
		for(int i=0; i<items.size(); i++) {
			currentItem = items.get(i);
			today_calories += currentItem.getCalories();
			today_chol += currentItem.getChol();
			today_sodium += currentItem.getSodium();
			today_vita += currentItem.getVitA();
			today_calcium += currentItem.getCalcium();
			today_fat += currentItem.getFat();
			today_carbs += currentItem.getCarbs();
			today_fiber += currentItem.getFiber();
			today_sugar += currentItem.getSuger();
			today_protein += currentItem.getProtein();
			today_vitc += currentItem.getVitC();
			today_iron += currentItem.getIron();
			meals.add(currentItem.getMeal());
		}
		if(meals.contains("dinner") || meals.isEmpty()) {
			today_calories = today_chol = today_sodium = today_vita = today_calcium = 0;
			today_carbs = today_fiber = today_sugar = today_protein = today_vitc = today_iron = today_fat = 0.0;
			nextMeal = BREAKFAST;
		}
		else if(meals.contains("lunch") && !meals.contains("dinner"))
			nextMeal = DINNER;
		else if(meals.contains("breakfast") && !meals.contains("dinner") && !meals.contains("lunch"))
			nextMeal = LUNCH;
		else
			nextMeal = -1;
	}
	
	
	private void getTodaysIndicators() {
		i_calories = info.getCaloriesIndicator(today_calories);	
		i_fat = info.getFatIndicator(today_fat);
		i_chol = info.getCholIndicator(today_chol);
		i_sodium = info.getSodiumIndicator(today_sodium);
		i_carbs = info.getCarbsIndicator(today_carbs);
		i_fiber = info.getFiberIndicator(today_fiber);
		i_sugar = info.getSugarIndicator(today_sugar);
		i_protein = info.getProteinIndicator(today_protein);
		i_vita = info.getVitAIndicator(today_vita);
		i_vitc = info.getVitCIndicator(today_vitc);	
		i_calcium = info.getCalciumIndicator(today_calcium);
		i_iron = info.getIronIndicator(today_iron);
	}
	
	private void getRecommendations() {
		
		List<FoodItem> items = pantry.getAllPantryItems();
		List<FoodItem> recommendations = new ArrayList<FoodItem>();
		List<FoodIndicator> pantryItems;
		List<Integer> usedItems = new ArrayList<Integer>();
		
		
		boolean betterItem = true;
		int betterItemLocation = -1, calories = 0;

		pantryItems = calculateDifferences(items);
		
		while(betterItem) {
			
			betterItemLocation = calculateBestItem(pantryItems, usedItems);
			if(betterItemLocation == -1){
				betterItem = false;
			} 
			else if((calories += items.get(betterItemLocation).getCalories()) > maxMealCalories) {
				betterItem = false;
			}
			else {
				usedItems.add(betterItemLocation);
				recommendations.add(items.get(betterItemLocation));
				getReason(pantryItems.get(betterItemLocation));
				
				today_calories += items.get(betterItemLocation).getCalories();
				today_chol += items.get(betterItemLocation).getChol();
				today_sodium += items.get(betterItemLocation).getSodium();
				today_vita += items.get(betterItemLocation).getVitA();
				today_calcium += items.get(betterItemLocation).getCalcium();
				today_fat += items.get(betterItemLocation).getFat();
				today_carbs += items.get(betterItemLocation).getCarbs();
				today_fiber += items.get(betterItemLocation).getFiber();
				today_sugar += items.get(betterItemLocation).getSuger();
				today_protein += items.get(betterItemLocation).getProtein();
				today_vitc += items.get(betterItemLocation).getVitC();
				today_iron += items.get(betterItemLocation).getIron();
				getTodaysIndicators();
				pantryItems = calculateDifferences(items);
			}
		}
		
		displayRecommendations(recommendations);
	}
	
	private void displayRecommendations(List<FoodItem> recommendations) {
		
		if(recommendations.isEmpty()) {
			reason = "Can not recommend anything.";
		} else {
			if(nextMeal == BREAKFAST)
				reason +="For <b>breakfast</b>, ";
			else if(nextMeal == LUNCH)
				reason +="For <b>lunch</b>, ";
			else if(nextMeal == DINNER)
				reason +="For <b>dinner</b>, ";
			
			reason += "to improve your...<br>";
			
			for(String s : reasons) {
				reason += "&nbsp&nbsp&nbsp<b>" +s+"</b><br>";
			}
			
			reason += "The following items are recommended.";
		}
		
		explaination.setText(Html.fromHtml(reason));
		ArrayAdapter<FoodItem> adapter = new ArrayAdapter<FoodItem>(this,
				R.layout.food_item_list_view, recommendations);
		setListAdapter(adapter);
		
	}
	
	private List<FoodIndicator> calculateDifferences(List<FoodItem> items) {
		
		List<FoodIndicator> pantryItems = new ArrayList<FoodIndicator>();
		FoodItem currentItem;
		FoodIndicator currentIndicator;
		
		for(int i=0; i <items.size(); i++) {
			currentItem = items.get(i);
			currentIndicator = new FoodIndicator(currentItem, 
				today_calories,  today_chol, today_sodium, today_vita, today_calcium,
				today_fat, today_carbs,  today_fiber, today_sugar, today_protein,
				today_vitc, today_iron, info);
			pantryItems.add(currentIndicator);	
		}
		return pantryItems;
	}
	
	//calculate currentbestitem
	// calories can not be RED
	// calories < maxMealCalories
	// delta(GREEN) + delta(RED to YELLOW) > delta(RED)
	private int calculateBestItem(List<FoodIndicator> pantryItems, List<Integer> used) {
		int currentBest = -1;
		int bestDifference = 0;
		int currentDifference = 0;
		int positive = 0, negative =0;
		FoodIndicator item;
		
		for(int i=0; i<pantryItems.size(); i++) {
			item = pantryItems.get(i);
			
			if(item.getCaloriesIndicator() == RED) {
				continue;
			}
			
			if(used.contains(i)) {
				continue;
			}

			if(item.getCaloriesIndicator() < i_calories)
				positive++;
			else if(item.getCaloriesIndicator() > i_calories)
				negative++;
			
			if(item.getCholIndicator() < i_chol)
				positive++;
			else if(item.getCholIndicator() > i_chol)
				negative++;
			
			if(item.getSodiumIndicator() < i_sodium)
				positive++;
			else if(item.getSodiumIndicator() > i_sodium)
				negative++;
			
			if(item.getVitAIndicator() < i_vita)
				positive++;
			else if(item.getVitAIndicator() > i_vita)
				negative++;
			
			if(item.getCalciumIndicator() < i_calcium)
				positive++;
			else if(item.getCalciumIndicator() > i_calcium)
				negative++;
			
			if(item.getFatIndicator() < i_fat)
				positive++;
			else if(item.getFatIndicator() > i_fat)
				negative++;
				
			if(item.getCarbsIndicator() < i_carbs)
				positive++;
			else if(item.getCarbsIndicator() > i_carbs)
				negative++;
			
			if(item.getFiberIndicator() < i_fiber)
				positive++;
			else if(item.getFiberIndicator() > i_fiber)
				negative++;
			
			if(item.getSugarIndicator() <i_sugar)
				positive++;
			else if(item.getSugarIndicator() > i_sugar)
				negative++;
			
			if(item.getProteinIndicator() <i_protein)
				positive++;
			else if(item.getProteinIndicator() > i_protein)
				negative++;
			
			if(item.getVitCIndicator() < i_vitc)
				positive++;
			else if(item.getVitCIndicator() > i_vitc)
				negative++;
			
			if(item.getIronIndicator() < i_iron)
				positive++;
			else if(item.getIronIndicator() > i_iron)
				negative++;
			
			currentDifference = positive - negative;
			if(currentDifference > bestDifference) {
				bestDifference = currentDifference;
				currentBest = i;
			}
			positive = negative = 0;
		}
		
		return currentBest;
	}
	
	
	private void getReason(FoodIndicator item) {
		if(item.getCaloriesIndicator() < i_calories)
			reasons.add("Calories");
		if(item.getCholIndicator() < i_chol)
			reasons.add("Cholesterol");
		if(item.getSodiumIndicator() < i_sodium)
			reasons.add("Sodium");
		if(item.getVitAIndicator() < i_vita)
			reasons.add("Vitamin A");
		if(item.getCalciumIndicator() < i_calcium)
			reasons.add("Calcium");
		if(item.getFatIndicator() < i_fat)
			reasons.add("Fat");
		if(item.getCarbsIndicator() < i_carbs)
			reasons.add("Carbohydrates");
		if(item.getFiberIndicator() < i_fiber)
			reasons.add("Fiber");
		if(item.getSugarIndicator() <i_sugar)
			reasons.add("Sugar");
		if(item.getProteinIndicator() <i_protein)
			reasons.add("Protein");
		if(item.getVitCIndicator() < i_vitc)
			reasons.add("Vitamin C");
		if(item.getIronIndicator() < i_iron)
			reasons.add("Iron");
	}
	
	private String getTodaysDate() {	
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH)+1+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR);
	}
	
    @Override
    protected void onListItemClick(ListView list, View v, int position, long id) {
     
    FoodItem selected = (FoodItem) list.getItemAtPosition(position);
    Intent intent = prepareIntent(selected);
     
     startActivity(intent); 
    }
    
	
    public Intent prepareIntent(FoodItem selected) {
        Intent intent = new Intent(this, ViewItemActivity.class);
        intent.putExtra("food_item", selected.getName());
        intent.putExtra("calories", selected.getCalories());
        intent.putExtra("protein", selected.getProtein());
        intent.putExtra("fat", selected.getFat());
        intent.putExtra("carbs", selected.getCarbs());
        intent.putExtra("fiber", selected.getFiber());
        intent.putExtra("suger",selected.getSuger());
        intent.putExtra("calcium",selected.getCalcium());
        intent.putExtra("iron",selected.getIron());
        intent.putExtra("sodium",selected.getSodium());
        intent.putExtra("vit_c",selected.getVitC());
        intent.putExtra("vit_a", selected.getVitA());
        intent.putExtra("chol", selected.getChol());
        intent.putExtra("add_date", selected.getDate());
        intent.putExtra("isPantry", true);
        
        return intent;
    }
	
	@Override
	protected void onResume() {
		journal.open();
		pantry.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		journal.close();
		pantry.close();
		super.onPause();
	}
	
}
