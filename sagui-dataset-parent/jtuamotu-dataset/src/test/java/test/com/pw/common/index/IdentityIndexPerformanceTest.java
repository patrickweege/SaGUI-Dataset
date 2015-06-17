package test.com.pw.common.index;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import com.tuamotu.commons.dataset.IBookmark;
import com.tuamotu.commons.index.IIdentityIndex;
import com.tuamotu.commons.index.IdentityIndex;
import com.tuamotu.commons.log.FatuLoggerFactory;
import com.tuamotu.commons.util.MultiColumnPrinter;

public class IdentityIndexPerformanceTest {

    private static final Logger log = FatuLoggerFactory.create(IdentityIndexPerformanceTest.class);

    private static int INDEX_COUNT = 1;

    private IIdentityIndex<String> getIndexIntance(int iIndex) {
        switch (iIndex) {
            case 0:
                return new IdentityIndex<String>();
            default:
                throw new IllegalArgumentException(iIndex + " No valid");
        }
    }

    @Test
    public void testAddOne() {
        log.info("Start - testAddOne()");
        MultiColumnPrinter table = new MultiColumnPrinter(3, 2, "-");
        table.addTitle(new String[] { "Index", "Action", "Time Milis" });

        String toAdd = "A";

        for (int i = 0; i < INDEX_COUNT; i++) {
            IIdentityIndex<String> index = getIndexIntance(i);
            long start = Calendar.getInstance().getTimeInMillis();
            index.add(toAdd);
            long end = Calendar.getInstance().getTimeInMillis();
            long elap = end - start;
            table.add(new String[] { index.getClass().getName(), "testAddOne()", Long.toString(elap) });
        }

        table.print(true);
        log.info("Start - testAddOne()\n");
    }

    @Test
    public void testAddTwoWithoutColision() {
        log.info("Start - testAddTwoWithoutColision()");

        MultiColumnPrinter table = new MultiColumnPrinter(3, 2, "-");
        table.addTitle(new String[] { "Index", "Action", "Time Milis" });

        String one = "A-->" + UUID.randomUUID().toString();
        String other = "B-->" + UUID.randomUUID().toString();
        while (System.identityHashCode(one) == System.identityHashCode(other)) {
            other = "B-->" + UUID.randomUUID().toString();
        }

        for (int i = 0; i < INDEX_COUNT; i++) {
            IIdentityIndex<String> index = getIndexIntance(i);
            long start = Calendar.getInstance().getTimeInMillis();
            index.add(one);
            index.add(other);
            long end = Calendar.getInstance().getTimeInMillis();
            long elap = end - start;
            table.add(new String[] { index.getClass().getName(), "testAddTwoWithoutColision()", Long.toString(elap) });
        }

        table.print(true);
        log.info("End - testAddTwoWithoutColision()\n");
    }

    @Test
    @Ignore
    public void testAddTwoWithColision() {
        log.info("Start - testAddTwoWithColision()");

        MultiColumnPrinter table = new MultiColumnPrinter(3, 2, "-");
        table.addTitle(new String[] { "Index", "Action", "Time Milis" });

        String one = "A-->" + 0;
        String other = "B-->" + 1;
        long iTry = 2;
        while (System.identityHashCode(one) != System.identityHashCode(other)) {
            other = "B-->" + ++iTry;
        }

        for (int i = 0; i < INDEX_COUNT; i++) {
            IIdentityIndex<String> index = getIndexIntance(i);
            long start = Calendar.getInstance().getTimeInMillis();
            index.add(one);
            index.add(other);
            long end = Calendar.getInstance().getTimeInMillis();
            long elap = end - start;
            table.add(new String[] { index.getClass().getName(), "testAddTwoWithoutColision()", Long.toString(elap) });
        }

        table.print(true);
        log.info("End - testAddTwoWithColision()");
    }

    @Test
    public void testAddMany() {
        final int MANY = 100000;
        log.info("Start - testAddMany() " + MANY + " Elements");

        MultiColumnPrinter table = new MultiColumnPrinter(4, 2, "-");
        table.addTitle(new String[] { "Index", "Action", "Time Milis", "Avg" });

        Set<String> data = new HashSet<String>();
        for (int i = 0; i < MANY; i++) {
            data.add(i + " --> Teste");
        }

        for (int i = 0; i < INDEX_COUNT; i++) {
            IIdentityIndex<String> index = getIndexIntance(i);
            long start1 = Calendar.getInstance().getTimeInMillis();
            for (String elem : data) {
                index.add(elem);
            }
            long stop = Calendar.getInstance().getTimeInMillis();
            long elap = (long) stop - (long) start1;
            double avg = (double) elap / (double) MANY;

            table.add(new String[] { index.getClass().getName(), "testAddMany() " + MANY, Long.toString(elap), Double.toString(avg) });

            index = null;
            System.gc();
        }

        table.print(true);
        log.info("End - testAddMany() " + MANY + " Elements");
    }

    @Test
    public void testAddAndSearchMany() {
        final int MANY = 1000000;
        log.info("Start - testAddAndSearchMany() " + MANY + " Elements");

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

        log.info("End - testAddAndSearchMany() " + MANY + " Elements");
    }

    @Test
    public void removeMany() {
        final int MANY = 1000000;

        log.info("Start - removeMany() " + MANY + " Elements");

        final Map<IBookmark<Object>, Object> allAdded = new HashMap<IBookmark<Object>, Object>();

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

        log.info("End - removeMany() " + MANY + " Elements");
    }

}
