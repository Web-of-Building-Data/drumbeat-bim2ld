package fi.aalto.cs.drumbeat.rdf.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.log4j.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.jena.rdf.model.Model;

import fi.aalto.cs.drumbeat.common.file.FileManager;

public class RdfUtils {
	
	private static final Logger logger = Logger.getRootLogger();
	
	/**
	 * Create RDF file name
	 * @param filePath
	 * @param format
	 * @param gzip
	 * @return
	 */
	public static String formatRdfFileName(String filePath, RDFFormat format, boolean gzip) {
		
		String rdfFileExtension = format.getLang().getFileExtensions().get(0);
		
		String gzipFileExtension = "";
		
		if (gzip) {
			gzipFileExtension = ".gz";
		}
		
		String fileExtension = rdfFileExtension + gzipFileExtension;

		if (FilenameUtils.isExtension(filePath, fileExtension)) {			
			return filePath;			
		} else if (FilenameUtils.isExtension(filePath, rdfFileExtension)) {			
			return FilenameUtils.normalize(filePath + gzipFileExtension); 			
		} else {			
			return FilenameUtils.normalize(filePath + "." + fileExtension);
		}
		
	}	
	
	
	/**
	 * Exports a Jena {@link Model} to a file
	 * 
	 * @param model
	 * @param baseUri
	 * @param filePath
	 * @param format
	 * @param gzip
	 * @return
	 * @throws IOException
	 */
	public static String exportJenaModelToRdfFile(Model model, String baseUri, String filePath, RDFFormat format, boolean gzip) throws IOException {
		
		String filePathWithExtension = formatRdfFileName(filePath, format, gzip);		

		logger.info(String.format("Exporting graph to file '%s' with format '%s'", filePathWithExtension, format));
		File file = FileManager.createFile(filePathWithExtension);
		OutputStream out = new FileOutputStream(file);
		if (gzip) {
			out = new GZIPOutputStream(out);
		}
		try {
//			String lang = format.getLang().getName();
//			model.write(out, lang, baseUri);
			RDFDataMgr.write(out, model, format);
		}
		finally {
			out.close();
		}
		logger.info(String.format("Exporting graph to file is completed, file size: %s", FileManager.getReadableFileSize(file.length())));
		
		return filePathWithExtension;
	}
	
}
