package ntp.test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.File;

import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import ntp.utils.IniFile;

public class DocToImageConverter {
	
	private static volatile OfficeManager officeManager;
	@SuppressWarnings("deprecation")
	private static volatile OfficeDocumentConverter converter;
	
	public static void main(String args[]) throws Throwable {
		
		Logger logger = Logger.getLogger("DocToImageConverter");  
	    FileHandler fh;   
		
		Path currentRelativePath = Paths.get("");
		String strFilePath = currentRelativePath.toAbsolutePath().toString();
		
		String dateInString = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date());
		dateInString = dateInString.replaceAll(":", "-");
        // This block configure the logger with handler and formatter  
        fh = new FileHandler(strFilePath+"/Log/DocToImageConverterLog_"+dateInString+".log");  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter); 
		
        /* Load Config.ini - START */
		IniFile configFile = new IniFile(strFilePath+"/config/config.ini");
		/* Load Config.ini - END */
		
		String strOfficePath = configFile.getString("CONFIG", "office_path", "");
		logger.info("Office home path: " + strOfficePath);
		
		String strInputFolder = configFile.getString("CONFIG", "input_folder", "");
		logger.info("Folder location of input files to convert:" + strInputFolder );
		
		String strOutputFolder = configFile.getString("CONFIG", "output_folder", "");
		logger.info("Output folder location:" + strOutputFolder );
		
		String strOutputFormat = configFile.getString("CONFIG", "output_format", "");
		logger.info("Output image format (jpg or png):" + strOutputFormat );
		
		ArrayList<File> inputfiles = new ArrayList<File>();
		inputfiles =  listFilesForFolder(logger, new File(strInputFolder), inputfiles, strInputFolder, strOutputFolder);
		
		startOfficeManager(strOfficePath);
		
		for(int iFilesCount=0; iFilesCount<inputfiles.size(); iFilesCount++) 
		{
			File ipfile = (File)inputfiles.get(iFilesCount);			
			
//			if(ipfile.getName().endsWith(".doc"))
//			{
				String strFileName = ipfile.getName().substring(0, ipfile.getName().lastIndexOf("."));
				
				String strOutputLoc = ipfile.getAbsolutePath().substring(0, ipfile.getAbsolutePath().lastIndexOf(File.separator));
				strOutputLoc = strOutputLoc.replace(strInputFolder, strOutputFolder);
						
				try {
					File opFile = new File(strOutputLoc+File.separator+strFileName+"."+strOutputFormat);
					
					logger.info(iFilesCount+"/"+ inputfiles.size() + " --> Output File Path:: " + opFile.getAbsolutePath());
					
					if(!opFile.exists())
						convert(ipfile, opFile);
				}
				catch(Exception ex)
				{
					logger.info("Exception during converting " + ipfile.getAbsolutePath() );
				}
//			}
		}
		
		stopOfficeManager();
		
	}
	
	private static ArrayList<File> listFilesForFolder(Logger logger, final File folder, ArrayList<File> zipfiles, String strInputFolder, String strOutputFolder) {
	    for (final File fileEntry : folder.listFiles()) {
	    	if(fileEntry.isFile() && fileEntry.getName().endsWith(".doc"))
	    	{
	    		zipfiles.add(fileEntry);
	    	}
	    	else if (fileEntry.isDirectory()) {
	        	String strOutputSubFolder = fileEntry.getAbsolutePath().replace(strInputFolder, strOutputFolder);
				File fOutputLocDir = new File(strOutputSubFolder);
		        if(!fOutputLocDir.exists()) fOutputLocDir.mkdirs();
		        logger.info("created output folder : " + fOutputLocDir.getAbsolutePath());
		        
	            listFilesForFolder(logger, fileEntry, zipfiles, strInputFolder, strOutputFolder);
	        } 
	    }
	    return zipfiles;
	}
	
	@SuppressWarnings("deprecation")
	public static void startOfficeManager(String strOfficePath) throws Throwable {
        try {
        	
        	officeManager = LocalOfficeManager.builder().install().officeHome(new File(strOfficePath))
                    .portNumbers(8100, 8101, 8102, 8103, 8104).build();
                    
            officeManager.start();
            
            converter = new OfficeDocumentConverter(officeManager);
            
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    

    @SuppressWarnings("deprecation")
	public static void convert(File inputFile, File outputFile) throws Throwable {

        converter.convert(inputFile, outputFile);
    }

    public static void stopOfficeManager() throws OfficeException{
        officeManager.stop();
    }
}