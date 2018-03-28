package cn.wrapper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataStream {
	private boolean input=false;
	private String filename;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
//	private BufferedReader buffin=null;
	public DataStream(String filename, String actions) throws FileNotFoundException {
		this.input=actions.startsWith("r");
		this.filename=filename;
		if(this.input) {
			inputStream=new DataInputStream(new FileInputStream(filename));
//			buffin=new BufferedReader(inputStream);
		}else {
			outputStream= new DataOutputStream(new FileOutputStream(filename));
		}
	}
	public void close() throws IOException {
		if(this.input) {
			inputStream.close();
		}else {
			outputStream.close();
		}
	}
	public float readFloat() throws IOException{
		return inputStream.readFloat();
	}
	public int readInt() throws IOException{
		return inputStream.readInt();
	}
	public void writeInt(int v) throws IOException {
		outputStream.writeInt(v);
	}
	public void writeFloat(float v) throws IOException {
		outputStream.writeFloat(v);
	}
	public String readLine() throws IOException {
//		buffin.readLine();
		return inputStream.readLine();
	}
}
