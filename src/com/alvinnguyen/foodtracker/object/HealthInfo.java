package com.alvinnguyen.foodtracker.object;


/**
 * Health Info Class.
 * Used for getting RDA values for a particular age group.
 */

public class HealthInfo {

	public static final int GREEN = 0;
	public static final int YELLOW = 1;
	public static final int RED = 2;
	
	private int rda_calcium, rda_sodium, rda_vit_a, rda_chol, cal;
	private Double rda_protein, rda_fat, rda_carbs, rda_fiber, rda_sugar, rda_iron, rda_vit_c;
	
	/*
	private int calcium, sodium, vit_a, chol;
	private Double protein, fat, carbs, fiber, sugar, iron, vit_c;
	*/
	
	
	public HealthInfo(int age, String sex, int weight, int cal) {

		double lb = weight * 0.453592;
		rda_carbs = 130.0;
		this.cal = cal;
		
		if(sex.equals("Male") && age >= 9 && age<=70) {

			if(age >= 9 && age <=13) {
				rda_calcium = 1300; rda_sodium = 1500; rda_vit_a = 600; rda_chol = 300;
				rda_protein = lb * .76; rda_fat = 65.0; rda_fiber = 31.0; rda_sugar = 40.0 ; rda_iron = 8.0; rda_vit_c = 45.0; 
			} else if(age >=14 && age <=18) {
				rda_calcium = 1300; rda_sodium = 1500; rda_vit_a = 900; rda_chol = 300;
				rda_protein = lb * .73; rda_fat = 65.0; rda_fiber = 38.0; rda_sugar = 40.0 ; rda_iron = 11.0; rda_vit_c = 75.0; 
			} else if(age >= 19 && age <=30) {
				rda_calcium = 1000; rda_sodium = 1500; rda_vit_a = 900; rda_chol = 300;
				rda_protein = lb * .66; rda_fat = 65.0; rda_fiber = 38.0; rda_sugar = 40.0 ; rda_iron = 8.0; rda_vit_c = 90.0; 
			} else if(age >=31 && age <= 50) {
				rda_calcium = 1000; rda_sodium = 1500; rda_vit_a = 900; rda_chol = 300;
				rda_protein = lb * .66; rda_fat = 65.0; rda_fiber = 38.0; rda_sugar = 40.0 ; rda_iron = 8.0; rda_vit_c = 90.0; 
			} else if(age >=51 && age <= 70) {
				rda_calcium = 1000; rda_sodium = 1300; rda_vit_a = 900; rda_chol = 300;
				rda_protein = lb * .66; rda_fat = 65.0; rda_fiber = 30.0; rda_sugar = 40.0 ; rda_iron = 8.0; rda_vit_c = 90.0; 
			}
		} else if(sex.equals("Female") && age >= 9 && age<=70) {
			
			if(age >= 9 && age <=13) {
				rda_calcium = 1300; rda_sodium = 1500; rda_vit_a = 600; rda_chol = 300;
				rda_protein = lb * .76; rda_fat = 65.0; rda_fiber = 26.0; rda_sugar = 40.0 ; rda_iron = 8.0; rda_vit_c = 45.0; 
			} else if(age >=14 && age <=18) {
				rda_calcium = 1300; rda_sodium = 1500; rda_vit_a = 700; rda_chol = 300;
				rda_protein = lb * .71; rda_fat = 65.0; rda_fiber = 26.0; rda_sugar = 40.0 ; rda_iron = 15.0; rda_vit_c = 65.0; 
			} else if(age >= 19 && age <=30) {
				rda_calcium = 1000; rda_sodium = 1500; rda_vit_a = 700; rda_chol = 300;
				rda_protein = lb * .66; rda_fat = 65.0; rda_fiber = 25.0; rda_sugar = 40.0 ; rda_iron = 18.0; rda_vit_c = 75.0; 
			} else if(age >=31 && age <= 50) {
				rda_calcium = 1000; rda_sodium = 1500; rda_vit_a = 700; rda_chol = 300;
				rda_protein = lb * .66; rda_fat = 65.0; rda_fiber = 25.0; rda_sugar = 40.0 ; rda_iron = 18.0; rda_vit_c = 75.0; 
			} else if(age >=51 && age <= 70) {
				rda_calcium = 1200; rda_sodium = 1300; rda_vit_a = 700; rda_chol = 300;
				rda_protein = lb * .66; rda_fat = 65.0; rda_fiber = 21.0; rda_sugar = 40.0 ; rda_iron = 8.0; rda_vit_c = 75.0; 
			}
		} else {
			//default RDA's
			
		}
	} 
	
