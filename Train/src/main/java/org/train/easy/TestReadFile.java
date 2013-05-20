package org.train.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TestReadFile {

	public static void main(String[] args) {
		BufferedReader br = null;
		HashMap<String,Integer> hash = new HashMap<String,Integer>();
		String line = null;
		try {
			br = new BufferedReader(new FileReader( new File("D:/aaa.txt")));
			while((line = br.readLine()) != null){
				if(hash.containsKey(line)){
					int n = hash.get(line);
					hash.put(line, ++n);
				}else{
					hash.put(line, 1);
				}
			}
			for(String key : hash.keySet()){
				System.out.println("key : " + key + " n :" + hash.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {br.close();} catch (IOException e) {
			}
		}
	}
}
