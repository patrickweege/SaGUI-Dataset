package test.com.pw.common.dataset;

import org.apache.commons.collections4.ComparatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tuamotu.commons.comparator.BeanComparatorUtil;
import com.tuamotu.commons.comparator.IBeanComparator;
import com.tuamotu.commons.comparator.IBeanComparator.Order;
import com.tuamotu.commons.comparator.IFieldComparatorMetadata;
import com.tuamotu.commons.dataset.Dataset;
import com.tuamotu.commons.dataset.DatasetIndex;
import com.tuamotu.commons.dataset.IBookmark;
import com.tuamotu.commons.dataset.IDatasetField;
import com.tuamotu.commons.field.ArrayBeanFieldImpl;

public class DatasetTest {

    private IDatasetField<Object[]> ID_FIELD;
    private IDatasetField<Object[]> NAME_FIELD;

    private Dataset<Object[]> dataset;
    private DatasetIndex<Object[]> ID_INDEX;

    @Before
    public final void before() {
        this.dataset = new Dataset<Object[]>();
        this.ID_FIELD = dataset.addField(new ArrayBeanFieldImpl<Object>(0, "ID", false));
        this.NAME_FIELD = dataset.addField(new ArrayBeanFieldImpl<Object>(1, "NAME", false));

        IFieldComparatorMetadata<Object[]> metadata = new IFieldComparatorMetadata<Object[]>(ID_FIELD, Order.ASC, true, ComparatorUtils.naturalComparator());
        IBeanComparator<Object[]> comparator = BeanComparatorUtil.getBeanComparator(metadata);
        this.ID_INDEX = this.dataset.addIndex(comparator);

    }

    @Test
    public final void testAddOne() {
        dataset.add(getTestRecord(1));

        Assert.assertEquals(1, dataset.getRecordCount());
    }

    @Test
    public final void testAddAndRemoveOne() {
        Dataset<Object[]> theDataset = this.dataset;

        IBookmark<Object[]> added = theDataset.add(getTestRecord(1));
        Assert.assertEquals(1, theDataset.getRecordCount());

        theDataset.remove(added);
        Assert.assertEquals(0, theDataset.getRecordCount());

    }

    @Test
    public final void findByExample() {
        Dataset<Object[]> theDataset = this.dataset;

        Object[] bean1 = getTestRecord(1, 1);
        theDataset.add(bean1);
        Assert.assertEquals(1, theDataset.getRecordCount());

        theDataset.add(getTestRecord(2, 2));
        Assert.assertEquals(2, theDataset.getRecordCount());

        theDataset.add(getTestRecord(1, 3));
        theDataset.add(getTestRecord(1, 4));
        theDataset.add(getTestRecord(1, 5));
        theDataset.add(getTestRecord(1, 6));
        Object[] last = getTestRecord(1, 7);
        theDataset.add(last);

        IBookmark<Object[]> first = theDataset.findFirst(getTestRecord(1), ID_INDEX);
        Object[] found = theDataset.getRow(first);
        Assert.assertSame(bean1, found);

    }

    private final Object[] getTestRecord(int id) {
        return getTestRecord(id, 0);
    }

    private final Object[] getTestRecord(int id, int sequence) {
        Object[] record = new Object[] { id, String.format("Test Record %1$s", id), sequence };
        return record;
    }

}