	public int getRDACalcium() {
		return rda_calcium;
	}
	
	public int getRDASodium() {
		return rda_sodium;
	}
	
	public int getRDAVitA() {
		return rda_vit_a;
	}
	
	public int getRDAChol() {
		return rda_chol;
	}
	
	public int getRDAProtein() {
		return rda_protein.intValue();
	}
	
	public int getRDAFat() {
		return rda_fat.intValue();
	}
	
	public int getRDACarbs() {
		return rda_carbs.intValue();
	}
	
	public int getRDAFiber() {
		return rda_fiber.intValue();
	}
	
	public int getRDASugar() {
		return rda_sugar.intValue();
	}
	
	public int getRDAIron() {
		return rda_iron.intValue();
	}
	
	public int getRDAVitC() {
		return rda_vit_c.intValue();
	}
	
	public int getCalories() {
		return cal;
	}

	/*
	 0-20% Red
	 21-60 Yellow
	 61-100 Green
	 >100 Red 
	 * 
	 */
	public int getCaloriesIndicator(int today) {
		if(today>=0 && today < cal * .21)
			return RED;
		else if(today>= cal * .21 && today <= cal * .60)
			return YELLOW;
		else if(today > cal * .60 && today <= cal)
			return GREEN;
		else if(today> cal)
			return RED;
		else {
			return -1;
		}
	}
	
	/*
	0-20% Red
	21%-80% Yellow
	81%-120% Green
	121%-2499 Yellow
	2500+ Red
	*/
	public int getCalciumIndicator(int today) {
		
		if((today>=0 && today <= rda_calcium * .20) || today >= 2500)
			return RED;
		else if(((today > rda_calcium * .20) && (today <= rda_calcium * .80)) ||
				((today > rda_calcium * 1.20) && (today <= 2499)))
			return YELLOW;
		else if((today > rda_calcium * .80) && today <= rda_calcium * 1.20)
			return GREEN;
		else
			return -1;
		
	}
	/*
	0-20% Red
	21%-80% Yellow
	81%-120% Green
	121%-2299 Yellow
	2300+ Red
	*/
	public int getSodiumIndicator(int today) {
		
		if((today>=0 && today <= rda_sodium * .20) || today >= 2300)
			return RED;
		else if(((today > rda_sodium * .20) && (today <= rda_sodium * .80)) ||
				((today > rda_sodium * 1.20) && (today <= 2299)))
			return YELLOW;
		else if((today > rda_sodium * .80) && (today <= rda_sodium * 1.20))
			return GREEN;
		else
			return -1;
		
	}
	
	/*
	0-20% Red
	21%-80% Yellow
	81%-200% Green
	201%-2999 Yellow
	3000+ Red
	 */
	public int getVitAIndicator(int today) {
		if((today>=0 && today <= rda_vit_a * .20) || today >= 3000)
			return RED;
		else if(((today > rda_vit_a * .20) && (today <= rda_vit_a * .80)) ||
				((today > rda_vit_a * 2.00) && (today <= 2999)))
			return YELLOW;
		else if((today > rda_vit_a * .80) && (today <= rda_vit_a * 2.00))
			return GREEN;
		else
			return -1;
	}
	
