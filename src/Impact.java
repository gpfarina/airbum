/**
 * Classe per gestire l'impatto fra il velivolo ed un proiettile
 * 
 */
public class Impact {

	/**
	 * Metodo per vedere se i due oggetti hanno avuto una collisione
	 * @param v oggetto di tipo velivolo
	 * @param p oggetto di tipo proiettile
	 * @return boolean
	 */
	public boolean checkImpact(Velivolo v, Proiettile p) {

		int xv = (int) v.getPosizione().getX();
		int yv = (int) v.getPosizione().getY();
		int vl = (int) v.getImageWidth();
		int vh = (int) v.getImageHeight();
		int xp = (int) p.getPosizione().getX();
		int yp = (int) p.getPosizione().getY();
		int pl = (int) p.getImageWidth();
		int ph = (int) p.getImageWidth();

		//L’angolo superiore sinistro coppia (x,y) e conpreso nello sprite

		if ( ((xv >= xp) && (xv <= xp + pl)) 
				&&
				((yv >= yp) && (yv <= yp + ph)) )
			return true;

		// angolo superiore destro
		if (  ((xv + vl >= xp) && (xv + vl <= xp + pl)) 
				&& 
				((yv >= yp) && (yv <= yp + ph)) )
			return true;

		// angolo inferiore sinistro
		if (  ((xv >= xp) && (xv <= xp + pl))
				&&
				(( yv + vh >= yp) && (yv + vh <= yp + ph)) )
			return true;

		// angolo inferiore destro
		if (  (( xv + vl >= xp) && (xv + vl <= xp + pl)) 
				&& ((yv + vh >= yp) && (yv + vh <= yp + ph)) )
			return true;

		// dal basso
		if (  (( xv <= xp) && ((xv + vl) >= xp)) 
				&& (((yv + vh) >= yp) && (yv <= yp)) )
			return true;

		/*
		 * se non si sono verificate le condizioni sopra allora significa che
		 * non c’è stata collisione e quindi ritorno false
		 */
		return false;
	}

}
