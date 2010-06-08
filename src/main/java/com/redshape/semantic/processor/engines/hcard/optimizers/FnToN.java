package com.redshape.semantic.processor.engines.hcard.optimizers;

import com.redshape.semantic.data.hcard.HCardElement;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 8, 2010
 * Time: 4:57:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class FnToN implements IOptimizer {

    public void optimize( HCardElement tree ) {
        HCardElement fn = tree.getByName("fn");
        if ( fn.getValue().isEmpty() ) {
            return;
        }

        HCardElement n = tree.getByName("n");

        String[] fnParts = fn.getValue().split(" ");
        if ( fnParts.length == 2 ) {
            n.getByName("given-name").setValue( fnParts[0] );
            n.getByName("family-name").setValue( fnParts[1] );
        } else if ( fnParts.length == 3 ) {
            n.getByName("given-name").setValue( fnParts[0] );

            if ( fnParts[1].contains(".") ) {
                n.getByName("honorific-suffix").setValue(fnParts[1]);
            } else {
                n.getByName("additional-name").setValue( fnParts[1] );
            }

            n.getByName("family-name").setValue( fnParts[2] );
        }
    }

}
