package com.redshape.semantic.processor.engines.hcard.optimizers;

import com.redshape.semantic.data.hcard.HCardElement;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 8, 2010
 * Time: 5:02:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class NToFn implements IOptimizer {

    public void optimize( HCardElement tree ) {
        HCardElement fn = tree.getByName("fn");
        HCardElement n = tree.getByName("n");

        fn.setValue( this.buildFullName( n ) );
    }

    private String buildFullName( HCardElement nameElement ) {
        StringBuilder builder = new StringBuilder();

        String honorificPrefix = nameElement.getByName("honorific-prefix").getValue();
        if ( !honorificPrefix.isEmpty() ) {
            builder.append( honorificPrefix )
                   .append( " " );
        }

        builder.append( nameElement.getByName("first-name").getValue() )
               .append(" ");

        String honorificSuffix = nameElement.getByName("honorific-suffix").getValue();
        if (  honorificSuffix.isEmpty() ) {
            builder.append( honorificSuffix )
                   .append( " ");
        }

        builder.append( nameElement.getByName("additional-name").getValue() );

        return builder.toString();
    }

}
