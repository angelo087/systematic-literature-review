package es.uca.pdf;

public class AnnualTrend {

	private int totalBooks = 0;
	private int totalConferences = 0;
	private int totalGenerics = 0;
	private int totalJournals = 0;
	private int totalOthers = 0;
	
	public AnnualTrend()
	{
		this.totalBooks = 0;
		this.totalConferences = 0;
		this.totalGenerics = 0;
		this.totalJournals = 0;
		this.totalOthers = 0;
	}
	
	public AnnualTrend(int totalBooks, int totalConferences,
			int totalGenerics, int totalJournals, int totalOthers) {

		this.totalBooks = totalBooks;
		this.totalConferences = totalConferences;
		this.totalGenerics = totalGenerics;
		this.totalJournals = totalJournals;
		this.totalOthers = totalOthers;
	}

	public int getTotalBooks() {
		return totalBooks;
	}

	public void setTotalBooks(int totalBooks) {
		this.totalBooks = totalBooks;
	}

	public int getTotalConferences() {
		return totalConferences;
	}

	public void setTotalConferences(int totalConferences) {
		this.totalConferences = totalConferences;
	}

	public int getTotalGenerics() {
		return totalGenerics;
	}

	public void setTotalGenerics(int totalGenerics) {
		this.totalGenerics = totalGenerics;
	}

	public int getTotalJournals() {
		return totalJournals;
	}

	public void setTotalJournals(int totalJournals) {
		this.totalJournals = totalJournals;
	}

	public int getTotalOthers() {
		return totalOthers;
	}

	public void setTotalOthers(int totalOthers) {
		this.totalOthers = totalOthers;
	}
}
