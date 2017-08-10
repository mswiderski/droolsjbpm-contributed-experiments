package org.drools.learner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.drools.learner.builder.Learner.DataType;
import org.drools.learner.builder.Learner.DomainAlgo;
import org.drools.learner.tools.FeatureNotSupported;
import org.drools.learner.tools.Util;
import org.kie.api.runtime.KieSession;

public class Memory {
    public static Memory createFromObjects( List<Object> objects, Class<?> clazz, DomainAlgo domain, DataType data ) throws FeatureNotSupported {
        // if mem == null
        Memory mem = new Memory();

        mem.setClassToClassify( clazz );
        // create schema from clazz
        Schema instSchema = null;
        try {
            instSchema = Schema.createSchemaStructure( clazz, domain, data );
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit( 0 );
        }

        // create a instance list that can hold objects from our schema
        mem.instances.put( clazz, new InstanceList( instSchema ) );

        /*
         * do they create an ObjectTypeNode for each new inserted object type?
         * even if there is no rule exists. No probably they do not
         */


        Iterator<Object> itObject = objects.iterator(); // how can i get the object type nodes
        while ( itObject.hasNext() ) {
            Object obj = itObject.next();
            // validating in the the factory during instantiation
            //if (clazz.isAssignableFrom(obj.getClass()))
            mem.instances.get( clazz ).addStructuredInstance( obj );
        }

        return mem;
    }
    //// class specification
    //private Schema schema;
    Class<?> clazzToClassify;

    // instance list used to train
    private HashMap<Class<?>, InstanceList> instances;

    private InstanceList trainInstances, testInstances;

    //	// instance list used to test
    //	private HashMap<Class<?>,InstanceList> test_instances;
    private double trainRatio = Util.TRAINING_RATIO;
    private double testRatio = Util.TESTING_RATIO;

    private Memory() {
        this.instances = new HashMap<Class<?>, InstanceList>();
    }

    public void setTrainRatio( double ratio ) {
        trainRatio = ratio;
    }

    public void setTestRatio( double ratio ) {
        testRatio = ratio;
    }

    public InstanceList getTrainSet() {
        return trainInstances;
    }

    public InstanceList getTestSet() {
        return testInstances;
    }

    public void processTestSet() {
        int splitIdx = (int) ( trainRatio * instances.get( this.clazzToClassify ).getSize() );
        //int split_idx2 =  split_idx + (int)(testRatio * instances.get(this.clazzToClassify).getSize());
        int splitIdx2 = instances.get( this.clazzToClassify ).getSize();

        trainInstances = instances.get( this.clazzToClassify ).subList( 0, splitIdx );
        testInstances = instances.get( this.clazzToClassify ).subList( splitIdx, splitIdx2 );//class_instances.getSize());
        return;
    }

    private void setClassToClassify( Class<?> clazz ) {
        this.clazzToClassify = clazz;
    }

    // target class instances
    public InstanceList getClassInstances() {
        return instances.get( this.clazzToClassify );
    }

}
