package com.birol.ems.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.birol.ems.dto.TimeReportDTO;

public interface TimeReportRepo extends JpaRepository<TimeReportDTO, String>{
	
	String time_report_query =  " SELECT  WEEK(date)  week ,dtl.date ,  ROUND((TIMESTAMPDIFF(MINUTE,work_start, work_end)- CAST(lunch_hour AS UNSIGNED))/60,2) as workHour, "
			+ " DAYNAME(date) day, smm.assigned_by_full_name assignedby,work_start as workStart,work_end workEnd,lunch_hour lunch, "
			+ " smm.work_desc as comment FROM employee_work_schedule_dtl dtl, employee_work_schedule smm "
			+ " WHERE dtl.work_sh_id=smm.work_sh_id and smm.full_name= ?1 and dtl.date BETWEEN ?2 and ?3";	
	@Query(value =time_report_query,nativeQuery = true)
	ArrayList<TimeReportDTO> getTimeReport(String full_name, String from_date, String to_date);

}
