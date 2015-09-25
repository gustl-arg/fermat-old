/*
 * @#CantPublishComponetMiddlewareException.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_api.layer.ccp_middleware.wallet_publisher.exceptions;

import com.bitdubai.fermat_api.layer.ccp_module.wallet_publisher.exceptions.CantPublishComponentException;

/**
 * The Class <code>com.bitdubai.fermat_api.layer.ccp_middleware.wallet_publisher.exceptions.CantPublishComponentMiddlewareException</code> define
 * the error occurred when can not be published a component
 * <p/>
 *
 * @author Create by Roberto Requena - (rart3001@gmail.com) on 14/08/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CantPublishComponentMiddlewareException extends CantPublishComponentException {
    /**
     * This is the constructor that every inherited FermatException must implement
     *
     * @param message        the short description of the why this exception happened, there is a public static constant called DEFAULT_MESSAGE that can be used here
     * @param cause          the exception that triggered the throwing of the current exception, if there are no other exceptions to be declared here, the cause should be null
     * @param context        a String that provides the values of the variables that could have affected the exception
     * @param possibleReason an explicative reason of why we believe this exception was most likely thrown
     */
    public CantPublishComponentMiddlewareException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