	/*
	0-30% Green
	31-80% Yellow
	>80% Red
	*/
	public int getCholIndicator(int today) {
		if(today>=0 && today <= rda_chol * .30)
			return GREEN;
		else if(today> rda_chol * .30 && today <= rda_chol * .80)
			return YELLOW;
		else if(today> rda_chol * .80)
			return RED;
		else
			return -1;
		
	}
	
	/*
	 0-30 Red
	 31-80 Yellow
	 81-110 Green
	 >110 Red
	 */
	public int getProteinIndicator(Double today) {
		if((today>=0 && today <= rda_protein * .30) || (today > rda_protein * 1.10))
			return RED;
		else if((today> rda_protein * .30) && (today <= rda_protein * .80))
			return YELLOW;
		else if((today> rda_protein * .80) && (today <= rda_protein * 1.10))
			return GREEN;
		else
			return -1;
	}
	
	/*
	 0-30 Green
	 31-100 Yellow
	 > 100 Red
	 */
	public int getFatIndicator(Double today) {
		if(today >= 0 && today <= rda_fat *.30)
			return GREEN;
		else if(today > rda_fat * .30 && today <= rda_fat ) 
			return YELLOW;
		else if(today > rda_fat)
			return RED;
		else 
			return -1;
	}
	
	/*
	 0-30 Red
	 31-80 Yellow
	 81-110 Green
	 >110 Red
	 */
	public int getCarbsIndicator(Double today) {
		
		if((today>=0 && today <= rda_carbs * .30) || (today > rda_carbs * 1.10))
			return RED;
		else if((today> rda_carbs * .30) && (today <= rda_carbs * .80))
			return YELLOW;
		else if((today> rda_carbs * .80) && (today <= rda_carbs * 1.10))
			return GREEN;
		else
			return -1;
	}
	/*
	 0-30 Red
	 31-80 Yellow
	 >80 Green
	 */
	public int getFiberIndicator(Double today) {
		if((today>=0 && today <= rda_fiber * .30))
			return RED;
		else if((today> rda_fiber * .30) && (today <= rda_fiber * .80))
			return YELLOW;
		else if((today> rda_fiber * .80))
			return GREEN;
		else
			return -1;
		
	}
	
	/*
	 0-60 Red
	 61-100 Yellow
	 >100 Green
	 */
	public int getSugarIndicator(Double today) {
		if((today>=0 && today < rda_sugar * .60))
			return GREEN;
		else if((today>= rda_sugar * .60) && (today <= rda_sugar))
			return YELLOW;
		else if(today> rda_sugar)
			return RED;
		else
			return -1;
		
	}
	
	/*
	0-20% Red
	21%-80% Yellow
	81%-120% Green
	121%-39 Yellow
	40+ Red
	*/
	public int getIronIndicator(Double today) {
		if((today>=0 && today <= rda_iron * .20) || today >= 40)
			return RED;
		else if(((today > rda_iron * .20) && (today <= rda_iron * .80)) ||
				((today > rda_iron * 1.20) && (today < 40)))
			return YELLOW;
		else if((today > rda_iron * .80) && (today <= rda_iron * 1.20))
			return GREEN;
		else
			return -1;
	}
	
	
	/*
	0-20% Red
	21%-80% Yellow
	81%-300% Green
	301%-1999 Yellow
	2000+ Red
	*/
	public int getVitCIndicator(Double today) {
		if((today>=0 && today <= rda_vit_c * .20) || today >= 2000)
			return RED;
		else if(((today > rda_vit_c * .20) && (today <= rda_vit_c * .80)) ||
				((today > rda_vit_c * 3.00) && (today <= 1999)))
			return YELLOW;
		else if((today > rda_vit_c * .80) && (today <= rda_vit_c * 3.00))
			return GREEN;
		else
			return -1;
	}
}
