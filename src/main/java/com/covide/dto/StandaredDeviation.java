package com.covide.dto;

public class StandaredDeviation {

	public double caluclateStanderedDeviation(double[] input_array) {
		
//		double[] input_array = { 35, 48, 60, 71, 80, 95, 130};
	      for (double i : input_array) {
	         System.out.print(i +" ");
	      }
	      double sum = 0.0, standard_deviation = 0.0;
	      int array_length = input_array.length;
	      for(double temp : input_array) {
	         sum += temp;
	      }
	      double mean = sum/array_length;
	      for(double temp: input_array) {
	         standard_deviation += Math.pow(temp - mean, 2);
	      }
	      double result = Math.sqrt(standard_deviation/array_length);
//	      System.out.format("\n\nThe Standard Deviation is: %.6f", result);
	      return result;
	}
}
