package com.redshape.semantic.processor.engines.hcard.optimizers;

import com.redshape.semantic.data.hcard.HCardElement;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 8, 2010
 * Time: 4:54:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class FnToNickname implements IOptimizer {

    public void optimize( HCardElement tree ) {
        HCardElement nickname = tree.getByName("nickname");
        if ( !nickname.getValue().isEmpty() ) {
            return;
        }

        nickname.setValue( tree.getByName("fn").getValue() );
    }

}
