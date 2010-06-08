package com.redshape.semantic.data.hcard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 7, 2010
 * Time: 3:34:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class HCardElement {

    private String name;

    private String value;

    private boolean singularity;

    private boolean required;

    private boolean is_composite;

    private HCardElement parent;

    private List<HCardElement> childs = new ArrayList<HCardElement>();

    /**
     * Name of value holder
     */
    private String valueHandler;

    private Set<String> valueHolders = new HashSet<String>();

    public HCardElement() {}

    public HCardElement( String name ) {
        this.name = name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setValue( String value ) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setRequired( boolean required ) {
        this.required = required;
    }

    public boolean hasChilds() {
        return !this.childs.isEmpty();
    }

    private void setParent( HCardElement element ) {
        this.parent = element;
    }

    public HCardElement getParent() {
        return this.parent;
    }

    public void addChild( HCardElement child ) {
        child.setParent(this);

        this.childs.add(child);
    }

    public List<HCardElement> getChilds() {
        return this.childs;
    }

    public String getValueHandler() {
        return this.valueHandler;
    }

    public void setValueHandler( String name ) {
        this.valueHandler = name;
    }

    public void addValueHolder( String name ) {
        this.valueHolders.add(name);
    }

    public Set<String> getValueHolders() {
        return this.valueHolders;
    }

    public void setSingularity( boolean singularity ) {
        this.singularity = singularity;
    }

    public boolean isSigular() {
        return this.singularity;
    }

    public boolean isRequired() {
        return this.required;
    }

    public HCardElement getByName( String name ) {
        HCardElement result = null;
        for ( HCardElement child : this.getChilds() ) {
            if ( child.getName().equals( name ) ) {
                result = child;
                break;
            }

            result = child.getByName( name );
            if( result != null ) {
                break;
            }
        }

        return result;
    }

}
