package com.redshape.semantic.processor.engines.hcard.optimizers;

import com.redshape.semantic.data.hcard.HCardElement;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 8, 2010
 * Time: 4:55:05 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IOptimizer {

    public void optimize( HCardElement tree );

}
