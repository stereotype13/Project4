package commandLineParser;

public class CommandLineParser {
	private String filePath = "student_schedule.txt";
	
	public CommandLineParser(String[] args) {
		//Default path to text file.
		for (int i = 0; i < args.length; ++i) {	
			if (args[i].equals("-i")) {
				filePath = args[i + 1];
			}
		}
		System.out.println("Reading " + filePath);
	}
	
	public String getFilePath() {
		return filePath;
	}
}
