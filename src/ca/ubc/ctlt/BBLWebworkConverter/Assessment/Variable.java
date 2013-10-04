package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

public class Variable {
	
	public static final String RANGE = "range";
	
	private String type;
	private String name;
	private double min;
	private double max;
	
	public Variable(String name, double min, double max)
	{
		type = RANGE;
		this.name = name;
		this.min = min;
		this.max = max;
	}

	public String getType() {
		return type;
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
