package fi.aalto.cs.drumbeat.rdf.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFLanguages;
import org.apache.log4j.Logger;
import org.apache.jena.rdf.model.Model;

import fi.aalto.cs.drumbeat.common.io.CompressedInputStreamManager;
import fi.aalto.cs.drumbeat.common.io.CompressedOutputStreamManager;
import fi.aalto.cs.drumbeat.common.io.FileManager;

public class RdfIOUtils {	
	
	/**
	 * getRdfFormatFromFilePath
	 * @param filePath
	 * @return
	 * @deprecated Use {@link CompressedInputStreamManager} and {@link RDFLanguages}
	 */
	public static RDFFormat getRdfFormatFromFilePath(String filePath) {
		filePath = filePath.toLowerCase();
		if (filePath.endsWith(".gzip")) {
			filePath = filePath.substring(0, filePath.length() - 5);
		} else if (filePath.endsWith(".gz")) {
			filePath = filePath.substring(0, filePath.length() - 3);			
		}
		
		Lang lang = RDFLanguages.filenameToLang(filePath);
		return new RDFFormat(lang);
		
//		if (filePath.endsWith("ttl")) {
//			return RDFFormat.TURTLE;
//		} else if (filePath.endsWith("nt")) {
//			return RDFFormat.NTRIPLES;
//		} else if (filePath.endsWith("nq")) {
//			return RDFFormat.NQUADS;
//		} else if (filePath.endsWith("jsonld")) {
//			return RDFFormat.JSONLD;
//		} else if (filePath.endsWith("trig")) {
//			return RDFFormat.TRIG;
//		} else {
//			return RDFFormat.RDFXML;
//		}
	}
	
	/**
	 * getRdfLangFromFilePath
	 * @param filePath
	 * @return
	 * @deprecated Use {@link CompressedInputStreamManager} and {@link RDFLanguages}
	 */
	@Deprecated
	public static Lang getRdfLangFromFilePath(String filePath) {
		filePath = filePath.toLowerCase();
		if (filePath.endsWith(".gzip")) {
			filePath = filePath.substring(0, filePath.length() - 5);
		} else if (filePath.endsWith(".gz")) {
			filePath = filePath.substring(0, filePath.length() - 3);			
		}
		
		return RDFLanguages.filenameToLang(filePath);
	}
	
	
	/**
	 * Imports a RDF file from a file
	 * 
	 * @param model
	 * @param filePath
	 * @throws IOException
	 */
	public static void importRdfFileToJenaModel(Model model, String filePath) throws IOException {
		
		CompressedInputStreamManager compressedInputStreamManager = new CompressedInputStreamManager(filePath);
		InputStream in = compressedInputStreamManager.getUncompressedInputStream(); 
		String fileExtension = compressedInputStreamManager.getFileExtensionNoCompressionFormat();
		Lang lang = RDFLanguages.fileExtToLang(fileExtension);
		
		try {
			RDFDataMgr.read(model, in, lang);
		} finally {
			in.close();
		}
		
	}
	
	
	
	
	/**
	 * Create RDF file name
	 * @param filePath
	 * @param format
	 * @param gzip
	 * @return
	 */
	public static String formatRdfFileName(String filePath, RDFFormat format, boolean gzip) {
		
		String rdfFileExtension = format.getLang().getFileExtensions().get(0);		
		return FileManager.appendFileExtensions(
				filePath,
				rdfFileExtension,
				gzip ? FileManager.FILE_EXTENSION_GZIP : null);
		
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
		
		Logger logger = Logger.getRootLogger();
		
		String filePathWithExtension = formatRdfFileName(filePath, format, gzip);		

		logger.info(String.format("Exporting graph to file '%s' with format '%s'", filePathWithExtension, format));
		
		CompressedOutputStreamManager compressedOutputStreamManager = new CompressedOutputStreamManager(filePathWithExtension);
		OutputStream out = compressedOutputStreamManager.getCompressedOutputStream();
		
		try {
//			String lang = format.getLang().getName();
//			model.write(out, lang, baseUri);
			RDFDataMgr.write(out, model, format);
		} finally {
			out.close();
		}
		
		File file = new File(filePathWithExtension);		
		logger.info(String.format("Exporting graph to file is completed, file size: %s", FileManager.getReadableFileSize(file.length())));
		
		return filePathWithExtension;
	}
	
	
	
	
}
