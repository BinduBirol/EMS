package com.birol.ems.repo;

import java.util.ArrayList;

import javax.persistence.Id;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.birol.ems.dto.Employee_work_schedule;


public interface WorkSchedureRepo extends CrudRepository<Employee_work_schedule, String> {
	 
	/*String getDailyworkSchQiery=" SELECT work_sh_id, userid,full_name,"
			+ " lunch_hour,TIME_FORMAT(work_start, '%r') work_start,TIME_FORMAT(work_end, '%r') work_end, if(status=1,'Attend','Absent') status,"
			+ " from_date , to_date , DATE_FORMAT(insert_time, '%d-%b-%Y') insert_time, assigned_by, assigned_by_full_name, work_desc "
			+ " FROM employee_work_schedule WHERE ?1 between from_date and to_date ";*/
	String getDailyworkSchQiery="SELECT if(status=1,'Attend','Absent') status, "
			+ "    IFNULL(ROUND( " + "        ( "
			+ "            TIMESTAMPDIFF(MINUTE, work_start, work_end) - CAST(lunch_hour AS UNSIGNED) "
			+ "        ) / 60, " + "        2 " + "    ) ,0)AS work_hour, "
			+ "emp.* FROM employee_work_schedule emp WHERE date=?1 ";
	@Query(value =getDailyworkSchQiery,nativeQuery = true)
	ArrayList<Employee_work_schedule> getDailyworkSch(String date);
	
	String get_dates_query="select * from "
			+ "(select adddate('1970-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from "
			+ " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, "
			+ " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, "
			+ " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, "
			+ " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, "
			+ " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v "
			+ "where selected_date between ?1 and ?2";
	@Query(value =get_dates_query,nativeQuery = true)
	ArrayList<String> getDates(String from_date, String to_date);
	
	/*String time_report_query =  " SELECT smm.work_sh_id, assigned_by, full_name,from_date,to_date,status,smm.insert_time,userid,  "
			+ " WEEK(dtl.date)  week ,DATE_FORMAT(dtl.date,'%d/%m/%Y') as date,  ROUND((TIMESTAMPDIFF(MINUTE,work_start, work_end)- CAST(lunch_hour AS UNSIGNED))/60,2) as work_hour, "
			+ " DAYNAME(dtl.date) day, smm.assigned_by_full_name,work_start ,work_end ,lunch_hour , "
			+ " smm.work_desc FROM employee_work_schedule_dtl dtl, employee_work_schedule smm "
			+ " WHERE dtl.work_sh_id=smm.work_sh_id and smm.full_name= ?1 and dtl.date BETWEEN ?2 and ?3";	*/
	
	String time_report_query = "SELECT " + "    WEEK(DATE,1) WEEK, " + "    DATE_FORMAT(DATE, '%d/%m/%Y') AS DATE, "
			+ "    IFNULL(ROUND( " + "        ( "
			+ "            TIMESTAMPDIFF(MINUTE, work_start, work_end) - CAST(lunch_hour AS UNSIGNED) "
			+ "        ) / 60, " + "        2 " + "    ) ,0)AS work_hour, " + "    DAYNAME(DATE) DAY, " + "    emp.* "
			+ "FROM " + "    employee_work_schedule emp " + "WHERE "
			+ "    full_name = ?1 AND DATE BETWEEN ?2 AND ?3 ";	
	@Query(value =time_report_query,nativeQuery = true)
	ArrayList<Employee_work_schedule> getTimeReport(String full_name, String from_date, String to_date);
	
	/*
	public interface TimeReportDTO {
		
		String getWeek();

		String getDate();

		String getWorkHour();

		String getDay();

		String getAssignedby();

		String getWorkStart();

		String getWorkEnd();

		String getLunch();

		String getComment();

	}*/
	

}
