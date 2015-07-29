package com.sagui.dataset.commons.index;


public class ElementNotFoundException extends RuntimeException {
    
    private static final String ELEMENT_NOT_FOUND = "Element not Found at This Index";
    
    public ElementNotFoundException() {
        super(ELEMENT_NOT_FOUND);
    }
    
}
