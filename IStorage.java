import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Interface IStorage consists of static methods needed to interact with txt files.
 */
public interface IStorage 
{
	/**
	 * Reads data from txt file.
	 * @param path
	 * @return List of Strings that comes from txt
	 */
	public static List<String> readFromTxt(String path)
	{
		try
		{
			Scanner sc = new Scanner(new FileReader("./"+path));
			List<String> lines = new ArrayList<String>();
			while(sc.hasNextLine())
			{
					lines.add(sc.nextLine());
			}
			sc.close();
			return lines;
		}
		catch(FileNotFoundException e) 
		{							   
			System.err.println(e);
			System.out.println("File not found");
			

		}
		catch(Exception e)
		{
			e.printStackTrace();;
		}
		return null;

		
	}
	/**
	 * This method writes lines to txt file
	 * @param lines
	 * @param path
	 * @throws IOException
	 */
	public static void writeToTxt(String[] lines, String path) throws IOException 
	{
		FileWriter writer = new FileWriter(new File("./"+path),false);
		for(int i =0;i<lines.length;i++)
		{
			writer.write(lines[i]);
		}
		writer.close();
	}
	/**
	 * This method appends to txt files (Example of usage: history.txt)
	 * @see history.txt
	 * @param lines
	 * @param path
	 * @throws IOException
	 */
	public static void appendToTxt(String[] lines,String path) throws IOException
	{
		
		FileWriter writer = new FileWriter(new File("./"+path),true);
		for(int i =0;i<lines.length;i++)
		{
				writer.write(lines[i]);
		}
		writer.close();
	}
	/**
	 * Checks if file exists.
	 * @param fileName
	 * @return true or false
	 */
	public static Boolean ifExists(String fileName)
	{
		return (new File("./"+fileName).isFile());
	}
	/**
	 * Creates empty txt file.
	 * @param fileName
	 * @throws IOException
	 */
	public static void createEmptyTxtFile(String fileName) throws IOException {
		File file = new File("./"+fileName);
		file.createNewFile();
	}
}
