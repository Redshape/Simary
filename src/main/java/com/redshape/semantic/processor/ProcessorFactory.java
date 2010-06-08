package com.redshape.semantic.processor;

import com.redshape.semantic.processor.annotations.Processor;
import com.vio.utils.PackageLoader;
import com.vio.utils.Registry;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 7, 2010
 * Time: 4:04:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorFactory {
    private final static Logger log = Logger.getLogger( ProcessorFactory.class );
    public static String PROCESSORS_PACKAGE = "com.vio.semantic.processor.engines";

    private static ProcessorFactory defaultInstance;

    /**
     * Default instance initializing
     *
     * @throws FactoryConfigurationException
     */
    static {
        try {
            defaultInstance = new ProcessorFactory();
        } catch ( FactoryConfigurationException e ) {
            log.error( e.getMessage(), e );
        }
    }

    private Map< Processor, IProcessor> processors = new HashMap< Processor, IProcessor>();

    public static void setDefault( ProcessorFactory factory ) {
        defaultInstance = factory;
    }

    public static ProcessorFactory getDefault() {
        return defaultInstance;
    }

    protected ProcessorFactory() throws FactoryConfigurationException {
        this.initializeProcessors();
    }

    public <T extends IProcessor> T getProcessor( Class<T> clazz ) throws InstantiationException {
        Processor annotation = clazz.getAnnotation(Processor.class);
        if ( annotation == null ) {
            throw new InstantiationException("Wrong processor type given!");
        }

        T processor = (T) this.processors.get(annotation);
        if ( processor != null ) {
            return processor;
        }

        processor = this.registerProcessor(clazz);

        return processor;
    }

    public <T extends IProcessor> T registerProcessor( Class<? extends T> processor ) throws InstantiationException {
        try {
            return this.registerProcessor( processor.newInstance() );
        } catch ( Throwable e ) {
            throw new InstantiationException();
        }
    }

    public <T extends IProcessor> T registerProcessor( T processor ) throws InstantiationException {
        Processor annotation = processor.getClass().getAnnotation( Processor.class );
        if ( annotation == null ) {
            throw new InstantiationException("Wrong processor type given!");
        }

        this.processors.put( annotation, processor );

        return processor;
    }

    protected void initializeProcessors() throws FactoryConfigurationException {
        try {
            Class<? extends IProcessor>[] processors = Registry.getPackagesLoader().getClasses( PROCESSORS_PACKAGE );
            for ( Class<? extends IProcessor> processorClazz : processors ) {
                this.registerProcessor( processorClazz );
            }
        } catch ( Throwable e ) {
            throw new FactoryConfigurationException();
        }
    }

}
