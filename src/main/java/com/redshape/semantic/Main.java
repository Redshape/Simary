package com.redshape.semantic;

import com.vio.utils.PackageLoader;
import com.vio.utils.Registry;
import com.vio.utils.ResourcesLoader;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 7, 2010
 * Time: 3:31:05 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Main {

    public static void main( final String[] args ) {
        Registry.setResourcesLoader( new ResourcesLoader() );
        Registry.setPackagesLoader( new PackageLoader() );
    }

}
