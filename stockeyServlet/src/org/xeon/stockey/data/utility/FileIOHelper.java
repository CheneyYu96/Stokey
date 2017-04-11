package org.xeon.stockey.data.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class FileIOHelper {
	private String fileName;
	public FileIOHelper(String name){
		this.fileName = name;
	}
	public void saveObject(Object obj){
		try{
		ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
		oo.writeObject(obj);
		oo.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Object readObject(){
		Object result = null;
		try{
			ObjectInputStream oi = new ObjectInputStream(new FileInputStream(new File(fileName)));
			result = oi.readObject();
			oi.close();
			}catch(IOException e){
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		return result;
	}
}
