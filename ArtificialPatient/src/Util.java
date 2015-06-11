import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


public class Util {
	private static final String url_host = "http://pl44.host.cs.st-andrews.ac.uk/AndroidApp/v2/";

    // URL list
    public static final String url_all_patients_info = url_host + "get_all_patients_info.php";
    public static final String url_patient_info = url_host + "get_patient_info.php";
    public static final String url_record_preference = url_host + "record_preference.php";
    public static final String url_record_select_sequence = url_host + "record_select_sequence.php";
    public static final String url_record_select_group_question = url_host + "record_select_group_question.php";
    public static final String TRAIN_FREQUENCY_INTERVENTION_NET_URL = url_host + "neuralNetFactory/runFrequencyInterventionNet.php";
    public static final String FREQUENCY_INTERVENTION_NET_URL = url_host + "neuralNetFactory/neuralNetfrequencyIntervention.eg";
    public static final String TRAIN_SLOT_INTERVENTION_NET_URL = url_host + "neuralNetFactory/runSlotInterventionNet.php";
    public static final String SLOT_INTERVENTION_NET_URL = url_host + "neuralNetFactory/neuralNetslotIntervention.eg";
    public static final String TRAIN_SELECT_SEQUENCE_NET_URL = url_host + "neuralNetFactory/runSelectSequenceNet.php";
    public static final String SELECT_SEQUENCE_NET_URL = url_host + "neuralNetFactory/neuralNetSelectSequence.eg";
    public static final String TRAIN_SELECT_GOAL_NET_URL = url_host + "neuralNetFactory/runSelectGoalNet.php";
    public static final String SELECT_GOAL_NET_URL = url_host + "neuralNetFactory/neuralNetSelectGoal.eg";
    public static final String TRAIN_SELECT_BEHAVIOUR_NET_URL = url_host + "neuralNetFactory/runSelectBehaviourNet.php";
    public static final String SELECT_BEHAVIOUR_NET_URL = url_host + "neuralNetFactory/neuralNetSelectBehaviour.eg";
    
    // file name
    public static final String FREQUENCY_INTERVENTION_NET_EG = "neuralNetfreqeuncyIntervention.eg";
    public static final String SLOT_INTERVENTION_NET_EG = "neuralNetslotIntervention.eg";
    public static final String SELECT_SEQUENCE_NET_EG = "neuralNetSelectSequence.eg";
    public static final String SELECT_GOAL_NET_EG = "neuralNetSelectGoal.eg";
    public static final String SELECT_BEHAVIOUR_NET_EG = "neuralNetSelectBehaviour.eg";
    
    // JSON Node names
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_PATIENTS = "patients";
    public static final String TAG_PATIENT = "patient";
    public static final String TAG_PID = "pid";
    public static final String TAG_NAME = "name";
    public static final String TAG_AGE = "age";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_SET_FREQ = "set_frequency";
    public static final String TAG_SET_SLOT = "set_slot";
    public static final String TAG_CONTROL_LEVEL = "control_level";
    public static final String TAG_SEQ1 = "seq1";
    public static final String TAG_SEQ2 = "seq2";
    public static final String TAG_SEQ3 = "seq3";
    public static final String TAG_SEQ4 = "seq4";
    public static final String TAG_SEQ5 = "seq5";
    public static final String TAG_SEQ6 = "seq6";
    public static final String TAG_SEQ7 = "seq7";
    public static final String TAG_SEQ8 = "seq8";
    public static final String TAG_PREVG = "previous_group";
    public static final String TAG_GROUP3 = "group3";
    public static final String TAG_GROUP4 = "group4";
    public static final String TAG_GROUP10 = "group10";
    public static final String TAG_GROUP11 = "group11";
    public static final String TAG_FREQ1 = "frequency1";
    public static final String TAG_FREQ2 = "frequency2";
    public static final String TAG_FREQ3 = "frequency3";
    public static final String TAG_FREQ4 = "frequency4";
    public static final String TAG_FREQ5 = "frequency5";
    public static final String TAG_FREQ6 = "frequency6";
    public static final String TAG_FREQ7 = "frequency7";
    public static final String TAG_SLOT1 = "slot1";
    public static final String TAG_SLOT2 = "slot2";
    public static final String TAG_SLOT3 = "slot3";
    public static final String TAG_SLOT4 = "slot4";
    public static final String TAG_SLOT5 = "slot5";
    public static final String TAG_SLOT6 = "slot6";
        
