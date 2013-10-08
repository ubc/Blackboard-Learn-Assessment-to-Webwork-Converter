package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

public class Variable {
	
	public static final String RANGE = "range";
	
	private String name;
	private double min;
	private double max;
	private int decimalPlaces;
	
	public int getDecimalPlaces()
	{
		return decimalPlaces;
	}

	public void setDecimalPlaces(int decimalPlaces)
	{
		this.decimalPlaces = decimalPlaces;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setMin(double min)
	{
		this.min = min;
	}

	public void setMax(double max)
	{
		this.max = max;
	}

	public String getName() {
		return name;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

}
