package com.alvinnguyen.foodtracker.object;

public class FoodIndicator {

	public static final int GREEN = 0;
	public static final int YELLOW = 1;
	public static final int RED = 2;
	
	
	private int[] indicators = {0,0,0};
	private int i_calories, i_fat, i_chol, i_sodium, i_carbs, i_fiber, i_sugar, i_protein, i_vita, i_vitc, i_calcium, i_iron;
	
	
	public FoodIndicator(FoodItem item, 
			 int today_calories, int today_chol,int today_sodium,int today_vita,int today_calcium,
			 Double today_fat, Double today_carbs,Double today_fiber,Double today_sugar,Double today_protein,
			 Double today_vitc,Double  today_iron, HealthInfo info) {
		
		i_calories = info.getCaloriesIndicator(item.getCalories()+today_calories);
		i_fat = info.getFatIndicator(item.getFat()+ today_fat);
		i_chol = info.getCholIndicator(item.getChol() + today_chol);
		i_sodium = info.getSodiumIndicator(item.getSodium() + today_sodium);
		i_carbs = info.getCarbsIndicator(item.getCarbs() + today_carbs);
		i_fiber = info.getFiberIndicator(item.getFiber() + today_fiber);
		i_sugar = info.getSugarIndicator(item.getSuger() + today_sugar);
		i_protein = info.getProteinIndicator(item.getProtein() + today_protein);
		i_vita = info.getVitAIndicator(item.getVitA() + today_vita);
		i_vitc = info.getVitCIndicator(item.getVitC() + today_vitc);
		i_calcium = info.getCalciumIndicator(item.getCalcium() + today_calcium);
		i_iron = info.getIronIndicator(item.getIron() + today_iron);
		
		indicators[i_calories] += 1;
		indicators[i_fat] += 1;
		indicators[i_chol] += 1;
		indicators[i_sodium] += 1;
		indicators[i_carbs] += 1;
		indicators[i_fiber] += 1;
		indicators[i_sugar] += 1;
		indicators[i_protein] += 1;
		indicators[i_vita] += 1;
		indicators[i_vitc] += 1;
		indicators[i_calcium] += 1;
		indicators[i_iron] += 1;
	}
	
	public int getNumGreen() {
		return indicators[GREEN];
	}
	
	public int getNumRed() {
		return indicators[RED];
	}
	
	public int getNumYellow() {
		return indicators[YELLOW];
	}
	
	public int getCaloriesIndicator() {
		return i_calories;
	}

	public int getFatIndicator() {
		return i_fat;
	}
	
	public int getCholIndicator() {
		return i_chol;
	}
	
	public int getSodiumIndicator() {
		return i_sodium;
	}
	
	public int getVitAIndicator() {
		return i_vita;
	}
	
	public int getVitCIndicator() {
		return i_vitc;
	}
	
	public int getCalciumIndicator() {
		return i_calcium;
	}
	
	public int getCarbsIndicator() {
		return i_carbs;
	}
	
	public int getFiberIndicator() {
		return i_fiber;
	}
	
	public int getSugarIndicator() {
		return i_sugar;
	}
	
	public int getProteinIndicator() {
		return i_protein;
	}
	
	public int getIronIndicator() {
		return i_iron;
	}
}












