package mainFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;

public class inHashmap {
	String out_path = "./index.post";

	
	public inHashmap() {
		
	}
	
	public void inHash() throws IOException, ClassNotFoundException {
		FileInputStream fileStream = new FileInputStream(out_path);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		System.out.println("읽어온 객체의 type → " + object.getClass());
		
		HashMap hashMap = (HashMap)object;
		Iterator<String> it = hashMap.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			System.out.println(key + " → " + hashMap.get(key));
		}
	}
}
