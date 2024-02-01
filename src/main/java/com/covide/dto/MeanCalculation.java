package com.covide.dto;

public class MeanCalculation {
	
	public double calculateMeanValue(double[] input) {
		/*//double[] input=new double[5];
		double[] input={10,20,30,40,50};*/
		double n=input.length,sum=0;
		for(int i=0;i<n;i++) 
		{
			sum=sum+input[i];
		}
	       System.out.println("Mean :"+sum/n); 
	     double meanVal = sum/n;
	    return meanVal;
	}

}
