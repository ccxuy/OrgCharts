package beans;

import java.sql.Clob;
import java.sql.Timestamp;

public class NodeBean {
	
	Clob struct;
	int seq_no;
	Timestamp time;
	String user_id;
	int data_lock;
	String chart_name;
	
	public NodeBean() {}
	
	public NodeBean(Clob initStruct, int initSeq_no, Timestamp initTime, String initUser_id, int initData_lock, String initChart_name){
		struct=initStruct;
		seq_no=initSeq_no;
		time=initTime;
		user_id=initUser_id;
		data_lock=initData_lock;
		chart_name=initChart_name;
	}
	
	public Clob getStruct(){
		return struct;
	}
	public void setStruct(Clob initStruct){
		struct=initStruct;
	}
	public int getSeq_no(){
		return seq_no;
	}
	public void setSeq_no(int initSeq_no){
		seq_no=initSeq_no;
	}
	public Timestamp getTime(){
		return time;
	}
	public void setTime(Timestamp initTime){
		time=initTime;
	}
	public String getUser_id(){
		return user_id;
	}
	public void setUser_id(String initUser_id){
		user_id=initUser_id;
	}
	public int getData_lock(){
		return data_lock;
	}
	public void setData_lock(int initData_lock){
		data_lock=initData_lock;
	}
	public String getChart_name(){
		return chart_name;
	}
	public void setChart_name(String initChart_name){
		chart_name=initChart_name;
	}
}
