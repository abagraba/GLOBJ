package lwjgl.debug;

import java.util.HashMap;

public class Support {

	private static HashMap<Integer, String> messages = new HashMap<Integer, String>();
	
	//Based on flags populate map with messages. Messages not present are not displayed. 4096 messages per source. Each source has unique offset.
	//time based warning instance buckets to supress excessive messages.
}
