package at.ppmrob.examples.main;

public enum LastKnownCircleLinePosition {
	
	
	LEFT_RECTANGLE_UPPER_HLAF(0),
	LEFT_RECTANGLE_BOTTOM_HLAF(1),
	
	RIGHT_RECTANGLE_UPPER_HLAF(2),
	RIGHT_RECTANGLE_BOTTOM_HLAF(3),
	
	CENTER_RECTANGLE_UPPER_HLAF(4),
	CENTER_RECTANGLE_BOTTOM_HLAF(5),
	
	CENTER_UP_AND_BOTTOM(6),
	
	LEFT_UP_AND_BOTTOM(7),
	
	RIGHT_UP_AND_BOTTOM(8),
	
	

	
	CENTER_UP_AND_RIGHT_BOTTOM(9),
	
	CENTER_UP_AND_LEFT_BOTTOM(10),
	
	LEFT_UP_AND_CENTER_OR_RIGHT(11),
	
	RIGHT_UP_AND_CENTER_OR_LEFT(12);
	private final int value;
	
	private LastKnownCircleLinePosition(int i) {
		value = i;
	}
	
	public int getLastPosition() {
		return value;
	}
}
