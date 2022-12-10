package com.lp.server.util.fastlanereader.service.query;


public abstract class FlrFeatureBase<T> {

	private T[] flrData = null ;
	private int flrRowCount = 0 ;
	
	protected FlrFeatureBase(QueryParameters query) {
		if(query instanceof QueryParametersFeatures) {
			initialize((QueryParametersFeatures) query) ;
		}
	}

	/**
	 * Basisinitialierung, Beispielsweise Ermittlung der verf&uuml;gbaren Feature
	 * @param qpf
	 */
	protected abstract void initialize(QueryParametersFeatures qpf) ;
	
	/**
	 * Das gesamte Array f&uuml;r die Aufnahme der Daten erzeugen
	 * @param rows die Anzahl der Zeilen
	 * @return ein Array, das <code>rows</code> aufnehmen kann
	 */
	protected abstract T[] createFlrData(int rows) ;

	/**
	 * Eine Datenzeile erzeugen</br>
	 * <p>Der <code>index</code> kann verwendet werden, wenn abh&auml;ngig von einer
	 * bestimmten Datenzeile zum Beispiel andere Daten initialisiert werden muessen.
	 * Generell sollte man ohne diese Information auskommen</p> 
	 *  
	 * @param index der Index des Objects.
	 * 
	 * @return eine mit Defaultwerten initialisierte Datenzeile 
	 */
	protected abstract T createFlrDataObject(int index) ;
	
	public void setFlrRowCount(int rows) {
		flrRowCount = rows ;
		flrData = createFlrData(rows) ;
	}
	
	public int getFlrRowCount() {
		return flrRowCount ;
	}
	
	public T[] getFlrData() {
		return flrData ;
	}
	
	public T getFlrDataObject(int row) {
		if(flrData[row] == null) {
			flrData[row] = createFlrDataObject(row) ;
		}
		return flrData[row] ;
	}
}
