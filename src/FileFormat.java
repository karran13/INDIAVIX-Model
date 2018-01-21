import java.io.FileReader;
import java.io.FileWriter;

import csvFileHandling.CSVReader;
import csvFileHandling.CSVWriter;

public class FileFormat {
	
	public String NEW_PATH="";
	public void read(String s)
	{
		try {
			// Create reader object
			CSVReader reader = new CSVReader(new FileReader(s), ',');
			// Create writer object in overwrite mode
			CSVWriter writer = new CSVWriter(new FileWriter(NEW_PATH));
			String[] newLine=new String[4];
			String[] nextLine;
			nextLine = reader.readNext();
			// Read line by line
			while ((nextLine = reader.readNext()) != null) {
				// Values available as nextLine[0],...
				newLine[0]=nextLine[4];		//Fixing Put call positions as per readoption functions reading style
				newLine[1]=nextLine[8];
				newLine[2]=nextLine[9];
				newLine[3]=nextLine[10];
				newLine[4]=nextLine[12];
		//		Date_origin=nextLine[1];
		//		Date_expiry=nextLine[2];
				writer.writeNext(newLine);
		//		System.out.println(nextLine[1]);

			}
			// Close object
			reader.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		
	}

}
