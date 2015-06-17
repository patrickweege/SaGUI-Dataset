package test.com.pw.common.index;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.tuamotu.commons.dataset.IBookmark;
import com.tuamotu.commons.index.IdentityIndex;


public class IdentityIndexTest {

    @Test
    public void testAddOne() {
        IdentityIndex<Object> index = new IdentityIndex<Object>();
        Object toAdd = new Object();
        index.add(toAdd);
    }
    
    @Test
    public void testAddTwoWithoutColision() {
        IdentityIndex<Object> index = new IdentityIndex<Object>();
        Object one = new Object();
        Object other = new Object();
        while(System.identityHashCode(one) == System.identityHashCode(other)) {
            other = new Object();
        }
        
        index.add(one);
        index.add(other);
    }

    
    @Test
    @Ignore
    public void testAddTwoWithColision() {
        IdentityIndex<Object> index = new IdentityIndex<Object>();
        Object one = new Object();
        Object other = new Object();
        while(System.identityHashCode(one) != System.identityHashCode(other)) {
            other = new Object();
        }
        
        index.add(one);
        index.add(other);
    }
    
    @Test
    public void testAddMany() {
        final int MANY = 1000000;
        IdentityIndex<Object> index = new IdentityIndex<Object>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < MANY; i++) {
            index.add(new Object());
        }
        long end = System.currentTimeMillis();
        
        System.out.println(end - start + " for add " + MANY + " Elements...");
        System.out.println("Avg.." + (end - start)/(double)MANY + " for add " + MANY + " Elements...");
    }
    
    
    @Test
    public void testAddAndSearchMany() {
        final int MANY = 1000000;
        
        final List<Object> allAdded = new ArrayList<Object>();
        
        IdentityIndex<Object> index = new IdentityIndex<Object>();
        for (int i = 0; i < MANY; i++) {
            Object toAdd = new Object();
            allAdded.add(toAdd);
            index.add(toAdd);
        }
        
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        long total = 0;
        
        for (Object object : allAdded) {
            long start = System.currentTimeMillis();
            IBookmark<Object> bkm = index.getBookmark(object);
            long end = System.currentTimeMillis();
            
            long time = end - start;
            total = total + time;
            
            minTime = minTime > time ? time : minTime;
            maxTime = maxTime < time ? time : maxTime;
            
            Assert.assertNotNull(bkm);
        }
        System.out.println("MinTime: " + minTime + " MaxTime: " + maxTime + " AVG: " + (MANY / total));
    }
    
    @Test
    public void removeMany() {
    
        final int MANY = 1000000;

        
        
        final Map<IBookmark<Object>,Object> allAdded = new HashMap<IBookmark<Object>, Object>();
        
        IdentityIndex<Object> index = new IdentityIndex<Object>();
        for (int i = 0; i < MANY; i++) {
            Object toAdd = new Object();
            index.add(toAdd);
            allAdded.put(index.getBookmark(toAdd), toAdd);
            
        }
        
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        long total = 0;

        for (IBookmark<Object> bkm : allAdded.keySet()) {
            long start = System.currentTimeMillis();
            Object removed = index.remove(bkm);
            long end = System.currentTimeMillis();
            
            long time = end - start;
            total = total + time;
            
            minTime = minTime > time ? time : minTime;
            maxTime = maxTime < time ? time : maxTime;
            
            Assert.assertSame(allAdded.get(bkm), removed);
        }
        System.out.println("Remove Statistics - MinTime: " + minTime + " MaxTime: " + maxTime + " AVG: " + (MANY / total));

        
    }



}