    public static String getStringPatirntInfo(HashMap<String, String> patient) {
		StringBuilder sb = new StringBuilder();
		sb.append(" Name: " + patient.get(Util.TAG_NAME));
		sb.append(" Age: " + patient.get(Util.TAG_AGE));
		sb.append(" Gender: " + (patient.get(Util.TAG_GENDER).equals("0")?"Male":"Female"));
		sb.append(" Preference: " + (patient.get(Util.TAG_SET_SLOT).equals("1")?"Slot ":"") + (patient.get(Util.TAG_SET_FREQ).equals("1")?"Frequency ":""));
		return sb.toString();
	}
    
	public static String getSlotTime(ArrayList<Integer> slots) {
		StringBuilder sb = new StringBuilder();
		for (int slot : slots) {
			if (sb.length()!=0) sb.append(", ");
			switch (slot) {
				case 1: sb.append("04:00 - 07:00"); break;
				case 2: sb.append("07:00 - 12:00"); break;
				case 3: sb.append("12:00 - 17:00"); break;
				case 4: sb.append("17:00 - 21:00"); break;
				case 5: sb.append("21:00 - 0:00"); break;
				case 6: sb.append("00:00 - 04:00"); break;
				default: break;
			}
		}
		return sb.toString();
	}
	
	public static ArrayList<Integer> getSlots(HashMap<String, String> preference) {
		ArrayList<Integer> slots = new ArrayList<Integer>();
		if (preference.get(TAG_SLOT1).equals("1")) slots.add(1);
		if (preference.get(TAG_SLOT2).equals("1")) slots.add(2);
		if (preference.get(TAG_SLOT3).equals("1")) slots.add(3);
		if (preference.get(TAG_SLOT4).equals("1")) slots.add(4);
		if (preference.get(TAG_SLOT5).equals("1")) slots.add(5);
		if (preference.get(TAG_SLOT6).equals("1")) slots.add(6);
		return slots;
	}
	
	public static int getFrequency(HashMap<String, String> preference) {
		int freq;
		if (preference.get(TAG_FREQ1).equals("1")) freq = 1;
		else if (preference.get(TAG_FREQ2).equals("1")) freq = 2;
		else if (preference.get(TAG_FREQ3).equals("1")) freq = 3;
		else if (preference.get(TAG_FREQ4).equals("1")) freq = 4;
		else if (preference.get(TAG_FREQ5).equals("1")) freq = 5;
		else if (preference.get(TAG_FREQ6).equals("1")) freq = 6;
		else freq = 7;
		return freq;
	}
	
