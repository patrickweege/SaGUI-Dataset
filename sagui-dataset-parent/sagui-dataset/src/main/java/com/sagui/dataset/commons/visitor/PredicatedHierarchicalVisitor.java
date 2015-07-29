package com.sagui.dataset.commons.visitor;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;

public abstract class PredicatedHierarchicalVisitor<T> extends HierarchicalVisitor<T> {

    private Predicate acceptParentPredicate;
    private Predicate acceptChildPredicate;
    private Closure acceptParentClosure;
    private Closure acceptChildClosure;

    public PredicatedHierarchicalVisitor(Predicate acceptParentPredicate, Predicate acceptChildPredicate, Closure acceptParentClosure, Closure acceptChildClosure, VisitorDirection... direction) {
        super(direction);
        this.acceptParentPredicate = acceptParentPredicate;
        this.acceptChildPredicate = acceptChildPredicate;
        this.acceptParentClosure = acceptParentClosure;
        this.acceptChildClosure = acceptChildClosure;
    }

    public PredicatedHierarchicalVisitor(Predicate acceptPredicate, Closure acceptClosure, VisitorDirection... direction) {
        this(acceptPredicate, acceptPredicate, acceptClosure, acceptClosure, direction);
    }

    @Override
    protected boolean acceptParent(T parent) {
        boolean accept = acceptParentPredicate.evaluate(parent);
        if (accept) acceptParentClosure.execute(parent);
        return accept;
    }

    @Override
    protected boolean acceptChild(T child) {
        boolean accept = acceptChildPredicate.evaluate(child);
        if (accept) acceptChildClosure.execute(child);
        return accept;
    }

}
