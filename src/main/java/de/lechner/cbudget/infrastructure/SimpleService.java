package de.lechner.cbudget.infrastructure;

import de.lechner.cbudget.rendite.ComputeRenditeBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lechner.cbudget.rendite.ComputeRendite;
 
@Service
public class SimpleService {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleService.class);
    @Autowired
    ComputeRendite cr;

    @Autowired
    ComputeRenditeBatch crb;
    public void processData() {

        //cr.rendite();
        crb.compute();

    }
}
