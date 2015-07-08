/*
 * @#SimpleRunner.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_p2p_plugin.layer.communication.cloud_server.developer.bitdubai.version_1;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communication.cloud_server.developer.bitdubai.version_1.CloudServerMainRunner</code> initialize
 * the plugin root that represent the cloud server and start it
 * <p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 08/07/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CloudServerMainRunner {

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String [ ] args){

        /*
         * Create the plugin root
         */
        CloudServerCommunicationPluginRoot cloudServerCommunicationPluginRoot = new CloudServerCommunicationPluginRoot();

        /*
         * Start the process
         */
        cloudServerCommunicationPluginRoot.start();
    }
}
