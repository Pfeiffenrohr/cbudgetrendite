package de.lechner.cbudget.infrastructure;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
 
import de.lechner.cbudget.infrastructure.SimpleService;
 
public class SimpleJob implements Job{
 
    @Autowired
    private SimpleService service;
 
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        service.processData();
    }
}
