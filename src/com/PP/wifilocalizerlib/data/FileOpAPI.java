package com.PP.wifilocalizerlib.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.PP.wifilocalizerlib.math.Model;

public class FileOpAPI {
	
	//global storage path
	public static final String SAVE_PATH_DIR = "/Android/data/com.TandonRobots.wifilocalizerlib/files/";
	public static final String SAVE_PATH = Environment.getExternalStoragePublicDirectory(SAVE_PATH_DIR).getPath() + "/";
	
	//files
	public static String LOG_FILE="log.csv";
	public static String MODEL_FILE="model.mod";
	
	/**
	 * Called to init directory structure.
	 */
	public static void init() {
		File f = Environment.getExternalStoragePublicDirectory(SAVE_PATH_DIR);
		if(!f.exists()) {
			boolean created = f.mkdirs();
		}
	}
	
	public static void clearData() {
		try {
			
			//remove temporary files
			File f1 = new File(SAVE_PATH + LOG_FILE);
			f1.delete();
			File f2 = new File(SAVE_PATH + MODEL_FILE);
			f2.delete();
			
			//call garbage collector
			System.gc();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}	


	/*
	 * Serialize model to file.
	 */
	public static void writeModel(Model m, String file) {
		try {
			String path = SAVE_PATH+file;		  
			File f = new File(path);
			FileOutputStream fOut = new FileOutputStream(f,false);		
			ObjectOutputStream os = new ObjectOutputStream(fOut);
			os.writeObject(m);
			os.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Model readModel(String file) {
		try {
			String path = SAVE_PATH+file;		  
			File f = new File(path);
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream is = new ObjectInputStream(fis);
			Model rtn = (Model) is.readObject();
			is.close();
			return rtn;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
  /*
   * Append string to file.
   */
  public static void writeFile(String str,String file){
	
	  try {
		  String path = SAVE_PATH+file;		  
		  File f = new File(path);
		  FileOutputStream fOut = new FileOutputStream(f,false);
		  OutputStreamWriter osw = new OutputStreamWriter(fOut);  
	      osw.write(str);
	      osw.flush();
	      osw.close();        
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
  }  
  
  /*
   * Read File contents and store in array list.
   */
  public static List<String> readFile(String file) {
	  BufferedReader br = null;
	  List<String> rtn = new ArrayList<String>();
	  try {
		  String path = SAVE_PATH+file;
		  File f = new File(path);	
		  br = new BufferedReader(new FileReader(f));
		  String line = "";
		  while((line=br.readLine())!=null) {
			  rtn.add(line);
		  }
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  finally {
		  if(br!=null) {
			  try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	  }
	  
	  return rtn;	  
  }
  
  /*
   * Append string to file.
   */
  public static void commitToFile(String str,String file){
	
	  try {
		  String path = SAVE_PATH+file;
		  File f = new File(path);
		  FileOutputStream fOut = new FileOutputStream(f,true);
		  OutputStreamWriter osw = new OutputStreamWriter(fOut);  
	      osw.write(str);
	      osw.flush();
	      osw.close();        
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
  }  
  
  /*
   * Write integer to file (used for const file)
   */
  @SuppressLint({ "NewApi", "NewApi" })
  public static void writeInt(String file, int val) {
	  try {
		  String path = SAVE_PATH+file;
		  File f = new File(path);
		  FileOutputStream fOut = new FileOutputStream(f,false);
		  OutputStreamWriter osw = new OutputStreamWriter(fOut);  
	      osw.write(""+val);
	      osw.flush();
	      osw.close();        
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }	  
  }
  
  /**
   * Read integer from file (used for const file)
   * @param file
   * @return
   */
  public static int readInt(String file) {
	  
	  BufferedReader br = null;
	  int rtn=-1;
	  try {
		  String path = SAVE_PATH+file;
		  File f = new File(path);	
		  br = new BufferedReader(new FileReader(f));
		  String line =  br.readLine().trim();
		  rtn = Integer.parseInt(line);
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  finally {
		  if(br!=null) {
			  try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	  }
	  
	  return rtn;
  }	
}