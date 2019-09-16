package ntp.test;

import java.io.File;

import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;

@SuppressWarnings("deprecation")
public class DocToJpeg {

	private static volatile OfficeManager officeManager;
	@SuppressWarnings("deprecation")
	private static volatile OfficeDocumentConverter converter;

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