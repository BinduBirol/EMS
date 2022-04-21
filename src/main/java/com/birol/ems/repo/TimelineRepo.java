package com.birol.ems.repo;

import java.util.ArrayList;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.birol.ems.dto.TIMELINE;

public interface TimelineRepo extends CrudRepository<TIMELINE, String> {


	String getdesc= "SELECT * FROM TIMELINE order by activity_date desc ";
	@Query(value =getdesc,nativeQuery = true)
	ArrayList<TIMELINE> getAlldesc();
	
	String getbyuser= "SELECT * FROM TIMELINE where activity_username=?1 order by activity_date desc ";
	@Query(value =getbyuser,nativeQuery = true)
	ArrayList<TIMELINE> getbyUserdesc(String username);
	
	
}
