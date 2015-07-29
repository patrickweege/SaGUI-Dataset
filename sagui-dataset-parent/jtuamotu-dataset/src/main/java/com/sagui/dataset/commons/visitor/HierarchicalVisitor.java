package com.sagui.dataset.commons.visitor;

import java.util.Collection;

public abstract class HierarchicalVisitor<T> {

    private final VisitorDirection[] direction;

    public HierarchicalVisitor(VisitorDirection... direction) {
        this.direction = direction;
    }

    protected abstract Collection<T> getChildren(T parent);

    protected abstract T getParent(T child);

    protected abstract boolean acceptParent(T parent);

    protected abstract boolean acceptChild(T child);

    public final void visit(Collection<T> toVisit) {
        for (T elem : toVisit) {
            this.visit(elem);
        }
    }

    public final void visit(T toVisit) {
        for (VisitorDirection dir : direction) {
            switch (dir) {
                case TO_TOP:
                    visitParents(toVisit);
                    break;
                case TO_BOTTON:
                    visitChildren(toVisit);
                    break;
            }
        }
    }

    private final void visitParents(T child) {
        T parent = child;
        if (acceptParent(parent)) {
            while ((parent = getParent(parent)) != null) {
                if (!acceptParent(parent)) break;
            }
        }
    }

    private final void visitChildren(T parent) {
        if (acceptChild(parent)) {
            for (T child : getChildren(parent)) {
                visitChildren(child);
            }
        }
    }
}
