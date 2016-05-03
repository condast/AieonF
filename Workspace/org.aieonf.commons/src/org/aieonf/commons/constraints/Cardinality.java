package org.aieonf.commons.constraints;

public enum Cardinality
{
	UNKNOWN,
	ZERO (0,0),
	ZEROORONE (0,1),
	ZEROORMORE(0),
	ONEORMORE(1),
	ZEROTOMAX(0),
	ONETOMAX (1),
	MINORMORE,
	MINTOMAX,
	EXACTLYONE(1,1);

	public static final String S_ERR_INVALID_CARDINALITY = "The provided input does not represent a valid caerdinality: ";

	private int min, max;

	Cardinality(){
		this.min = 0;
		this.max = 0;
	}

	Cardinality( int min ){
		this.min = min;
		this.max = Integer.MAX_VALUE;
	}

	Cardinality( int min, int max ){
		this.min = min;
		this.max = max;
	}
	
	/**
	 * @return the min
	 */
	public int getMin()
	{
		return min;
	}


	/**
	 * @param min the min to set
	 */
	public void setMin(int min)
	{
		this.min = min;
	}


	/**
	 * @return the max
	 */
	public int getMax()
	{
		return max;
	}


	/**
	 * @param max the max to set
	 */
	public void setMax(int max)
	{
		this.max = max;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		Cardinality card = Cardinality.valueOf( super.toString() );
		switch( card ){
			case ZERO:
				return "[0..0]";
			case ZEROORONE:
				return "[0..1]";
			case ZEROORMORE:
				return "[0..n]";
			case ONEORMORE:
				return "[1..n]";
			case ZEROTOMAX:
				return "[0.." + max + "x]";
			case ONETOMAX:
				return "[1.."+ max + "]";
			case MINORMORE:
				return "["+ min + "..n]";
			case MINTOMAX:
				return "[" + min + ".." + max + "]";
			case EXACTLYONE:
				return "[1..1]";
			default:
				return "[?..?]";
		}
	}
	

	/**
	 * Return the Cardinality of the given String
	 * @param str
	 * @return
	 * @throws NumberFormatException
	 */
	public static Cardinality toCardinality( String str ) throws NumberFormatException{
		String replace = str.replace("[\\[\\]\\.]"," ").trim();
		replace = replace.replace(" ", " ");
		String split[] = replace.split("\\s");
		Cardinality card;
		int mn, mx;
		switch( split.length ){
			case 1:
				mn = Integer.valueOf( split[0] );
				switch( mn ){
					case 0:
						card = ZEROORMORE;
						card.setMin( 0 );
						break;
					case 1:
						card = ONEORMORE;
						card.setMin( 1 );
						break;
					default:
						card = MINORMORE;
						card.setMin( mn );
						break;
				}
			case 2:
				mn = Integer.valueOf( split[0] );
				boolean mxText = split[1].matches("[A..Za..z]");
				switch( mn ){
					case 0:
						if( mxText ){
							card = ZEROORMORE;
							card.setMin(0);
						}
						mx = Integer.valueOf( split[1] );
						switch( mx ){
							case 0:
								card = ZERO;
								card.setMax(0);
								break;
							case 1:
								card = ZEROORONE;
								card.setMax(1);
								break;
							default:
								card = ZEROTOMAX;
								break;
						}
						card.setMin(0);
						break;
					case 1:
						if( mxText ){
							card = ONEORMORE;
							card.setMin(1);
							return card;
						}
						mx = Integer.valueOf( split[1] );
						switch( mx ){
							case 0:
								throw new NumberFormatException( S_ERR_INVALID_CARDINALITY + str );
							case 1:
								card = EXACTLYONE;
								card.setMax(1);
							default:
								card = ONETOMAX;
								card.setMax( mx );
						}
						card.setMin(1);
						break;
					default:
						if( mxText ){
							card = MINORMORE;
							card.setMin(mn);							
							return card;
						}
						mx = Integer.valueOf( split[1] );
						switch( mn ){
							case 0:
							case 1:
								throw new NumberFormatException( S_ERR_INVALID_CARDINALITY + str );
							default:
								card = MINTOMAX;
								card.setMax(mx);
								break;
						}
				}
				break;
			default:
				throw new NumberFormatException( S_ERR_INVALID_CARDINALITY + str );
		}
		return card;
	}
}