    public static void loadNet() {
		String[] loadURLs = { Util.FREQUENCY_INTERVENTION_NET_URL, Util.SLOT_INTERVENTION_NET_URL,
							  Util.SELECT_SEQUENCE_NET_URL, Util.SELECT_GOAL_NET_URL, Util.SELECT_BEHAVIOUR_NET_URL };
		String[] fileNames = { Util.FREQUENCY_INTERVENTION_NET_EG, Util.SLOT_INTERVENTION_NET_EG,
				  			   Util.SELECT_SEQUENCE_NET_EG, Util.SELECT_GOAL_NET_EG, Util.SELECT_BEHAVIOUR_NET_EG };

        try {

        	for (int i=0; i<fileNames.length; i++) {
	            URL url = new URL(loadURLs[i]);
	            URLConnection connection = url.openConnection();
	
	            connection.connect();
	            InputStream inputStream = connection.getInputStream();
	
	            File neuralNet = new File(fileNames[i]);
	            FileOutputStream outputStream = new FileOutputStream(neuralNet);
	
	            byte buffer[] = new byte[1024];
	            int dataSize;
	            
	            while ((dataSize = inputStream.read(buffer)) != -1) {
	            	outputStream.write(buffer, 0, dataSize);
	            }
	
	            outputStream.close();

	            //System.out.println("Updated!");
        	}
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    public static void loadNet(String loadURL, String fileName) {
		try {

        	URL url = new URL(loadURL);
            URLConnection connection = url.openConnection();

            connection.connect();
            InputStream inputStream = connection.getInputStream();

            File neuralNet = new File(fileName);
            FileOutputStream outputStream = new FileOutputStream(neuralNet);

            byte buffer[] = new byte[1024];
            int dataSize;
            
            while ((dataSize = inputStream.read(buffer)) != -1) {
            	outputStream.write(buffer, 0, dataSize);
            }

            outputStream.close();

            //System.out.println("Updated!");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
    public static void trainNet() {
		//Train
    	System.out.println("Neural networks is being trained...");
		try {
			String[] trainURLs = { TRAIN_FREQUENCY_INTERVENTION_NET_URL, TRAIN_SLOT_INTERVENTION_NET_URL,
								   TRAIN_SELECT_SEQUENCE_NET_URL, TRAIN_SELECT_GOAL_NET_URL, SELECT_BEHAVIOUR_NET_URL };
			
			for (int i=0; i<trainURLs.length; i++) {
	            URL url = new URL(trainURLs[i]);
	            try {
	
	                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	
	                in.close();
	
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
			}
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
    public static void trainNet(String trainURL) {
		//Train
    	System.out.println("Neural networks is being trained...");
		try {
			URL url = new URL(trainURL);
            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                in.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
    
    public static ArrayList<HashMap<String, String>> getAllPatient() {
    	ArrayList<HashMap<String, String>> patientsList = new ArrayList<HashMap<String, String>>();
    	Map<String,Object> params = new LinkedHashMap<>();
        
    	try {
        	JsonObject json = getHTTP(params, url_all_patients_info);
        	
           	// Checking for SUCCESS TAG
            int success = json.get(TAG_SUCCESS).getAsInt();

            if (success == 1) {
                // patients found
                // Getting Array of Patients
            	JsonArray patients = json.get(TAG_PATIENTS).getAsJsonArray();

                // looping through All Patient
                for (int i = 0; i < patients.size(); i++) {
                    JsonObject c = patients.get(i).getAsJsonObject();

                    // Storing each json item in variable
                    String id = c.get(TAG_PID).getAsString();
                    String name = c.get(TAG_NAME).getAsString();
                    String age = c.get(TAG_AGE).getAsString();
                    String gender = c.get(TAG_GENDER).getAsString();
                    String set_frequency = c.get(TAG_SET_FREQ).getAsString();
                    String set_slot = c.get(TAG_SET_SLOT).getAsString();

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_PID, id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_AGE, age);
                    map.put(TAG_GENDER, gender);
                    map.put(TAG_SET_FREQ, set_frequency);
                    map.put(TAG_SET_SLOT, set_slot);

                    // adding HashList to ArrayList
                    patientsList.add(map);
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return patientsList;
    }
    
    public static HashMap<String, String> getPatientPreference(String pid, int ctlLv) {
    	HashMap<String, String> preference = new HashMap<String, String>();
    	Map<String,Object> params = new LinkedHashMap<>();
    	params.put(TAG_PID, pid);
        params.put(TAG_CONTROL_LEVEL, String.valueOf(ctlLv));
        
    	try {
        	JsonObject json = getHTTP(params, url_patient_info);
        	
           	// Checking for SUCCESS TAG
            int success = json.get(TAG_SUCCESS).getAsInt();

            if (success == 1) {
                // patients found
                // Getting Array of Patients
            	JsonArray patients = json.get(TAG_PATIENT).getAsJsonArray();

                // looping through All Patient
               if (patients.size() > 0) {
                    JsonObject c = patients.get(0).getAsJsonObject();

                    // Storing each json item in variable
                    String id = c.get(TAG_PID).getAsString();
                    String name = c.get(TAG_NAME).getAsString();
                    String age = c.get(TAG_AGE).getAsString();
                    String gender = c.get(TAG_GENDER).getAsString();
                    String freq1 = (c.get(TAG_FREQ1).isJsonNull()?"":c.get(TAG_FREQ1).getAsString());
                    String freq2 = (c.get(TAG_FREQ2).isJsonNull()?"":c.get(TAG_FREQ2).getAsString());
                    String freq3 = (c.get(TAG_FREQ3).isJsonNull()?"":c.get(TAG_FREQ3).getAsString());
                    String freq4 = (c.get(TAG_FREQ4).isJsonNull()?"":c.get(TAG_FREQ4).getAsString());
                    String freq5 = (c.get(TAG_FREQ5).isJsonNull()?"":c.get(TAG_FREQ5).getAsString());
                    String freq6 = (c.get(TAG_FREQ6).isJsonNull()?"":c.get(TAG_FREQ6).getAsString());
                    String freq7 = (c.get(TAG_FREQ7).isJsonNull()?"":c.get(TAG_FREQ7).getAsString());
                    String slot1 = (c.get(TAG_SLOT1).isJsonNull()?"":c.get(TAG_SLOT1).getAsString());
                    String slot2 = (c.get(TAG_SLOT2).isJsonNull()?"":c.get(TAG_SLOT2).getAsString());
                    String slot3 = (c.get(TAG_SLOT3).isJsonNull()?"":c.get(TAG_SLOT3).getAsString());
                    String slot4 = (c.get(TAG_SLOT4).isJsonNull()?"":c.get(TAG_SLOT4).getAsString());
                    String slot5 = (c.get(TAG_SLOT5).isJsonNull()?"":c.get(TAG_SLOT5).getAsString());
                    String slot6 = (c.get(TAG_SLOT6).isJsonNull()?"":c.get(TAG_SLOT6).getAsString());

                    // adding each child node to HashMap key => value
                    preference.put(TAG_PID, id);
                    preference.put(TAG_NAME, name);
                    preference.put(TAG_AGE, age);
                    preference.put(TAG_GENDER, gender);
                    preference.put(TAG_FREQ1, freq1);
                    preference.put(TAG_FREQ2, freq2);
                    preference.put(TAG_FREQ3, freq3);
                    preference.put(TAG_FREQ4, freq4);
                    preference.put(TAG_FREQ5, freq5);
                    preference.put(TAG_FREQ6, freq6);
                    preference.put(TAG_FREQ7, freq7);
                    preference.put(TAG_SLOT1, slot1);
                    preference.put(TAG_SLOT2, slot2);
                    preference.put(TAG_SLOT3, slot3);
                    preference.put(TAG_SLOT4, slot4);
                    preference.put(TAG_SLOT5, slot5);
                    preference.put(TAG_SLOT6, slot6);
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return preference;
    }
    
    public static void recordFrequency(String pid, int ctlLv, int freq, double rate) {
    	Map<String,Object> params = new LinkedHashMap<>();
        params.put(TAG_PID, pid);
        params.put(TAG_CONTROL_LEVEL, String.valueOf(ctlLv));
        switch (freq) {
        	case 1: params.put(TAG_FREQ1, String.valueOf(rate)); break;
        	case 2: params.put(TAG_FREQ2, String.valueOf(rate)); break;
        	case 3: params.put(TAG_FREQ3, String.valueOf(rate)); break;
        	case 4: params.put(TAG_FREQ4, String.valueOf(rate)); break;
        	case 5: params.put(TAG_FREQ5, String.valueOf(rate)); break;
        	case 6: params.put(TAG_FREQ6, String.valueOf(rate)); break;
        	case 7: params.put(TAG_FREQ7, String.valueOf(rate)); break;
        	default: break;
        }
        
        postHTTP(params, url_record_preference);
    }
    
    public static void recordSlot(String pid, int ctlLv, int slot, double rate) {
    	Map<String,Object> params = new LinkedHashMap<>();
        params.put(TAG_PID, pid);
        params.put(TAG_CONTROL_LEVEL, String.valueOf(ctlLv));
        switch (slot) {
        	case 1: params.put(TAG_SLOT1, String.valueOf(rate)); break;
        	case 2: params.put(TAG_SLOT2, String.valueOf(rate)); break;
        	case 3: params.put(TAG_SLOT3, String.valueOf(rate)); break;
        	case 4: params.put(TAG_SLOT4, String.valueOf(rate)); break;
        	case 5: params.put(TAG_SLOT5, String.valueOf(rate)); break;
        	case 6: params.put(TAG_SLOT6, String.valueOf(rate)); break;
        	default: break;
        }
        
        postHTTP(params, url_record_preference);
    }
    
    public static void recordSelectGroupQuestion(String pid, int ctlLv, int prevG, int group, double rate) {
    	Map<String,Object> params = new LinkedHashMap<>();
        params.put(TAG_PID, pid);
        params.put(TAG_CONTROL_LEVEL, String.valueOf(ctlLv));
        params.put(TAG_PREVG, String.valueOf(prevG));
        switch (group) {
        	case 3: params.put(TAG_GROUP3, String.valueOf(rate)); break;
        	case 4: params.put(TAG_GROUP4, String.valueOf(rate)); break;
        	case 10: params.put(TAG_GROUP10, String.valueOf(rate)); break;
        	case 11: params.put(TAG_GROUP11, String.valueOf(rate)); break;
        	default: break;
        }
        
        postHTTP(params, url_record_select_group_question);
    }
    
    public static void recordSelectSequence(String pid, int ctlLv, int seq, double rate) {
    	Map<String,Object> params = new LinkedHashMap<>();
        params.put(TAG_PID, pid);
        params.put(TAG_CONTROL_LEVEL, String.valueOf(ctlLv));
        switch (seq) {
        	case 1: params.put(TAG_SEQ1, String.valueOf(rate)); break;
        	case 2: params.put(TAG_SEQ2, String.valueOf(rate)); break;
        	case 3: params.put(TAG_SEQ3, String.valueOf(rate)); break;
        	case 4: params.put(TAG_SEQ4, String.valueOf(rate)); break;
        	case 5: params.put(TAG_SEQ5, String.valueOf(rate)); break;
        	case 6: params.put(TAG_SEQ6, String.valueOf(rate)); break;
        	case 7: params.put(TAG_SEQ7, String.valueOf(rate)); break;
        	case 8: params.put(TAG_SEQ8, String.valueOf(rate)); break;
        	default: break;
        }
        
        postHTTP(params, url_record_select_sequence);
    }
    
    private static void postHTTP(Map<String,Object> params, String url) {
    	try {
	        StringBuilder postData = new StringBuilder();
	        for (Map.Entry<String,Object> param : params.entrySet()) {
	            if (postData.length() != 0) postData.append('&');
	            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	            postData.append('=');
	            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	        }
	        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
	        
	        //System.out.println(postData.toString());
	
	        final HttpURLConnection conn = getHttpConnection(new URL(url));
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	        conn.setDoOutput(true);
	        conn.getOutputStream().write(postDataBytes);

	        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        //for (int c; (c = in.read()) >= 0; System.out.print((char)c));
	        in.close();
	        
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    private static JsonObject getHTTP(Map<String,Object> params, String url) {
    	JsonObject json = null;
    	
    	try {
    		
    		StringBuilder postData = new StringBuilder();
	        for (Map.Entry<String,Object> param : params.entrySet()) {
	            if (postData.length() != 0) postData.append('&');
	            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	            postData.append('=');
	            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	        }
	        
	        final HttpURLConnection conn = getHttpConnection(new URL(url+(postData.length()==0?"":"?")+postData.toString()));
	        conn.setRequestMethod("GET");
		    conn.connect();

	        try {
	        	json = parseBody(conn).getAsJsonObject();
	        	
	        	//System.out.println(json);
	        } catch (JsonParseException e) {
	            e.printStackTrace();
	        }
		} catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
	    
	    return json;
	    
    }
	    
    private static JsonElement parseBody(final HttpURLConnection conn) {
        try {
            InputStream is = conn.getInputStream();
            Reader reader = new InputStreamReader(is);
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(reader, JsonElement.class);
        } catch (JsonParseException e) {
            throw new RuntimeException("Error parsing response from server.", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading response from server.", e);
        }
    }
    
    private static HttpURLConnection getHttpConnection(final URL url) throws IOException {
        final String protocol = url.getProtocol();
        if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https")) {
            final URLConnection conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                return (HttpURLConnection) conn;
            } else {
                throw new RuntimeException("Got URLConnection of type " + conn.getClass());
            }
        } else {
            throw new IllegalArgumentException("URL needs to use the HTTP or HTTPS protocols.");
        }
    }
}
