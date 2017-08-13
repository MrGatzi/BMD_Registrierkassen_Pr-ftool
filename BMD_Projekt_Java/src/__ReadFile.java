import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
//classe die zum einlesen von Files dient.
public class __ReadFile {
	
	// Universale funktion um einen Text aus einem angebenen File zu lesen. (Input File-Path)
		public String Readtxt(String File) throws IOException{
			String Output="";
			FileInputStream inputStream = new FileInputStream(File);
			try {
				Output = IOUtils.toString(inputStream);
			} finally {
				inputStream.close();
			}
			return Output;
			
		}
}
