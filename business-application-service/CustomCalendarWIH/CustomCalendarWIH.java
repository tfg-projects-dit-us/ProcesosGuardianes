package com.kie;

import java.util.Map;
import java.util.SortedSet;
import java.util.Date;
import java.util.HashMap;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomCalendarWIH implements WorkItemHandler  {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomCalendarWIH.class);
	public final static String MONTH = "Month";
	public final static String YEAR = "Year";

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
    	
    	int year = (int) workItem.getParameter(YEAR);
    	int month = (int) workItem.getParameter(MONTH);
    	SortedSet<Date> fechas = null;
 			
 			CalendarUI calendarUI = new CalendarUI();
			calendarUI.showCalendarDialog(year, month);
			calendarUI.getSelectedDates();
			
			fechas = calendarUI.getSelectedDates();

    	
    	logger.info("Las fechas seleccionadas son ", fechas);
    	
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("Result", fechas);
        manager.completeWorkItem(workItem.getId(), results);
        
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        // No es necesario implementar el método abortWorkItem
    }

    

    
    
   
}


