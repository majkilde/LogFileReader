package dk.majkilde.logreader.files;

/**
 * 
 * Used with the file readers. Each line will be processed by the filter to determin if it should be included in the final report
 *
 */
public interface IFilter {
	public boolean included(String line);
}
