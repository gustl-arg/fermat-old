package com.bitdubai.fermat_api.layer.ccp_network_service.wallet_store.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by rodrigo on 7/23/15.
 */
public class CantPublishLanguageInCatalogException extends FermatException {
    public CantPublishLanguageInCatalogException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
